
package it.sella.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import it.sella.JsonUtil;
import it.sella.QnaResponse;
import it.sella.model.RequestPayload;
import it.sella.model.UserDetail;

@RestController
public class SellaFbController {

	private static final String SIGNATURE_HEADER_NAME = "X-Hub-Signature";
	private static final String ACCESS_TOKEN = "EAADwyglYT3gBAADvD7dQiT1ANwuht7BafeUUmp0ATRfL5XwJTgEaqG5URT7zGeZCbb8Hmwkp7Iy9vjrvd7sJISIWNDPsqMRUZB29jdU4Rp1qaseqsLaG1xxdnjOXmzjy3ZAS1JgmZAcjDbanwOvWZBI5tU6oZBmBDhvCOlVydZBjgZDZD";
	private static final String FB_GRAPH_API_URL_MESSAGES = "https://graph.facebook.com/v2.6/me/messages?access_token=%s";
	private static final Logger logger = LoggerFactory.getLogger(SellaFbController.class);

	@GetMapping("/webhook")
	public ResponseEntity<?> verify(@RequestParam("hub.challenge") String challenge,
			@RequestParam("hub.verify_token") String token) {
		logger.info("Challenge is:{} and token is {}", challenge, token);
		if (token.equals("sellatoken123"))
			return new ResponseEntity<String>(challenge, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	@PostMapping(path = "/webhook", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> getMessage(@RequestBody final String payLoad,
			@RequestHeader(SIGNATURE_HEADER_NAME) final String signature) {
		logger.info("Response payload:{} && signature: {}", payLoad, signature);
		RequestPayload reqPayload=getResponseObject(payLoad);
		logger.info("reqpayload>>>>{}",reqPayload);
		String eventType=getEventType(reqPayload);
		String textMessage="";
		if(eventType.equals("PostbackEvent")) {
			textMessage=reqPayload.getEntry().get(0).getMessaging().get(0).getPostback().getPayload();
			logger.info("getPayload>>>>{}",textMessage);
		}else{
			 textMessage = reqPayload.getEntry().get(0).getMessaging().get(0).getMessage().getText();
			 logger.info("getRequestedText>>>>{}",textMessage);
		}
		String senderId= reqPayload.getEntry().get(0).getMessaging().get(0).getSender().getId();
		logger.info("senderId>>>>{}",senderId);
		UserDetail userDetail=getUserDetail(senderId);
		sendMessage(QnaResponse.getJsonResponse(senderId, textMessage,userDetail));
		return new ResponseEntity<String>("Success", HttpStatus.OK);
	}
	
	public void sendMessage(String payLoad) {
		String url = String.format(FB_GRAPH_API_URL_MESSAGES, ACCESS_TOKEN);
		logger.info("url>>>>{}",url);
		RestTemplate restTemplate=new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);	
		HttpEntity<String> entity = new HttpEntity<String>(payLoad, headers);		
		restTemplate.postForObject(url, entity, String.class);
	}
	
	public UserDetail getUserDetail(String senderId) {
		String formattedUrl = "https://graph.facebook.com/%saccess_token=%s";
		String url = String.format(formattedUrl, senderId + "?fields=first_name,last_name,profile_pic&", ACCESS_TOKEN);
		RestTemplate restTemplate=new RestTemplate();	
		UserDetail userDetail = restTemplate.getForObject(url, UserDetail.class);
		return userDetail;
	}
	
	
	
	
	/*public String getCarouselJson() {
		String jsonResponse="{   \"recipient\":{     \"id\":\"recipientid\"   },   \"message\":{     \"attachment\":{       \"type\":\"template\",       \"payload\":{         \"template_type\":\"generic\",         \"elements\":[            {             \"title\":\"Welcome!\",             \"image_url\":\" localhost:8080/img/conti_correnti_bse.gif\",             \"subtitle\":\"We have the right hat for everyone.\",             \"default_action\": {               \"type\": \"web_url\",               \"url\": \"https://petersfancybrownhats.com/view?item=103\",               \"webview_height_ratio\": \"tall\"             },             \"buttons\":[               {                 \"type\":\"web_url\",                 \"url\":\"https://petersfancybrownhats.com\",                 \"title\":\"View Website\"               },{                 \"type\":\"postback\",                 \"title\":\"Start Chatting\",                 \"payload\":\"You have invoked Welcome\"               }                          ]                }, 		   {             \"title\":\"NEW!\",             \"image_url\":\"https://chatbot-hook.herokuapp.com/img/image2.jpg\",             \"subtitle\":\"NEW subtitle.\",             \"default_action\": {               \"type\": \"web_url\",               \"url\": \"https://petersfancybrownhats.com/view?item=103\",               \"webview_height_ratio\": \"tall\"             },             \"buttons\":[               {                 \"type\":\"web_url\",                 \"url\":\"https://petersfancybrownhats.com\",                 \"title\":\"View Website\"               },{                 \"type\":\"postback\",                 \"title\":\"Start Chatting\",                 \"payload\":\"you have invoked New \"               }                          ]                }         ]       }     }   } }";
		logger.info("My Json Carousel::"+jsonResponse);
		return jsonResponse;
	}*/

	private RequestPayload getResponseObject(final String responsePayload) {		
		return JsonUtil.getInstance().fromJson(responsePayload, RequestPayload.class);
	}
	
	private String getEventType(RequestPayload requestPayload) {
		String eventType="TextEvent";
		if(requestPayload.getEntry().get(0).getMessaging().get(0).getPostback()!=null) {
			eventType="PostbackEvent";
		}
		logger.info("The requested event Tyepe {}",eventType);
		return eventType;		
	}
	
}
