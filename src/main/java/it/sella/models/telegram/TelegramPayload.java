package it.sella.models.telegram;

import java.util.Map;


/**
 * @author chitti
 *   "message":{"message_id":4,"
 *    from":{"id":638661856,"is_bot":false,"first_name":"Udayab","language_code":"en-GB"},
 *   "chat":{"id":638661856,"first_name":"Udayab","type":"private"},
 *   "date":1534318423,
 *   "text":"hi"}}
 */
public class TelegramPayload {
	private Long message_id;
	private Map<String, String> from;
	private Map<String, String> chat;
	private String date;
	private String text;

	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getMessage_id() {
		return message_id;
	}

	public void setMessage_id(Long message_id) {
		this.message_id = message_id;
	}

	public Map<String, String> getFrom() {
		return from;
	}

	public void setFrom(Map<String, String> from) {
		this.from = from;
	}

	public Map<String, String> getChat() {
		return chat;
	}

	public void setChat(Map<String, String> chat) {
		this.chat = chat;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TelegramPayload [message_id=");
		builder.append(message_id);
		builder.append(", from=");
		builder.append(from);
		builder.append(", chat=");
		builder.append(chat);
		builder.append(", date=");
		builder.append(date);
		builder.append(", text=");
		builder.append(text);
		builder.append("]");
		return builder.toString();
	}

	

}
