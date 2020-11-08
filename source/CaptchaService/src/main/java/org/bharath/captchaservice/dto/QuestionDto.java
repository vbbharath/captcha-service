package org.bharath.captchaservice.dto;

import lombok.Data;

@Data
public class QuestionDto {
	private String question;
	private String token;
}
