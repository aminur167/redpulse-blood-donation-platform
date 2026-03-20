package com.example.projectredpulsenew;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Edit_Controller {

    @FXML private TextField txtName;
    @FXML private TextField txtAge;
    @FXML private ComboBox<String> cmbGender;
    @FXML private ComboBox<String> cmbBlood;
    @FXML private TextField txtDistrict;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    private final String FILE_PATH = AppPaths.userDetailsJson().toString();
    private final String NOTI_FILE = AppPaths.notificationsJson().toString();
    private final String POST_FILE = AppPaths.postDetailsJson().toString();

    @FXML
    public void initialize() {

        cmbGender.getItems().addAll("Male", "Female", "Other");
        cmbBlood.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");

        loadCurrentUser();
    }

    private void loadCurrentUser() {

        User user = LoginDetails.getUser();
        if (user == null) return;

        txtName.setText(user.getName());
        txtAge.setText(user.getAge());
        cmbGender.setValue(user.getGender());
        cmbBlood.setValue(user.getBloodGroup());
        txtDistrict.setText(user.getDistrict());
        txtPhone.setText(user.getNumber());
        txtEmail.setText(user.getEmail());
    }

    @FXML
    void handleSave(ActionEvent event) {

        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            Type listType = new TypeToken<ArrayList<User>>(){}.getType();
            ArrayList<User> users = gson.fromJson(new FileReader(FILE_PATH), listType);

            User current = LoginDetails.getUser();

            // Store original email for matching
            String originalEmail = current.getEmail();

            for (User u : users) {
                if (u.getEmail().equals(originalEmail) && u.getPassword().equals(current.getPassword())) {

                    u.setName(txtName.getText());
                    u.setAge(txtAge.getText());
                    u.setGender(cmbGender.getValue());
                    u.setBloodGroup(cmbBlood.getValue());
                    u.setDistrict(txtDistrict.getText());
                    u.setNumber(txtPhone.getText());
                    u.setEmail(txtEmail.getText());

                    // 🔥 ALSO UPDATE CURRENT SESSION USER
                    current.setName(txtName.getText());
                    current.setAge(txtAge.getText());
                    current.setGender(cmbGender.getValue());
                    current.setBloodGroup(cmbBlood.getValue());
                    current.setDistrict(txtDistrict.getText());
                    current.setNumber(txtPhone.getText());
                    current.setEmail(txtEmail.getText());

                    break;
                }
            }

            FileWriter writer = new FileWriter(FILE_PATH);
            gson.toJson(users, writer);


            // 2️⃣ Update Noti.json
            Type notiType = new TypeToken<ArrayList<NotificationDetails>>(){}.getType();
            ArrayList<NotificationDetails> notis = gson.fromJson(new FileReader(NOTI_FILE), notiType);

            for (NotificationDetails n : notis) {
                if (n.getClickerEmail().equals(originalEmail)) {
                    n.setClickerName(current.getName());
                    n.setClickerBloodgroup(current.getBloodGroup());
                    n.setClickerDistrict(current.getDistrict());
                    n.setClickerNumber(current.getNumber());
                    n.setClickerEmail(current.getEmail());
                    n.setClickerPassword(current.getPassword());
                }
                if (n.getReceiverEmail().equals(originalEmail)) {
                    n.setReceiverEmail(current.getEmail());
                    n.setReceiverPassword(current.getPassword());
                }
            }

            try (FileWriter writer1 = new FileWriter(NOTI_FILE)) {
                gson.toJson(notis, writer1);
            }

            // 3️⃣ Update PostDetails.json
            Type postType = new TypeToken<ArrayList<PostDetails>>(){}.getType();
            ArrayList<PostDetails> posts = gson.fromJson(new FileReader(POST_FILE), postType);

            for (PostDetails p : posts) {
                if (p.getUserEmail().equals(originalEmail)) {
                    p.setUserName(current.getName());
                    p.setUserEmail(current.getEmail());
                    p.setUserPassword(current.getPassword());
                    p.setUserProfilePic(current.getProfilePicPath());
                }
            }

            try (FileWriter writer2 = new FileWriter(POST_FILE)) {
                gson.toJson(posts, writer2);
            }




            writer.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Profile Updated Successfully!");
            alert.showAndWait();

            // Close popup only
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @FXML
    void handleCancel(ActionEvent event) throws Exception {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

}
