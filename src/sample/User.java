package sample;

import javafx.collections.FXCollections;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static sample.Controller.connection;

public class User {

    private Integer id = null;
    private String login = null;
    private String password = null;

    private List<String> friendsList = new ArrayList<>();

    User(Integer id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

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
