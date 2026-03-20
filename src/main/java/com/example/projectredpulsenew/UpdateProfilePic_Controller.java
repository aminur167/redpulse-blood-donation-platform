package com.example.projectredpulsenew;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

/*
    Controller Purpose:
    -------------------
    1. Select new profile image
    2. Update UserDetails.json
    3. Update PostDetails.json (so old posts show new pic)
    4. Update Noti.json
    5. Update current session object immediately
*/

public class UpdateProfilePic_Controller {

    @FXML private Circle profileCircle;
    @FXML private Button btnChoose, btnSavePP, btnCancelPP;
    @FXML private Label lblStatus;

    // Holds selected image path temporarily
    private String selectedImagePath = null;

    // JSON file paths
    private final String USER_FILE = AppPaths.userDetailsJson().toString();
    private final String POST_FILE = AppPaths.postDetailsJson().toString();
    private final String NOTI_FILE = AppPaths.notificationsJson().toString();


    // ================= INITIALIZE =================
    @FXML
    public void initialize() {

        // Load current logged-in user
        User currentUser = LoginDetails.getUser();

        if (currentUser != null && currentUser.getProfilePicPath() != null) {
            updateCircleImage(currentUser.getProfilePicPath());
        }
    }


    // ================= CHOOSE IMAGE =================
    @FXML
    void chooseImage(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(btnChoose.getScene().getWindow());

        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();
            updateCircleImage(selectedImagePath);
            lblStatus.setText("Selected: " + selectedFile.getName());
        }
    }


    // ================= UPDATE CIRCLE PREVIEW =================
    private void updateCircleImage(String path) {

        try {
            File file = new File(path);

            if (file.exists()) {
                Image image = new Image("file:" + file.getAbsolutePath());
                profileCircle.setFill(new ImagePattern(image));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ================= SAVE CHANGES =================
    @FXML
    void handleSave(ActionEvent event) {

        // If no image selected → just close
        if (selectedImagePath == null) {
//            closeWindow();
            return;
        }

        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            User currentUser = LoginDetails.getUser();
            String email = currentUser.getEmail();

            // ---------------- UPDATE USER FILE ----------------
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            ArrayList<User> users = gson.fromJson(new FileReader(USER_FILE), userListType);

            for (User u : users) {
                if (u.getEmail().equals(email)) {
                    u.setProfilePicPath(selectedImagePath);
                    currentUser.setProfilePicPath(selectedImagePath); // session update
                    break;
                }
            }

            try (FileWriter writer = new FileWriter(USER_FILE)) {
                gson.toJson(users, writer);
            }

            // ---------------- UPDATE POSTS ----------------
            try {
                Type postType = new TypeToken<ArrayList<PostDetails>>(){}.getType();
                ArrayList<PostDetails> posts = gson.fromJson(new FileReader(POST_FILE), postType);

                if (posts != null) {
                    for (PostDetails p : posts) {
                        if (p.getUserEmail().equals(email)) {
                            p.setUserProfilePic(selectedImagePath);
                        }
                    }

                    try (FileWriter writer = new FileWriter(POST_FILE)) {
                        gson.toJson(posts, writer);
                    }
                }

            } catch (Exception ignored) { }

            // ---------------- UPDATE NOTIFICATIONS ----------------
            try {
                Type notiType = new TypeToken<ArrayList<NotificationDetails>>(){}.getType();
                ArrayList<NotificationDetails> notis = gson.fromJson(new FileReader(NOTI_FILE), notiType);

                if (notis != null) {
                    for (NotificationDetails n : notis) {
                        if (n.getClickerEmail().equals(email)) {
                            n.setClickerPic(selectedImagePath);
                        }
                    }

                    try (FileWriter writer = new FileWriter(NOTI_FILE)) {
                        gson.toJson(notis, writer);
                    }
                }

            } catch (Exception ignored) { }

            // Success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Profile picture updated successfully!");
            alert.showAndWait();

            Stage stage = (Stage) btnSavePP.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ================= CANCEL =================
    @FXML
    void handleCancel(ActionEvent event) {
        Stage stage = (Stage) btnCancelPP.getScene().getWindow();
        stage.close();
    }


}
