package searchEngine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class SearchEngine {

	public static void main(String[] args) {

		List objectList = DataSource.getPersonData();
		List fieldList = DataSource.getPersonFields();

		Preprocessor preprocessor = new Preprocessor(objectList, fieldList);
		Map<String, Attribute> attributeObjectMap = preprocessor.getObjectMap();
		
		String query = "(name = A or (address = E or id > 4)) and (name = M or emailId = F)";
		QueryParser parser = new QueryParser(attributeObjectMap);
		HashSet<String> result = parser.getResult(query);
		
		for (String str : result) {
			System.out.println(preprocessor.getDataMap().get(str));
		}
	}
}
