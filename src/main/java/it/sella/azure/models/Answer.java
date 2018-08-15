
package it.sella.azure.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "questions", "answer", "score", "id", "source", "metadata" })
public class Answer {

	@JsonProperty("questions")
	private List<String> questions = null;
	@JsonProperty("answer")
	private String answer;
	@JsonProperty("score")
	private Double score;
	@JsonProperty("id")
	private Integer id;
	@JsonProperty("source")
	private String source;
	@JsonProperty("metadata")
	private List<Object> metadata = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("questions")
	public List<String> getQuestions() {
		return questions;
	}

	@JsonProperty("questions")
	public void setQuestions(List<String> questions) {
		this.questions = questions;
	}

	@JsonProperty("answer")
	public String getAnswer() {
		return answer;
	}

	@JsonProperty("answer")
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@JsonProperty("score")
	public Double getScore() {
		return score;
	}

	@JsonProperty("score")
	public void setScore(Double score) {
		this.score = score;
	}

	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}

	@JsonProperty("source")
	public String getSource() {
		return source;
	}

	@JsonProperty("source")
	public void setSource(String source) {
		this.source = source;
	}

	@JsonProperty("metadata")
	public List<Object> getMetadata() {
		return metadata;
	}

	@JsonProperty("metadata")
	public void setMetadata(List<Object> metadata) {
		this.metadata = metadata;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
