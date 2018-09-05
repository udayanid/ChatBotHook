package it.sella.controllers;

import static com.github.messenger4j.Messenger.SIGNATURE_HEADER_NAME;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
import com.github.messenger4j.send.message.TemplateMessage;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.send.message.template.ListTemplate;
import com.github.messenger4j.send.message.template.button.Button;
import com.github.messenger4j.send.message.template.button.UrlButton;
import com.github.messenger4j.send.message.template.common.Element;
import com.google.gson.Gson;

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
	 * 
	 * @param challenge
	 * @param token
	 * @return
	 */
	@GetMapping("/webhook")
	public ResponseEntity<?> verify(@RequestParam("hub.challenge") String challenge,
			@RequestParam("hub.verify_token") String token) {

		logger.info("Challenge is:{} and token is{}", challenge, token);
		if (token.equals("mycustomtoken23"))
			return new ResponseEntity<String>(challenge, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	/**
	 * Process the facebook messages and provide bot response.
	 * 
	 * @param payLoad
	 * @param signature
	 * @return
	 */

	@PostMapping("/webhook")
	public ResponseEntity<?> getMessage(@RequestBody final String payLoad,
			@RequestHeader(SIGNATURE_HEADER_NAME) final String signature) {
		logger.debug("Received Messenger Platform callback - payload: {} | signature: {}", payLoad, signature);

		String jsonResponse = "{\r\n" + "    \"text\":\"hello, world!\"\r\n" + "  }";

		try {
			this.messenger.onReceiveEvents(payLoad, of(signature), event -> {
				if (event.isTextMessageEvent()) {
					try {
						logger.info("PAYLOAD........." + payLoad + "---" + signature);
						final String senderId = event.senderId();
						Gson gson = new Gson();
						TextMessage textMessage = gson.fromJson(jsonResponse, TextMessage.class);

						final MessagePayload messagePayload = MessagePayload.create(senderId, MessagingType.RESPONSE,
								textMessage);
						messenger.send(messagePayload);
					} catch (MessengerApiException | MessengerIOException e) {
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

	private void sendListMessageMessage(String recipientId)
			throws MessengerApiException, MessengerIOException, MalformedURLException {
		List<Button> riftButtons = new ArrayList<>();
		riftButtons.add(UrlButton.create("Open Web URL", new URL("https://www.oculus.com/en-us/rift/")));

		List<Button> touchButtons = new ArrayList<>();
		touchButtons.add(UrlButton.create("Open Web URL", new URL("https://www.oculus.com/en-us/touch/")));

		final List<Element> elements = new ArrayList<>();

		elements.add(Element.create("rift", of("Next-generation virtual reality"),
				of(new URL("https://www.oculus.com/en-us/rift/")), empty(), of(riftButtons)));
		elements.add(Element.create("touch", of("Your Hands, Now in VR"),
				of(new URL("https://www.oculus.com/en-us/touch/")), empty(), of(touchButtons)));

		final ListTemplate listTemplate = ListTemplate.create(elements);
		final TemplateMessage templateMessage = TemplateMessage.create(listTemplate);
		final MessagePayload messagePayload = MessagePayload.create(recipientId, MessagingType.RESPONSE,
				templateMessage);
		this.messenger.send(messagePayload);
	}

}
