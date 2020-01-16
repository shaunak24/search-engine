package searchEngine;

public class PersonAdapter implements Adapter {

	private Person person;

	public PersonAdapter(Person person) {

		this.person = person;
	}

	@Override
	public String getValue(String field) {

		switch (field) {
		case "id":
			return person.getId();
		case "name":
			return person.getName();
		case "address":
			return person.getAddress();
		case "emailId":
			return person.getEmailId();
		default:
			break;
		}
		return null;
	}
}
