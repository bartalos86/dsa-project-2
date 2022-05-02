package sk.stuba.fiit.xbartalosm;

public class TesterInput implements Comparable<TesterInput>{

    private String key;
    private int value;

    public TesterInput(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public int compareTo(TesterInput o) {
        return 0;
    }
}
