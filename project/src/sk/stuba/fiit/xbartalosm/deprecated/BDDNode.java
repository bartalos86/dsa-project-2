package sk.stuba.fiit.xbartalosm.deprecated;

import java.util.HashSet;

public class BDDNode {

    private String subExpression = "";

    private BDDNode leftNode;
    private BDDNode rightNode;

    private static int ID = 0;
    private int id;

    private BDDNode parent;

    private int position;
    private String name;
    private String charcterOrder;
    private char value = 'u';


    public BDDNode(String charcterOrder) {
        this.charcterOrder = charcterOrder;
        this.id = ID++;
    }



    public void addToExpression(String expPart){

        if(!this.subExpression.isEmpty())
            this.subExpression += "+" + expPart;
        else
            this.subExpression = expPart;

        this.subExpression = simplifyExpression(subExpression);

    }



    public void processExpression(){
      /*  HashSet<Character> characters = new HashSet<>();

        for(int i = 0; i< subExpression.length();i++){
            if(Character.isAlphabetic(subExpression.charAt(i))){
                characters.add(Character.toUpperCase(subExpression.charAt(i)));
            }
        }*/

        String[] parts = subExpression.split("\\+");

        if(charcterOrder.isEmpty())
            return;

            Character currentChar = charcterOrder.charAt(0);//gets first char
            this.setName(currentChar.toString());

            BDDNode leftNode = new BDDNode(charcterOrder.substring(1));
            BDDNode rightNode = new BDDNode(charcterOrder.substring(1));
            for(String part : parts){


                //Negative
                if(part.contains(Character.toLowerCase(currentChar)+ "")){

                    part =  part.replaceAll(""+Character.toLowerCase(currentChar),"");

                    //input ex. bC
                    if(!subExpression.contains("+") && subExpression.length()>1)
                        rightNode.setValue('0');

                    //IF contains for ex. b
                    if(part.isEmpty() && subExpression.length() >1)
                        leftNode.setValue('1');


                    leftNode.addToExpression(part);
                    continue;
                }

                //Positive
                if(part.contains(currentChar.toString())){

                    part =  part.replaceAll(""+currentChar,"");

                    //Case final AND
                    if(!subExpression.contains("+") && subExpression.length()>1)
                        leftNode.setValue('0');


                    if(part.isEmpty() && subExpression.length() >1)
                        rightNode.setValue('1');



                    rightNode.addToExpression(part);
                    continue;
                }


                leftNode.addToExpression(part);
                rightNode.addToExpression(part);


            }


        this.setLeftNode(leftNode);
        this.setRightNode(rightNode);


            //Last part
            if(charcterOrder.length() == 1 ){

                if(subExpression.isEmpty() || subExpression.isBlank()){
                    System.out.println("Setting from parent - " + getName() +" " + this.value);

                    leftNode.setValue('p');
                    rightNode.setValue('p');
                    return;
                }

                System.out.println(subExpression);


                //if its sibling is the same
                checkSiblingReduction();



                if(Character.isLowerCase(subExpression.charAt(0))){
                    leftNode.setValue('1');
                    rightNode.setValue('0');
                }else{
                    leftNode.setValue('0');
                    rightNode.setValue('1');
                }


            }



            leftNode.processExpression();
            rightNode.processExpression();

    }

    private String simplifyExpression(String expression){

        HashSet<Character> characters = new HashSet<>();
        boolean containsAddition = false;
        for(int i = 0; i< subExpression.length();i++) {
            if (Character.isAlphabetic(subExpression.charAt(i))) {
                characters.add(subExpression.charAt(i));

            }

            if(subExpression.charAt(i) == '+')
                containsAddition = true;
        }

        if(characters.size() == 1 && containsAddition){
            return expression.charAt(0)+""; //C+C+C = C
        }

        return expression;

    }

    private boolean checkSiblingReduction(){
        BDDNode parent =  getParent();
        if(parent == null)
            return false;

        //If the nodes are equal
      /*  if(leftNode.getSubExpression().equals(rightNode.getSubExpression())){
            this.getParent().setRightNode(leftNode);
        }*/

        //Do not shorten if
        if(parent.getLeftNode().getValue() == '1' || parent.getLeftNode().getValue() == '0' ||
                parent.getRightNode().getValue() == '1' || parent.getRightNode().getValue() == '0')
            return false;



        if(getPosition() == 1){
            if(parent.getLeftNode().getSubExpression().equals(subExpression)){
                parent.setRightNode(parent.getLeftNode());
                //parent.setLeftNode(null);
                return true;
            }
        }else{
            if(parent.getRightNode().getSubExpression().equals(subExpression)){
                parent.setLeftNode(parent.getRightNode());
                return true;
            }
        }

       //return parent.checkSiblingreduction();
        return false;
    }



    public void setValue(char value) {
        //Only set the value if its a leaf
       // if (subExpression.isEmpty())


        if(value != 'p')
            this.value = value;


        if(parent != null){
            if(parent.getValue() == '0'){
                this.value = '0';
                //this.parent.setValue('q');
            }

            else if(parent.getValue() == '1'){
                this.value = '1';
                //this.parent.setValue('q');
            }

        }


    }

    public String getCharcterOrder() {
        return charcterOrder;
    }

    public void setCharcterOrder(String charcterOrder) {
        this.charcterOrder = charcterOrder;
    }

    public char getValue() {
        return value;
    }

    public BDDNode getParent() {
        return parent;
    }

    public void setParent(BDDNode parent) {
        this.parent = parent;
    }

    public int getId() {
        return id;
    }

    public BDDNode getLeftNode() {
        return leftNode;
    }

    public BDDNode getRightNode() {
        return rightNode;
    }

    public void setLeftNode(BDDNode leftNode) {
        leftNode.setParent(this);
        leftNode.setPosition(-1);
        this.leftNode = leftNode;

    }

    public void setRightNode(BDDNode rightNode) {
        rightNode.setParent(this);
        rightNode.setPosition(1);
        this.rightNode = rightNode;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubExpression() {
        return subExpression;
    }

    public void setSubExpression(String subExpression) {
        this.subExpression = subExpression;
    }

    public boolean hasChildren(){
        return leftNode != null || rightNode != null;
    }
}
