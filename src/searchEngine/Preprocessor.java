package searchEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Preprocessor {
	
	public HashMap<String, Attribute> objectMap = new HashMap<String, Attribute>();
	public List<String> attributeNames;

	public DatabasePreprocessing(HashMap<String, SearchEngineInterface> map, List<String> attributeNames) {
		this.attributeNames = attributeNames;
		buildObjectMap(map);
	}

	public void buildObjectMap(HashMap<String, SearchEngineInterface> map) {

		for (String name : attributeNames) {
			List<SearchEngineInterface> list = new ArrayList();

			for (SearchEngineInterface record : map.values()) {
				list.add(record);
			}
			objectMap.put(name, new Attribute(name, list, attributeNames.get(0)));
			objectMap.put("~" + name, new Attribute("~" + name, list, attributeNames.get(0)));
		}
	}

	public Map<String, Attribute> getObjectMap() {
		return objectMap;
	}

	public List<String> getAttributeNames() {
		return attributeNames;
	}
}
