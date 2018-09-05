package it.sella.util;

import com.google.gson.Gson;

public class JsonBuilder {

	private static Gson gson = new Gson();

	public static Gson getInstance() {
		return gson;
	}

//	public  T getJson(String jsonString, Class<T> classType){
//		return (T) gson.fromJson(jsonString, classType);
//	}
}
