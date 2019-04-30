package sample;

import javax.swing.plaf.nimbus.State;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static sample.Controller.connection;

public class User {

    private Integer id = null;
    private String login = null;
    private String password = null;
    private List<User> friendsList = new ArrayList<>();
    private Float coordinateX;
    private Float coordinateY;

    User(Integer id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    void addFriend(Integer friendId) {
        String sql;
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            sql = "SELECT * FROM accounts WHERE id = '" + friendId + "'";
            resultSet = statement.executeQuery(sql);
            assert resultSet != null;
            if (resultSet.next()) {
                User newFriend = new User(friendId, resultSet.getString("login"), resultSet.getString("password"));
                newFriend.setCoordinateX(resultSet.getFloat("coordinateX"));
                newFriend.setCoordinateY(resultSet.getFloat("coordinateY"));
                friendsList.add(newFriend);
            } else {
                throw new Exception("MySQL Add Friend error!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    List<User> getFriends() {
        return friendsList;
    }

    Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    String getLogin() {
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

    public Float getCoordinateX() {
        return coordinateX;
    }
    void setCoordinateX(Float coordinateX) {
        this.coordinateX = coordinateX;
    }
    public Float getCoordinateY() {
        return coordinateY;
    }
    void setCoordinateY(Float coordinateY) {
        this.coordinateY = coordinateY;
    }
}
