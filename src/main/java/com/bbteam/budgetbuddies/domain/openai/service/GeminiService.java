package com.bbteam.budgetbuddies.domain.openai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bbteam.budgetbuddies.domain.openai.dto.ChatRequest;
import com.bbteam.budgetbuddies.domain.openai.dto.ChatResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeminiService {

	@Qualifier("geminiRestTemplate")
	@Autowired
	private RestTemplate restTemplate;

	@Value("${spring.gemini.api.url}")
	private String apiUrl;

	@Value("${spring.gemini.api.key}")
	private String geminiApiKey;

	public String getContents(String prompt) {

		String requestUrl = apiUrl + "?key=" + geminiApiKey;

		ChatRequest request = new ChatRequest(prompt);
		ChatResponse response = restTemplate.postForObject(requestUrl, request, ChatResponse.class);

		String message = response.getCandidates().get(0).getContent().getParts().get(0).getText().toString();

		return message;
	}
}
