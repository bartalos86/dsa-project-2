package sk.stuba.fiit.xbartalosm.layered;

import sk.stuba.fiit.xbartalosm.hashtables.closed.ClosedHashTable;


public class DecisionDiagram {

    private DecisionNode root;
    private int totalNodes = 1;
    private int expectedSize = 0;
    private double reductionRate = 0;

    public DecisionDiagram(DecisionNode root) {
        this.root = root;
        this.totalNodes = countNodes(root);

    }

    public static DecisionDiagram createBDD(String expression, String order) {

        ClosedHashTable<DecisionNode>[] levels = new ClosedHashTable[order.length() + 1];
        DecisionNode rootNode;

        //Root node
        levels[0] = new ClosedHashTable<>();
        rootNode = new DecisionNode(0);
        rootNode.setExpression(expression);
        levels[0].insert(rootNode);

        //Last level
        levels[order.length()] = new ClosedHashTable<>();
        FinalNode zeroFinal = new FinalNode(order.length(), 0);
        FinalNode oneFinal = new FinalNode(order.length(), 1);
        levels[order.length()].insert(zeroFinal);
        levels[order.length()].insert(oneFinal);


        for (int i = 0; i < order.length(); i++) {

            if (levels[i + 1] == null)
                levels[i + 1] = new ClosedHashTable<>();

            char currentChar = order.charAt(i);

            DecisionNode[] nodes = levels[i].getAllItems(DecisionNode.class);
            boolean isLastLayer = order.length() == i + 1;

            if(nodes != null)
            for (int node = 0; node < nodes.length; node++) {

                DecisionNode parentNode = nodes[node];
                //TODO: maybe to expression loop
                if (parentNode instanceof FinalNode)
                    continue;

                String[] expressionParts = parentNode.getExpression().split("\\+");

                DecisionNode leftNode = new DecisionNode(i + 1);
                DecisionNode rightNode = new DecisionNode(i + 1);

                for (int j = 0; j < expressionParts.length; j++) {
                    String part = expressionParts[j];

                    //Contradiction == 0
                    if (part.contains(Character.toString(Character.toLowerCase(currentChar))) && part.contains(Character.toString(currentChar))) {

                        if (parentNode.getExpression().contains("+"))
                            continue;
                        else {
                            rightNode = zeroFinal;
                            leftNode = zeroFinal;
                            break;
                        }

                    }

                    boolean isSingleCharResult = (part.length() == 1 && (part.equals(Character.toString(currentChar)) ||
                            part.equals(Character.toString(Character.toLowerCase(currentChar)))));
                    boolean isAbsoluteAnd = !parentNode.getExpression().contains("+") && parentNode.getExpression().length() > 1;


                    //Maybe add diferent charcter recognition
                    boolean everyPartContainsCharBig = true;
                    boolean everyPartContainsCharSmall = true;

                    for (int ex = 0; ex < expressionParts.length; ex++) {
                        if (!expressionParts[ex].contains(Character.toString(currentChar)))
                            everyPartContainsCharBig = false;

                        if (!expressionParts[ex].contains(Character.toString(Character.toLowerCase(currentChar))))
                            everyPartContainsCharSmall = false;
                    }

                    isAbsoluteAnd = isAbsoluteAnd || ((everyPartContainsCharBig ^ everyPartContainsCharSmall) && parentNode.getExpression().contains("+"));

                    //Positive
                    if (part.contains(Character.toString(currentChar))) {
                        if (isSingleCharResult) {
                            rightNode = oneFinal;
                            //Final node
                            if (parentNode.getExpression().length() == 1) {
                                leftNode = zeroFinal;
                                isLastLayer = true;
                            }
                        }

                        if (isAbsoluteAnd) {
                            leftNode = zeroFinal;
                        }
                        String cleanSubExp = part.replaceAll(Character.toString(currentChar), "");
                        if (!(rightNode instanceof FinalNode))
                            rightNode.addToExpression(cleanSubExp);
                        continue;
                    }

                    //Negative
                    if (part.contains(Character.toString(Character.toLowerCase(currentChar)))) {

                        if (isSingleCharResult) {
                            leftNode = oneFinal;

                            //Final node
                            if (parentNode.getExpression().length() == 1) {
                                rightNode = zeroFinal;
                                isLastLayer = true;
                            }
                        }

                        if (isAbsoluteAnd) {
                            rightNode = zeroFinal;
                        }

                        String cleanSubExp = part.replaceAll(Character.toString(Character.toLowerCase(currentChar)), "");

                        if (!(leftNode instanceof FinalNode))
                            leftNode.addToExpression(cleanSubExp);

                        continue;
                    }


                    if (!(rightNode instanceof FinalNode))
                        rightNode.addToExpression(part);

                    if (!(leftNode instanceof FinalNode))
                        leftNode.addToExpression(part);

                }

                //Add the nodes to next level
                DecisionNode rightNodeExists = levels[i + 1].search(rightNode);
                if (rightNodeExists == null) {

                    if (!rightNode.getExpression().isEmpty()) {
                        //ZERO AND ONE doesnt need to be inserted;
                        if (!(rightNode instanceof FinalNode)) {
                            levels[i + 1].insert(rightNode);
                        }
                        parentNode.setRightChild(rightNode);

                    }


                } else {
                    DecisionNode existingNode = rightNodeExists;
                    parentNode.setRightChild(existingNode);
                }

                //Left side
                DecisionNode leftNodeExists = levels[i + 1].search(leftNode);
                if (leftNodeExists == null) {

                    if (!leftNode.getExpression().isEmpty()) {

                        if (!(leftNode instanceof FinalNode)) {
                            levels[i + 1].insert(leftNode);
                        }

                        parentNode.setLeftChild(leftNode);
                    }

                } else {

                    DecisionNode existingNode = leftNodeExists;//levels[i+1].search(leftNode);


                    if (!leftNode.getExpression().isEmpty())
                        parentNode.setLeftChild(existingNode);
                }

                if (isLastLayer) {

                    //We can use side because its the lastly added element
                    if (parentNode.getSide() == DecisionNode.Side.LEFT) {
                        if (parentNode.getLeftChild() == null)
                            parentNode.setLeftChild(oneFinal);

                        if (parentNode.getRightChild() == null)
                            parentNode.setRightChild(zeroFinal);
                    } else {
                        if (parentNode.getLeftChild() == null)
                            parentNode.setLeftChild(zeroFinal);
                        if (parentNode.getRightChild() == null)
                            parentNode.setRightChild(oneFinal);
                    }


                }


                rootNode = reductionS(parentNode);


            }
        }
        DecisionDiagram diagram = new DecisionDiagram(rootNode);
       // rootNode = reductionS(rootNode);
        diagram.setExpectedSize((int) Math.pow(2, order.length()) - 1);
        return diagram;

    }

    private static DecisionNode reductionS(DecisionNode parentNode) {

        if (parentNode == null)
            return null;

            DecisionNode[] parents = parentNode.getParents().getAllItems(DecisionNode.class);

            if(parents != null)
        for (DecisionNode parent : parents) {
            DecisionNode grandparent = parent;

            //Reduction S
            if (parentNode.getLeftChild().compareTo(parentNode.getRightChild()) == 0) {

                if (grandparent != null) {
                    DecisionNode.Side pSide = parentNode.sideRelativeToParent(grandparent);

                    if (pSide == DecisionNode.Side.LEFT || pSide == DecisionNode.Side.BOTH) {
                        grandparent.setLeftChild(parentNode.getLeftChild());
                    } else if (pSide == DecisionNode.Side.RIGHT) {
                        grandparent.setRightChild(parentNode.getLeftChild());
                    }

                    parentNode.getLeftChild().removeParent(parentNode);
                } else { //Probably never going to happen
                    return parentNode.getRightChild(); //This will be the root
                }

            }
            if (grandparent == null) {
                return parentNode;
            }

        }

            if(parents == null && parentNode.getLeftChild().compareTo(parentNode.getRightChild()) == 0){
                return parentNode.getRightChild();
            }

        DecisionNode[]  updatedParent = parents;// parentNode.getParents().getAllItems(DecisionNode.class);

        if (updatedParent == null) {
            return parentNode;
        }

        for (DecisionNode parent : updatedParent) {
            return reductionS(parent);
        }

        return null;
    }

    public static int BDDuse(DecisionDiagram diagram, String result) {
        DecisionNode currentNode = diagram.getRoot();

        for (int i = 0; i < result.length(); i++) {
            char current = result.charAt(i);

            if (currentNode.getLevel() != i)
                continue;

            if (current == '1') {
                currentNode = currentNode.getRightChild();
            } else if (current == '0') {
                currentNode = currentNode.getLeftChild();
            }
        }

        if (currentNode instanceof FinalNode) {
            return ((FinalNode) currentNode).getValue();
        }

        return -1;
    }

    private int countNodes(DecisionNode root) {
        ClosedHashTable<Integer> ids = new ClosedHashTable<>();
        countRecursiveInOrder(root, ids);

        return ids.size();
    }

    private void countRecursiveInOrder(DecisionNode currentNode, ClosedHashTable<Integer> ids) {

        if (currentNode != null && !(currentNode instanceof FinalNode)) {
            if (ids.search(currentNode.getId()) == null) {
                ids.insert(currentNode.getId());
            }

            countRecursiveInOrder(currentNode.getLeftChild(), ids);

            countRecursiveInOrder(currentNode.getRightChild(), ids);

        }
    }


    private void traverseInOrderRecursive(DecisionNode currentNode) {

        if (currentNode != null) {
            traverseInOrderRecursive(currentNode.getLeftChild());
            System.out.print(currentNode.getExpression() + " - Level: " + currentNode.getLevel() + " ID: " + currentNode.getId());

            if (currentNode instanceof FinalNode)
                System.out.print(" Value: " + ((FinalNode) currentNode).getValue() + "\n");
            else
                System.out.println();

            traverseInOrderRecursive(currentNode.getRightChild());
        }

    }

    public void printTreeNorm() {
        System.out.println("#########Tree###########");

        traverseInOrderRecursive(root);
        printStatistics();

        System.out.println("###########end tree#############");

    }

    public void printStatistics() {
        System.out.println("Expected size: " + expectedSize);
        System.out.println("Total nodes: " + totalNodes);
        System.out.println("Reduction rate: " + reductionRate);
    }


    public DecisionNode getRoot() {
        return root;
    }

    public int getTotalNodes() {
        return totalNodes;
    }

    public void setTotalNodes(int totalNodes) {
        this.totalNodes = totalNodes;
    }

    public int getExpectedSize() {
        return expectedSize;
    }

    public void setExpectedSize(int expectedSize) {
        this.expectedSize = expectedSize;
        this.reductionRate = (1 - totalNodes / (double) expectedSize) * 100;

    }

    public double getReductionRate() {
        return reductionRate;
    }
}
