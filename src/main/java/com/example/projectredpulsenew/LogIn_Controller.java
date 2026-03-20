package com.example.projectredpulsenew;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.projectredpulsenew.chkLogin.setlogin;

public class LogIn_Controller {

    @FXML
    private Button loginBtn;

    @FXML
    private TextField login_email;

    @FXML
    private TextField login_pass;

    @FXML
    private Button login_signup;

    @FXML
    private Label loginMessage;

    private Runnable onLoginSuccess;




    @FXML
    void login(ActionEvent event) {
        String emailInput = login_email.getText().trim();
        String passwordInput = login_pass.getText().trim();

        if (emailInput.isEmpty() || passwordInput.isEmpty()) {
            loginMessage.setText("Please enter both email and password!");
            loginMessage.setStyle("-fx-text-fill: red;");
            return;
        }

        List<User> userList = loadUsersFromJson();
        boolean found = false;


        for (User user : userList) {
            if (user.getEmail().equals(emailInput) && user.getPassword().equals(passwordInput)) {
                LoginDetails.temp = user;
                found = true;
                break;
            }
        }

        if (found) {
            loginMessage.setText("Login Successful! Redirecting...");
            loginMessage.setStyle("-fx-text-fill: green;");
            chkLogin.setlogin();
            try {
//                // 1️⃣ Get popup stage
//                Stage popupStage = (Stage) loginBtn.getScene().getWindow();
//
//                // 2️⃣ Get main (owner) stage
//                Stage mainStage = (Stage) popupStage.getOwner();
//
//                // 3️⃣ Load Newsfeed
//                Parent root = FXMLLoader.load(getClass().getResource("Newsfeed.fxml"));
//
//                // 4️⃣ Set scene to main window
//                mainStage.setScene(new Scene(root));
//                mainStage.show();

                if (onLoginSuccess != null) {
                    onLoginSuccess.run();   // 🔥 Caller page refresh হবে
                }

                // 5️⃣ Close popup
                Stage popupStage = (Stage) loginBtn.getScene().getWindow();
                popupStage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            loginMessage.setText("Invalid email or password!");
            loginMessage.setStyle("-fx-text-fill: red;");
        }
    }


    private List<User> loadUsersFromJson() {
        List<User> userList = new ArrayList<>();
        try {
            File file = AppPaths.userDetailsJson().toFile();
            if (file.exists()) {
                Reader reader = new FileReader(file);
                Type listType = new TypeToken<List<User>>(){}.getType();
                userList = new Gson().fromJson(reader, listType);
                if (userList == null) {
                    userList = new ArrayList<>();
                }
                reader.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public void logtosign(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
            Parent root = loader.load();

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));
            popupStage.setTitle("Sign Up");

            popupStage.initOwner(login_signup.getScene().getWindow());
            popupStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            popupStage.setResizable(false);

            popupStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setOnLoginSuccess(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }

}
