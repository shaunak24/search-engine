package searchEngine;

import java.util.HashSet;
import java.util.Map;
import java.util.Stack;

public class QueryProcessor {

    private Map<String, Attribute> attributeMap;
    
    public QueryProcessor(Map<String, Attribute> attributeMap) {
        
        this.attributeMap = attributeMap;
    }

    public HashSet<String> processQuery(String query) {

        HashSet<String> resultSet = new HashSet<>();
        Stack<String> operator = new Stack<>();
        Stack<HashSet<String>> operand = new Stack<>();
        
        for (int i = 0; i < query.length(); i++) {

            String token = String.valueOf(query.charAt(i));
            if (" ".equals(token)) {
                continue;
            }
            if ("(".equals(token)) {
                operator.push(String.valueOf(token));
            }
            else if (")".equals(token)) {
                while (operator.peek() != "(") {
                    operand.push(applyOperator(operator.pop(), operand.pop(), operand.pop()));
                }
                operator.pop();
            }
            else if ("and".equals(token) || "or".equals(token)) {

                while (!operator.empty() && hasPrecedence(token, operator.peek())) { 
                  operand.push(applyOperator(operator.pop(), operand.pop(), operand.pop())); 
                }
                operator.push(token); 
            }
            else {
                StringBuilder subQuery = new StringBuilder(token);
            }
        }

        return resultSet;
    }

    private HashSet<String> applyOperator(String operator, HashSet<String> set1, HashSet<String> set2) {
        
        if ("and".equals(operator)) {
            set1.retainAll(set2);
            return set1;
        }
        else {
            set1.addAll(set2);
            return set1;
        }
    }

    private boolean hasPrecedence(String op1, String op2) {

        if (op2 == "(" || op2 == ")") 
            return false; 
        else
            return true; 
    } 
}