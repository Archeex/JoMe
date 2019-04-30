package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class AddFriendController {
    @FXML
    Pane mainPane;

    private VBox mainBox = new VBox();
    private Label loginLabel = new Label("Login");
    private TextField loginField = new TextField();
    private HBox loginSearch = new HBox();
    private Button searchButton = new Button("Add friend!");
    private Label errorField = new Label();

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
        errorField.setText("");
        loginSearch.getChildren().addAll(loginLabel, loginField);
        mainBox.getChildren().addAll(loginSearch, searchButton, errorField);
        mainPane.getChildren().addAll(mainBox);

        searchButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            String sql;
            ResultSet resultSet = null;
            try {
                statement = Controller.connection.createStatement();

                sql = "SELECT * FROM accounts WHERE id = '" + loginField.getText() + "'";
                resultSet = statement.executeQuery(sql);
                if(resultSet.next()) {
                    Integer tempId = resultSet.getInt("id");
                    if(!Objects.equals(tempId, Controller.user.getId())) {
                        Controller.user.addFriend(Integer.valueOf(loginField.getText()));
                        errorField.setText("Friend was added!");
                        Controller.friends.setItems(FXCollections.observableList(Controller.LoadFriends(Controller.user.getFriends())));
                    }
                }
                else {
                    errorField.setText("No such user!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
