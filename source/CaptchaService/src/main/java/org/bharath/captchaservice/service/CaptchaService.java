package org.bharath.captchaservice.service;

import org.bharath.captchaservice.dto.AnswerDto;
import org.bharath.captchaservice.dto.QuestionDto;

public interface CaptchaService {
	QuestionDto getQuestion();

	boolean validateAnswer(AnswerDto answerDto);
}
