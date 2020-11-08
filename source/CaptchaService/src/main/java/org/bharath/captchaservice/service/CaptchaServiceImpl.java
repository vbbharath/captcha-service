package org.bharath.captchaservice.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.bharath.captchaservice.AppConstants;
import org.bharath.captchaservice.dto.AnswerDto;
import org.bharath.captchaservice.dto.QuestionDto;
import org.bharath.captchaservice.exception.CustomException;
import org.bharath.captchaservice.utils.JWTUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CaptchaServiceImpl implements CaptchaService {

	@Override
	public QuestionDto getQuestion() {
		RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
		int maxElements = randomDataGenerator.nextInt(2, 5);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < maxElements; i++) {
			if (builder.length() > 0) {
				builder.append(",");
			}
			builder.append(randomDataGenerator.nextInt(1, 10));
		}
		QuestionDto dto = new QuestionDto();
		String question = AppConstants.QUESTION_TEMPLATE + builder.toString();
		dto.setQuestion(question);

		Map<String, Object> claims = new HashMap<>();
		claims.put("question", question);
		dto.setToken(JWTUtils.createJWT(claims));
		return dto;
	}

	@Override
	public boolean validateAnswer(AnswerDto answerDto) {
		String question = answerDto.getQuestion();
		if (StringUtils.isEmpty(question)) {
			throw new CustomException("Question not found.");
		}
		if (StringUtils.isEmpty(answerDto.getToken())) {
			throw new CustomException("Token not found.");
		}
		if (!question.startsWith(AppConstants.QUESTION_TEMPLATE)) {
			throw new CustomException("Invalid question.");
		}

		String questionInHeader = JWTUtils.getClaim(answerDto.getToken(), "question");
		if (!question.equals(questionInHeader)) {
			throw new CustomException("Not the original question. The question has been modified.");
		}

		String[] split = question.substring(AppConstants.QUESTION_TEMPLATE.length()).split(",");
		int answer = 0;
		for (String string : split) {
			answer += Integer.valueOf(string);
		}
		return answer == answerDto.getAnswer();
	}

}
