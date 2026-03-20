package com.example.projectredpulsenew;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AdminPanelController {

    @FXML private VBox postContainer;
    @FXML private VBox userContainer;
    @FXML private Button backSetting;

    private final String POST_FILE = AppPaths.postDetailsJson().toString();
    private final String USER_FILE = AppPaths.userDetailsJson().toString();
    private final Gson gson = new Gson();

    @FXML
    public void initialize() {
        loadPosts();
        loadUsers();
    }

    // ---------------- Back Button ----------------
    @FXML
    void handleBackToSettings(ActionEvent event)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
        Stage stage = (Stage) backSetting.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // ---------------- POSTS ----------------
    private void loadPosts() {
        postContainer.getChildren().clear();
        List<PostDetails> posts = readJson(POST_FILE, new TypeToken<List<PostDetails>>() {}.getType());
        if(posts == null) posts = new ArrayList<>();

        for(PostDetails post : posts) {
            postContainer.getChildren().add(createPostCard(post, posts));
        }
    }

    private HBox createPostCard(PostDetails post, List<PostDetails> allPosts) {
        HBox card = new HBox(10);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: #ecf0f1; -fx-padding: 5; -fx-border-radius: 5; -fx-background-radius: 5;");
        card.setPrefHeight(70);  // Slightly taller for full info

        VBox infoBox = new VBox(2);

        // ✅ Show all post information
        infoBox.getChildren().addAll(
                createLabel("Patient: " + post.getPatientName(), true),
                createLabel("Blood Group: " + post.getBloodGroup(), false),
                createLabel("Units Needed: " + post.getUnits(), false),
                createLabel("Location: " + post.getLocation(), false),
                createLabel("Date Needed: " + post.getDateNeeded(), false),
                createLabel("Phone: " + post.getPhone(), false),
                createLabel("Notes: " + post.getNotes(), false),
                createLabel("Posted by: " + post.getUserName() + " | Email: " + post.getUserEmail(), false)
        );

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button deleteBtn = new Button("Remove");
        deleteBtn.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 10px;");
        deleteBtn.setOnAction(e -> {
            allPosts.remove(post);
            saveJson(POST_FILE, allPosts);
            loadPosts();
            showAlert("Success", "Post removed successfully");
        });

        card.getChildren().addAll(infoBox, spacer, deleteBtn);
        return card;
    }

    // ---------------- USERS ----------------
    private void loadUsers() {
        userContainer.getChildren().clear();
        List<User> users = readJson(USER_FILE, new TypeToken<List<User>>() {}.getType());
        if(users == null) users = new ArrayList<>();

        for(User user : users) {
            userContainer.getChildren().add(createUserCard(user, users));
        }
    }

    private HBox createUserCard(User user, List<User> allUsers) {

        User currentUser = LoginDetails.getUser();
        if (currentUser == null) {
            currentUser = new User();
        }

        HBox card = new HBox(10);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: #ecf0f1; -fx-padding: 5; -fx-border-radius: 5; -fx-background-radius: 5;");
        card.setPrefHeight(80);  // Slightly taller

        VBox infoBox = new VBox(2);
        String roleText = getRoleText(user);
        infoBox.getChildren().addAll(
                createLabel("Name: " + user.getName() + roleText, true),
                createLabel("Email: " + user.getEmail(), false),
                createLabel("Phone: " + user.getNumber(), false),
                createLabel("Gender: " + user.getGender(), false),
                createLabel("Blood Group: " + user.getBloodGroup(), false),
                createLabel("District: " + user.getDistrict(), false),
                createLabel("Last Donate Date: " + (user.getLastDonateDate() != null ? user.getLastDonateDate() : "N/A"), false),
                createLabel("Next Eligible Donation: " + (user.getNextDonateDate() != null ? user.getNextDonateDate() : "N/A"), false),
                createLabel("Eligibility: " + user.getEligibility(), false)
        );

        if(user.isAdmin()) {
            infoBox.getChildren().get(0).setStyle("-fx-text-fill: darkblue; -fx-font-weight: bold;");
        } else if (user.isModerator()) {
            infoBox.getChildren().get(0).setStyle("-fx-text-fill: #d35400; -fx-font-weight: bold;");
        }

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox actions = new HBox(5);

        // 🔥 If this user is ADMIN → NO BUTTONS AT ALL
        if(user.isAdmin()) {
            card.getChildren().addAll(infoBox, spacer);
            return card;
        }


        // ✅ ADMIN ONLY: Moderator Control
        if(currentUser.isAdmin() && !user.isAdmin()) {

            Button modBtn = new Button();

            if(user.isModerator()) {
                modBtn.setText("Remove from Moderator");
                modBtn.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-size: 10px;");
                modBtn.setOnAction(e -> {
                    user.setModerator(false);
                    saveJson(USER_FILE, allUsers);
                    loadUsers();
                    showAlert("Updated", user.getName() + " removed from Moderator");
                });
            } else {
                modBtn.setText("Make Moderator");
                modBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 10px;");
                modBtn.setOnAction(e -> {
                    user.setModerator(true);
                    saveJson(USER_FILE, allUsers);
                    loadUsers();
                    showAlert("Success", user.getName() + " is now Moderator");
                });
            }

            actions.getChildren().add(modBtn);
        }

        // ✅ Admin & Moderator can delete user
        if(currentUser.isAdmin() || currentUser.isModerator()) {

            Button deleteBtn = new Button("Delete");
            deleteBtn.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 10px;");

            deleteBtn.setOnAction(e -> {
                allUsers.remove(user);
                saveJson(USER_FILE, allUsers);
                loadUsers();
                showAlert("Deleted", "User removed successfully");
            });

            actions.getChildren().add(deleteBtn);
        }

        card.getChildren().addAll(infoBox, spacer, actions);
        return card;
    }


    // ---------------- HELPER METHODS ----------------
    private <T> List<T> readJson(String fileName, Type type) {
        try(FileReader reader = new FileReader(fileName)) {
            return gson.fromJson(reader, type);
        } catch(IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private final Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
    private <T> void saveJson(String fileName, List<T> data) {
        try(FileWriter writer = new FileWriter(fileName)) {
            gson1.toJson(data, writer);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private Label createLabel(String text, boolean bold) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-size: 12px;" + (bold ? "-fx-font-weight: bold;" : ""));
        return lbl;
    }

    private String getRoleText(User user) {
        if (user.isAdmin()) {
            return " (ADMIN)";
        }
        if (user.isModerator()) {
            return " (MODERATOR)";
        }
        return "";
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
