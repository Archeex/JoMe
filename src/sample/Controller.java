package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Controller {

    //MySQL
    // private String urlDB = "jdbc:mysql://51.38.132.58:3306/jome";
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


    private Label signInTitle = new Label("Sign in:");
    private TextField signInLogin = new TextField();
    private PasswordField signInPassword = new PasswordField();
    private Button signInButtonAccept = new Button("Sign in");

    private HBox loginHBox = new HBox(new Label("Login:"), signInLogin);
    private HBox passwordHBox = new HBox(new Label("Password:"), signInPassword);

    private WebView webView = new WebView();
    private WebEngine webEngine = webView.getEngine();

    private Connection connection = null;
    private Statement statement = null;


    private VBox userVBox = new VBox();

    @FXML
    protected void initialize() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            connection = DriverManager.getConnection(urlDB, usernameDB, passDB);
            System.out.println("Connection to Store DB succesfull!");
        } catch (Exception ex) {
            System.out.println("Connection failed...");

            System.out.println(ex);
        }

        signInButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
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
        });

        signUpButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
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
        });
        signInButtonAccept.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                statement = connection.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        String sql = "INSERT INTO accounts (login, password) VALUES ('zubko', '12345678');";
        //String sql = "INSERT INTO accounts (login, password) VALUES ('" + signInLogin.getText() + "'), '" + signInPassword.getText() + "');";
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

