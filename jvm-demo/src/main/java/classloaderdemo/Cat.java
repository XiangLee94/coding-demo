package classloaderdemo;

public class Cat implements MyInterface {

    static {
        System.out.println("init cat");
    }

    @Override
    public String saySelf() {
        return "CAT";
    }
}
