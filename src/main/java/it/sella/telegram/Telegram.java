package it.sella.telegram;

import java.io.IOException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import it.sella.telegram.models.TelegramPayload;

public class Telegram {
	private static String URL = "https://api.telegram.org/%s/sendMessage";

	private String apiKey;
	private TelegramPayload telegramPayload;

	private static Telegram telegram;

	private Telegram(String apiKey) {
		this.apiKey = apiKey;
	}

	public static Telegram getInstance(String apiKey) {
		if (telegram == null) {
			telegram = new Telegram(apiKey);
		}
		return telegram;
	}

	public Telegram updatePayload(String payload) throws JsonParseException, JsonMappingException, IOException {
		telegramPayload = TelegramPayload.fromJson(payload);
		return this;
	}

	public String getText() throws JsonParseException, JsonMappingException, IOException {
		return telegramPayload.getMessage().getText();
	}

	public ResponseEntity<?> sendTelegram(final String answer) {

		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		final MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		final String chatId = telegramPayload.getMessage().getChat().getId().toString();
		map.add("chat_id", chatId);
		map.add("text", answer);
		final HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(map,
				headers);
		return restTemplate.postForEntity(URL, httpEntity, String.class);
	}
}
