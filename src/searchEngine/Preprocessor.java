package searchEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Preprocessor {
    
    private Map<String, Adapter> dataMap = new HashMap<String, Adapter>();
	private Map<String, Attribute> objectMap = new HashMap<String, Attribute>();
	private List<String> attributeNames;

	public Preprocessor(List objectList, List<String> attributeNames) {

        this.attributeNames = attributeNames;
        adapt(objectList);
		buildObjectMap(dataMap);
	}

	public void buildObjectMap(Map<String, Adapter> map) {

		for (String name : attributeNames) {
			List<Adapter> list = new ArrayList<>();

			for (Adapter record : map.values()) {
				list.add(record);
            }
            
			objectMap.put(name, new Attribute(name, list));
			objectMap.put("~" + name, new Attribute("~" + name, list));
		}
    }
    
    public void adapt(List objectList) {

		for (Object object : objectList) {
			Adapter adapter = AdapterFactory.getInstance("person", object);
			dataMap.put(adapter.getValue("id"), adapter);
		}
    }

	public Map<String, Attribute> getObjectMap() {
		return objectMap;
	}

	public List<String> getAttributeNames() {
		return attributeNames;
	}
}
