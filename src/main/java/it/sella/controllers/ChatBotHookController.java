package it.sella.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatBotHookController {

	@GetMapping("/webhook")
	public ResponseEntity<?> verify(@RequestParam("hub.challenge") String challenge,
			@RequestParam("hub.verify_token") String token) {
		if (token.equals("mycustomtoken23"))
			return new ResponseEntity<String>(challenge, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	// @PostMapping("/webhook")
	// public ResponseEntity<?> getMessage(HttpServletRequest
	// request,HttpServletResponse response) {
	// System.out.println(request.getContentType());
	// return new ResponseEntity<>(HttpStatus.OK);
	// }

	@PostMapping("/webhook")
	public ResponseEntity<?> getMessage(@RequestBody final String payLoad,
			@RequestHeader("X-Hub-Signature") final String signature) {
		System.out.println(signature);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<?> sayConnected() {

		return new ResponseEntity<String>("Webhook is Listening", HttpStatus.OK);
	}
}
