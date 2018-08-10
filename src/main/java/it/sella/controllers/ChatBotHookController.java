package it.sella.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

	@PostMapping("/webhook")
	public ResponseEntity<?> getMessage(@RequestBody String message) {
		System.out.println(message);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<?> sayConnected() {

		return new ResponseEntity<String>("Webhook is Listening", HttpStatus.OK);
	}
}
