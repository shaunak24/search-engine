package searchEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class searchEngine {
    
    private static void printInorder(TreeNode node) { 
        if (node == null) 
            return; 

        printInorder(node.left); 
        System.out.print(node.getData() + " "); 
        printInorder(node.right); 
    }

	public static void main(String[] args) {

		List objectList = DataSource.getPersonData();
        List fieldList = DataSource.getPersonFields();
        
        Preprocessor preprocessor = new Preprocessor(objectList, fieldList);
        Map<String, Attribute> attributeObjectMap = preprocessor.getObjectMap();
		
		// for (Map.Entry entry : preprocessor.getObjectMap().entrySet()) {
		// 	System.out.println(entry.getKey());
		// 	Attribute attribute = (Attribute) entry.getValue();
        //     System.out.println(attribute.getAttributeMap());
        //     printInorder(attribute.getNodeRef());
		// }
    }
}
    
