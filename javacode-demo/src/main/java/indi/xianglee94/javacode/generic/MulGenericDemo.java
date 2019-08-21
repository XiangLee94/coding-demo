package indi.xianglee94.javacode.generic;

public class MulGenericDemo<T, A, R> {
    public static void main(String[] args) {

    }

    public <A,R> R getR(MulGenericDemo<? super T,A,R> gene){
        return null;
    }

}
class Gene<T, R>{

}
class Atype {

}
class Btype {

}
class Ctype {

}