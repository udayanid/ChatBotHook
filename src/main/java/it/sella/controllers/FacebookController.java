package it.sella.controllers;

import static com.github.messenger4j.Messenger.SIGNATURE_HEADER_NAME;
import static java.util.Optional.of;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.exception.MessengerVerificationException;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.MessagingType;
import com.github.messenger4j.send.message.TextMessage;

import it.sella.azure.AzureQnA;

@RestController
public class FacebookController {

	private static final Logger logger = LoggerFactory.getLogger(ChatBotHookController.class);
	private final Messenger messenger;
	private final AzureQnA azureQnA;

	@Autowired
	public FacebookController(Messenger messenger, AzureQnA azureQnA) {
		this.messenger = messenger;
		this.azureQnA = azureQnA;
	}

	/**
	 * Facebook web hook verification.
	 * @param challenge
	 * @param token
	 * @return
	 */
	@GetMapping("/webhook")
	public ResponseEntity<?> verify(@RequestParam("hub.challenge") String challenge,
			@RequestParam("hub.verify_token") String token) {
		if (token.equals("mycustomtoken23"))
			return new ResponseEntity<String>(challenge, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	/**
	 * Process the facebook messages and provide bot response.
	 * @param payLoad
	 * @param signature
	 * @return
	 */
	@PostMapping("/webhook")
	public ResponseEntity<?> getMessage(@RequestBody final String payLoad,
			@RequestHeader(SIGNATURE_HEADER_NAME) final String signature) {
		logger.debug("Received Messenger Platform callback - payload: {} | signature: {}", payLoad, signature);
		try {
			this.messenger.onReceiveEvents(payLoad, of(signature), event -> {
				if (event.isTextMessageEvent()) {
					try {
						logger.info("PAYLOAD........." + payLoad + "---" + signature);
						final String senderId = event.senderId();
						final String text = event.asTextMessageEvent().text();
						final String answer = azureQnA.ask(text).getAnswers().get(0).getAnswer();
						final TextMessage textMessage = TextMessage.create(answer);
						final MessagePayload messagePayload = MessagePayload.create(senderId, MessagingType.RESPONSE,
								textMessage);
						messenger.send(messagePayload);
					} catch (MessengerApiException | MessengerIOException | JsonProcessingException e) {
						logger.warn("Processing of callback payload failed: {}", e.getMessage());
					}
				}
			});
		} catch (final MessengerVerificationException e) {
			logger.warn("Processing of callback payload failed: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		logger.debug("Processed callback payload successfully");
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
