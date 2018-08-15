package it.sella.telegram.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "is_bot", "first_name", "language_code" })
public class From {

	@JsonProperty("id")
	private Integer id;
	@JsonProperty("is_bot")
	private Boolean isBot;
	@JsonProperty("first_name")
	private String firstName;
	@JsonProperty("language_code")
	private String languageCode;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}

	@JsonProperty("is_bot")
	public Boolean getIsBot() {
		return isBot;
	}

	@JsonProperty("is_bot")
	public void setIsBot(Boolean isBot) {
		this.isBot = isBot;
	}

	@JsonProperty("first_name")
	public String getFirstName() {
		return firstName;
	}

	@JsonProperty("first_name")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@JsonProperty("language_code")
	public String getLanguageCode() {
		return languageCode;
	}

	@JsonProperty("language_code")
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
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
