package com.bbteam.budgetbuddies.domain.openai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bbteam.budgetbuddies.domain.openai.dto.ChatGPTRequest;
import com.bbteam.budgetbuddies.domain.openai.dto.ChatGPTResponse;

@RestController
@RequestMapping("/bot")
public class OpenAiController {
	@Value("${spring.openai.model}")
	private String model;

	@Value("${spring.openai.api.url}")
	private String apiURL;

	@Autowired
	private RestTemplate template;

	@GetMapping("/chat")
	public String chat(@RequestParam(name = "prompt") String prompt) {
		ChatGPTRequest request = new ChatGPTRequest(model, prompt);
		ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, request, ChatGPTResponse.class);
		return chatGPTResponse.getChoices().get(0).getMessage().getContent();
	}

}