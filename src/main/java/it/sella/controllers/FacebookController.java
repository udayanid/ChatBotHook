package it.sella.controllers;

import static com.github.messenger4j.Messenger.SIGNATURE_HEADER_NAME;
import static com.github.messenger4j.send.message.richmedia.RichMediaAsset.Type.IMAGE;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
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

import com.github.messenger4j.Messenger;
import com.github.messenger4j.common.WebviewHeightRatio;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.exception.MessengerVerificationException;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.MessagingType;
import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.message.RichMediaMessage;
import com.github.messenger4j.send.message.TemplateMessage;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.send.message.richmedia.UrlRichMediaAsset;
import com.github.messenger4j.send.message.template.ButtonTemplate;
import com.github.messenger4j.send.message.template.button.Button;
import com.github.messenger4j.send.message.template.button.CallButton;
import com.github.messenger4j.send.message.template.button.PostbackButton;
import com.github.messenger4j.send.message.template.button.UrlButton;
import com.github.messenger4j.send.recipient.IdRecipient;
import com.github.messenger4j.userprofile.UserProfile;
import com.github.messenger4j.webhook.event.PostbackEvent;
import com.github.messenger4j.webhook.event.TextMessageEvent;

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
		logger.info("Received Messenger Platform callback - payload: {} | signature: {}", payLoad, signature);

		try {
			this.messenger.onReceiveEvents(payLoad, of(signature), event -> {
				final String senderId = event.senderId();
				if (event.isTextMessageEvent()) {
					try {
						TextMessageEvent messageEvent = event.asTextMessageEvent();
						final String messageText = messageEvent.text();
						if (messageText.equalsIgnoreCase("Hi") || messageText.equalsIgnoreCase("Hello")
								|| messageText.equalsIgnoreCase("Helo")) {
							sendUserDetails(senderId);
						} else if (messageText.contains("product") || messageText.contains("Product")) {
							sendButtonMessage(senderId);
						} else if (messageText.equalsIgnoreCase("image")) {
							sendImageMessage(senderId);
						} else if ("button".equalsIgnoreCase(messageText)) {
							sendButtonMessage(senderId);
						}
					} catch (MessengerApiException | MessengerIOException | MalformedURLException e) {
						logger.info("Processing of callback payload failed: {}", e.getMessage());
					}
				} else if (event.isPostbackEvent()) {
					PostbackEvent pbEvent = event.asPostbackEvent();
					logger.info(pbEvent.payload().get());
				}

			});
		} catch (final MessengerVerificationException e) {
			logger.warn("Processing of callback payload failed: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		logger.debug("Processed callback payload successfully");
		return new ResponseEntity<>(HttpStatus.OK);

	}

	private void sendUserDetails(String recipientId) throws MessengerApiException, MessengerIOException {
		final UserProfile userProfile = this.messenger.queryUserProfile(recipientId);
		sendTextMessage(recipientId, String.format("Hi %s %s", userProfile.firstName(), userProfile.lastName()));
		logger.info("User Profile Picture: {}", userProfile.profilePicture());
	}

	private void sendButtonMessage(String recipientId)
			throws MessengerApiException, MessengerIOException, MalformedURLException {
		final List<Button> buttons = Arrays.asList(
				UrlButton.create("Open Web URL", new URL("https://spring.io/"), of(WebviewHeightRatio.TALL), of(false),
						empty(), empty()),
				PostbackButton.create("Trigger Postback", "DEVELOPER_DEFINED_PAYLOAD"),
				CallButton.create("Call Phone Number", "+16505551234"));

		final ButtonTemplate buttonTemplate = ButtonTemplate.create("Tap a button", buttons);
		final TemplateMessage templateMessage = TemplateMessage.create(buttonTemplate);
		final MessagePayload messagePayload = MessagePayload.create(recipientId, MessagingType.RESPONSE,
				templateMessage);
		logger.info("message PayLoad {}", messagePayload);
		this.messenger.send(messagePayload);
	}

	private void sendTextMessage(String recipientId, String text) {
		try {
			final IdRecipient recipient = IdRecipient.create(recipientId);
			final NotificationType notificationType = NotificationType.REGULAR;
			final String metadata = "DEVELOPER_DEFINED_METADATA";

			final TextMessage textMessage = TextMessage.create(text, empty(), of(metadata));
			final MessagePayload messagePayload = MessagePayload.create(recipient, MessagingType.RESPONSE, textMessage,
					of(notificationType), empty());
			this.messenger.send(messagePayload);
		} catch (MessengerApiException | MessengerIOException e) {
			handleSendException(e);
		}
	}

	private void sendImageMessage(String recipientId)
			throws MessengerApiException, MessengerIOException, MalformedURLException {
		final UrlRichMediaAsset richMediaAsset = UrlRichMediaAsset.create(IMAGE,
				new URL("https://chatbot-hook.herokuapp.com/img/image1.jpg"));
		sendRichMediaMessage(recipientId, richMediaAsset);
	}

	private void sendRichMediaMessage(String recipientId, UrlRichMediaAsset richMediaAsset)
			throws MessengerApiException, MessengerIOException {
		final RichMediaMessage richMediaMessage = RichMediaMessage.create(richMediaAsset);
		final MessagePayload messagePayload = MessagePayload.create(recipientId, MessagingType.RESPONSE,
				richMediaMessage);
		this.messenger.send(messagePayload);
	}

	private void handleSendException(Exception e) {
		logger.error("Message could not be sent. An unexpected error occurred.", e);
	}
}
