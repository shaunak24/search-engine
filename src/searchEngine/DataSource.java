package searchEngine;

import java.util.ArrayList;
import java.util.List;

public class DataSource {
	
	public static List<Person> getPersonData() {
		List<Person> list = new ArrayList<>();
		list.add(new Person("1", "A", "B", "C"));
		list.add(new Person("2", "D", "E", "F"));
		list.add(new Person("3", "G", "H", "I"));
		list.add(new Person("4", "J", "K", "L"));
		list.add(new Person("5", "M", "N", "O"));
		list.add(new Person("6", "P", "Q", "R"));
		return list;
	}
	
	public static List<String> getPersonFields() {
		List<String> list = new ArrayList<>();
		list.add("id");
		list.add("name");
		list.add("address");
		list.add("emailId");
		return list;
	}
}
