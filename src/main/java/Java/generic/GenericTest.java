package Java.generic;

import java.util.List;

public class GenericTest <T>{
    public static void main(String[] args) {

    }

    public <T extends Human> void  addHuman(List<? super T> list, T human){
        list.add(human);
    }

    public T get(List<? super Human> list){
      ///  return list.get(0);
        return null;
    }
}


abstract class Human implements Animal {
    public abstract void mei();
}

class Man extends Human {
    @Override
    public void mei() {

    }
}

class Woman extends Human {
    @Override
    public void mei() {

    }
}

interface Animal{

}


class Pair<K,T,V> {
    private K key;
    private T tt;
    private V value;
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    public void setKey(K key) { this.key = key; }
    public void setValue(V value) { this.value = value; }
    public K getKey()   { return key; }
    public V getValue() { return value; }
}