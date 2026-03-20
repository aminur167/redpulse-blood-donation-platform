package com.example.projectredpulsenew;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;


public class Dashboard_Controller implements Initializable {

    @FXML
    private Button btnChat, btnCreatePost, btnHome, btnLogin, btnNewsFeed,
            btnNotifications, btnProfile, btnRequestBlood, btnSettings,
            btnSignUp, btnLogOut;

    @FXML
    private HBox loginBox, logoutBox, signupBox;

    @FXML private PieChart donationPieChart;

    @FXML private VBox interestedDonorsContainer, availableDonorsContainer;

    @FXML private Label lblAvailableDonorsCount, lblRequestCount, lblTotalDonorCount, lblWelcome;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ButtonsVisibility();
        //---------------------------------------------------------------------------------
        loadAvailableDonorsFromJson();
        loadTotalRequestsFromJson();
        updateWelcome();
    }


    private void loadAvailableDonorsFromJson() {

        availableDonorsContainer.getChildren().clear();
        int availableDonorCount = 0;

        try {

            Gson gson = new Gson();
            Reader reader = java.nio.file.Files.newBufferedReader(AppPaths.userDetailsJson());

            Type listType = new TypeToken<List<User>>() {}.getType();
            List<User> users = gson.fromJson(reader, listType);

            for (User user : users) {

                if ("Eligible".equalsIgnoreCase(user.getEligibility())) {

                    HBox row = createAvailableUserRow(user);
                    availableDonorsContainer.getChildren().add(row);

                    availableDonorCount++;  // Count increase
                }
            }
            lblAvailableDonorsCount.setText(String.valueOf(availableDonorCount));

            int totalDonorCount = users.size(); // সব user
            lblTotalDonorCount.setText(String.valueOf(totalDonorCount));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private HBox createAvailableUserRow(User user) {

        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("list-item");

        Label name = new Label(user.getName());
        name.setPrefWidth(200);

        Label blood = new Label(user.getBloodGroup());
        blood.setPrefWidth(100);
        blood.setStyle("-fx-font-weight: bold; -fx-text-fill: #2E7D32;");

        Label phone = new Label(user.getNumber());
        phone.setPrefWidth(150);

        Label district = new Label(user.getDistrict());
        district.setPrefWidth(150);

        Label status = new Label("Available");
        status.setStyle("-fx-background-color:#E8F5E9; -fx-text-fill:#2E7D32; -fx-padding:4 10; -fx-background-radius:15;");

        row.getChildren().addAll(name, blood, phone, district, status);

        return row;
    }


    private void loadTotalRequestsFromJson() {
        try {
            Gson gson = new Gson();
            Reader reader = java.nio.file.Files.newBufferedReader(AppPaths.postDetailsJson());

            Type listType = new TypeToken<List<PostDetails>>() {}.getType();
            List<PostDetails> posts = gson.fromJson(reader, listType);

            // ✅ Total requests = total number of posts
            int totalRequests = posts != null ? posts.size() : 0;

            lblRequestCount.setText(String.valueOf(totalRequests));

        } catch (Exception e) {
            e.printStackTrace();
            lblRequestCount.setText("0");
        }
    }

    public void updateWelcome(){
        User loggedinUser = LoginDetails.getUser();

        if (loggedinUser != null && loggedinUser.getName() != null && !loggedinUser.getName().isEmpty()) {
            lblWelcome.setText("Welcome back, " + loggedinUser.getName() + "!");
        } else {
            lblWelcome.setText("Welcome back!");
        }
    }




    //    Sidebar Buttons
    @FXML
    void DashtoNews(ActionEvent event)throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("Newsfeed.fxml"));
        Stage stage = (Stage) btnNewsFeed.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void DashtoCreate(ActionEvent event)throws Exception{
        if(chkLogin.isLoggedIn()) {
        Parent root = FXMLLoader.load(getClass().getResource("CreatePost.fxml"));
        Stage stage = (Stage) btnCreatePost.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        }
        else{
            chkLogin.alert();
        }
    }

    @FXML
    void DashtoNoti(ActionEvent event)throws Exception{
        if(chkLogin.isLoggedIn()) {
            Parent root = FXMLLoader.load(getClass().getResource("Notification.fxml"));
            Stage stage = (Stage) btnNotifications.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        else{
            chkLogin.alert();
        }
    }

    @FXML
    void DashtoChat(ActionEvent event)throws Exception{
        if(chkLogin.isLoggedIn()) {
            Parent root = FXMLLoader.load(getClass().getResource("Chat.fxml"));
            Stage stage = (Stage) btnChat.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        else{
            chkLogin.alert();
        }
    }

    @FXML
    void DashtoProf(ActionEvent event)throws Exception{
        if(chkLogin.isLoggedIn()) {
        Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        Stage stage = (Stage) btnProfile.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        }
        else{
            chkLogin.alert();
        }
    }

    @FXML
    void Dashtostng(ActionEvent event)throws Exception{
        if(chkLogin.isLoggedIn()) {
            Parent root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
            Stage stage = (Stage) btnSettings.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        else{
            chkLogin.alert();
        }
    }




    @FXML
    void RequestBlood(ActionEvent event)throws Exception{
        if(chkLogin.isLoggedIn()) {
            Parent root = FXMLLoader.load(getClass().getResource("CreatePost.fxml"));
            Stage stage = (Stage) btnRequestBlood.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        else{
            chkLogin.alert();
        }
    }



//    @FXML
//    void logout_d(ActionEvent event) throws Exception {
//        chkLogin.setlogout();
//        ButtonsVisibility();
//
//        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
//        Stage stage = (Stage) btnLogOut.getScene().getWindow();
//        stage.setScene(new Scene(root));
//        stage.show();
//    }
//}


    private void ButtonsVisibility() {
        boolean isLogged = chkLogin.isLoggedIn();

        btnLogin.setVisible(!isLogged);
        btnSignUp.setVisible(!isLogged);
        btnLogOut.setVisible(isLogged);

        // SIDEBAR (IMPORTANT FIX)
        loginBox.setVisible(!isLogged);
        loginBox.setManaged(!isLogged);

        logoutBox.setVisible(isLogged);
        logoutBox.setManaged(isLogged);

        signupBox.setVisible(!isLogged);
    }

    @FXML
    void login_d(ActionEvent event) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
            Parent root = loader.load();

            // 🔥 Controller instance নাও
            LogIn_Controller controller = loader.getController();

            // 🔥 এখানে বলছি login success হলে কী হবে
            controller.setOnLoginSuccess(() ->{
                ButtonsVisibility();
                updateWelcome();
            });


            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));
            popupStage.setTitle("Log in");

            popupStage.initOwner(btnLogin.getScene().getWindow());
            popupStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            popupStage.setResizable(false);

            popupStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void signup_d(ActionEvent event) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
            Parent root = loader.load();

            // 🔥 Controller instance নাও
            SignUp_Controller controller = loader.getController();

            // 🔥 এখানে বলছি login success হলে কী হবে
            controller.setOnSignUpSuccess(() -> ButtonsVisibility());

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));
            popupStage.setTitle("Sign up");

            popupStage.initOwner(btnSignUp.getScene().getWindow());
            popupStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            popupStage.setResizable(false);

            popupStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void logout_d(ActionEvent event) throws Exception {
        chkLogin.setlogout();

        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Stage stage = (Stage) btnLogOut.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
