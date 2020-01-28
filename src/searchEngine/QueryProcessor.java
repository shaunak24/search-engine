package searchEngine;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

public class QueryProcessor {

    private Map<String, Attribute> attributeMap;
    
    public QueryProcessor(Map<String, Attribute> attributeMap) {
        
        this.attributeMap = attributeMap;
    }

    public HashSet<String> processQuery(String query) {

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
                StringBuilder subQuery = new StringBuilder();
                int spaces = 0;
                for (int index = i; index < query.length(); index++) {
                    if (' ' == query.charAt(index)) {
                        spaces++;
                    }
                    if (spaces == 3 || ')' == query.charAt(index)) {
                        i = index - 1;
                        break;
                    }
                    subQuery.append(query.charAt(index));
                }
                String[] queryTokens = subQuery.toString().trim().split(" ");
                operand.push(executeQuery(new Query(queryTokens[0], queryTokens[1], queryTokens[2])));
            }
        }

        while (!operator.empty()) {
            operand.push(applyOperator(operator.pop(), operand.pop(), operand.pop())); 
        }

        return operand.pop();
    }

    private HashSet<String> executeQuery(Query query) {

        switch (query.getOperator()) {
            case "=":
                return equalTo(query.getAttribute(), query.getValue());
            case ">":
                return greaterThan(query.getAttribute(), attributeMap.get(query.getAttribute()).getNodeRef());
            case "<":
                return lessThan(query.getAttribute(), attributeMap.get(query.getAttribute()).getNodeRef());
            case ">=":
                return applyOperator("and", equalTo(query.getAttribute(), query.getValue()),
                    greaterThan(query.getAttribute(), attributeMap.get(query.getAttribute()).getNodeRef()));
            case "<=":
                return applyOperator("and", equalTo(query.getAttribute(), query.getValue()),
                    lessThan(query.getAttribute(), attributeMap.get(query.getAttribute()).getNodeRef()));
            case "!=":
                break;
        }

        return null;
    }

    private HashSet<String> lessThan(String attribute, TreeNode nodeRef) {
        
        String nodeValue = nodeRef.getData();
		HashSet<String> tempResultSet = new HashSet<String>();
		TreeNode rootNode = attributeMap.get(attribute).getNodeRef();

		while (rootNode != null) {
			if (rootNode.getData().compareTo(nodeValue) > 0) {
				tempResultSet.add(rootNode.getData());
				tempResultSet.addAll(getNodesFromTree(rootNode.right));
				rootNode = rootNode.left;
			} else {
				rootNode = rootNode.right;
			}
		}
		return tempResultSet;
    }

    private HashSet<String> greaterThan(String attribute, TreeNode nodeRef) {

        String nodeValue = nodeRef.getData();
		HashSet<String> tempResultSet = new HashSet<String>();
		TreeNode rootNode = attributeMap.get(attribute).getNodeRef();

		while (rootNode != null) {
			if (rootNode.getData().compareTo(nodeValue) < 0) {
				tempResultSet.add(rootNode.getData());
				tempResultSet.addAll(getNodesFromTree(rootNode.left));
				rootNode = rootNode.right;
			} else {
				rootNode = rootNode.left;
			}
		}
		return tempResultSet;
    }

    private HashSet<String> getNodesFromTree(TreeNode newNode) {
		HashSet<String> tempResultSet = new HashSet<String>();
		Stack<TreeNode> nodeStack = new Stack<TreeNode>();
		while (!nodeStack.isEmpty() || newNode != null) {
			if (newNode != null) {
				nodeStack.push(newNode);
				newNode = newNode.left;
			} else {
				newNode = nodeStack.pop();
				tempResultSet.add(newNode.getData());
				newNode = newNode.right;

			}
		}
		return tempResultSet;
	}

    private HashSet<String> equalTo(String attribute, String value) {

        LinkedList linkedList = attributeMap.get(attribute).getAttributeMap().get(value);
        HashSet<String> set = new HashSet<>();
        set.addAll(linkedList);
        
        return set;
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