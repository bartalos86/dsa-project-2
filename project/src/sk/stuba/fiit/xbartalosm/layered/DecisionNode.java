package sk.stuba.fiit.xbartalosm.layered;

import java.util.Objects;

public class DecisionNode implements Comparable<DecisionNode> {

    public enum Side{
        LEFT,RIGHT
    }

    private static int ID = 0;
    private int id;

    private String expression = "";
    private int level;
    private DecisionNode leftChild;
    private DecisionNode rightChild;
    private DecisionNode parent;

    private Side side;

    public DecisionNode( int level) {
        this.level = level;
        this.id = ID++;
    }

    public DecisionNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(DecisionNode leftChild) {
        if(leftChild != null){
            leftChild.setParent(this);
            leftChild.setSide(Side.LEFT);
        }

        this.leftChild = leftChild;
    }

    public DecisionNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(DecisionNode rightChild) {
        if(rightChild != null){
            rightChild.setParent(this);
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

    public void addToExpression(String subExpression){
        if(this.expression.isBlank()){
            this.expression = subExpression;
        }else{
            this.expression += "+" + subExpression;
        }
    }

    @Override
    public int hashCode() {
        return expression.hashCode();
    }



    @Override
    public int compareTo(DecisionNode o) {
        return this.expression.compareTo(o.getExpression());
    }
}
