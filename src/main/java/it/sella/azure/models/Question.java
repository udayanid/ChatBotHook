package it.sella.azure.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static it.sella.util.AppUtil.getObjectMapper;
public class Question {
	private String question;

	public Question(String question) {
		this.question = question;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String toJson() throws JsonProcessingException {
		return getObjectMapper().writeValueAsString(this);
	}

}
