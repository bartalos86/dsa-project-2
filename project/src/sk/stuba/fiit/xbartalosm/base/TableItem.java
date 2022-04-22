package sk.stuba.fiit.xbartalosm.base;

public class TableItem<T extends Comparable<T>>  {

    private T data;

    public TableItem(T data){
        this.data = data;

    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }
}
