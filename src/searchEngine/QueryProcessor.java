package searchEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

public class QueryProcessor {

	public HashMap<String, Attribute> attributeMap;

	public QueryProcessor(HashMap<String, Attribute> attributeMap) {
		this.attributeMap = attributeMap;
	}

	public HashSet<String> getResults(ArrayList<HashMap<String, Object>> queue) {
		HashSet<String> resultSet = new HashSet<String>();
		Iterator subQueryIterator = queue.iterator();

		while (subQueryIterator.hasNext()) {
			HashMap<String, Object> operationMap = (HashMap) subQueryIterator.next();
			HashSet<String> tempResultSet = new HashSet<String>();
			for (String attributeName : operationMap.keySet()) {
				if (operationMap.get(attributeName) instanceof String) {
					tempResultSet = equalOperation(attributeName, (String) operationMap.get(attributeName),
							tempResultSet);
				} else if (operationMap.get(attributeName) instanceof TreeNode) {
					HashSet<String> dataSet = new HashSet<String>();
					HashSet<String> operationTotalSet = new HashSet<String>();

					if (attributeName.startsWith("~")) {
						dataSet = lessThanOperation(attributeName, (TreeNode) operationMap.get(attributeName));
						Iterator dataIterator = dataSet.iterator();
						while (dataIterator.hasNext()) {
							LinkedList<String> valueList = (LinkedList) attributeMap
									.get(attributeName.substring(1)).getAttributeMap().get(dataIterator.next());
							Iterator recordIterator = valueList.iterator();
							while (recordIterator.hasNext()) {
								operationTotalSet.add((String) recordIterator.next());
							}
						}
					} else {
						dataSet = greaterThanOperation(attributeName, (TreeNode) operationMap.get(attributeName));
						Iterator dataIterator = dataSet.iterator();
						while (dataIterator.hasNext()) {
							LinkedList<String> valueList = (LinkedList) attributeMap.get(attributeName).getAttributeMap()
									.get(dataIterator.next());
							Iterator recordIterator = valueList.iterator();
							while (recordIterator.hasNext()) {
								operationTotalSet.add((String) recordIterator.next());
							}
						}
					}

					if (tempResultSet.isEmpty()) {
						tempResultSet.addAll(operationTotalSet);
					} else {
						HashSet<String> intersectionSet = new HashSet<String>();
						Iterator recordIterator = operationTotalSet.iterator();
						while (recordIterator.hasNext()) {
							String row = (String) recordIterator.next();
							if (tempResultSet.contains(row)) {
								intersectionSet.add(row);
							}
						}
						tempResultSet = intersectionSet;
					}
				} else if (operationMap.get(attributeName) instanceof HashSet) {
					HashSet<String> queryResultSet = (HashSet<String>) operationMap.get(attributeName);
					HashSet<String> instersectionResultSet = new HashSet<String>();

					if (tempResultSet.isEmpty()) {
						tempResultSet.addAll(queryResultSet);
					} else {
						Iterator recordIterator = queryResultSet.iterator();
						while (recordIterator.hasNext()) {
							String row = (String) recordIterator.next();
							if (tempResultSet.contains(row)) {
								instersectionResultSet.add(row);
							}
						}
						tempResultSet = instersectionResultSet;
					}
				}
			}
			resultSet.addAll(tempResultSet);
		}
		return resultSet;
	}

	public HashSet<String> greaterThanOperation(String attribute, TreeNode node) {
		String nodeValue = node.getData();
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

	public HashSet<String> lessThanOperation(String attribute, TreeNode node) {
		String nodeValue = node.getData();
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

	public HashSet<String> getNodesFromTree(TreeNode newNode) {
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

	public HashSet<String> equalOperation(String attribute, String value, HashSet<String> resultSet) {
		LinkedList<String> valueList = (LinkedList) attributeMap.get(attribute).getAttributeMap().get(value);
		Iterator recordIterator = valueList.iterator();
		HashSet<String> tempResultSet = new HashSet<String>();

		if (resultSet.isEmpty()) {
			while (recordIterator.hasNext()) {
				resultSet.add((String) recordIterator.next());
			}
			return resultSet;
		} else {
			while (recordIterator.hasNext()) {
				String row = (String) recordIterator.next();
				if (resultSet.contains(row)) {
					tempResultSet.add(row);
				}
			}
			return tempResultSet;
		}

	}
}