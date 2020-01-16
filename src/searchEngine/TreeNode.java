package searchEngine;

public class TreeNode {

	private String data;
	TreeNode left, right;

	public TreeNode(String data) {

		this.data = data;
		this.left = null;
		this.right = null;
	}
	
	public TreeNode(String data, TreeNode left, TreeNode right) {
		
		this.data = data;
		this.left = left;
		this.right = right;
	}

	public String getData() {
		return this.data;
	}
}
