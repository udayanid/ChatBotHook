/**
 * 
 */
package it.sella.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author chitti
 *
 */
public class AppUtil {

	private final static ObjectMapper mapper = new ObjectMapper(new JsonFactory());

	public static ObjectMapper getObjectMapper() {
		return mapper;
	}

}
