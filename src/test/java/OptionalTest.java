import java.util.List;
import java.util.Optional;

public class OptionalTest {
    public static void main(String[] args) {
        User defaultUser = new User();
        defaultUser.setName("laoli");
        User u =  null ;
        Optional<User> user = Optional.ofNullable(defaultUser);
        user.map(i->i.getName()).map(i->i.toUpperCase()).ifPresent(System.out::println);

    }
}





class User{
    private List<String> orders;
    private String name;
    private int age;

    public List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
