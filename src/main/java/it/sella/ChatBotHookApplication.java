package it.sella;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.messenger4j.Messenger;

import it.sella.azure.AzureQnA;
import it.sella.telegram.Telegram;

@SpringBootApplication
public class ChatBotHookApplication {
	 @Bean
	    public Messenger messenger(@Value("${messenger4j.pageAccessToken}") String pageAccessToken,
	                               @Value("${messenger4j.appSecret}") final String appSecret,
	                               @Value("${messenger4j.verifyToken}") final String verifyToken) {
	        return Messenger.create(pageAccessToken, appSecret, verifyToken);
	    }
	 @Bean
	 public AzureQnA azureQnA(@Value("${sella.azureApiKey}")String apiKey) {
		 return AzureQnA.getInstance(apiKey);
	 }
	 @Bean
	 public Telegram telegram(@Value("${sella.telegramApiKey}")String apiKey) {
		 return Telegram.getInstance(apiKey);
	 }

	public static void main(String[] args) {
		SpringApplication.run(ChatBotHookApplication.class, args);
	}
}
