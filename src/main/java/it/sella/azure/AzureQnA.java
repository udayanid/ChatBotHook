package it.sella.azure;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.sella.azure.models.QnAResponse;
import it.sella.azure.models.Question;



public class AzureQnA {
	private static final String QnA_URL = "https://messengerqna.azurewebsites.net/qnamaker/knowledgebases/%s/generateAnswer";
	
	private String azureApiKey;
	private static AzureQnA azureQnA;
	
	private AzureQnA(String azureApiKey) {
		this.azureApiKey=azureApiKey;
	}
	public static AzureQnA getInstance(String azureApiKey) {
		if(azureQnA==null)
			azureQnA=new AzureQnA(azureApiKey);
		return azureQnA;
	}
	private String getURL() {
		return String.format(QnA_URL, azureApiKey);
	}
	
	
	public QnAResponse ask(String strQuestion) throws JsonProcessingException {
		final RestTemplate restTemplate = new RestTemplate();
		
		Question question=new Question(strQuestion);
		final String json = question.toJson();
		
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "EndpointKey 1d5815e4-34dd-46be-8d3e-e8619b7de192");
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		final HttpEntity<String> entity = new HttpEntity<String>(json, headers);
		return restTemplate.postForObject(getURL(), entity, QnAResponse.class);
	}
}
