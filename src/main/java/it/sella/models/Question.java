package it.sella.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Question {
	String question;

	public Question(String question) {
		this.question = question;
	}

	public String toJson() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = mapper.writeValueAsString(this);
			return jsonInString;
		} catch (JsonProcessingException exception) {
			return null;
		}
	}

}
