package sk.stuba.fiit.xbartalosm.layered;

import sk.stuba.fiit.xbartalosm.hashtables.closed.ClosedHashTable;

import java.util.ArrayList;

public class DecisionNode implements Comparable<DecisionNode> {

    public enum Side {
        LEFT, RIGHT, BOTH
    }

    private static int ID = 0;
    private int id;

    ClosedHashTable<DecisionNode> parents = new ClosedHashTable<>();

    private String expression = "";
    private int level;
    private DecisionNode leftChild;
    private DecisionNode rightChild;
    private DecisionNode parent;

    private Side side;

    public DecisionNode(int level) {
        this.level = level;
        this.id = ID++;


    }

    public DecisionNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(DecisionNode leftChild) {
        if (leftChild != null) {
            leftChild.setParent(this);
            leftChild.addParent(this);
            leftChild.setSide(Side.LEFT);
        }

        this.leftChild = leftChild;
    }

    public DecisionNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(DecisionNode rightChild) {
        if (rightChild != null) {
            rightChild.setParent(this);
            rightChild.addParent(this);
            rightChild.setSide(Side.RIGHT);
        }


        this.rightChild = rightChild;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Side getSide() {
        return side;
    }

    public Side sideRelativeToParent(DecisionNode parent){
        if(this.parent != null){

            if(parent.getRightChild() == this && parent.getLeftChild() == this)
                return Side.BOTH;

            if(parent.getRightChild() == this)
                return Side.RIGHT;
            else if(parent.getLeftChild() == this)
                return Side.LEFT;
        }

        return null;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public DecisionNode getParent() {
        return parent;
    }

    public void setParent(DecisionNode parent) {
        this.parent = parent;
    }

    public int getId() {
        return id;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public void addToExpression(String subExpression) {

        if (subExpression.isBlank())
            return;

        if (this.expression.isBlank()) {
            this.expression = subExpression;
        } else {
            this.expression += "+" + subExpression;
        }
    }

    @Override
    public int hashCode() {
        expression = simplifyExpression(expression);
        return expression.hashCode();
    }

    private String simplifyExpression(String expression) {

        if (expression.contains("+")) {
            ClosedHashTable<String> partsMap = new ClosedHashTable<>();

            String[] parts = expression.split("\\+");

            for (int i = 0; i < parts.length; i++) {
                if(partsMap.search(parts[i]) == null) //Only one
                partsMap.insert(parts[i]);
            }

            String[] allParts = partsMap.getAllItems(String.class);

            StringBuilder expressionBuilder = new StringBuilder();
            for (int i = 0; i < allParts.length; i++) {
                if (expressionBuilder.isEmpty())
                    expressionBuilder.append(allParts[i]);
                else
                    expressionBuilder.append("+").append(allParts[i]);
            }
            expression = expressionBuilder.toString().replace(" ", "");


        }

        return expression;

    }

    public void addParent(DecisionNode node){
        parents.insert(node);
    }

    public ClosedHashTable<DecisionNode> getParents() {
        return parents;
    }

    public void removeParent(DecisionNode parent){
        parents.delete(parent);
    }

    @Override
    public int compareTo(DecisionNode o) {
        this.expression = simplifyExpression(expression);
        return this.expression.compareTo(o.getExpression());
    }
}
