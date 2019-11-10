package classloaderdemo;

public class Dog implements  MyInterface {
    static {
        System.out.println("init dog");
    }

    @Override
    public String saySelf() {
        System.out.println("imma DOGG");
        return "DOG";
    }
}
