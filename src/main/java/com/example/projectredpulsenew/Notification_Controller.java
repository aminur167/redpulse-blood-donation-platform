package com.example.projectredpulsenew;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

public class Notification_Controller {

    @FXML
    private Label appNameLabel;

    @FXML
    private Button btnChat, btnCreatePost, btnLogOut, btnNewsFeed, btnHome, btnNotifications, btnProfile, btnSettings;

    @FXML
    private VBox sidebar, sidebarButtons, notificationContainer;

    @FXML
    private HBox sidebarHeader, topBar, topBarRight;

    @FXML
    void NotitoNews(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Newsfeed.fxml"));
        Stage stage = (Stage) btnNewsFeed.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void NotitoHome(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Stage stage = (Stage) btnHome.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void NotitoCreate(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("CreatePost.fxml"));
        Stage stage = (Stage) btnCreatePost.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void NotitoChat(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Chat.fxml"));
        Stage stage = (Stage) btnChat.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void NotitoProf(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        Stage stage = (Stage) btnProfile.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void NotitoStng(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
        Stage stage = (Stage) btnSettings.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void logout_noti(ActionEvent event) throws Exception {
        chkLogin.setlogout();
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Stage stage = (Stage) btnLogOut.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void initialize() {
        loadNotifications();
    }

    private void loadNotifications() {
        try (Reader reader = new FileReader(AppPaths.notificationsJson().toFile())) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<NotificationDetails>>() {}.getType();
            List<NotificationDetails> notiList = gson.fromJson(reader, listType);

            User currentUser = LoginDetails.getUser();
            if (currentUser == null) {
                return;
            }

            String currentEmail = currentUser.getEmail();
            String currentPass = currentUser.getPassword();

            notificationContainer.getChildren().clear();
            if (notiList == null) {
                return;
            }

            for (NotificationDetails notification : notiList) {
                if (notification.getReceiverEmail().equals(currentEmail)
                        && notification.getReceiverPassword().equals(currentPass)) {
                    HBox card = createNotificationCard(notification);
                    notificationContainer.getChildren().add(card);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading notifications: " + e.getMessage());
        }
    }

    private HBox createNotificationCard(NotificationDetails notification) {
        HBox card = new HBox(15);
        card.setMaxWidth(900);
        card.getStyleClass().add("noti-card");

        Circle avatar = new Circle(25);
        avatar.setStroke(Color.WHITE);
        avatar.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);

        try {
            String picPath = notification.getClickerProfilePic();
            File picFile = (picPath != null && !picPath.isEmpty()) ? new File(picPath) : null;

            if (picFile != null && picFile.exists()) {
                Image image = new Image("file:" + picFile.getAbsolutePath(), false);
                avatar.setFill(new ImagePattern(image));
            } else {
                File defaultFile = AppPaths.defaultImage().toFile();
                if (defaultFile.exists()) {
                    Image image = new Image("file:" + defaultFile.getAbsolutePath(), false);
                    avatar.setFill(new ImagePattern(image));
                } else {
                    avatar.setFill(Color.web("#cccccc"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            avatar.setFill(Color.web("#cccccc"));
        }

        StackPane iconBg = new StackPane(avatar);
        iconBg.setPrefSize(50, 50);

        Label title = new Label();
        VBox infoBox = new VBox(2);

        if (notification.getNotiType().equalsIgnoreCase("interested")) {
            title.setText(notification.getClickerName() + " showed interest in your post");

            Label emailLabel = new Label("Donor's email: " + notification.getClickerEmail());
            Label numberLabel = new Label("Donor's number: " + notification.getClickerNumber());
            Label bloodLabel = new Label("Donor's Blood group: " + notification.getClickerBloodgroup());
            Label districtLabel = new Label("Donor's District: " + notification.getClickerDistrict());

            emailLabel.getStyleClass().add("noti-subtitle");
            numberLabel.getStyleClass().add("noti-subtitle");
            bloodLabel.getStyleClass().add("noti-subtitle");
            districtLabel.getStyleClass().add("noti-subtitle");

            infoBox.getChildren().addAll(emailLabel, numberLabel, bloodLabel, districtLabel);
        } else if (notification.getNotiType().equalsIgnoreCase("request")) {
            title.setText("New request blood request posted");
        }

        title.getStyleClass().add("noti-title");

        VBox textBox = new VBox(4, title, infoBox);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        Label time = new Label(getTimeAgo(notification.getTimestamp()));
        time.getStyleClass().add("noti-time");

        card.getChildren().addAll(iconBg, textBox, time);
        return card;
    }

    private String getTimeAgo(long notificationTime) {
        long currentTime = System.currentTimeMillis();
        long diff = currentTime - notificationTime;

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (seconds < 60) {
            return "Just now";
        } else if (minutes < 60) {
            return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
        } else if (hours < 24) {
            return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        }

        return days + " day" + (days > 1 ? "s" : "") + " ago";
    }
}
