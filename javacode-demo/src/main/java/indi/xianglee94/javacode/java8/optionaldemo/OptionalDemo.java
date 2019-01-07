package indi.xianglee94.javacode.java8.optionaldemo;

import java.util.Optional;

public class OptionalDemo {

    public static void main(String[] args) {
        Person person = new Person();
        Optional<Person> optionalPerson =  Optional.of(person);
        //how to use map() ，自动封装成Optional
        Optional<Integer> optionalInteger1 = optionalPerson.map(Person::getCar).map(Car::getInsurance).map(Insurance::getAmount);
        //how to use flatmap , 不会自动封装成Optional
        Optional<Integer> optionalInteger2 = optionalPerson.flatMap(i->Optional.ofNullable(i.getCar())).flatMap(i->Optional.ofNullable(i.getInsurance()))
                .flatMap(i->Optional.ofNullable(i.getAmount()));
    }

}

class Person{
    private Car car;

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
class Car{
    private Insurance insurance;

    public Insurance getInsurance() {
        return insurance;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }
}
class Insurance{
    private Integer amount;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}