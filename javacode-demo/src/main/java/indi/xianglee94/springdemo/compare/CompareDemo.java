package indi.xianglee94.springdemo.compare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CompareDemo {
    public static void main(String[] args) {
        List<Human> humans = new ArrayList<>();
        humans.add(new Human("Sarah", 10));
        humans.add(new Human("Jack", 12));

        //simple lambda demo
        humans.sort((h1, h2) -> h1.getName().compareTo(h2.getName()));
        //static invoke replace lambda
        humans.sort(Human::compareByNameThenAge);
        //Collections.sort
        Collections.sort(humans,Comparator.comparing(Human::getName));
        //直接构造Comparator
        humans.sort(Comparator.comparing(Human::getName));

    }


}


class Human {
    private String name;
    private int age;

    public Human(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static int compareByNameThenAge(Human lhs, Human rhs) {
        if (lhs.name.equals(rhs.name)) {
            return lhs.age - rhs.age;
        } else {
            return lhs.name.compareTo(rhs.name);
        }
    }
}
