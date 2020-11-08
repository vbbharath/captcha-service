package org.bharath.captchaservice.dto;

import lombok.Data;

@Data
public class AnswerDto {
	private String question;
	private int answer;
	private String token;
}
