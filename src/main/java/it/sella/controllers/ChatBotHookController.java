package it.sella.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatBotHookController {
	@GetMapping("/")
	public String sayHello() {
		return "Ready to hook";
	}

	@PostMapping("/query")
	public String getQuery(@RequestParam("message") String message) {
		return "Got the message " + message;
	}

	@PostMapping("/message")
	public String getMessage(@RequestBody String message) {
		return message;
	}
}
