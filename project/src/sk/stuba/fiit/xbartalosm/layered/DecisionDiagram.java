package sk.stuba.fiit.xbartalosm.layered;

import sk.stuba.fiit.xbartalosm.BDDNode;
import sk.stuba.fiit.xbartalosm.base.HashTable;
import sk.stuba.fiit.xbartalosm.base.TableItem;

import java.util.ArrayList;

public class DecisionDiagram {

    private DecisionNode root;

    public DecisionDiagram(DecisionNode root) {
        this.root = root;
    }

    public static DecisionDiagram createBDD(String expression, String order){


        HashTable<DecisionNode, TableItem<DecisionNode>>[] levels = new HashTable[order.length()+1];
        DecisionNode rootNode;

        //Root node
        levels[0] = new HashTable<>(TableItem.class);
        rootNode = new DecisionNode(0);
        rootNode.setExpression(expression);
        levels[0].insert(rootNode);

        //Last level
        levels[order.length()] = new HashTable<>(TableItem.class);
        FinalNode zeroFinal = new FinalNode(order.length(),0);
        FinalNode oneFinal = new FinalNode(order.length(),1);
        levels[order.length()].insert(zeroFinal);
        levels[order.length()].insert(oneFinal);


            for (int i = 0; i < order.length(); i++) {

                if (levels[i+1] == null)
                    levels[i+1] = new HashTable<>(TableItem.class);

                char currentChar = order.charAt(i);

                ArrayList<DecisionNode> nodes = levels[i].getAllItems();

                for(int node = 0; node < nodes.size(); node++) {

                    DecisionNode parentNode = nodes.get(node);

                    String[] expressionParts = parentNode.getExpression().split("\\+");


                   /* if(parentNode instanceof FinalNode)
                        System.out.println("final");*/


                    DecisionNode leftNode = new DecisionNode(i + 1);
                    DecisionNode rightNode = new DecisionNode(i + 1);

                    for (int j = 0; j < expressionParts.length; j++) {

                        String part = expressionParts[j];

                        System.out.println(part);

                        //Contradiction == 0
                        if (part.contains(Character.toString(Character.toLowerCase(currentChar))) && part.contains(Character.toString(currentChar))) {
                            continue;
                        }

                        boolean isSingleCharResult = part.length() == 1 && (part.equals(Character.toString(currentChar)) ||
                                part.equals(Character.toString(Character.toLowerCase(currentChar))));



                        //Positive
                        if (part.contains(Character.toString(currentChar))) {

                            if(isSingleCharResult){
                                parentNode.setRightChild(oneFinal);
                                break;
                            }

                            String cleanSubExp = part.replaceAll(Character.toString(currentChar), "");
                            rightNode.addToExpression(cleanSubExp);
                            continue;
                        }

                        //Negative
                        if (part.contains(Character.toString(Character.toLowerCase(currentChar)))) {

                            if(isSingleCharResult){
                                parentNode.setLeftChild(zeroFinal);
                                break;
                            }

                            String cleanSubExp = part.replaceAll(Character.toString(Character.toLowerCase(currentChar)), "");
                            leftNode.addToExpression(cleanSubExp);
                            continue;
                        }

                        rightNode.addToExpression(part);
                        leftNode.addToExpression(part);

                    }

                    boolean isLastLayer = order.length() == i+1;


                    //Add the nodes to next level
                    DecisionNode setuppedRightNode;
                    if(levels[i+1].search(rightNode) == null){
                        setuppedRightNode = rightNode;
                        levels[i+1].insert(rightNode);
                        parentNode.setRightChild(rightNode);
                    }else{
                       DecisionNode existingNode =  levels[i+1].search(rightNode);
                        setuppedRightNode = existingNode;
                        parentNode.setRightChild(existingNode);
                    }

                   /* if(isLastLayer){
                        if(Character.isUpperCase(setuppedRightNode.getExpression().charAt(0))){
                            parentNode.setLeftChild(oneFinal);
                            parentNode.setRightChild(zeroFinal);
                        }else{
                            parentNode.setLeftChild(zeroFinal);
                            parentNode.setRightChild(oneFinal);
                        }

                    }*/


                    //Left side
                    DecisionNode setuppedLeftNode;
                    if(levels[i+1].search(leftNode) == null){

                        setuppedLeftNode = leftNode;
                        levels[i+1].insert(leftNode);
                        parentNode.setLeftChild(leftNode);
                    }else{

                        DecisionNode existingNode =  levels[i+1].search(leftNode);
                        setuppedLeftNode = existingNode;

                        parentNode.setLeftChild(existingNode);
                    }

                    if(isLastLayer){
                        if(Character.isLowerCase(parentNode.getExpression().charAt(0))){
                            parentNode.setLeftChild(oneFinal);
                            parentNode.setRightChild(zeroFinal);
                        }else{
                            parentNode.setLeftChild(zeroFinal);
                            parentNode.setRightChild(oneFinal);
                        }

                    }






                    //Reduction I
                    if(parentNode.getLeftChild().compareTo(parentNode.getRightChild()) == 0){

                       if(parentNode.getParent() != null){

                           DecisionNode.Side pSide =  parentNode.getSide();

                           if(pSide == DecisionNode.Side.LEFT){
                               parentNode.getParent().setLeftChild(rightNode);
                           }else{
                               parentNode.getParent().setRightChild(rightNode);
                           }

                       }

                    }

                }
            }




        return new DecisionDiagram(rootNode);

    }



    public void traverseInOrderRecursive(DecisionNode currentNode) {

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

    public void printTree(){
      //  calculateSize();
       // getFinalVector();
       // System.out.println("Size: " + size);
        System.out.println("#########Tree###########");

        traverseInOrderRecursive(root);

        System.out.println("###########end tree#############");

    }

}
