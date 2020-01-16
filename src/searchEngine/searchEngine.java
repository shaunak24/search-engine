package searchEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class searchEngine {
	
	public static void main(String[] args) {

		List objectList = DataSource.getPersonData();
		List fieldList = DataSource.getPersonFields();

		Map<String, Adapter> dataMap = new HashMap<String, Adapter>();
		for (Object object : objectList) {
			Adapter adapter = AdapterFactory.getInstance("person", object);
			dataMap.put(adapter.getValue("id"), adapter);
		}

		//System.out.println(fieldList);
		//System.out.println(dataMap);
		
		Preprocessor preprocessor = new Preprocessor(dataMap, fieldList);
		
		for (Map.Entry entry : preprocessor.getObjectMap().entrySet()) {
			System.out.println(entry.getKey());
			Attribute attribute = (Attribute) entry.getValue();
			System.out.println(attribute.getAttributeMap());
		}
	}
}
