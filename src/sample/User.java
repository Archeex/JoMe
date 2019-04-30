package sample;

<<<<<<< HEAD
import javax.swing.plaf.nimbus.State;
import java.sql.ResultSet;
=======
import javafx.collections.FXCollections;

import java.sql.ResultSet;
import java.sql.SQLException;
>>>>>>> f886a3b5f38ac11e0e833055659c84dbf72baa07
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static sample.Controller.connection;

public class User {

    private Integer id = null;
    private String login = null;
    private String password = null;
<<<<<<< HEAD
    private List<User> friendsList = new ArrayList<>();
    private Float coordinateX;
    private Float coordinateY;
=======

    private List<String> friendsList = new ArrayList<>();
>>>>>>> f886a3b5f38ac11e0e833055659c84dbf72baa07

    User(Integer id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

<<<<<<< HEAD
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
=======
    public void addFriend(String friendLogin) {
        friendsList.add(friendLogin);
    }
    public List<String> getFriends() {
        String sql;
        ResultSet resultSet = null;
        try {
            Statement statement = Controller.connection.createStatement();

            sql = "SELECT * FROM accounts WHERE login = '" + Controller.user.getLogin() + "'";
            resultSet = statement.executeQuery(sql);

            assert resultSet != null;
            if (resultSet.next())
                friendsList = ParseStringToList(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return friendsList;
    }

    private List<String> ParseStringToList(ResultSet resultSet) throws SQLException {
        String str = resultSet.getString("friends");
        List<String> list = new ArrayList<>();
        List<String> newList = new ArrayList<>();
        if(!str.equals("")) {
            list.addAll(FXCollections.observableArrayList(str.split(",")));
            //Create names with id's
            for(String item : list) {
                String sql;
                Statement statement = Controller.connection.createStatement();

                sql = "SELECT * FROM accounts WHERE id = '" + Integer.valueOf(item) + "'";
                resultSet = statement.executeQuery(sql);

                assert resultSet != null;
                if (resultSet.next())
                    newList.add(resultSet.getString("login") + "#" + resultSet.getInt("id"));
                //System.out.println(item);
            }
        }

        return newList;
    }

    public Integer getId() {
>>>>>>> f886a3b5f38ac11e0e833055659c84dbf72baa07
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
