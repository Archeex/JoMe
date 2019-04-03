package sample;

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
    TextField loginField = new TextField();
    private HBox loginSearch = new HBox();
    private Button searchButton = new Button("Add friend!");
    Label errorField = new Label();

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

                sql = "SELECT * FROM accounts WHERE login = '" + loginField.getText() + "'";
                resultSet = statement.executeQuery(sql);
                if(resultSet.next()) {
                    Integer tempId = resultSet.getInt("id");
                    if(!Objects.equals(tempId, Controller.user.getId())) {
                        sql = "UPDATE accounts SET friends = '" + tempId + "' WHERE login = '" + Controller.user.getLogin() + "'";
                        statement.executeUpdate(sql);
                        errorField.setText("Friend was added!");
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
