package searchEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Preprocessor {
	
	public HashMap<String, Attribute> objectMap = new HashMap<String, Attribute>();
	public List<String> attributeNames;

<<<<<<< HEAD
	public Preprocessor(HashMap<String, SearchEngineInterface> map, List<String> attributeNames) {
=======
	public Preprocessor(HashMap<String, SearchEngineInterface> map, List<String> attributeNames) {
>>>>>>> 58714085f95928b79c9c6e4a553ebbe1eb2aa3b7
		this.attributeNames = attributeNames;
		buildObjectMap(map);
	}

	public void buildObjectMap(Map<String, Adapter> map) {

		for (String name : attributeNames) {
			List<Adapter> list = new ArrayList();

			for (Adapter record : map.values()) {
				list.add(record);
			}
			objectMap.put(name, new Attribute(name, list));
			objectMap.put("~" + name, new Attribute("~" + name, list));
		}
	}

	public Map<String, Attribute> getObjectMap() {
		return objectMap;
	}

	public List<String> getAttributeNames() {
		return attributeNames;
	}
}