package it.sella.controllers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import it.sella.azure.AzureQnA;
import it.sella.telegram.Telegram;

@RestController
public class TelegramController {
	private static final Logger logger = LoggerFactory.getLogger(TelegramController.class);
	private final AzureQnA azureQnA;
	private final Telegram telegram;

	@Autowired
	public TelegramController(Telegram telegram, AzureQnA azureQnA) {
		this.telegram = telegram;
		this.azureQnA = azureQnA;
	}

	@PostMapping("/telegramhook")
	public ResponseEntity<?> getMessage(@RequestBody final String payload) {
		logger.info("telegram message hook>>{}", payload);

		try {
			final String question = telegram.updatePayload(payload).getText();
			final String answer = azureQnA.ask(question).getFirstAnswer();
			return telegram.sendTelegram(answer);
		} catch (final JsonParseException | JsonMappingException  e) {
			// TODO: Remove System.out.println
			System.out.println("ourException:" + e.getMessage());
			logger.error("Parsing exception during the telegramPayload Parsing", e);
		} catch (IOException e) {
			// TODO: Remove System.out.println
			System.out.println("ourException:" + e.getMessage());
		}
		return new ResponseEntity<String>("contact Support", HttpStatus.BAD_REQUEST);
	}

}
