package it.sella;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.sella.model.UserDetail;

public class QnaResponse {
	private static final Logger logger = LoggerFactory.getLogger(QnaResponse.class);


	public static String getJsonResponse(String senderId, String requestText, UserDetail userDetail) {
		String jsonResponse = "";
		switch (requestText.toLowerCase().trim()) {
		case "hi":
		case "hai":
		case "hello": 
			jsonResponse=String.format("{ \"recipient\": { \"id\": \"recipientId\" }, \"message\": { \"text\": \"%s\" } }","Hello "+userDetail.getFirstName()+" "+userDetail.getLastName());
		break;
		case "carte di debito":
		case "debito":
			jsonResponse="{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"CARTE DI DEBITO\", \"image_url\":\"https://chatbot-hook.herokuapp.com/img/cirrus_maestro_basic.jpg\", \"subtitle\":\"Ricevila a casa o ritirala in succursale\\n Saldo del c/c in tempo reale\\n Max sicurezza con Chip & PIN \\n Paghi e prelevi ovunque con il circuito Maestro \\n Accedi a promozioni dedicate \\n Scopri un mondo di servizi\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/carte/cirrus-maestro-basic.jsp\", \"title\":\"For More Details\" }, { \"type\":\"web_url\", \"url\":\"https://www.sella.it/AuthenticationDelegatedServlet?delegated_service=219&SECODE=ONBOARDING_CL&SEACTION=LOGIN&SEPARAMS=parameterCode=CIRRUSBASIC\", \"title\":\"On Boarding\" }, { \"type\":\"postback\", \"title\":\"Any Other Query\", \"payload\":\"call our representative\" } ] } ] } } } }";
			break;
		case "credito":
		case "visa classic":
		case "mastercard classic":
		case "carte di credito":
			jsonResponse="{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"VISA CLASSIC\", \"image_url\":\"https://chatbot-hook.herokuapp.com/img/cirrus_maestro_basic.jpg\", \"subtitle\":\"Con Visa Classic puoi fare acquisti e prelievi in tutto il mondo, con la garanzia della massima sicurezza grazie alla tecnologia Chip & PIN e al servizio Verified by Visa.\\n Canone primo anno 31 €\\n Gratis oltre i 7000 € di spese/anno\\n Sicura con Chip & PIN e Verified by Visa\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/carte/visa-classic.jsp\", \"title\":\"For More Details\" }, { \"type\":\"web_url\", \"url\":\"https://www.sella.it/AuthenticationDelegatedServlet?delegated_service=219&SECODE=ONBOARDING_CL&SEACTION=LOGIN&SEPARAMS=parameterCode=VISA_CLASSIC_BSE\", \"title\":\"On Boarding\" }, { \"type\":\"postback\", \"title\":\"Any Other Query\", \"payload\":\"call our representative\" } ] }, { \"title\":\"MASTERCARD CLASSIC\", \"image_url\":\"https://chatbot-hook.herokuapp.com/img/cirrus_maestro_basic.jpg\", \"subtitle\":\"Mastercard Classic è la carta di credito che prevede, se utilizzata a saldo, un addebito unico mensile del tuo speso. E' adatta per la gestione della tua liquidità e ti permette di effettuare pagamenti, anche tramite la funzionalità contatless\\n Canone primo anno 31 € \\n Gratis oltre i 7000 € di spese/anno \\n Sicura con Chip & PIN e Mastercard SecureCode\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/carte/mastercard-classic.jsp\", \"title\":\"For More Details\" }, { \"type\":\"web_url\", \"url\":\"https://www.sella.it/AuthenticationDelegatedServlet?delegated_service=219&SECODE=ONBOARDING_CL&SEACTION=LOGIN&SEPARAMS=parameterCode=MASTERCARD_CLASSIC\", \"title\":\"On Boarding\" }, { \"type\":\"postback\", \"title\":\"Any Other Query\", \"payload\":\"call our representative\" } ] } ] } } } }";
			break;
		case "hype plus":
		case "hype":
		case "plus":
			jsonResponse="{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"HYPE PLUS\", \"image_url\":\"https://chatbot-hook.herokuapp.com/img/hype_bse.gif\", \"subtitle\":\"HYPE Plus è semplice e smart: controlla tutto ciò che serve dal tuo smartphone\\n Puoi ricaricare la tua carta fino a 50.000€ all'anno\\n Prelievi gratuiti da tutti gli ATM in Italia e nel mondo\\n Visualizzi all'istante i movimenti della tua carta\\n La metti in pausa quando vuoi, direttamente dall'app\\n Hai il tuo IBAN attivo in pochi minuti\\n Puoi pagare con il tuo smartphone\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/carte/hype.jsp\", \"title\":\"For More Details\" }, { \"type\":\"web_url\", \"url\":\"https://www.hype.it/signup/request/hype/contact-info\", \"title\":\"On Boarding\" }, { \"type\":\"postback\", \"title\":\"Any Other Query\", \"payload\":\"call our representative\" } ] }  ] } } } }";
		break;
		case "all carte details":
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"CARTE DI DEBITO\", \"image_url\":\"https://chatbot-hook.herokuapp.com/img/cirrus_maestro_basic.jpg\", \"subtitle\":\"CARTE\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"CARTE DI DEBITO\", \"payload\":\"CARTE DI DEBITO\" } ] }, { \"title\":\"CARTE DI CREDITO\", \"image_url\":\"https://chatbot-hook.herokuapp.com/img/cirrus_maestro_basic.jpg\", \"subtitle\":\"CARTE\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"CARTE DI CREDITO\", \"payload\":\"CARTE DI CREDITO\" } ] }, { \"title\":\"HYPE PLUS\", \"image_url\":\"https://chatbot-hook.herokuapp.com/img/hype_bse.gif\", \"subtitle\":\"CARTE\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"HYPE PLUS\", \"payload\":\"HYPE PLUS\" } ] } ] } } } }";
			break;
		case "conti":
		case "list conti":	
			jsonResponse ="{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"CONTI\", \"image_url\":\"https://chatbot-hook.herokuapp.com/img/conti_correnti_bse.gif\", \"subtitle\":\"WEBSELLA\", \"default_action\":{ \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/websella.jsp\", \"webview_height_ratio\":\"compact\" }, \"buttons\":[{ \"type\":\"postback\", \"title\":\"Websella\", \"payload\":\"websella\" } ] } ] } } } }";
			break;
		case "websella":
			jsonResponse="{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"Canone mensile zero euro  0 Euro\\n Una carta di debito a zero euro\\n operatività 24h/24h \\n13h al giorno di assistenza\\n4 prelievi gratuiti al mese da tutti gli ATM UE\\n300 succursali\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/websella.jsp\", \"title\":\"For View More\" }, { \"type\":\"web_url\", \"url\":\"https://www.sella.it/onboarding/fe/03268/contowebsella/#/start/CONTO_WEBSELLA\", \"title\":\"Apri Il Conto WebSella\" } ] } } } }";
			break;
		case "cirrus maestro basic":
			jsonResponse="{ \"recipient\": { \"id\": \"recipientId\" }, \"message\": { \"text\": \"You are applying cirrus maestro basic \" } }";
			break;
		case "hypeplus application":
			jsonResponse="{ \"recipient\": { \"id\": \"recipientId\" }, \"message\": { \"text\": \"You are applying hypeplus application \" } }";
			break;
		case "first hand shake":
			jsonResponse="{ \"recipient\": { \"id\": \"recipientId\" }, \"message\": { \"text\": \"Welcome to Sella Chatbot \" } }";
			break;			
		case "list products":
		case "products":
		case "product":
		case "our products":
			jsonResponse="{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"Our Products!!!\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"CONTI\", \"payload\":\"list conti\" }, { \"type\":\"postback\", \"title\":\"CARTE\", \"payload\":\"list carte\" }, { \"type\":\"postback\", \"title\":\"INVESTIMENTI E RISPARMIO\", \"payload\":\"list investimenti e risparmio\" } ] } } } }";
			break;
		case "list carte":
		case "carte":		
		jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"Our Carte!!!\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"CARTE DI DEBITO\", \"payload\":\"all carte details\" }, { \"type\":\"postback\", \"title\":\"CARTE DI CREDITO\", \"payload\":\"all carte details\" }, { \"type\":\"postback\", \"title\":\"HYPE PLUS\", \"payload\":\"all carte details\" }  ] } } } }";
		break;
		case "list investimenti e risparmio":
		case "investimenti":
		case "list investimenti":
		case "investments":
		case "investment":
		case "savings":		
		case "saving":
		jsonResponse="{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"Our investimenti!!!\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"CONSULENZA INVESTIMENTI\", \"payload\":\"all investimenti details\" }, { \"type\":\"postback\", \"title\":\"SELLA EVOLUTION GESTIONI PATRIMONIALI\", \"payload\":\"all investimenti details\" }, { \"type\":\"postback\", \"title\":\"SELLA MULTISOLUTION\", \"payload\":\"all investimenti details\" } ] } } } }";
		break;
		case "all investimenti details":
			jsonResponse="{ \"recipient\": { \"id\": \"recipientId\" }, \"message\": { \"text\": \"under development.....\" } }";
		break;
		case "call our representative":
			jsonResponse ="{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"For further assistance,please talk to our representative\", \"buttons\":[ { \"type\":\"phone_number\", \"title\":\"Call Representative\", \"payload\":\"+02224410023\" } ] } } } }";
			break;
		default:
			jsonResponse="{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"You query is not matched, So please talk to our representative\", \"buttons\":[ { \"type\":\"phone_number\", \"title\":\"Call Representative\", \"payload\":\"+02224410023\" } ] } } } }";
			break;
		}
		jsonResponse = jsonResponse.replace("recipientId", senderId);
		logger.info("jsonresponse>>>>{}",jsonResponse);
		return jsonResponse;
	}
}


//jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"CARTE DI DEBITO\", \"image_url\":\"https://chatbot-hook.herokuapp.com/img/cirrus_maestro_basic.jpg\", \"subtitle\":\"CIRRUS MAESTRO BASIC\", \"default_action\":{ \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/websella.jsp\", \"webview_height_ratio\":\"compact\" }, \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/AuthenticationDelegatedServlet?delegated_service=219&SECODE=ONBOARDING_CL&SEACTION=LOGIN&SEPARAMS=parameterCode=CIRRUSBASIC\", \"title\":\"On Boarding\" }, { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/carte/cirrus-maestro-basic.jsp\", \"title\":\"For View More\" }, { \"type\":\"postback\", \"title\":\"CIRRUS MAESTRO BASIC \", \"payload\":\"CIRRUS MAESTRO BASIC\" } ] }, { \"title\":\"For Further Assistence\", \"image_url\":\"https://chatbot-hook.herokuapp.com/img/conti_correnti_bse.gif\", \"subtitle\":\"VISA CLASSIC \\n MASTERCARD CLASSIC\", \"default_action\":{ \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/websella.jsp\", \"webview_height_ratio\":\"compact\" }, \"buttons\":[ { \"type\":\"postback\", \"title\":\"VISA CLASSIC\", \"payload\":\"VISA CLASSIC\" }, { \"type\":\"postback\", \"title\":\"MASTERCARD CLASSIC\", \"payload\":\"On boarding clicked!!!\" } ] }, { \"title\":\"Other Cards\", \"image_url\":\"https://chatbot-hook.herokuapp.com/img/hype_bse.gif\", \"subtitle\":\"HYPEPLUS\", \"default_action\":{ \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/websella.jsp\", \"webview_height_ratio\":\"compact\" }, \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/carte/hype.jsp\", \"title\":\"For View More\" }, { \"type\":\"web_url\", \"url\":\"https://www.hype.it/signup/request/hype/contact-info\", \"title\":\"On Boarding\" }, { \"type\":\"postback\", \"title\":\"Click here to apply\", \"payload\":\"hypeplus application\" } ] } ] } } } }";

