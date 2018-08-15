package it.sella.azure.models;

import static it.sella.util.AppUtil.getObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "answers" })
public class QnAResponse {

	@JsonProperty("answers")
	private List<Answer> answers = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("answers")
	public List<Answer> getAnswers() {
		return answers;
	}

	@JsonIgnore
	public String getFirstAnswer() {
		return answers.get(0).getAnswer();
	}

	@JsonProperty("answers")
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public static QnAResponse fromJson(String json) throws JsonParseException, JsonMappingException, IOException {
		return getObjectMapper().readValue(json, QnAResponse.class);
	}

}