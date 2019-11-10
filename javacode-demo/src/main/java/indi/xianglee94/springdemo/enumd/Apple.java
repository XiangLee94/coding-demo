package indi.xianglee94.springdemo.enumd;

public enum  Apple {
    FUJI("富士","produce from shandong"),
    PIPPIN("皮蓬","a red and yellow dessert apple."),
    GRANNY_SMITH("澳洲青苹","from Aus");
    private String name ;
    private String desc ;

    Apple(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }
}


class Test{
    public static void main(String[] args) {
        Apple[] list = Apple.values();
    }
}
