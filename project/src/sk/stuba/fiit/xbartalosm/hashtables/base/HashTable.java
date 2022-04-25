package sk.stuba.fiit.xbartalosm.hashtables.base;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HashTable<T extends Comparable<T>, U extends TableItem<T>> {

    protected U[] array;
    protected int tableSize = 25;
    protected int freeSpace = tableSize;
    private Class type;

    public HashTable(Class type){

        array = (U[])  Array.newInstance(type,tableSize);;
        this.type = type;
    }

    protected void resize(){
        int newSize = array.length*2;
        U[] temp = (U[]) Array.newInstance(type,newSize);

        for(int i = 0 ; i < array.length; i++){
            if(array[i] != null){
                int itemHash = Math.abs(array[i].hashCode());
              //  System.out.println(itemHash);
                temp[itemHash % newSize] = array[i];
            }
        }

        freeSpace = newSize - (tableSize-freeSpace);
        tableSize = newSize;

        array = temp;
    }


    public boolean insert(T data){
        TableItem<T> item = new TableItem<>(data);

        int hashCode = Math.abs(data.hashCode());
        int tableSize = array.length;

        if(freeSpace <= 15){
            resize();
        }

        if(array[hashCode % tableSize] != null)
            return false;

        array[hashCode % tableSize] = (U) item;
        freeSpace--;
        return true;


    }

    public boolean delete(T data){
        int hashCode = Math.abs(data.hashCode());
        int tableSize = array.length;

        if(array[hashCode % tableSize] != null){
            freeSpace++;
            array[hashCode % tableSize] = null;
            return true;
        }

        return false;
    }

    public T search(T data){
        int hashCode = Math.abs(data.hashCode());
        int tableSize = array.length;
        if(array[hashCode % tableSize] != null){
            return array[hashCode % tableSize].getData();
        }

        return null;
    }


    public void printTable(){
        System.out.println("Table size: " + array.length);
        System.out.println("Free space: " + freeSpace);

        for(int i = 0 ; i < array.length; i++){
            if(array[i] != null)
                System.out.println(array[i].getData() + ": " + i);
        }
    }

    public ArrayList<T> getAllItems(){
        ArrayList<T> items = new ArrayList<>();

        for(int i = 0; i < array.length; i++){
            if(array[i] != null){
                items.add(array[i].getData());
            }
        }

        return items;

    }

    public int size(){
        return tableSize - freeSpace;
    }


}
