package searchEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Attribute {

	private String attributeName;
	private HashMap<String, LinkedList<String>> attributeMap = new HashMap<String, LinkedList<String>>();
	private TreeNode nodeRef;

	public Attribute(String attributeName, List<Adapter> list) {

		this.attributeName = attributeName;
		if (attributeName.startsWith("~")) {
			buildNegativeAttributeMap(list);
		} else {
			buildPositiveAttributeMap(list);
		}
		this.nodeRef = buildAttributeTree(list);
		this.attributeMap = getAttributeMap();
	}

	public TreeNode buildAttributeTree(List list) {

		List<String> keyList = new ArrayList<>(attributeMap.keySet());
		Collections.sort(keyList);
		return sortedListToTree(keyList, 0, keyList.size() - 1);
	}

	public TreeNode sortedListToTree(List<String> keyList, int start, int end) {

		if (start > end) {
			return null;
		}

		int mid = (start + end) / 2;
		TreeNode treeNode = new TreeNode(keyList.get(mid));
		treeNode.left = sortedListToTree(keyList, start, mid - 1);
		treeNode.right = sortedListToTree(keyList, mid + 1, end);
		return treeNode;
	}

	public void buildNegativeAttributeMap(List<Adapter> list) {

		HashSet<String> uniqueSet = new HashSet<String>();

		for (Adapter record : list) {
			String attributeValue = record.getValue(attributeName.substring(1));
			if (!uniqueSet.contains(attributeValue)) {
				uniqueSet.add(attributeValue);
				attributeMap.put(attributeValue, new LinkedList<String>());
			}
		}

		Iterator uniqueSetIterator = uniqueSet.iterator();

		while (uniqueSetIterator.hasNext()) {
			String attributeValue = (String) uniqueSetIterator.next();
			for (Adapter record : list) {
				if (!attributeValue.equals(record.getValue(attributeName.substring(1)))) {
					attributeMap.get(attributeValue).add(record.getValue("id"));
				}
			}
		}
	}

	public void buildPositiveAttributeMap(List<Adapter> list) {

		// Create map<unique values, linkedList> based on attributeName
		for (Adapter record : list) {
			String positiveValue = record.getValue(attributeName);

			if (attributeMap.containsKey(positiveValue)) {
				attributeMap.get(positiveValue).add(record.getValue("id"));
			} else {
				LinkedList<String> linkedList = new LinkedList<>();
				linkedList.add(record.getValue("id"));
				attributeMap.put(positiveValue, linkedList);
			}
		}
	}

	public String getAttributeName() {

		return attributeName;
	}

	public HashMap<String, LinkedList<String>> getAttributeMap() {

		return attributeMap;
    }
    
    public TreeNode getNodeRef() {

        return nodeRef;
    }
}
