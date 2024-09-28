package com.bbteam.budgetbuddies.domain.openai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bbteam.budgetbuddies.domain.openai.dto.ChatGPTRequest;
import com.bbteam.budgetbuddies.domain.openai.dto.ChatGPTResponse;

@Service
public class OpenAiService {

	@Value("${spring.openai.model}")
	private String model;

	@Value("${spring.openai.api.url}")
	private String apiURL;

	@Autowired
	private RestTemplate template;

	public String chat(String prompt) {
		ChatGPTRequest request = new ChatGPTRequest(model, prompt);
		ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, request, ChatGPTResponse.class);
		return chatGPTResponse.getChoices().get(0).getMessage().getContent();
	}
}
