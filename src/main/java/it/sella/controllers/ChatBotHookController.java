package it.sella.controllers;

import static com.github.messenger4j.Messenger.SIGNATURE_HEADER_NAME;
import static java.util.Optional.of;

import java.io.IOException;
import java.util.Map;

import javax.print.DocFlavor.STRING;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.exception.MessengerVerificationException;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.MessagingType;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.webhook.Event;

import it.sella.models.telegram.TelegramPayload;

@RestController
public class ChatBotHookController {
	private final String QnA_URL = "https://messengerqna.azurewebsites.net/qnamaker/knowledgebases/2130e964-ee28-4340-bef6-4af81d916292/generateAnswer";
	private static final Logger logger = LoggerFactory.getLogger(ChatBotHookController.class);
	private final Messenger messenger;

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

	@PostMapping("/telegramhook")
	public ResponseEntity<?> getMessage(@RequestBody final String payLoad)
	{
		logger.info("TELEGRAM>>>>" + payLoad);
		TelegramPayload telegramPayLoad;
		try {
			telegramPayLoad = getPayLoad(payLoad);
			final String question = telegramPayLoad.getMessage().getText();
			final String answer = askAzureBot(question);
			final Integer chatId = telegramPayLoad.getMessage().getChat().getId();
			logger.info("ChatId:::"+chatId+",Question::::"+question);
			return answerTextToTelegram(chatId, answer);
		} catch (final JsonParseException | JsonMappingException e) {
			System.out.println("ourException:"+e.getMessage());
			logger.error("Parsing exception during the telegramPayload Parsing", e);
			//logger.trace("TelegramPayload Parsing", e);
		} catch (IOException e) {
			System.out.println("ourException:"+e.getMessage());
			//logger.trace("TelegramPayload Parsing", e);
		}
		return new ResponseEntity<String>("contact Support",HttpStatus.BAD_REQUEST);
	}

	private TelegramPayload getPayLoad(String json) throws JsonParseException, JsonMappingException, IOException {
		final JsonFactory factory = new JsonFactory();
		final ObjectMapper mapper = new ObjectMapper(factory);
		final TelegramPayload payload = mapper.readValue(json, TelegramPayload.class);
		return payload;
	}

	private ResponseEntity<?> answerTextToTelegram(final Integer chatId, final String answer) {
		final String URL = "https://api.telegram.org/bot644221417:AAHMHTD5eMpT67dlcHAushnMHYNFieu7n1A/sendMessage";
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		final MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("chat_id", chatId.toString());
		map.add("text", answer);
		final HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(map,
				headers);
		return restTemplate.postForEntity(URL, httpEntity, String.class);
	}

	@PostMapping("/webhook")
	public ResponseEntity<?> getMessage(@RequestBody final String payLoad,
			@RequestHeader(SIGNATURE_HEADER_NAME) final String signature) {
		logger.debug("Received Messenger Platform callback - payload: {} | signature: {}", payLoad, signature);
		try {
			this.messenger.onReceiveEvents(payLoad, of(signature), event -> {
				if (event.isTextMessageEvent()) {
					logger.info("PAYLOAD........." + payLoad + "---" + signature);
					processTextMessage(event);
				}
			});
		} catch (final MessengerVerificationException e) {
			logger.warn("Processing of callback payload failed: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.debug("Processed callback payload successfully");
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private String askAzureBot(String question) {
		final RestTemplate restTemplate = new RestTemplate();
		final String json = "{\"question\":\"	" + question + "\"}";
		logger.info("Message from Facebook:" + ((json == null) ? "It is NULL" : json));
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "EndpointKey 1d5815e4-34dd-46be-8d3e-e8619b7de192");
		headers.setContentType(MediaType.APPLICATION_JSON);
		final HttpEntity<String> entity = new HttpEntity<String>(json, headers);
		final String answer = restTemplate.postForObject(QnA_URL, entity, String.class);
		return answer;
	}

	private void processTextMessage(Event event) {
		final String senderId = event.senderId();
		final String text = event.asTextMessageEvent().text();
		final String answer = askAzureBot(text);
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
