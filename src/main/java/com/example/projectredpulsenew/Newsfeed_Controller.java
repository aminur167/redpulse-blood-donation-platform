package com.example.projectredpulsenew;

import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class Newsfeed_Controller implements Initializable {

    @FXML private Label appNameLabel;
    @FXML private Button btnChat;
    @FXML private Button btnCreatePost;
    @FXML private Button btnNewsHome;
    @FXML private Button btnNewsFeed;
    @FXML private Button btnNotifications;
    @FXML private Button btnProfile;
    @FXML private Button btnSettings;
    @FXML private Button btnLogOut, btnLogin, btnSignUp;
    @FXML private HBox loginBox, logoutBox, signupBox;

    @FXML private VBox sidebar;
    @FXML private VBox sidebarButtons;
    @FXML private HBox sidebarHeader;
    @FXML private HBox topBar;
    @FXML private HBox topBarRight;
    @FXML private VBox feedContainer;


    @FXML private TextField txtSearchLocation;
    @FXML private TextField txtSearchBlood;
    @FXML private Button btnSearchLocation;
    @FXML private Button btnSearchBlood;

    private List<User> allPostOwners = new ArrayList<>();

    //--------------------Navigatin bar start---------------------------
    @FXML
    void NewstoDash(ActionEvent event) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Stage stage = (Stage) btnNewsHome.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void NewstoCreat(ActionEvent event) throws Exception {
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
    void NewstoNoti(ActionEvent event) throws Exception {
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
    void NewstoChat(ActionEvent event) throws Exception {
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
    void NewstoProf(ActionEvent event) throws Exception {
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
    void NewstoStng(ActionEvent event) throws Exception {
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
    //------------------------------Navigation bar end--------------------------------------------------------------------

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ButtonsVisibility();
        loadPostOwners();
        loadPosts();
        setupSearch();
    }

    // ---------------- SEARCH SETUP ----------------
    private void setupSearch() {

        btnSearchLocation.setOnAction(e -> searchPosts());
        btnSearchBlood.setOnAction(e -> searchPosts());

        txtSearchLocation.setOnAction(e -> searchPosts());
        txtSearchBlood.setOnAction(e -> searchPosts());
    }

    // ---------------- SEARCH LOGIC ----------------
    private void searchPosts() {

        String locationKeyword = txtSearchLocation.getText() == null
                ? ""
                : txtSearchLocation.getText().trim().toLowerCase();

        String bloodKeyword = txtSearchBlood.getText() == null
                ? ""
                : txtSearchBlood.getText().trim().toLowerCase();

        List<PostDetails> posts = readPostsFromFile();

        feedContainer.getChildren().clear();

        boolean found = false;

        for (PostDetails post : posts) {

            boolean matchesLocation =
                    locationKeyword.isEmpty()
                            || post.getLocation().toLowerCase().contains(locationKeyword)
                            || post.getDistrict().toLowerCase().contains(locationKeyword);

            boolean matchesBlood =
                    bloodKeyword.isEmpty()
                            || post.getBloodGroup().toLowerCase().contains(bloodKeyword);

            if (matchesLocation && matchesBlood) {
                addPostCard(post);
                found = true;
            }
        }

        if (!found) {
            Label emptyLabel = new Label("No matching posts found.");
            emptyLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 16px;");
            feedContainer.getChildren().add(emptyLabel);
        }
    }

    // ---------------- READ JSON ----------------
    private List<PostDetails> readPostsFromFile() {

        List<PostDetails> posts = new ArrayList<>();
        Gson gson = new Gson();

        try {
            File file = AppPaths.postDetailsJson().toFile();

            if (file.exists() && file.length() > 0) {

                try (Reader reader = new FileReader(file)) {
                    Type listType = new TypeToken<List<PostDetails>>() {}.getType();
                    List<PostDetails> existing = gson.fromJson(reader, listType);

                    if (existing != null) {
                        posts = existing;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return posts;
    }

    //---------------------------------Main Profile Show Start----------------------------------------------------------
    private void loadPosts() {
        // ফিক্স: CreatePost_Controller এর ফাইলের পাথের সাথে মিলিয়ে দেওয়া হয়েছে
        List<PostDetails> posts = new ArrayList<>();
        Gson gson = new Gson();

        try {
            File file = AppPaths.postDetailsJson().toFile();
            System.out.println("Reading from file at: " + file.getAbsolutePath());
            if (file.exists() && file.length() > 0) {
                try (Reader reader = new FileReader(file)) {
                    Type listType = new TypeToken<List<PostDetails>>(){}.getType();
                    List<PostDetails> existingPosts = gson.fromJson(reader, listType);

                    if (existingPosts != null && !existingPosts.isEmpty()) {
                        posts = existingPosts;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading posts: " + e.getMessage());
        }

        // ফিক্স: আগের সব পোস্ট কন্টেইনার থেকে রিমুভ করা (হেডার বাদ দিয়ে)
        if (feedContainer.getChildren().size() > 1) {
            feedContainer.getChildren().remove(1, feedContainer.getChildren().size());
        }

        // যদি কোনো পোস্ট না থাকে
        if (posts.isEmpty()) {
            Label emptyLabel = new Label("No blood requests yet. Be the first to post!");
            emptyLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 16px;");
            VBox.setMargin(emptyLabel, new Insets(30, 0, 0, 0));
            feedContainer.getChildren().add(emptyLabel);
            return;
        }

        // নতুন পোস্টগুলো উপরে দেখানোর জন্য লিস্ট রিভার্স করা
        Collections.reverse(posts);

        // ফাইলে থাকা প্রতিটি ডাটার জন্য কার্ড এড করা
        for (PostDetails post : posts) {
            System.out.println("Adding post for: " + post.getUserName());
            addPostCard(post);
        }
    }

    private void addPostCard(PostDetails post) {
        // Main Card Container
        VBox card = new VBox();
        card.setMaxWidth(800.0);
        card.getStyleClass().add("feed-card");
        card.setPadding(new Insets(20, 25, 20, 25));
        VBox.setMargin(card, new Insets(0, 0, 15, 0));

        // Top Row: Avatar, Name, Time
        HBox topRow = new HBox(10);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Circle avatar = new Circle(20);
        avatar.setStroke(Color.WHITE);
        avatar.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);

//        Post owner profile pic
        try {
            String picPath = post.getUserProfilePic();
            File picFile = (picPath != null && !picPath.equals("N/A")) ? new File(picPath) : null;

            if (picFile != null && picFile.exists()) {
                Image img = new Image("file:" + picFile.getAbsolutePath(), false);
                avatar.setFill(new ImagePattern(img));
            } else {
                // Default picture
                File defaultFile = AppPaths.defaultImage().toFile();
                if (defaultFile.exists()) {
                    Image img = new Image("file:" + defaultFile.getAbsolutePath(), false);
                    avatar.setFill(new ImagePattern(img));
                } else {
                    avatar.setFill(Color.web("#e0e0e0")); // fallback color if even default pic missing
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            avatar.setFill(Color.web("#e0e0e0")); // fallback color in case of error
        }
//        ---------------------------

        VBox nameBox = new VBox();
        String displayName = post.getUserName();

        Label userName = new Label(displayName);
        userName.setTextFill(Color.web("#1a1a1a"));
        userName.setFont(Font.font("System", FontWeight.BOLD, 16));

        Label timeLabel = new Label("Recently posted");
        timeLabel.setTextFill(Color.web("#888888"));
        timeLabel.setFont(Font.font(12));

        nameBox.getChildren().addAll(userName, timeLabel);

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label urgent = new Label("URGENT");
        urgent.getStyleClass().add("urgent-badge");


        // Done Button:---------------------------------------
        Button btnDone = new Button("✔");
        btnDone.getStyleClass().add("btn-outline");
        btnDone.setStyle("-fx-font-size: 10px; -fx-padding: 3 8 3 8;");

        User currentUser = LoginDetails.getUser();

        // Only show button if logged user is post owner
        if (currentUser != null &&
                currentUser.getEmail().equals(post.getUserEmail()) && currentUser.getPassword().equals(post.getUserPassword())) {

            btnDone.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Remove this post?");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        removePost(post);
                        feedContainer.getChildren().remove(card);
                    }
                });

            });

        } else {
            btnDone.setVisible(false);
            btnDone.setManaged(false);
        }
        //--------------------------------------------------------

        topRow.getChildren().addAll(avatar, nameBox, spacer, urgent, btnDone);

        // Separator
        Separator sep = new Separator();
        sep.setStyle("-fx-opacity: 0.5;");
        VBox.setMargin(sep, new Insets(15, 0, 15, 0));

        // Content Row: Blood Group + Grid
        HBox contentRow = new HBox(25);

        StackPane bloodPane = new StackPane();
        bloodPane.getStyleClass().add("blood-group-indicator");

        Circle bigCircle = new Circle(35);
        bigCircle.setFill(Color.web("#ffebee"));
        bigCircle.setStroke(Color.web("#ffcdd2"));
        bigCircle.setStrokeWidth(2);
        bigCircle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);

        Label bloodLabel = new Label(post.getBloodGroup());
        bloodLabel.setTextFill(Color.web("#d32f2f"));
        bloodLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        bloodPane.getChildren().addAll(bigCircle, bloodLabel);

        // Details Grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(5);
        HBox.setHgrow(grid, Priority.ALWAYS);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.SOMETIMES);
        col1.setMaxWidth(130);
        col1.setMinWidth(100);
        col1.setPrefWidth(110);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.SOMETIMES);
        col2.setMinWidth(10);

        grid.getColumnConstraints().addAll(col1, col2);

        addGridRow(grid, 0, "Hospital:", post.getLocation(), true);
        addGridRow(grid, 1, "Patient Name:", post.getPatientName(), false);
        addGridRow(grid, 2, "Date Needed:", post.getDateNeeded(), false);
        addGridRow(grid, 3, "Units (Bags):", post.getUnits(), false);
        addGridRow(grid, 4, "Contact:", post.getPhone(), false);
        addGridRow(grid, 5, "District:", post.getDistrict(), false);

        contentRow.getChildren().addAll(bloodPane, grid);

        // Notes Section
        VBox notesBox = new VBox(5);
        VBox.setMargin(notesBox, new Insets(10, 0, 0, 0));

        Label notesTitle = new Label("Notes:");
        notesTitle.getStyleClass().add("detail-label-title");

        Label notesText = new Label(post.getNotes());
        notesText.setTextFill(Color.web("#666666"));
        notesText.setWrapText(true);

        notesBox.getChildren().addAll(notesTitle, notesText);

        // Action Buttons
        HBox actions = new HBox(15);
        actions.setAlignment(Pos.CENTER_RIGHT);
        VBox.setMargin(actions, new Insets(20, 0, 0, 0));

        Button btnInfo = new Button("Contact Info");
        btnInfo.getStyleClass().add("btn-outline");
        setupContactButton(btnInfo, post);

        Button btnInterest = new Button("Interested");
        btnInterest.getStyleClass().add("btn-filled");
        btnInterest.setOnAction(e -> handleInterest(post));

        actions.getChildren().addAll(btnInfo, btnInterest);

        // Final Assembly
        card.getChildren().addAll(topRow, sep, contentRow, notesBox, actions);

        feedContainer.getChildren().add(card);
    }

    private void addGridRow(GridPane grid, int row, String titleText, String valueText, boolean isBold) {
        Label title = new Label(titleText);
        title.getStyleClass().add("detail-label-title");

        Label value = new Label(valueText);

        if (isBold) {
            value.setTextFill(Color.web("#444444"));
            value.setFont(Font.font("System", FontWeight.BOLD, 14));
        } else {
            value.setTextFill(Color.web("#555555"));
        }

        grid.add(title, 0, row);
        grid.add(value, 1, row);
    }
    //----------------------------------Main Profile Show End-------------------------------------------------------------


    //------------------------------------Handle Interest button start------------------------------------------------
    private void handleInterest(PostDetails post) {

        if(chkLogin.isLoggedIn()) {
            // 1️⃣ Clicker (current logged-in user)
        User clicker = LoginDetails.getUser();
        if (clicker == null) {
            System.out.println("No user logged in!");
            return;
        }

        // 2️⃣ Receiver (post owner)
        String receiverEmail = post.getUserEmail();
        String receiverPassword = post.getUserPassword();
        long currentTime = System.currentTimeMillis();

        // 3️⃣ Notification object
        NotificationDetails noti = new NotificationDetails(
                clicker.getName(),
                clicker.getBloodGroup(),
                clicker.getDistrict(),
                clicker.getNumber(),
                clicker.getEmail(),
                clicker.getPassword(),
                clicker.getProfilePicPath(),
                receiverEmail,
                receiverPassword,
                "interested",
                currentTime
        );

        saveNotification(noti);
        }
        else{
            chkLogin.alert();
        }
    }

    private void saveNotification(NotificationDetails noti) {

        String filePath = AppPaths.notificationsJson().toString();
        List<NotificationDetails> notifications = new ArrayList<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            File file = new File(filePath);
            if (file.exists() && file.length() > 0) {
                Reader reader = new FileReader(file);
                Type listType = new TypeToken<List<NotificationDetails>>(){}.getType();
                List<NotificationDetails> existing = gson.fromJson(reader, listType);
                if (existing != null) notifications = existing;
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        notifications.add(noti);

        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(notifications, writer);
            System.out.println("Notification saved!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //-------------------------------------Handle Interest button end-------------------------------------------------

    //-------------------------------------Handle Contact-Info Button Start-------------------------------------------
    // ==========================================================
    // Load all post owners once
    // ==========================================================
    private void loadPostOwners() {
        Gson gson = new Gson();
        try {
            File file = AppPaths.userDetailsJson().toFile();
            if (file.exists() && file.length() > 0) {
                try (Reader reader = new FileReader(file)) {
                    Type listType = new TypeToken<List<User>>() {}.getType();
                    List<User> postOwnerList = gson.fromJson(reader, listType);
                    if (postOwnerList != null) allPostOwners = postOwnerList;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================================================
    // Find post owner by email
    // ==========================================================
    private User findPostOwnerByEmail(String email) {
        if (email == null) return null;
        for (User postOwner : allPostOwners) {
            if (postOwner.getEmail() != null && postOwner.getEmail().equals(email))
                return postOwner;
        }
        return null;
    }

    // ==========================================================
    // Open Profile Popup
    // ==========================================================
    private void openPostOwnerProfile(User postOwner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PostOwnerProfileView.fxml"));
            Parent root = loader.load();

            PostOwnerProfileView_Controller controller = loader.getController();
            controller.setPostOwnerData(postOwner);

            Stage stage = new Stage();
            stage.setTitle("Profile Details");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================================================
    // Setup Contact Info Button
    // ==========================================================
    private void setupContactButton(Button btnInfo, PostDetails post) {
        btnInfo.setOnAction(e -> {
            User postOwner = findPostOwnerByEmail(post.getUserEmail());
            if (postOwner != null) openPostOwnerProfile(postOwner);
            else System.out.println("Post owner not found: " + post.getUserEmail());
        });
    }
    //-------------------------------------Handle Contact-Info Button End-------------------------------------------




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
    void login_n(ActionEvent event) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
            Parent root = loader.load();

            // 🔥 Controller instance নাও
            LogIn_Controller controller = loader.getController();

            // 🔥 এখানে বলছি login success হলে কী হবে
            // 🔥 Login success হলে feed refresh
            controller.setOnLoginSuccess(() -> {
                ButtonsVisibility();
                refreshFeed();
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
    void signup_n(ActionEvent event) throws Exception {
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
    void logout_n(ActionEvent event) throws Exception {
        chkLogin.setlogout();
        ButtonsVisibility();

        Parent root = FXMLLoader.load(getClass().getResource("Newsfeed.fxml"));
        Stage stage = (Stage) btnLogOut.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    private void removePost(PostDetails postToRemove) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<PostDetails> posts = new ArrayList<>();

        try {
            File file = AppPaths.postDetailsJson().toFile();

            if (file.exists() && file.length() > 0) {
                Reader reader = new FileReader(file);
                Type listType = new TypeToken<List<PostDetails>>(){}.getType();
                List<PostDetails> existing = gson.fromJson(reader, listType);
                if (existing != null) posts = existing;
                reader.close();
            }

            // Remove post by matching unique fields
            posts.removeIf(post ->
                    post.getUserEmail().equals(postToRemove.getUserEmail()) &&
                            post.getPatientName().equals(postToRemove.getPatientName()) &&
                            post.getDateNeeded().equals(postToRemove.getDateNeeded())
            );

            Writer writer = new FileWriter(file);
            gson.toJson(posts, writer);
            writer.close();

            System.out.println("Post removed successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshFeed() {
        feedContainer.getChildren().clear();
        loadPosts();
    }


}
