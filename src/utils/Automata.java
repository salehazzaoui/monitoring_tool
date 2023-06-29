package utils;

public class Automata {

    public static BinaryTree<String> getComplexTree(BinaryTree<String> root, String expression) {
        String operator = "";
        String left = "";
        String right = "";

        if (!expression.contains("[]")) {
            root = getSimpleTree(root, expression);
            return root;
        }

        int i = 0;
        while(i < expression.length() && expression.charAt(i) != '['){
            i++;
        }

        operator = expression.substring(i, i+2);
        left = expression.substring(0, i);
        right = expression.substring(i+2);

        root.setNode(operator);

        BinaryTree<String> leftChild = new BinaryTree<>(null);
        leftChild = getSimpleTree(leftChild, left);
        root.setLeft(leftChild);

        BinaryTree<String> rightChild = new BinaryTree<>(null);
        rightChild = getComplexTree(rightChild, right);
        root.setRight(rightChild);

        return root;
    }

    private static BinaryTree<String> getSimpleTree(BinaryTree<String> root, String expression){
        String operator = "";
        String left = "";
        String right = "";

        int i = 0;
        while(i < expression.length() && !isOperator(String.valueOf(expression.charAt(i)))){
            i++;
        }
        int j = 0;
        operator = String.valueOf(expression.charAt(i)).equals(";") ? expression.substring(i, j = i+1)
                : String.valueOf(expression.charAt(i)).equals("|") ? expression.substring(i, j = i+3)
                : expression.substring(i, j = i+2);

        left = expression.substring(0, i);
        if(left.startsWith("(")){
            left = left.substring(1);
        }
        if(left.endsWith(")")){
            left = left.substring(0, left.length()-1);
        }

        right = expression.substring(j);
        if(right.startsWith("(")){
            right = right.substring(1);
        }
        if(right.endsWith(")")){
            right = right.substring(0, right.length()-1);
        }

        root.insert(root, operator);
        root.insert(root, left);
        if(isEnd(right)){
            return getSimpleTree(root, right);
        }

        root.insert(right);
        return root;
    }

    public static boolean isOperator(String val) {
        if(val.startsWith("-") || val.startsWith("[") || val.startsWith("|") || val.startsWith(";")){
            return true;
        }
        return false;
    }

    private static boolean isEnd(String val){
        return (val.contains("|||") || val.contains("[]") || val.contains("->") || val.contains(";"));
    }

    private boolean isPar(String val) {
        if(val.charAt(0) == '(') {
            return true;
        }
        return false;
    }
}
