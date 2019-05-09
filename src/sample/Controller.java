package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.lang.Float;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Controller {

    //MySQL
    private String urlDB = "jdbc:mysql://localhost:3306/jome?serverTimezone=Europe/Moscow&useSSL=false";
    private String usernameDB = "root";
    private String passDB = "";

    @FXML
    Pane mainPane;

    @FXML
    VBox regAuthVBox;

    @FXML
    Button signInButton;
    @FXML
    Button signUpButton;

    //Fields
    private Label signInTitle = new Label("Sign in:");
    private TextField signInUpLogin = new TextField();
    private PasswordField signInUpPassword = new PasswordField();
    private Button signInButtonAccept = new Button("Sign in");
    private Label signUpTitle = new Label("Sign up:");
    private Button signUpButtonAccept = new Button("Sign up");

    private Button backToRegAuthButon = new Button("Back");

    private Label signInUpError = new Label();

    private HBox loginHBox = new HBox(new Label("Login:"), signInUpLogin);
    private HBox passwordHBox = new HBox(new Label("Password:"), signInUpPassword);

    private WebView webView = new WebView();
    private WebEngine webEngine = webView.getEngine();

    static Connection connection = null;
    private Statement statement = null;

    private Button userAddFriendButton = new Button("Add friend");

    private VBox userVBox = new VBox();
    static User user;

    static ListView<String> friends = new ListView<>();

    @FXML
    ImageView logo;

    @FXML
    protected void initialize() {
        //mainPane.getStylesheets().add("res/signInButton.css");
//        signInButton.setStyle("signInButton.css");
        ConnectToDatabase();

        signInButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            regAuthVBox.getChildren().remove(signInButton);
            regAuthVBox.getChildren().remove(signUpButton);
            regAuthVBox.getChildren().remove(logo);

            ShowSignInWindow();
        });
        signInButtonAccept.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            String sql;
            ResultSet resultSet = null;
            try {
                statement = connection.createStatement();

                sql = "SELECT * FROM accounts WHERE login = '" + signInUpLogin.getText() + "' AND password = '" + signInUpPassword.getText() + "'";
                resultSet = statement.executeQuery(sql);

                assert resultSet != null;
                if (resultSet.next()) {
                    regAuthVBox.getChildren().remove(signInButtonAccept);
                    regAuthVBox.getChildren().remove(loginHBox);
                    regAuthVBox.getChildren().remove(passwordHBox);
                    regAuthVBox.getChildren().remove(signInTitle);
                    regAuthVBox.getChildren().remove(signInUpError);
                    regAuthVBox.getChildren().remove(backToRegAuthButon);

                    LoadUserInfo(resultSet.getInt("id"), resultSet.getString("login"), resultSet.getString("password"), resultSet.getFloat("coordinateX"), resultSet.getFloat("coordinateY"));
                    ShowMainWindow();
                } else {
                    signInUpError.setText("Incorrect data or no such account!");
                    throw new Exception("MySQL Add to Database error!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        backToRegAuthButon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            regAuthVBox.getChildren().removeAll(signInTitle, signUpTitle, signInButtonAccept, signUpButtonAccept, backToRegAuthButon, loginHBox, passwordHBox, signInUpError);
            ShowStartupWindow();
        });
        userAddFriendButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Parent root;
            try {
                root = FXMLLoader.load(getClass().getResource("addFriend.fxml"));
                //AddFriendController addFriendController = new AddFriendController(user, connection, statement);
                Stage stage = new Stage();
                stage.setTitle("JoMe / Add friend");
                stage.setScene(new Scene(root, 450, 250));
                stage.show();
                // Hide this current window (if this is what you want)
                //((Node)(event.getSource())).getScene().getWindow().hide();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            webEngine.load("https://goo.gl/maps/prciAcn9z862");
        });

        signUpButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            regAuthVBox.getChildren().remove(signInButton);
            regAuthVBox.getChildren().remove(signUpButton);
            regAuthVBox.getChildren().remove(logo);

            ShowSignUpWindow();
        });
        signUpButtonAccept.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            String sql;
            ResultSet resultSet = null;
            try {
                statement = connection.createStatement();

                sql = "SELECT * FROM accounts WHERE login = '" + signInUpLogin.getText() + "'";
                resultSet = statement.executeQuery(sql);
                if(resultSet.next()) {
                    signInUpError.setText("This login is used!");
                    throw new Exception("MySQL Add to Database error!");
                }

                sql = "INSERT INTO accounts (login, password) VALUES ('" + signInUpLogin.getText() + "', '" + signInUpPassword.getText() + "')";
                statement.executeUpdate(sql);

                sql = "SELECT * FROM accounts WHERE login = '" + signInUpLogin.getText() + "'";
                resultSet = statement.executeQuery(sql);

                assert resultSet != null;
                if (resultSet.next()) {
                    regAuthVBox.getChildren().remove(signUpButtonAccept);
                    regAuthVBox.getChildren().remove(loginHBox);
                    regAuthVBox.getChildren().remove(passwordHBox);
                    regAuthVBox.getChildren().remove(signUpTitle);
                    regAuthVBox.getChildren().remove(signInUpError);
                    regAuthVBox.getChildren().remove(backToRegAuthButon);

                    SaveUserInfo(resultSet.getInt("id"), resultSet.getString("login"), resultSet.getString("password"), resultSet.getFloat("coordinateX"), resultSet.getFloat("coordinateY"));
                    ShowMainWindow();
                } else {
                    throw new Exception("MySQL Add to Database error!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void ShowStartupWindow() {
        regAuthVBox.getChildren().addAll(logo, signInButton, signUpButton);
    }
    private void ShowMainWindow() {
        regAuthVBox.setLayoutX(0);
        regAuthVBox.setPrefWidth(700);

        userVBox.setLayoutX(700);
        userVBox.setPrefWidth(300);
        userVBox.setSpacing(10);
        userVBox.setAlignment(Pos.CENTER);

        Label userPage = new Label("User: " + user.getLogin());
        userPage.setPadding(new Insets(10, 0, 0, 0));
        userPage.setFont(new Font("Arial", 24));
        userVBox.getChildren().add(userPage);

        friends.setItems(FXCollections.observableList(LoadFriends(user.getFriends())));
        TitledPane friendsList = new TitledPane("Friends", friends);
        friendsList.setAnimated(true);
        friendsList.setExpanded(false);

        userVBox.getChildren().add(friendsList);

        userVBox.getChildren().add(userAddFriendButton);

        mainPane.getChildren().add(userVBox);

        webEngine.load(CreateMapRequest());
        webView.setContextMenuEnabled(false);
        regAuthVBox.getChildren().addAll(webView);
    }

    private String CreateMapRequest() {
        StringBuilder mapURL = new StringBuilder("https://maps.googleapis.com/maps/api/staticmap?&size=700x550&maptype=roadmap");
        for(User item : user.getFriends()) {
            if(!item.getCoordinateX().equals(0.0f)) {
                mapURL.append("&markers=color:blue%7Clabel:").append(GetIdInList(item.getLogin())).append("%7C").append(item.getCoordinateX()).append(",").append(item.getCoordinateY());
            }
        }
        mapURL.append("&key=AIzaSyDJZqRCFMS5d0eU8K5Sch2mhQYjzqDbgRM");
        System.out.println(mapURL);
        return mapURL.toString();
    }

    private Integer GetIdInList(String name) {
        Integer count = 1;
        for(String item : friends.getItems()) {
            if(item.contains(name))
                return count;
            count++;
        }
        return -1;
    }

    private void ShowSignInWindow() {
        regAuthVBox.getChildren().add(signInTitle);

        loginHBox.setMaxWidth(250);
        loginHBox.setSpacing(20);
        loginHBox.setAlignment(Pos.CENTER_LEFT);
        regAuthVBox.getChildren().add(loginHBox);

        passwordHBox.setMaxWidth(250);
        passwordHBox.setSpacing(20);
        passwordHBox.setAlignment(Pos.CENTER_LEFT);
        regAuthVBox.getChildren().add(passwordHBox);

        regAuthVBox.getChildren().addAll(signInButtonAccept, backToRegAuthButon);
        regAuthVBox.getChildren().add(signInUpError);
    }
    private void ShowSignUpWindow() {
        regAuthVBox.getChildren().add(signUpTitle);

        loginHBox.setMaxWidth(250);
        loginHBox.setSpacing(20);
        loginHBox.setAlignment(Pos.CENTER_LEFT);
        regAuthVBox.getChildren().add(loginHBox);

        passwordHBox.setMaxWidth(250);
        passwordHBox.setSpacing(20);
        passwordHBox.setAlignment(Pos.CENTER_LEFT);
        regAuthVBox.getChildren().add(passwordHBox);

        regAuthVBox.getChildren().addAll(signUpButtonAccept, backToRegAuthButon);
        regAuthVBox.getChildren().add(signInUpError);
    }
    private void SaveUserInfo(Integer id, String login, String password, Float x, Float y) {
        user = new User(id, login, password);
        user.setCoordinateX(x);
        user.setCoordinateY(y);
    }
    private void LoadUserInfo(Integer id, String login, String password, Float x, Float y) {
        user = new User(id, login, password);
        user.setCoordinateX(x);
        user.setCoordinateY(y);

        String sql;
        ResultSet resultSet = null;
        String friends;
        try {
            statement = connection.createStatement();
            sql = "SELECT * FROM accounts WHERE id = '" + id + "'";
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                friends = resultSet.getString("friends");
                if(!friends.isEmpty()) {
                    for(String str : friends.split(",")) {
                        user.addFriend(Integer.valueOf(str));
                    }
                }
                else {
                    throw new Exception("No friends :(");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void ConnectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            connection = DriverManager.getConnection(urlDB, usernameDB, passDB);
            System.out.println("Connection to Store DB succesfull!");
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
    }
    public static List<String> LoadFriends(List<User> list) {
        List<String> newList = new ArrayList<>();
        Integer num = 1;
        for (User item : list) {
            newList.add(num++ + ": " + item.getLogin());
        }
        return newList;
    }

    @Deprecated
    private List<String> LoadFriendsById(List<Integer> list) {
        List<String> newList = new ArrayList<>();
        for (Integer item : list) {
            String sql;
            ResultSet resultSet = null;
            try {
                statement = connection.createStatement();

                sql = "SELECT * FROM accounts WHERE id = '" + item + "'";
                resultSet = statement.executeQuery(sql);

                assert resultSet != null;
                if (resultSet.next())
                    newList.add(resultSet.getString("login"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newList;
    }
}
