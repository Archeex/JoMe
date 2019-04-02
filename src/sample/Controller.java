package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.sql.*;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

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

    private Label signInUpError = new Label();

    private HBox loginHBox = new HBox(new Label("Login:"), signInUpLogin);
    private HBox passwordHBox = new HBox(new Label("Password:"), signInUpPassword);

    private WebView webView = new WebView();
    private WebEngine webEngine = webView.getEngine();

    private Connection connection = null;
    private Statement statement = null;

    private VBox userVBox = new VBox();
    private User user;

    @FXML
    protected void initialize() {
        ConnectToDatabase();

        signInButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            regAuthVBox.getChildren().remove(signInButton);
            regAuthVBox.getChildren().remove(signUpButton);

            ShowSignInWindow();
        });
        signInButtonAccept.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
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
                    regAuthVBox.getChildren().remove(signInButtonAccept);
                    regAuthVBox.getChildren().remove(loginHBox);
                    regAuthVBox.getChildren().remove(passwordHBox);
                    regAuthVBox.getChildren().remove(signInTitle);
                    regAuthVBox.getChildren().remove(signInUpError);

                    ShowMainWindow();
                    SaveUserInfo(resultSet.getInt("id"), resultSet.getString("login"), resultSet.getString("password"));
                } else {
                    throw new Exception("MySQL Add to Database error!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        signUpButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            regAuthVBox.getChildren().remove(signInButton);
            regAuthVBox.getChildren().remove(signUpButton);

            ShowSignUpWindow();
        });
        signUpButtonAccept.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            String sql;
            ResultSet resultSet = null;
            try {
                statement = connection.createStatement();

                sql = "SELECT * FROM accounts WHERE login = '" + signInUpLogin.getText() + "' AND password = '" + signInUpPassword.getText() + "'";
                resultSet = statement.executeQuery(sql);

                assert resultSet != null;
                if (resultSet.next()) {
                    regAuthVBox.getChildren().remove(signUpButtonAccept);
                    regAuthVBox.getChildren().remove(loginHBox);
                    regAuthVBox.getChildren().remove(passwordHBox);
                    regAuthVBox.getChildren().remove(signUpTitle);
                    regAuthVBox.getChildren().remove(signInUpError);

                    ShowMainWindow();
                    SaveUserInfo(resultSet.getInt("id"), resultSet.getString("login"), resultSet.getString("password"));
                } else {
                    signInUpError.setText("Incorrect data or no such account!");
                    throw new Exception("MySQL Add to Database error!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void ShowMainWindow() {
        regAuthVBox.getChildren().remove(signInButton);
        regAuthVBox.getChildren().remove(signUpButton);

        regAuthVBox.setLayoutX(0);
        regAuthVBox.setPrefWidth(700);

        userVBox.setLayoutX(700);
        userVBox.setPrefWidth(300);
        userVBox.setSpacing(10);
        userVBox.setAlignment(Pos.CENTER);

        Label userPage = new Label("User Page");
        userPage.setFont(new Font("Arial", 24));
        userVBox.getChildren().add(userPage);
        ListView<String> friends = new ListView<>();
        friends.setItems(FXCollections.observableArrayList("Klimded", "Archeex", "alyohea", "ZubDestroy", "IvanMazur"));
        TitledPane friendsList = new TitledPane("Friends", friends);
        friendsList.setAnimated(true);
        friendsList.setExpanded(false);
        userVBox.getChildren().add(friendsList);

        mainPane.getChildren().add(userVBox);

        webEngine.load("https://www.google.com/maps");

        regAuthVBox.getChildren().addAll(webView);
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

        regAuthVBox.getChildren().add(signUpButtonAccept);
        regAuthVBox.getChildren().add(signInUpError);
    }
    private void ShowSignInWindow() {
        regAuthVBox.getChildren().remove(signInButton);
        regAuthVBox.getChildren().remove(signUpButton);

        regAuthVBox.getChildren().add(signInTitle);

        loginHBox.setMaxWidth(250);
        loginHBox.setSpacing(20);
        loginHBox.setAlignment(Pos.CENTER_LEFT);
        regAuthVBox.getChildren().add(loginHBox);

        passwordHBox.setMaxWidth(250);
        passwordHBox.setSpacing(20);
        passwordHBox.setAlignment(Pos.CENTER_LEFT);
        regAuthVBox.getChildren().add(passwordHBox);

        regAuthVBox.getChildren().add(signInButtonAccept);
        regAuthVBox.getChildren().add(signInUpError);
    }
    private void SaveUserInfo(Integer id, String login, String password) {
        user = new User(id, login, password);
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
}
