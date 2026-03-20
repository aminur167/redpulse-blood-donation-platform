package com.example.projectredpulsenew;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Chat_Controller {

    @FXML
    private Label appNameLabel;

    @FXML
    private Button btnChat;

    @FXML
    private Button btnCreatePost;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnNewsFeed;

    @FXML
    private Button btnNotifications;

    @FXML
    private Button btnProfile;

    @FXML
    private Button btnSettings;

    @FXML
    private VBox sidebar;

    @FXML
    private VBox sidebarButtons;

    @FXML
    private HBox sidebarHeader;

    @FXML
    private HBox topBar;

    @FXML
    private HBox topBarRight;

    @FXML
    void ChattoNews(ActionEvent event)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Newsfeed.fxml"));
        Stage stage = (Stage) btnNewsFeed.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void ChattoHome(ActionEvent event)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Stage stage = (Stage) btnHome.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void ChattoCreate(ActionEvent event)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("CreatePost.fxml"));
        Stage stage = (Stage) btnCreatePost.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void ChattoNoti(ActionEvent event)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Notification.fxml"));
        Stage stage = (Stage) btnNotifications.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void ChattoProf(ActionEvent event)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        Stage stage = (Stage) btnProfile.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    @FXML
    void ChattoStng(ActionEvent event)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
        Stage stage = (Stage) btnSettings.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }



    @FXML
    void logout_cht(ActionEvent event) throws Exception {
        chkLogin.setlogout();
        //updateTopButtons();

        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Stage stage = (Stage) btnLogOut.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
