package Java.javaEight;

import java.util.List;
import java.util.Optional;

public class OptionalTest {
    public static void main(String[] args) {
        Order o =new Order();
        User ou = new User();
        ou.setName("shabi");
        o.setUser(ou);
        User u = new User();
        Optional.ofNullable(o).map(i->i.getUser()).map(i->i.getName()).map(i->i.toUpperCase()).ifPresent(i->u.setName(i));
        System.out.println(u.getName());
    }
}
class Order{
    private User user;
    private String packageName;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
class User{
    private String name;
    private List<String> friends;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }
}