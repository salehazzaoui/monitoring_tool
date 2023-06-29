package utils;

import java.util.Stack;

public class GenerateTree {
    private String[] outExp = new String[100];
    private int pos = 0;
    private String exp;
    private String current;
    private Stack<String> stack = new Stack<>();

    private boolean isOperator(String s) {
        if (s.charAt(0) == '-') {
            outExp[pos] = s.substring(0, 2);
            this.current = s.substring(0, 2);
            this.exp = s.substring(2);
            this.pos++;
            return true;
        } else if (s.charAt(0) == '|') {
            outExp[pos] = s.substring(0, 3);
            this.current = s.substring(0, 3);
            this.exp = s.substring(3);
            this.pos++;
            return true;
        } else if (s.charAt(0) == '[') {
            outExp[pos] = s.substring(0, 2);
            this.current = s.substring(0, 2);
            this.exp = s.substring(2);
            this.pos++;
            return true;
        } else if (s.charAt(0) == ';') {
            outExp[pos] = s.substring(0, 1);
            this.current = s.substring(0, 1);
            this.exp = s.substring(1);
            this.pos++;
            return true;
        }

        else
            return false;
    }

    private boolean isMethod(String s) {
        if (s.charAt(0) == 'D' || s.charAt(0) == 'F') {
            int i = 1;
            while (s.toLowerCase().charAt(i) >= 'a' && s.toLowerCase().charAt(i) <= 'z'
                    || s.charAt(i) >= '0' && s.charAt(i) == '9' && i < s.length()) {
                i++;
            }
            outExp[pos] = s.substring(0, i);
            this.current = s.substring(0, i);
            this.exp = s.substring(i);
            this.pos++;
            return true;
        } else
            return false;
    }

    private boolean isPar(String s) {
        if(s.charAt(0) == '(') {
            return true;
        }
        return false;
    }

    public BinaryTree<String> getComponentTree(String s) {
        BinaryTree<String> tree = new BinaryTree<String>(null);
        this.exp = s;
        while (!this.exp.equals("SKIP))")) {

            if (isMethod(this.exp)) {
                if (stack.isEmpty()) {
                    stack.push(this.current);
                    // System.out.println(this.corrent);
                } else {
                    tree.insert(stack.pop());
                    stack.push(this.current);
                    // System.out.println(this.corrent);
                }

            } else if (isOperator(this.exp)) {
                tree.insert(this.current);
                // System.out.println(this.corrent);
            }
            else if(isPar(s)){
                this.exp = this.exp.substring(1);
            }
        }
        tree.insert(stack.pop());
        this.exp = "SKIP";
        tree.insert(this.exp);
        return tree;
    }

    /*
    public SyntaxTree getConfigurationTree(Configuration conf) {
        String [] formula = conf.methodFormula(1).split("|||");
        SyntaxTree tree = new SyntaxTree();
        for(int i=0;i<formula.length;i++) {
            tree.node = "|||";
            tree.children.add(getComponentTree(formula[i]));
        }
        return tree;
    }
     */
}
