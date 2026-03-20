package com.example.projectredpulsenew;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ChangePass_Controller {

    @FXML private PasswordField txtOldPass, txtNewPass, txtConfirmPass;
    @FXML private Label lblMessage;
    @FXML private Button changePassCancelBtn, changePassSaveBtn;

    private final String USER_FILE = AppPaths.userDetailsJson().toString();
    private final String POST_FILE = AppPaths.postDetailsJson().toString();
    private final String NOTI_FILE = AppPaths.notificationsJson().toString();

    @FXML
    void handleChangePass(ActionEvent event) {

        try {
            User current = LoginDetails.getUser();

            String oldPass = txtOldPass.getText();
            String newPass = txtNewPass.getText();
            String confirmPass = txtConfirmPass.getText();

            // ✅ Validation
            if(!oldPass.equals(current.getPassword())){
                lblMessage.setText("Old password incorrect!");
                return;
            }

            if(!newPass.equals(confirmPass)){
                lblMessage.setText("New passwords do not match!");
                return;
            }

            if(newPass.isEmpty()){
                lblMessage.setText("Password cannot be empty!");
                return;
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // ================= UPDATE USER FILE =================
            Type userType = new TypeToken<ArrayList<User>>(){}.getType();
            ArrayList<User> users = gson.fromJson(new FileReader(USER_FILE), userType);

            for(User u : users){
                if(u.getEmail().equals(current.getEmail())){
                    u.setPassword(newPass);
                    break;
                }
            }

            try(FileWriter writer = new FileWriter(USER_FILE)){
                gson.toJson(users, writer);
            }

            // ================= UPDATE POST FILE =================
            Type postType = new TypeToken<ArrayList<PostDetails>>(){}.getType();
            ArrayList<PostDetails> posts = gson.fromJson(new FileReader(POST_FILE), postType);

            for(PostDetails p : posts){
                if(p.getUserEmail().equals(current.getEmail())){
                    p.setUserPassword(newPass);
                }
            }

            try(FileWriter writer = new FileWriter(POST_FILE)){
                gson.toJson(posts, writer);
            }

            // ================= UPDATE NOTIFICATION FILE =================
            Type notiType = new TypeToken<ArrayList<NotificationDetails>>(){}.getType();
            ArrayList<NotificationDetails> notis = gson.fromJson(new FileReader(NOTI_FILE), notiType);

            for(NotificationDetails n : notis){

                // if user is clicker
                if(n.getClickerEmail().equals(current.getEmail())){
                    n.setClickerPassword(newPass);
                }

                // if user is receiver
                if(n.getReceiverEmail().equals(current.getEmail())){
                    n.setReceiverPassword(newPass);
                }
            }

            try(FileWriter writer = new FileWriter(NOTI_FILE)){
                gson.toJson(notis, writer);
            }

            // ================= UPDATE CURRENT OBJECT =================
            current.setPassword(newPass);

            lblMessage.setStyle("-fx-text-fill: green;");
            lblMessage.setText("Password updated successfully!");


            Stage stage = (Stage) changePassSaveBtn.getScene().getWindow();
            stage.close();

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void handlePassCancel(ActionEvent event) {
        Stage stage = (Stage) changePassCancelBtn.getScene().getWindow();
        stage.close();
    }
}
