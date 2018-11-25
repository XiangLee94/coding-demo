package Java.javaEight;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamTest {
    public static void main(String[] args) {

        Trader liu = new Trader("Lau", "Beijing");
        Trader lee = new Trader("Lee", "Shanghai");
        Trader zhang = new Trader("Zhang", "Guangzhou");
        Trader wang = new Trader("Wang", "Beijing");

        List<Transaction> transactions = Arrays.asList(
                new Transaction(liu, 2016, 300),
                new Transaction(lee, 2015, 100),
                new Transaction(lee, 2016, 500),
                new Transaction(zhang, 2016, 9000),
                new Transaction(wang, 2017, 1000),
                new Transaction(liu, 2016, 1500)
        );

        List<Transaction> list = transactions.stream().filter(i -> i.getYear() == 2016).sorted((i1, i2) -> i1.getValue() == i2.getValue() ? 0 : (i1.getValue() > i2.getValue() ? -1 : 1)).collect(Collectors.toList());
        List<String> citys = transactions.stream().map(i -> i.getTrader()).map(i -> i.getCity()).distinct().collect(Collectors.toList());
        List<String> names = transactions.stream().map(Transaction::getTrader).map(Trader::getName).sorted().distinct().collect(Collectors.toList());
        boolean b = !transactions.stream().map(Transaction::getTrader).map(i -> {
            citys.add(i.getCity());
            return i.getCity();
        }).filter(i -> i.equals("Guangzhou")).collect(Collectors.toList()).isEmpty();
        String a = transactions.stream().map(Transaction::getTrader).map(Trader::getCity).reduce((i, j) -> j).get();


        System.out.println(a);
        names.forEach(System.out::println);
        // citys.forEach(System.out::println);
        // list.forEach(System.out::println);

    }
}

class Trader {

    private String name;
    private String city;

    public Trader(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Trader{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}

class Transaction {

    private Trader trader;
    private int year;
    private int value;

    public Transaction(Trader trader, int year, int value) {
        this.trader = trader;
        this.year = year;
        this.value = value;
    }

    public Trader getTrader() {
        return trader;
    }

    public void setTrader(Trader trader) {
        this.trader = trader;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "trader=" + trader +
                ", year='" + year + '\'' +
                ", value=" + value +
                '}';
    }
}

