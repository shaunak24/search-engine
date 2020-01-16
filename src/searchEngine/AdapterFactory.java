package searchEngine;

public class AdapterFactory {
	
	public static Adapter getInstance(String type, Object object) {
		
		if("person".equals(type)) {
			return new PersonAdapter((Person) object);
		}
		return null;
	}
}
