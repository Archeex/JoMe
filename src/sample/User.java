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

    private void addFriend(Integer friendId) {
        friendsList.add(friendId);
    }
}
