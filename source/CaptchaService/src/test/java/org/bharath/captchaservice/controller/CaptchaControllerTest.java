package org.bharath.captchaservice.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.bharath.captchaservice.AbstractTest;
import org.bharath.captchaservice.AppConstants;
import org.bharath.captchaservice.dto.AnswerDto;
import org.bharath.captchaservice.dto.QuestionDto;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CaptchaControllerTest extends AbstractTest {

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	public void testGetQuestion() throws Exception {

		String uri = "/rest/v1/question";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		QuestionDto questionDto = super.mapFromJson(content, QuestionDto.class);
		assertNotEquals("", questionDto.getToken());
		assertNotEquals("", questionDto.getQuestion());
	}

	@Test
	public void testValidateAnswer() throws Exception {
		String uri = "/rest/v1/question";
		AnswerDto answerDto = new AnswerDto();
		answerDto.setQuestion("Please sum the numbers 1,10");
		answerDto.setToken("");
		answerDto.setAnswer(11);
		String inputJson = super.mapToJson(answerDto);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		int status = mvcResult.getResponse().getStatus();

		assertEquals(500, status);
		assertEquals(content, "Token not found.");
	}

	@Test
	public void testGetQuestionAndValidateAnswer() throws Exception {
		String uri = "/rest/v1/question";
		AnswerDto answerDto = new AnswerDto();

		MvcResult mvcGetResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		String getContent = mvcGetResult.getResponse().getContentAsString();
		QuestionDto questionDto = super.mapFromJson(getContent, QuestionDto.class);

		// Correct answer
		answerDto.setQuestion(questionDto.getQuestion());
		answerDto.setToken(questionDto.getToken());

		String[] split = questionDto.getQuestion().substring(AppConstants.QUESTION_TEMPLATE.length()).split(",");
		int answer = 0;
		for (String string : split) {
			answer += Integer.valueOf(string);
		}
		answerDto.setAnswer(answer);
		String inputJson = super.mapToJson(answerDto);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		int status = mvcResult.getResponse().getStatus();

		assertEquals(200, status);
		assertEquals(content, "That’s great.");

		// Incorrect answer
		answerDto.setAnswer(-1);
		inputJson = super.mapToJson(answerDto);
		mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		content = mvcResult.getResponse().getContentAsString();
		status = mvcResult.getResponse().getStatus();

		assertEquals(400, status);
		assertEquals(content, "That’s wrong. Please try again.");

		// Question modified
		answerDto.setQuestion("Please sum the numbers 1,2");
		answerDto.setAnswer(3);
		inputJson = super.mapToJson(answerDto);
		mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		content = mvcResult.getResponse().getContentAsString();
		status = mvcResult.getResponse().getStatus();

		assertEquals(500, status);
		assertEquals(content, "Not the original question. The question has been modified.");
	}

}
