package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class AddFriendController {
    @FXML
    Pane mainPane;

    private VBox mainBox = new VBox();
    private Label loginLabel = new Label("Input user's login:");
    private TextField loginField = new TextField();
    private HBox loginSearch = new HBox();
    private Button searchButton = new Button("Add friend!");
    private Label errorField = new Label();

    private ImageView exitIcon = new ImageView();

    private double xOffset = 0;
    private double yOffset = 0;


    //private Connection connection;
    private Statement statement;
    //private User user;

    public AddFriendController() {}
    AddFriendController(User user, Connection conn, Statement stmt) {
        //this.connection = conn;
        //this.statement = stmt;
        //this.user = user;
    }

    @FXML
    protected void initialize() {
        searchButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(loginField.getText().isEmpty()) {
                errorField.setText("Empty field!");
                return;
            }
            for (User item : Controller.user.getFriends()) {
                try {
                    if (Objects.equals(loginField.getText(), item.getLogin())) {
                        errorField.setText("This user already added to your friendslist!");
                        throw new Exception("This user already added to your friendslist!");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
            }
            String sql;
            ResultSet resultSet = null;
            try {
                statement = Controller.connection.createStatement();

                sql = "SELECT * FROM accounts WHERE login = '" + loginField.getText() + "'";
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    Integer tempId = resultSet.getInt("id");
                    if (!Objects.equals(tempId, Controller.user.getId())) {
                        Controller.user.addFriend(tempId);
                        errorField.setText("Friend was added!");
                        Controller.friends.setItems(FXCollections.observableList(Controller.LoadFriends(Controller.user.getFriends())));
                    }
                } else {
                    errorField.setText("No such user!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mainPane.setStyle("-fx-background-image: url(\"res/background.png\");");
        exitIcon.setImage(new Image("res/iconExit.png", 15, 15, false, false));

        loginLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 16px; -fx-font-weight: bold;");
        loginField.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFFFF; -fx-border-color: #FFFFFF; -fx-border-width: 1;");
        searchButton.setStyle("-fx-text-fill: #FFFFFF; -fx-background-color: #ddcb49; -fx-font-size: 16px;");
        errorField.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14px;");

        errorField.setText("");
        loginSearch.setSpacing(20);
        loginSearch.setAlignment(Pos.CENTER);
        loginSearch.getChildren().addAll(loginLabel, loginField);
        mainBox.getChildren().addAll(loginSearch, searchButton, errorField);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPrefWidth(450);
        mainBox.setPrefHeight(250);
        mainBox.setSpacing(25);
        mainPane.getChildren().addAll(mainBox);
        mainPane.getChildren().addAll(exitIcon);
        exitIcon.setLayoutX(430);
        exitIcon.setLayoutY(5);


        mainPane.setOnMousePressed(event -> {
            xOffset = Controller.stage.getX() - event.getScreenX();
            yOffset = Controller.stage.getY() - event.getScreenY();
        });
        mainPane.setOnMouseDragged(event -> {
            Controller.stage.setX(event.getScreenX() + xOffset);
            Controller.stage.setY(event.getScreenY() + yOffset);
        });

        exitIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Controller.stage.getScene().getWindow().hide();
        });
        exitIcon.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> Controller.stage.getScene().setCursor(Cursor.HAND));
        exitIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> Controller.stage.getScene().setCursor(Cursor.DEFAULT));

        searchButton.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> Controller.stage.getScene().setCursor(Cursor.HAND));
        searchButton.addEventHandler(MouseEvent.MOUSE_EXITED, event -> Controller.stage.getScene().setCursor(Cursor.DEFAULT));
    }
}
