package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.lang.Float;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

    private Button backToRegAuthButtonIn = new Button("Back");
    private Button backToRegAuthButtonUp = new Button("Back");

    HBox signInButtons = new HBox(signInButtonAccept, backToRegAuthButtonIn);
    HBox signUpButtons = new HBox(signUpButtonAccept, backToRegAuthButtonUp);

    private Label signInUpError = new Label();

    Label signInUpLoginText = new Label("Login");
    Label signInUpPasswordText = new Label("Password");

    private WebView webView = new WebView();
    private WebEngine webEngine = webView.getEngine();

    static Connection connection = null;
    private Statement statement = null;

    private Button userAddFriendButton = new Button("Add friend");

    private VBox userVBox = new VBox();
    static User user;
    Label userPage = new Label();

    Label friendsLabel = new Label("Friends");
    ScrollPane friendsPane = new ScrollPane();
    ArrayList<Label> friendsList = new ArrayList<>();
//    TitledPane friendsList = new TitledPane();
    static ListView<String> friends = new ListView<>();

    @FXML
    ImageView logo;

    private ImageView exitIcon = new ImageView();
    private ImageView rollIcon = new ImageView();

    private double xOffset = 0;
    private double yOffset = 0;

    static Stage stage;

    @FXML
    protected void initialize() {
        ConnectToDatabase();
        SetHandlers();
        InitializeStyle();
    }

    private void InitializeStyle() {
        exitIcon.setImage(new Image("res/iconExit.png", 15, 15, false, false));
        rollIcon.setImage(new Image("res/iconRoll.png", 15, 5, false, false));
        mainPane.getChildren().addAll(rollIcon, exitIcon);
        exitIcon.setLayoutX(920);
        exitIcon.setLayoutY(3);
        rollIcon.setLayoutX(900);
        rollIcon.setLayoutY(12);
        logo.setImage(new Image("res/logo.png"));
        mainPane.setStyle("-fx-background-image: url(\"res/background.png\");");
        signInButton.setStyle("-fx-background-color: #6ebb7e");
        signUpButton.setStyle("-fx-background-color: #7085d1");

        signInTitle.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 24px; -fx-font-weight: bold;");
        signUpTitle.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 24px; -fx-font-weight: bold;");
        signInUpLoginText.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 16px;");
        signInUpPasswordText.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 16px;");
        signInUpError.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 16px;");

        signInButtons.setSpacing(20);
        signInButtonAccept.setStyle("-fx-text-fill: #FFFFFF; -fx-background-color: #6ebb7e;");
        signUpButtons.setSpacing(20);
        signUpButtonAccept.setStyle("-fx-text-fill: #FFFFFF; -fx-background-color: #7085d1;");
        backToRegAuthButtonIn.setStyle("-fx-text-fill: #FFFFFF; -fx-background-color: #f0735e;");
        backToRegAuthButtonUp.setStyle("-fx-text-fill: #FFFFFF; -fx-background-color: #f0735e;");
        signInUpLogin.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFFFF; -fx-border-color: #FFFFFF; -fx-border-width: 1;");
        signInUpPassword.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFFFF; -fx-border-color: #FFFFFF; -fx-border-width: 1;");

        friendsLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 16px;");
        userPage.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 24px; -fx-font-weight: bold;");
        userAddFriendButton.setStyle("-fx-text-fill: #FFFFFF; -fx-background-color: #ddcb49; -fx-font-size: 16px;");

//        friendsList.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFFFF; -fx-border-color: #FFFFFF; -fx-border-width: 1;");

        regAuthVBox.setSpacing(10);
    }


    private void ShowStartupWindow() {
        regAuthVBox.getChildren().addAll(logo, signInButton, signUpButton);
    }
    private void ShowMainWindow() {
        regAuthVBox.setLayoutX(0);
        regAuthVBox.setLayoutY(0);
        regAuthVBox.setPrefWidth(640);
        regAuthVBox.setPrefHeight(530);

        userVBox.setLayoutY(20);
        userVBox.setLayoutX(640);
        userVBox.setPrefWidth(300);
        userVBox.setPrefHeight(530);
        userVBox.setSpacing(10);
        userVBox.setAlignment(Pos.TOP_CENTER);

        userPage.setText("User: " + user.getLogin());
        userPage.setPadding(new Insets(10, 0, 0, 0));
//        userPage.setFont(new Font("Arial", 24));
        userVBox.getChildren().add(userPage);

        SetFriendsList();
        VBox friendsVBox = new VBox();
        friendsVBox.setSpacing(5);
        for(Label item : friendsList) {
            friendsVBox.getChildren().add(item);
        }
        friendsPane.setContent(friendsVBox);
//        friendsPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        friendsPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent ; -fx-hbar-policy: never; -fx-vbar-policy : never; -fx-background-insets: 0;");
        friendsPane.setPrefViewportHeight(400);
//        friendsPane.setDisable(true);
        userVBox.getChildren().add(friendsPane);

        userVBox.getChildren().add(userAddFriendButton);

        mainPane.getChildren().add(userVBox);

        webEngine.load(CreateMapRequest());
        webView.setContextMenuEnabled(false);
        regAuthVBox.getChildren().addAll(webView);
    }

    private void SetFriendsList() {
        ArrayList<String> list = new ArrayList<>(FXCollections.observableList(LoadFriends(user.getFriends())));
        for(String item: list) {
            Label label = new Label(item);
            label.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFFFF; -fx-border-color: #FFFFFF; -fx-border-width: 1; -fx-font-size: 14px;");
            label.setPrefWidth(295);
            label.setPrefHeight(40);
            label.setPadding(new Insets(0, 0, 0, 10));
            label.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                Main.getPrimaryStage().getScene().setCursor(Cursor.HAND);
                label.setStyle("-fx-border-color: #ddcb49; -fx-font-size: 14px;");
                label.setTooltip(new Tooltip("Some address here.. Soon.")); //TODO
            });
            label.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
                Main.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
                label.setStyle("-fx-border-color: #FFFFFF; -fx-font-size: 14px;");
            });
            friendsList.add(label);
        }
    }

    private void ShowSignInWindow() {
        regAuthVBox.setSpacing(10);
        regAuthVBox.getChildren().add(signInTitle);
        regAuthVBox.getChildren().add(signInUpLoginText);
        regAuthVBox.getChildren().add(signInUpLogin);
        regAuthVBox.getChildren().add(signInUpPasswordText);
        regAuthVBox.getChildren().add(signInUpPassword);

        regAuthVBox.getChildren().add(signInButtons);
        regAuthVBox.getChildren().add(signInUpError);
    }
    private void ShowSignUpWindow() {
        regAuthVBox.getChildren().add(signUpTitle);
        regAuthVBox.getChildren().add(signInUpLoginText);
        regAuthVBox.getChildren().add(signInUpLogin);
        regAuthVBox.getChildren().add(signInUpPasswordText);
        regAuthVBox.getChildren().add(signInUpPassword);

        regAuthVBox.getChildren().add(signUpButtons);
        regAuthVBox.getChildren().add(signInUpError);
    }

    private void SetHandlers() {
        signInButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            regAuthVBox.getChildren().remove(signInButton);
            regAuthVBox.getChildren().remove(signUpButton);
            regAuthVBox.getChildren().remove(logo);
            signInUpError.setText("");
            signInUpLogin.setText("");
            signInUpPassword.setText("");

            ShowSignInWindow();
        });
        signInButtonAccept.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(signInUpLogin.getText().isEmpty() || signInUpPassword.getText().isEmpty()) {
                signInUpError.setText("Incorrect data or no such account!");
            }
            else {
                String sql;
                ResultSet resultSet = null;
                try {
                    statement = connection.createStatement();

                    sql = "SELECT * FROM accounts WHERE login = '" + signInUpLogin.getText() + "' AND password = '" + signInUpPassword.getText() + "'";
                    resultSet = statement.executeQuery(sql);

                    assert resultSet != null;
                    if (resultSet.next()) {
                        regAuthVBox.getChildren().remove(signInTitle);
                        regAuthVBox.getChildren().remove(signInUpLoginText);
                        regAuthVBox.getChildren().remove(signInUpLogin);
                        regAuthVBox.getChildren().remove(signInUpPasswordText);
                        regAuthVBox.getChildren().remove(signInUpPassword);
                        regAuthVBox.getChildren().remove(signInButtons);
                        regAuthVBox.getChildren().remove(signInUpError);

                        LoadUserInfo(resultSet.getInt("id"), resultSet.getString("login"), resultSet.getString("password"), resultSet.getString("coordinateX"), resultSet.getString("coordinateY"));
                        ShowMainWindow();
                    } else {
                        signInUpError.setText("Incorrect data or no such account!");
                        throw new Exception("MySQL Add to Database error!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        backToRegAuthButtonIn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            regAuthVBox.getChildren().removeAll(signInTitle, signUpTitle, signInButtons, signInUpLoginText, signInUpLogin, signInUpPasswordText, signInUpPassword, signInUpError);
            ShowStartupWindow();
        });
        backToRegAuthButtonUp.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            regAuthVBox.getChildren().removeAll(signInTitle, signUpTitle, signUpButtons, signInUpLoginText, signInUpLogin, signInUpPasswordText, signInUpPassword, signInUpError);
            ShowStartupWindow();
        });

        userAddFriendButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Parent root;
            try {
                root = FXMLLoader.load(getClass().getResource("addFriend.fxml"));
                //AddFriendController addFriendController = new AddFriendController(user, connection, statement);
                stage = new Stage();
                stage.setTitle("JoMe / Add friend");
                stage.setScene(new Scene(root, 450, 250));
                stage.initStyle(StageStyle.UNDECORATED);
                stage.show();
                // Hide this current window (if this is what you want)
                //((Node)(event.getSource())).getScene().getWindow().hide();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

        signUpButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            regAuthVBox.getChildren().remove(signInButton);
            regAuthVBox.getChildren().remove(signUpButton);
            regAuthVBox.getChildren().remove(logo);
            signInUpError.setText("");
            signInUpLogin.setText("");
            signInUpPassword.setText("");

            ShowSignUpWindow();
        });
        signUpButtonAccept.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(signInUpLogin.getText().isEmpty() || signInUpPassword.getText().isEmpty()) {
                signInUpError.setText("Incorrect data or no such account!");
            }
            else {
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
                        regAuthVBox.getChildren().remove(signUpTitle);
                        regAuthVBox.getChildren().remove(signInUpLoginText);
                        regAuthVBox.getChildren().remove(signInUpLogin);
                        regAuthVBox.getChildren().remove(signInUpPasswordText);
                        regAuthVBox.getChildren().remove(signInUpPassword);
                        regAuthVBox.getChildren().remove(signUpButtons);
                        regAuthVBox.getChildren().remove(signInUpError);

                        SaveUserInfo(resultSet.getInt("id"), resultSet.getString("login"), resultSet.getString("password"), resultSet.getString("coordinateX"), resultSet.getString("coordinateY"));
                        ShowMainWindow();
                    } else {
                        throw new Exception("MySQL Add to Database error!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        exitIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            System.exit(0);
        });
        rollIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Stage stage = (Stage) rollIcon.getScene().getWindow();
            stage.setIconified(true);
        });
        exitIcon.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> Main.getPrimaryStage().getScene().setCursor(Cursor.HAND));
        exitIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> Main.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));
        rollIcon.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> Main.getPrimaryStage().getScene().setCursor(Cursor.HAND));
        rollIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> Main.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));

        mainPane.setOnMousePressed(event -> {
            xOffset = Main.getPrimaryStage().getX() - event.getScreenX();
            yOffset = Main.getPrimaryStage().getY() - event.getScreenY();
        });
        mainPane.setOnMouseDragged(event -> {
            Main.getPrimaryStage().setX(event.getScreenX() + xOffset);
            Main.getPrimaryStage().setY(event.getScreenY() + yOffset);
        });

        signInButton.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> Main.getPrimaryStage().getScene().setCursor(Cursor.HAND));
        signInButton.addEventHandler(MouseEvent.MOUSE_EXITED, event -> Main.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));
        signUpButton.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> Main.getPrimaryStage().getScene().setCursor(Cursor.HAND));
        signUpButton.addEventHandler(MouseEvent.MOUSE_EXITED, event -> Main.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));
        signInButtonAccept.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> Main.getPrimaryStage().getScene().setCursor(Cursor.HAND));
        signInButtonAccept.addEventHandler(MouseEvent.MOUSE_EXITED, event -> Main.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));
        signUpButtonAccept.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> Main.getPrimaryStage().getScene().setCursor(Cursor.HAND));
        signUpButtonAccept.addEventHandler(MouseEvent.MOUSE_EXITED, event -> Main.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));
        backToRegAuthButtonIn.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> Main.getPrimaryStage().getScene().setCursor(Cursor.HAND));
        backToRegAuthButtonIn.addEventHandler(MouseEvent.MOUSE_EXITED, event -> Main.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));
        backToRegAuthButtonUp.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> Main.getPrimaryStage().getScene().setCursor(Cursor.HAND));
        backToRegAuthButtonUp.addEventHandler(MouseEvent.MOUSE_EXITED, event -> Main.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));
        userAddFriendButton.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> Main.getPrimaryStage().getScene().setCursor(Cursor.HAND));
        userAddFriendButton.addEventHandler(MouseEvent.MOUSE_EXITED, event -> Main.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));
    }

    private void SaveUserInfo(Integer id, String login, String password, String x, String y) {
        user = new User(id, login, password);
        user.setCoordinateX(x);
        user.setCoordinateY(y);
    }
    private void LoadUserInfo(Integer id, String login, String password, String x, String y) {
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
    static List<String> LoadFriends(List<User> list) {
        List<String> newList = new ArrayList<>();
        Integer num = 1;
        for (User item : list) {
//            newList.add(num++ + ": " + item.getLogin());
            newList.add(item.getLogin());
        }
        return newList;
    }

    private String CreateMapRequest() {
        StringBuilder mapURL = new StringBuilder("https://maps.googleapis.com/maps/api/staticmap?&size=700x550&maptype=roadmap");
        for(User item : user.getFriends()) {
            if(!item.getCoordinateX().equals("0")) {
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
