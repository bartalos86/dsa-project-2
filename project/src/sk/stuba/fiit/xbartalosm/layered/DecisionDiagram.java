package sk.stuba.fiit.xbartalosm.layered;

import sk.stuba.fiit.xbartalosm.hashtables.closed.ClosedHashTable;

import java.util.ArrayList;

public class DecisionDiagram {

    private DecisionNode root;
    private int totalNodes = 1;

    public DecisionDiagram(DecisionNode root) {
        this.root = root;
    }

    public static DecisionDiagram createBDD(String expression, String order){


        ClosedHashTable<DecisionNode>[] levels = new ClosedHashTable[order.length()+1];
        DecisionNode rootNode;
        int totalNodes = 0;

        //Root node
        levels[0] = new ClosedHashTable<>();
        rootNode = new DecisionNode(0);
        rootNode.setExpression(expression);
        levels[0].insert(rootNode);

        //Last level
        levels[order.length()] = new ClosedHashTable<>();
        FinalNode zeroFinal = new FinalNode(order.length(),0);
        FinalNode oneFinal = new FinalNode(order.length(),1);
        levels[order.length()].insert(zeroFinal);
        levels[order.length()].insert(oneFinal);


            for (int i = 0; i < order.length(); i++) {

                if (levels[i+1] == null)
                    levels[i+1] = new ClosedHashTable<DecisionNode>();

                char currentChar = order.charAt(i);

                ArrayList<DecisionNode> nodes = levels[i].getAllItems();
                boolean isLastLayer = order.length() == i+1;

                for(int node = 0; node < nodes.size(); node++) {

                    DecisionNode parentNode = nodes.get(node);

                    if(parentNode instanceof FinalNode)
                        continue;

                    String[] expressionParts = parentNode.getExpression().split("\\+");

                    DecisionNode leftNode = new DecisionNode(i + 1);
                    DecisionNode rightNode = new DecisionNode(i + 1);

                    boolean breakHappened = false;

                    /*if(parentNode.getExpression().length() <= 0)
                        continue;*/

                    for (int j = 0; j < expressionParts.length; j++) {

                        String part = expressionParts[j];

                        System.out.println(part);


                        //Contradiction == 0
                        if (part.contains(Character.toString(Character.toLowerCase(currentChar))) && part.contains(Character.toString(currentChar))) {

                            if(parentNode.getExpression().contains("+"))
                                continue;
                            else{
                                rightNode = zeroFinal;
                                leftNode = zeroFinal;
                                break;
                            }

                        }

                        boolean isSingleCharResult = (part.length() == 1 && (part.equals(Character.toString(currentChar)) ||
                                part.equals(Character.toString(Character.toLowerCase(currentChar)))));//&& parentNode.getExpression().contains("+");

                        boolean isAbsoluteAnd = !parentNode.getExpression().contains("+") && parentNode.getExpression().length()>1;
                        //Maybe add diferent charcter recognition
                        boolean everyPartContainsCharBig = true;
                        boolean everyPartContainsCharSmall = true;

                        for (int ex = 0; ex < expressionParts.length; ex++){
                            if(!expressionParts[ex].contains(Character.toString(currentChar)))
                                everyPartContainsCharBig = false;

                            if(!expressionParts[ex].contains(Character.toString(Character.toLowerCase(currentChar))))
                                everyPartContainsCharSmall = false;
                        }

                        isAbsoluteAnd = isAbsoluteAnd || ((everyPartContainsCharBig ^ everyPartContainsCharSmall) && parentNode.getExpression().contains("+"));


                        System.out.println("Current expression: " + parentNode.getExpression());
                        if(isAbsoluteAnd)
                            System.out.println("ABS: " + parentNode.getExpression());

                        //Positive
                        if (part.contains(Character.toString(currentChar))) {

                            if(isSingleCharResult){
                                rightNode = oneFinal;

                                //Final node
                                if(parentNode.getExpression().length() == 1){
                                    leftNode = zeroFinal;
                                    isLastLayer = true;
                                }

                            }

                            if(isAbsoluteAnd){
                                leftNode = zeroFinal;
                            }

                            String cleanSubExp = part.replaceAll(Character.toString(currentChar), "");

                            if(!(rightNode instanceof FinalNode))
                                    rightNode.addToExpression(cleanSubExp);


                            continue;
                        }

                        //Negative
                        if (part.contains(Character.toString(Character.toLowerCase(currentChar)))) {

                            if(isSingleCharResult){
                                leftNode = oneFinal;

                                //Final node
                                if(parentNode.getExpression().length() == 1){
                                    rightNode = zeroFinal;
                                    isLastLayer = true;
                                }
                            }

                            if(isAbsoluteAnd) {
                                rightNode = zeroFinal;
                            }

                            String cleanSubExp = part.replaceAll(Character.toString(Character.toLowerCase(currentChar)), "");

                            if(!(leftNode instanceof FinalNode))
                                leftNode.addToExpression(cleanSubExp);

                            continue;
                        }


                        if(!(rightNode instanceof FinalNode))
                            rightNode.addToExpression(part);

                        if(!(leftNode instanceof FinalNode))
                            leftNode.addToExpression(part);

                    }




                    //isLastLayer = parentNode.getExpression().length() == 1;




                    //Add the nodes to next level
                    DecisionNode setuppedRightNode;
                    if( levels[i+1].search(rightNode) == null){

                        if(!rightNode.getExpression().isEmpty()){
                            //ZERO AND ONE doesnt need to be inserted;
                            if(!(rightNode instanceof FinalNode)){
                                levels[i+1].insert(rightNode);
                                System.out.println("Inserting: " + rightNode.getExpression());

                            }
                                totalNodes++;
                                parentNode.setRightChild(rightNode);

                        }


                    }else{
                       DecisionNode existingNode =  levels[i+1].search(rightNode);

                            parentNode.setRightChild(existingNode);
                    }

                    //Left side
                    DecisionNode setuppedLeftNode;
                    if(levels[i+1].search(leftNode) == null){

                        if(!leftNode.getExpression().isEmpty()){

                            if(!(leftNode instanceof FinalNode)){
                                levels[i+1].insert(leftNode);
                                System.out.println("Inserting: " + leftNode.getExpression());
                            }
                            totalNodes++;
                            parentNode.setLeftChild(leftNode);
                        }

                    }else{

                        DecisionNode existingNode =  levels[i+1].search(leftNode);
                        if(!leftNode.getExpression().isEmpty())
                        parentNode.setLeftChild(existingNode);
                    }


                    boolean areNodesSetup = leftNode instanceof FinalNode || rightNode instanceof FinalNode;


                    if(isLastLayer){

                       // if(Character.isLowerCase(parentNode.getExpression().charAt(0))){
                        if(parentNode.getSide() == DecisionNode.Side.LEFT){
                            if(parentNode.getLeftChild() == null)
                            parentNode.setLeftChild(oneFinal);

                            if(parentNode.getRightChild() == null)
                                parentNode.setRightChild(zeroFinal);
                        }else{
                            if(parentNode.getLeftChild() == null)
                                parentNode.setLeftChild(zeroFinal);
                            if(parentNode.getRightChild() == null)
                             parentNode.setRightChild(oneFinal);
                        }

                    }

                   rootNode = reductionI(parentNode,rightNode);

                }
            }
            DecisionDiagram diagram =new DecisionDiagram(rootNode);
            diagram.setTotalNodes(totalNodes);
        return diagram;

    }

    //TODO: maybe dont do reduction when children are FinalNodes
    private static DecisionNode reductionI(DecisionNode parentNode, DecisionNode node){

        if(parentNode == null)
            return null;


        //Reduction I
        if(parentNode.getLeftChild().compareTo(parentNode.getRightChild()) == 0){

            if(parentNode.getParent() != null){
                System.out.println("Reducing:" + parentNode.getExpression());


                DecisionNode.Side pSide =  parentNode.getSide();

                if(pSide == DecisionNode.Side.LEFT){
                    parentNode.getParent().setLeftChild(node);
                }else{
                    parentNode.getParent().setRightChild(node);
                }

            }else{

                    return parentNode.getRightChild(); //This will be the root

            }



        }

        if(parentNode.getParent() == null){
            return  parentNode;
        }


        return reductionI(parentNode.getParent(),node);
    }

    public static int BDDuse(DecisionDiagram diagram, String result){

        DecisionNode currentNode = diagram.getRoot();

        for (int i = 0; i < result.length(); i++) {
            char current = result.charAt(i);

            if(currentNode.getLevel() != i)
                continue;

            if(current == '1'){
                currentNode = currentNode.getRightChild();
            }else if(current == '0'){
                currentNode = currentNode.getLeftChild();
            }

        }

        if(currentNode instanceof FinalNode){
            return  ((FinalNode)currentNode).getValue();
        }

        return -1;


    }


    private void traverseInOrderRecursive(DecisionNode currentNode) {

        if (currentNode != null) {
            traverseInOrderRecursive(currentNode.getLeftChild());
            System.out.print(currentNode.getExpression() + " - Level: " + currentNode.getLevel() + " ID: " + currentNode.getId());

            if(currentNode instanceof FinalNode)
                System.out.print(" Value: " + ((FinalNode) currentNode).getValue() + "\n");
            else
                System.out.println();

            traverseInOrderRecursive(currentNode.getRightChild());
        }

    }

    public void printTreeNorm(){
      //  calculateSize();
       // getFinalVector();
       // System.out.println("Size: " + size);
        System.out.println("#########Tree###########");

        traverseInOrderRecursive(root);
        System.out.println("Total nodes: " + totalNodes);

        System.out.println("###########end tree#############");

    }


    void inorderTraversalHelper(DecisionNode node)
    {
        if(node!=null)
        {
            inorderTraversalHelper(node.getLeftChild());
            System.out.printf("%s ", node.getExpression());
            inorderTraversalHelper(node.getRightChild());
        }
    }
    //function to print inorder traversal
    public void inorderTraversal()
    {
        inorderTraversalHelper(this.root);
    }
    // helper function to print the tree.
    void printTreeHelper(DecisionNode root, int space)
    {
        int i;
        if(root != null)
        {
            space = space + 10;
            printTreeHelper(root.getRightChild(), space);
            System.out.printf("\n");
            for ( i = 10; i < space; i++)
            {
                System.out.printf(" ");
            }
            System.out.printf("%s", root.getExpression());
            System.out.printf("\n");
            printTreeHelper(root.getLeftChild(), space);
        }
    }
    // function to print the tree.
    public void printTree()
    {
        printTreeHelper(this.root, 0);
    }


    public DecisionNode getRoot() {
        return root;
    }

    public int getTotalNodes() {
        return totalNodes;
    }

    public void setTotalNodes(int totalNodes) {
        this.totalNodes = totalNodes +1;
    }
}
