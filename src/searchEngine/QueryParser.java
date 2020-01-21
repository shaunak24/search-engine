package searchEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;

public class QueryParser {

	public Map<String, HashSet<String>> queryMap = new HashMap<>();
	public Map<String, Attribute> attributeMap;

	public QueryParser(Map<String, Attribute> attributeMap) {
		this.attributeMap = attributeMap;
	}

	public HashSet<String> getResult(String query) {
		String subQuery = "";
		int queryNumber = 1;
		QueryProcessor queryProcessor = new QueryProcessor((HashMap<String, Attribute>) attributeMap);
		ArrayList<HashMap<String, Object>> queue;
		query = simplifyQuery(query);

		while (query.indexOf("(") != -1) {
			HashSet<String> result = new HashSet<String>();
			subQuery = getSubQuery(query);
			queue = processQuery(subQuery);
			result.addAll(queryProcessor.getResults(queue));
			queryMap.put("#query" + queryNumber, result);
			query = query.replace("(" + subQuery + ")", "#query" + queryNumber++);
		}

		HashSet<String> result = new HashSet<String>();
		queue = processQuery(query);
		result.addAll(queryProcessor.getResults(queue));
		queryMap.put("#query" + queryNumber, result);
		query = query.replace("(" + subQuery + ")", "#query" + queryNumber++);
		System.out.println("Final result Set" + result);

		return result; 
	}

	private String simplifyQuery(String query) {
		String newQuery = "", tempQuery = "";
		String[] tokens = query.split(" ");
		boolean querySplitNeeded = false;

		for (int tokenIndex = 0; tokenIndex < tokens.length; tokenIndex++) {
			if ("and".equals(tokens[tokenIndex]) || "or".equals(tokens[tokenIndex])) {
				if (querySplitNeeded) {
					String copyOfTempQuery = tempQuery;
					tempQuery = tempQuery.replace(">=", ">");
					tempQuery = tempQuery.replace("<=", "<");

					copyOfTempQuery = copyOfTempQuery.replace(">=", "=");
					copyOfTempQuery = copyOfTempQuery.replace("<=", "=");
					newQuery = newQuery + " ( " + tempQuery + " or " + copyOfTempQuery + " ) " + tokens[tokenIndex];
					querySplitNeeded = false;
				} else {
					newQuery = newQuery + " " + tempQuery + " " + tokens[tokenIndex];
				}
				tempQuery = "";
			} else {
				if (">=".equals(tokens[tokenIndex]) || "<=".equals(tokens[tokenIndex])) {
					querySplitNeeded = true;
				}
				tempQuery = tempQuery + " " + tokens[tokenIndex];
			}
		}
		if (querySplitNeeded) {
			String copyOfTempQuery = tempQuery;
			tempQuery = tempQuery.replace(">=", ">");
			tempQuery = tempQuery.replace("<=", "<");

			copyOfTempQuery = copyOfTempQuery.replace(">=", "=");
			copyOfTempQuery = copyOfTempQuery.replace("<=", "=");
			newQuery = newQuery + "(" + tempQuery + "or" + copyOfTempQuery + ")";
			querySplitNeeded = false;
		} else {
			newQuery = newQuery + tempQuery;
		}
		return newQuery;
	}

	private ArrayList<HashMap<String, Object>> processQuery(String query) {

		ArrayList<HashMap<String, Object>> queue = new ArrayList<>();
		String[] operations = query.split(" or ");

		for (String operation : operations) {
			String[] subOperations = operation.split(" and ");
			HashMap<String, Object> operationMap = new HashMap<>();

			for (String subOperation : subOperations) {
				subOperation = subOperation.trim();
				if (subOperation.startsWith("#query")) {
					operationMap.put(subOperation, queryMap.get(subOperation));
				} else {

					StringTokenizer parameter = new StringTokenizer(subOperation, "!><=", true);
					String attribute, operator, value;
					if (parameter.countTokens() == 3) {
						attribute = parameter.nextToken().trim();
						operator = parameter.nextToken().trim();
						value = parameter.nextToken().trim();
					} else {
						attribute = parameter.nextToken().trim();
						operator = parameter.nextToken().trim();
						operator += parameter.nextToken().trim();
						value = parameter.nextToken().trim();
					}

					if ("=".equals(operator)) {
						operationMap.put(attribute, value);
					} else if (">".equals(operator)) {
						operationMap.put(attribute, getNode(attribute, value, false));
					} else if ("<".equals(operator)) {
						operationMap.put("~" + attribute, getNode(attribute, value, false));
					}
					else if ("!=".equals(operator)) {
						operationMap.put("~" + attribute, value);
					}
				}
			}
			queue.add(operationMap);
		}

		return queue;

	}

	private TreeNode getNode(String attribute, String value, boolean includerRoot) {
		TreeNode searchPointer = attributeMap.get(attribute).getNodeRef();
		while (searchPointer != null) {
			if (value.equals(searchPointer.getData())) {
				return searchPointer;
			} else if (value.compareTo(searchPointer.getData()) > 0) {
				searchPointer = searchPointer.right;
			} else {
				searchPointer = searchPointer.left;
			}
		}

		return searchPointer;
	}

	private String getSubQuery(String query) {
		int startIndex = -1, endIndex = -1;
		for (int characterIndex = 0; characterIndex < query.length(); characterIndex++) {
			if (query.charAt(characterIndex) == '(') {
				startIndex = characterIndex;
			} else if (query.charAt(characterIndex) == ')') {
				endIndex = characterIndex;
				break;
			}
		}
		return query.substring(startIndex + 1, endIndex);
	}
}