package org.bharath.captchaservice.controller;

import org.bharath.captchaservice.dto.AnswerDto;
import org.bharath.captchaservice.dto.QuestionDto;
import org.bharath.captchaservice.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/v1")
public class CaptchaController {

	@Autowired
	CaptchaService service;

	@GetMapping("/question")
	public ResponseEntity<QuestionDto> getQuestion() {
		return ResponseEntity.ok(service.getQuestion());
	}

	@PostMapping("/question")
	public ResponseEntity<String> validateAnswer(@RequestBody AnswerDto answerDto) {
		if (service.validateAnswer(answerDto)) {
			return ResponseEntity.ok("That’s great.");
		} else {
			return ResponseEntity.badRequest().body("That’s wrong. Please try again.");
		}
	}
}
