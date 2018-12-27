package Java.javaEight;

import java.util.List;
import java.util.Optional;

public class OptionalTest {
    public static void main(String[] args) {
        Order o =new Order();
        User ou = new User();
//        ou.setName("shabi");
        o.setUser(ou);
        User u = new User();
        u.setName("nihao");
        Optional.ofNullable(o).map(Order::getUser).map(rdd->rdd.getName()).map(String::toUpperCase).ifPresent(i->u.setName(i));
        if(u!=null){
            if(o.getUser() != null){
                if(o.getUser().getName() != null){

                }
            }
        }
        u.setName(o.getUser().getName().toUpperCase());
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