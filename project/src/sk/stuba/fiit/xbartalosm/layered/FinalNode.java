package sk.stuba.fiit.xbartalosm.layered;

public class FinalNode extends DecisionNode{

    private int value;

    public FinalNode(int level, int value) {
        super(level);
        this.value = value;
        this.setExpression(String.valueOf(value));
    }

    @Override
    public int hashCode() {
        return value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void addToExpression(String subExpression) {
        System.out.println("You are trying to add to final expression");
        return;
    }

    @Override
    public int compareTo(DecisionNode o) {

        if(o instanceof FinalNode){
            if(((FinalNode)o).getValue() == this.getValue())
                return 0;
            else if (((FinalNode)o).getValue() > this.getValue())
                return -1;
            else
                return 1;
        }else{
            return -1;
        }

    }


    @Override
    public void setLeftChild(DecisionNode leftChild) {
        System.out.println("You cant do that");
    }

    @Override
    public void setRightChild(DecisionNode rightChild) {
        System.out.println("You cant do that");
    }
}
