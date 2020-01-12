package searchEngine;

public class Person {

	private String id;
	private String name;
	private String address;
	private String emailId;

	public Person(String id, String name, String address, String emailId) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.emailId = emailId;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getEmailId() {
		return emailId;
	}
}