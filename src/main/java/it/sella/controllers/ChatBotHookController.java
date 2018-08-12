package it.sella.controllers;

import static com.github.messenger4j.Messenger.SIGNATURE_HEADER_NAME;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import static java.util.Optional.of;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.exception.MessengerVerificationException;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.MessagingType;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.webhook.Event;

@RestController
public class ChatBotHookController {
	private static final Logger logger = LoggerFactory.getLogger(ChatBotHookController.class);
	final Messenger messenger;

	@Autowired
	public ChatBotHookController(Messenger messenger) {
		this.messenger = messenger;
	}

	@GetMapping("/webhook")
	public ResponseEntity<?> verify(@RequestParam("hub.challenge") String challenge,
			@RequestParam("hub.verify_token") String token) {
		if (token.equals("mycustomtoken23"))
			return new ResponseEntity<String>(challenge, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	@PostMapping("/webhook")
	public ResponseEntity<?> getMessage(@RequestBody final String payLoad,
			@RequestHeader(SIGNATURE_HEADER_NAME) final String signature) {
		logger.debug("Received Messenger Platform callback - payload: {} | signature: {}", payLoad, signature);
		try {
			this.messenger.onReceiveEvents(payLoad, of(signature), event -> {

				if (event.isTextMessageEvent()) {
					processTextMessage(event);
				}

			});
		} catch (MessengerVerificationException e) {
			logger.warn("Processing of callback payload failed: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.debug("Processed callback payload successfully");
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private void processTextMessage(Event event) {
		final String senderId = event.senderId();
		final String text = event.asTextMessageEvent().text();
		RestTemplate restTemplate = new RestTemplate();

		final String url = "https://messengerqna.azurewebsites.net/qnamaker/knowledgebases/2130e964-ee28-4340-bef6-4af81d916292/generateAnswer";
		String json = "{\"question\":\"what can be sell on amazon?\"}";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "EndpointKey 1d5815e4-34dd-46be-8d3e-e8619b7de192");
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity(json, headers);
		String answer = restTemplate.postForObject(url, entity, String.class);
		final TextMessage textMessage = TextMessage.create(answer);
		final MessagePayload messagePayload = MessagePayload.create(senderId, MessagingType.RESPONSE, textMessage);

		try {
			messenger.send(messagePayload);
		} catch (MessengerApiException | MessengerIOException e) {
			logger.warn("Processing of callback payload failed: {}", e.getMessage());
		}

	}

	@GetMapping("/")
	public ResponseEntity<?> sayConnected() {
		return new ResponseEntity<String>("Webhook is Listening", HttpStatus.OK);
	}
}
