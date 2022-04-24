package sk.stuba.fiit.xbartalosm.deprecated;

public class BDDTree {

    private BDDNode root;
    private int size = 0;

    //Start the tree generation
    public BDDTree BDD_create(String expression, String order){

        root = new BDDNode(order);
        root.setSubExpression(expression);
        root.processExpression();

        return this;

    }

    public void traverseInOrderRecursive(BDDNode currentNode, int lvl) {

        if (currentNode != null) {
            traverseInOrderRecursive(currentNode.getLeftNode(),lvl+1);
            System.out.println(currentNode.getSubExpression() + " - Level: " + lvl + "\tValue:" + currentNode.getValue() + " Name: " + currentNode.getName()+
                    " Id: " + currentNode.getId());
            traverseInOrderRecursive(currentNode.getRightNode(),lvl+1);
        }

    }


    public void countNodesRecursive(BDDNode currentNode, int number) {

        //TODO: implemmentalni
        if (currentNode != null) {
            countNodesRecursive(currentNode.getLeftNode(),0);
            countNodesRecursive(currentNode.getRightNode(),0);
        }

    }

    private void getFinalVectorRecursive(BDDNode currentNode, StringBuilder vectorBuilder) {

        if (currentNode != null) {

            getFinalVectorRecursive(currentNode.getLeftNode(),vectorBuilder);
            if(currentNode.getValue() != 'u' && !currentNode.hasChildren())
                vectorBuilder.append(currentNode.getValue());
            getFinalVectorRecursive(currentNode.getRightNode(),vectorBuilder);

        }

    }

    public String getFinalVector(){
        StringBuilder vector = new StringBuilder();
        getFinalVectorRecursive(root,vector);

        System.out.println(vector);

        return vector.toString();
    }

    public void calculateSize(){
        countNodesRecursive(root,1);
        this.size /= 2;


    }

    public void printTree(){
        calculateSize();
        getFinalVector();
        System.out.println("Size: " + size);
        System.out.println("#########Tree###########");

        traverseInOrderRecursive(root,0);

        System.out.println("###########end tree#############");

    }

}
