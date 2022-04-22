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
}
