package sk.stuba.fiit.xbartalosm.hashtables.closed;

import sk.stuba.fiit.xbartalosm.hashtables.base.HashTable;

import java.util.ArrayList;

public class ClosedHashTable<T extends Comparable<T>> extends HashTable<T, ClosedTableItem<T>> {


    public ClosedHashTable(){
        super(ClosedTableItem.class);
    }

    @Override
    protected void resize() {

        int newSize = array.length*2;
        ClosedTableItem<T>[] temp = new ClosedTableItem[newSize];
        freeSpace = newSize;

        for(int i = 0 ; i < array.length; i++){
            if(array[i] != null){

                ClosedTableItem<T> tableItem = array[i];
                int hashCode  = tableItem.hashCode()  % newSize;

                //Insert the single item
                if(!tableItem.hasNext()){
                    if(temp[hashCode] != null){
                        //Add to already existing item
                        temp[hashCode].addItem(tableItem);
                        freeSpace--;
                    }else{
                        //Put as root
                        tableItem.setPrev(null);
                        tableItem.setNext(null);
                        temp[hashCode] = tableItem;
                        freeSpace--;
                    }
                }else{
                    //rehash the full list
                    while(tableItem != null){
                        hashCode  = tableItem.hashCode() % newSize;
                        ClosedTableItem<T> next = tableItem.getNext();

                        if(temp[hashCode] != null){
                            //Add to already existing item
                            tableItem.setNext(null);
                            temp[hashCode].addItem(tableItem);
                            freeSpace--;
                        }else{
                            //Put as root
                            tableItem.setPrev(null);
                            tableItem.setNext(null);
                            temp[hashCode] = tableItem;
                            freeSpace--;
                        }

                        tableItem = next;

                    }
                }


            }
        }

        tableSize = newSize;
        array = temp;


    }

    @Override
    public boolean insert(T data) {
        ClosedTableItem<T> item = new ClosedTableItem<>(data);
        int hashCode = Math.abs(data.hashCode());
        int tableSize = array.length;

        //if the free space is less than 20%
        if(freeSpace <= tableSize*0.2){
            resize();
        }
        int position = hashCode % tableSize;

        if(array[position] != null){
            array[position].addItem(item);
            freeSpace--;
        }else{
            array[position] = item;
            freeSpace--;
        }
        return true;
    }

    @Override
    public boolean delete(T data) {
        int position = Math.abs(data.hashCode()) % tableSize;

        if(array[position] == null){
            return false;
        }

        //the item at the position isnt the correct, 0 == equals
        if(array[position].getData().compareTo(data) != 0){

            if(array[position].hasNext()){
                ClosedTableItem<T> itemToDelete = array[position];

                while(itemToDelete.hasNext() && itemToDelete.getData().compareTo(data) != 0){
                    itemToDelete =  itemToDelete.getNext();
                }

                //the searched item found
                if(itemToDelete.getData().compareTo(data) == 0){
                    itemToDelete.getPrev().setNext(itemToDelete.getNext());//deletes from the linked list
                        freeSpace++;
                    return true;
                }
            }

        }else{
            if(array[position].hasNext()){
                //set the next item as root
                array[position].getNext().setPrev(null);
                array[position] = array[position].getNext();
                freeSpace++;
                return true;
            }else{
                array[position] = null;
                freeSpace++;
                return true;
            }

        }

    return false;
    }

    @Override
    public T search(T data) {
        int position = Math.abs(data.hashCode()) % array.length;

        if(array[position] == null){
            return null;
        }

        if(array[position].getData().compareTo(data) != 0){
            ClosedTableItem<T> tableItem = array[position];
            while(tableItem != null){

                if(tableItem.getData().compareTo(data) == 0){
                    return tableItem.getData();
                }
                tableItem = tableItem.getNext();
            }
            return null;
        }

        return array[position].getData();
    }

    @Override
    public void printTable() {
        System.out.println("Table size: " + array.length);
        System.out.println("Free space: " + freeSpace);

        for(int i = 0 ; i < array.length; i++){
            if(array[i] != null){
                ClosedTableItem<T> tableitem = array[i];
                tableitem.printItem();
                System.out.println("- " + i );

            }

        }
    }
    @Override
    public ArrayList<T> getAllItems(){
        ArrayList<T> items = new ArrayList<>();

        for(int i = 0; i < array.length; i++){
            if(array[i] != null){
                items.add(array[i].getData());
                ClosedTableItem<T> current = array[i];

                while(current.hasNext()){
                    items.add(current.getNext().getData());
                  current =  current.getNext();
                }


            }
        }

        return items;

    }
}
