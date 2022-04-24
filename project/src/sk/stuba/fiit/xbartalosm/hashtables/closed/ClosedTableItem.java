package sk.stuba.fiit.xbartalosm.hashtables.closed;

import sk.stuba.fiit.xbartalosm.hashtables.base.TableItem;

public class ClosedTableItem<T extends Comparable<T>> extends TableItem<T> {

    private ClosedTableItem<T> next = null;
    private ClosedTableItem<T> prev = null;

    public ClosedTableItem(T data) {
        super(data);
    }

    public ClosedTableItem<T> getNext() {
        return next;
    }

    public void setNext(ClosedTableItem<T> next) {
        this.next = next;
        if(next != null)
        next.setPrev(this);
    }

    public ClosedTableItem<T> getPrev() {
        return prev;
    }

    public void setPrev(ClosedTableItem<T> prev) {
        this.prev = prev;
    }

    public boolean addItem(ClosedTableItem<T> item){
        if(this.hasNext()){
           next.addItem(item);
        }else{
            setNext(item);
        }

        return true;
    }

    public void printItem(){
        if(hasNext()){
            next.printItem();
        }
        System.out.print(getData() + " ");

    }

    public boolean hasNext(){
        return next != null;
    }

    public boolean isRoot(){
        return prev == null;
    }
}
