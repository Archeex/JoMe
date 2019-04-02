package sample;

import java.util.ArrayList;
import java.util.List;

public class User {

    private Integer id = null;
    private String login = null;
    private String password = null;

    private List<Integer> friendsList = new ArrayList<>();

    User(Integer id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public void addFriend(Integer friendId) {
        friendsList.add(friendId);
    }
    public List<Integer> getFriends() {
        return friendsList;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
