package com.flagpicker.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

/**
 * Util class to provide utility methods.
 * 
 * @author nikhil
 */
public class FlagSearchUtil {

	/**
	 * Method to convert JSON to Object
	 * 
	 * @param <T>
	 * @param jsonFileName
	 * @param tClass
	 * @return list of objects
	 * @throws IOException
	 */
	public static <T> List<T> jsonArrayToObjectList(String jsonFileName, Class<T> tClass) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		final File file = ResourceUtils.getFile(jsonFileName);
		CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass);
		List<T> ts = mapper.readValue(file, listType);
		return ts;
	}
}