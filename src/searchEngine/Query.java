package searchEngine;

public class Query {

    private String attribute;
    private String operator;
    private String value;

    public Query(String attribute, String operator, String value) {
        this.attribute = attribute;
        this.operator = operator;
        this.value = value;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getOperator() {
        return operator;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return attribute + " " + operator + " " + value;
    }
}