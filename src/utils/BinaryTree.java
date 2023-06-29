package utils;

public class BinaryTree<T extends Comparable> {
    private T node;
    private BinaryTree <T> left;
    private BinaryTree <T> right;
    private boolean heap = false;

    public BinaryTree(T value)
    {
        this.node = value;
        this.right = null;
        this.left = null;
    }

    public void insert(BinaryTree<T> root, T val){
        if(root.getNode() == null){
            root.setNode(val);
        }else {
            boolean isNotOperator = !isOperator((String) val);
            boolean isLeft = root.getLeft() != null ? (root.getLeft().getNode() == null || !isOperator((String) root.getLeft().getNode())) : true;
            if(isNotOperator && isLeft){
                if(root.getRight() == null){
                    if(root.getLeft() == null){
                        BinaryTree<T> child = new BinaryTree<>(val);
                        root.setLeft(child);
                    }else{
                        insert(root.getLeft(), val);
                    }
                }else {
                    insert(root.getRight(), val);
                }
            }else {
                if(root.getRight() == null){
                    BinaryTree<T> child = new BinaryTree<>(val);
                    root.setRight(child);
                }else{
                    insert(root.getRight(), val);
                }
            }
        }
    }

    private boolean isOperator(String val) {
        if(val.startsWith("-") || val.startsWith("[") || val.startsWith("|") || val.startsWith(";")){
            return true;
        }
        return false;
    }

    public void insert(T i)
    {
        if(this.getNode() == null)
        {
            this.buildTree(i);
            this.heap = true;
            //System.out.println("sommet fait sa valeur est : "+i+"\n");
        }
        else
        {
            if(((String)i).charAt(0) == 'D' || ((String)i).charAt(0) == 'F')
            {
                if(this.getRight() != null) {
                    this.getRight().insert(i);
                }
                else
                {
                    BinaryTree <T> a = new BinaryTree<>(i);
                    this.setLeft(a);
                }
            }
            else
            {
                if(this.getRight() == null)
                {
                    BinaryTree <T> a = new BinaryTree<T>(i);
                    this.setRight(a);
                }
                else
                {
                    this.getRight().insert(i);
                }
            }
        }
    }

    public boolean isEmpty()
    {
        if (this.node == null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void buildTree(T i)
    {
        this.setNode(i);
        this.right = null;
        this.left = null;
    }

    public void printTree()
    {
        if(this.getLeft() != null)
        {
            this.getLeft().printTree();
            //System.out.println("G");
        }

        System.out.print(this.getNode()+((this.heap) ? " <== sommet" : ""));

        if(this.getRight() != null)
        {
            this.getRight().printTree();
            //System.out.println("D");
        }
    }

    public boolean search(T x)
    {
        if(this.getNode() != null)
        {
            if(this.getNode().compareTo(x) == 0)
            {
                System.out.println("Recherche effectué avec Succès : elt "+this.getNode()+" trouvé ");
                return true;
            }

            if(this.getNode().compareTo(x) > 0 && this.getLeft() != null)
            {
                return this.getLeft().search(x);
            }

            if(this.getNode().compareTo(x) < 0 && this.getRight() != null)
            {
                return this.getRight().search(x);
            }
        }
        System.out.println("Recherche effectué avec Succès : elt "+x+" non trouvé ");
        return false;
    }

    public T getNode() {
        return node;
    }

    public void setNode(T node) {
        this.node = node;
    }

    public BinaryTree<T> getLeft() {
        return left;
    }

    public void setLeft(BinaryTree<T> left) {
        this.left = left;
    }

    public BinaryTree<T> getRight() {
        return right;
    }

    public void setRight(BinaryTree<T> right) {
        this.right = right;
    }
}
