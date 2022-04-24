package sk.stuba.fiit.xbartalosm.layered;

import sk.stuba.fiit.xbartalosm.hashtables.base.HashTable;
import sk.stuba.fiit.xbartalosm.hashtables.base.TableItem;

import java.util.ArrayList;
import java.util.HashSet;

public class DecisionNode implements Comparable<DecisionNode> {

    public enum Side {
        LEFT, RIGHT
    }

    private static int ID = 0;
    private int id;

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
            HashTable<String, TableItem<String>> partsMap = new HashTable<>(TableItem.class);

            String[] parts = expression.split("\\+");

            for (int i = 0; i < parts.length; i++) {
                partsMap.insert(parts[i]);
            }

            ArrayList<String> allParts = partsMap.getAllItems();

            StringBuilder expressionBuilder = new StringBuilder();
            for (int i = 0; i < allParts.size(); i++) {
                if (expressionBuilder.isEmpty())
                    expressionBuilder.append(allParts.get(i));
                else
                    expressionBuilder.append("+").append(allParts.get(i));
            }
            expression = expressionBuilder.toString();


        }

        HashSet<Character> characters = new HashSet<>();
        boolean containsAddition = false;
        for (int i = 0; i < expression.length(); i++) {
            if (Character.isAlphabetic(expression.charAt(i))) {
                characters.add(expression.charAt(i));

            }

            if (expression.charAt(i) == '+')
                containsAddition = true;
        }

        if (characters.size() == 1 && containsAddition) {
            return expression.charAt(0) + ""; //C+C+C = C
        }

        return expression;

    }


    @Override
    public int compareTo(DecisionNode o) {
        return this.expression.compareTo(o.getExpression());
    }
}
