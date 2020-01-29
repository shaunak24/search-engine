package searchEngine;

import java.util.ArrayList;
import java.util.Arrays;
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

        ArrayList<String> tokensList = getTokens(query);
        System.out.println(tokensList);
        Stack<String> operator = new Stack<>();
        Stack<HashSet<String>> operand = new Stack<>();
        
        for (int i = 0; i < tokensList.size(); i++) {

            System.out.println(i + " " + tokensList.get(i));
            
            if ("(".equals(tokensList.get(i))) {
                operator.push(tokensList.get(i));
            }
            else if (")".equals(tokensList.get(i))) {
                while (!"(".equals(operator.peek())) {
                    operand.push(applyOperator(operator.pop(), operand.pop(), operand.pop()));
                }
                operator.pop();
            }
            else if ("and".equals(tokensList.get(i)) || "or".equals(tokensList.get(i))) {

                while (!operator.empty() && hasPrecedence(tokensList.get(i), operator.peek())) { 
                  operand.push(applyOperator(operator.pop(), operand.pop(), operand.pop())); 
                }
                operator.push(tokensList.get(i)); 
            }
            else {
                operand.push(executeQuery(new Query(tokensList.get(i), tokensList.get(i + 1),
                             tokensList.get(i + 2))));
                i += 2;
            }
            System.out.println("Operator Stack: " + operator);
            System.out.println("Operand Stack: " + operand);
        }

        while (!operator.empty()) {
            operand.push(applyOperator(operator.pop(), operand.pop(), operand.pop())); 
        }

        System.out.println("Final Result Set: " + operand.peek());
        return operand.pop();
    }

    private ArrayList<String> getTokens(String query) {
        ArrayList<String> tokensList = new ArrayList<>();
        StringBuilder temp = new StringBuilder();

        for (char element : query.toCharArray()) {
            if (' ' == element) {
                tokensList.add(temp.toString());
                temp = temp.delete(0, temp.length());
                continue;
            }
            if ('(' == element || ')' == element) {
                if (!"".equals(temp)) {
                    tokensList.add(temp.toString());
                    temp = temp.delete(0, temp.length());
                }
                tokensList.add(String.valueOf(element));
                continue;
            }
            temp.append(element);
        }
        if (!"".equals(temp)) {
            tokensList.add(temp.toString());
        }

        tokensList.removeAll(Arrays.asList(""));
        return tokensList;
    }

    private HashSet<String> executeQuery(Query query) {

        switch (query.getOperator()) {
            case "=":
                return equalTo(query.getAttribute(), query.getValue());
            case ">":
                return greaterThan(query.getAttribute(), query.getValue());
            case "<":
                return lessThan(query.getAttribute(), query.getValue());
            case ">=":
                return applyOperator("or", equalTo(query.getAttribute(), query.getValue()),
                    greaterThan(query.getAttribute(), query.getValue()));
            case "<=":
                return applyOperator("or", equalTo(query.getAttribute(), query.getValue()),
                    lessThan(query.getAttribute(), query.getValue()));
            case "!=":
                break;
        }

        return null;
    }

    private HashSet<String> lessThan(String attribute, String value) {
        
		HashSet<String> tempResultSet = new HashSet<String>();
        TreeNode rootNode = attributeMap.get(attribute).getNodeRef();
        System.out.println("RootNode: " + rootNode.getData());

		while (rootNode != null) {
			if (rootNode.getData().compareTo(value) >= 0) {
				rootNode = rootNode.left;
			} else {
                tempResultSet.add(rootNode.getData());
				tempResultSet.addAll(getNodesFromTree(rootNode.left));
				rootNode = rootNode.right;
			}
		}
		return tempResultSet;
    }

    private HashSet<String> greaterThan(String attribute, String value) {

		HashSet<String> tempResultSet = new HashSet<String>();
        TreeNode rootNode = attributeMap.get(attribute).getNodeRef();

		while (rootNode != null) {
			if (rootNode.getData().compareTo(value) <= 0) {
				rootNode = rootNode.right;
			} else {
                tempResultSet.add(rootNode.getData());
				tempResultSet.addAll(getNodesFromTree(rootNode.right));
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

        if ("(".equals(op2) || ")".equals(op2)) 
            return false; 
        else
            return true; 
    } 
}