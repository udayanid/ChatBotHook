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
		
		String jsonResponse = "{\r\n" + 
				"  \"recipient\":{\r\n" + 
				"    \"id\":\"2174277166143287\"\r\n" + 
				"  }, \r\n" + 
				"  \"message\": {\r\n" + 
				"    \"attachment\": {\r\n" + 
				"      \"type\": \"template\",\r\n" + 
				"      \"payload\": {\r\n" + 
				"        \"template_type\": \"list\",\r\n" + 
				"        \"top_element_style\": \"compact\",\r\n" + 
				"        \"elements\": [\r\n" + 
				"          {\r\n" + 
				"            \"title\": \"Classic T-Shirt Collection\",\r\n" + 
				"            \"subtitle\": \"See all our colors\",\r\n" + 
				"            \"image_url\": \"https://peterssendreceiveapp.ngrok.io/img/collection.png\",          \r\n" + 
				"            \"buttons\": [\r\n" + 
				"              {\r\n" + 
				"                \"title\": \"View\",\r\n" + 
				"                \"type\": \"web_url\",\r\n" + 
				"                \"url\": \"https://peterssendreceiveapp.ngrok.io/collection\",\r\n" + 
				"                \"messenger_extensions\": true,\r\n" + 
				"                \"webview_height_ratio\": \"tall\",\r\n" + 
				"                \"fallback_url\": \"https://peterssendreceiveapp.ngrok.io/\"            \r\n" + 
				"              }\r\n" + 
				"            ]\r\n" + 
				"          },\r\n" + 
				"          {\r\n" + 
				"            \"title\": \"Classic White T-Shirt\",\r\n" + 
				"            \"subtitle\": \"See all our colors\",\r\n" + 
				"            \"default_action\": {\r\n" + 
				"              \"type\": \"web_url\",\r\n" + 
				"              \"url\": \"https://peterssendreceiveapp.ngrok.io/view?item=100\",\r\n" + 
				"              \"messenger_extensions\": false,\r\n" + 
				"              \"webview_height_ratio\": \"tall\"\r\n" + 
				"            }\r\n" + 
				"          },\r\n" + 
				"          {\r\n" + 
				"            \"title\": \"Classic Blue T-Shirt\",\r\n" + 
				"            \"image_url\": \"https://peterssendreceiveapp.ngrok.io/img/blue-t-shirt.png\",\r\n" + 
				"            \"subtitle\": \"100% Cotton, 200% Comfortable\",\r\n" + 
				"            \"default_action\": {\r\n" + 
				"              \"type\": \"web_url\",\r\n" + 
				"              \"url\": \"https://peterssendreceiveapp.ngrok.io/view?item=101\",\r\n" + 
				"              \"messenger_extensions\": true,\r\n" + 
				"              \"webview_height_ratio\": \"tall\",\r\n" + 
				"              \"fallback_url\": \"https://peterssendreceiveapp.ngrok.io/\"\r\n" + 
				"            },\r\n" + 
				"            \"buttons\": [\r\n" + 
				"              {\r\n" + 
				"                \"title\": \"Shop Now\",\r\n" + 
				"                \"type\": \"web_url\",\r\n" + 
				"                \"url\": \"https://peterssendreceiveapp.ngrok.io/shop?item=101\",\r\n" + 
				"                \"messenger_extensions\": true,\r\n" + 
				"                \"webview_height_ratio\": \"tall\",\r\n" + 
				"                \"fallback_url\": \"https://peterssendreceiveapp.ngrok.io/\"            \r\n" + 
				"              }\r\n" + 
				"            ]        \r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"         \"buttons\": [\r\n" + 
				"          {\r\n" + 
				"            \"title\": \"View More\",\r\n" + 
				"            \"type\": \"postback\",\r\n" + 
				"            \"payload\": \"payload\"            \r\n" + 
				"          }\r\n" + 
				"        ]  \r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}";
		logger.debug("Payload information:{}", jsonResponse);
		return new ResponseEntity<String>(jsonResponse, HttpStatus.OK);

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
