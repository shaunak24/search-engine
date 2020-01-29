package searchEngine;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class SearchEngine {

	public static void main(String[] args) {

		List objectList = DataSource.getPersonData();
		List fieldList = DataSource.getPersonFields();

		Preprocessor preprocessor = new Preprocessor(objectList, fieldList);
		Map<String, Attribute> attributeObjectMap = preprocessor.getObjectMap();
		
        //String query = "(name = A or (address = E or id >= 4)) and (name = M or emailId = F)";
        //String query = "((name = A) and (address != E)) or (id <= 3)";
        String query = "((name = M or address = E) and id != 6)";

        QueryProcessor queryProcessor = new QueryProcessor(attributeObjectMap);
        HashSet<String> result = queryProcessor.processQuery(query);
		
		for (String str : result) {
			System.out.println(preprocessor.getDataMap().get(str));
        }
        System.out.println("####################################################################");
	}
}
