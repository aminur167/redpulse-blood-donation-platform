package com.example.projectredpulsenew;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;




import java.time.format.DateTimeFormatter;

import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;




/*
==========================================================
                PROFILE CONTROLLER
==========================================================
This controller:
1. Loads current logged-in user's profile dynamically
2. Shows profile picture in circular format
3. Displays personal & address information
4. Opens medical PDF file
5. Shows NID front & back images
6. Keeps sidebar navigation unchanged
==========================================================
*/

public class Profile_Controller {

    // ======================================================
    //                SIDEBAR NAVIGATION BUTTONS
    // (UNCHANGED - same as your previous design)
    // ======================================================

    @FXML private Button btnHome;
    @FXML private Button btnNewsFeed;
    @FXML private Button btnNotifications;
    @FXML private Button btnChat;
    @FXML private Button btnCreatePost;
    @FXML private Button btnSettings;
    @FXML private Button btnLogOut;

    // ======================================================
    //                PROFILE HEADER SECTION
    // ======================================================

    @FXML private Circle profileCircle;   // Round profile picture
    @FXML private Label lblUserName;// User name in header
    @FXML private Label lblHeaderDistrict;

    // ======================================================
    //                PERSONAL INFORMATION SECTION
    // ======================================================

    @FXML private Label lblFullName;
    @FXML private Label lblAge;
    @FXML private Label lblGender;
    @FXML private Label lblBloodGroup;
    @FXML private Label lblPhone;
    @FXML private Label lblEmail;

    // ======================================================
    //                ADDRESS SECTION
    // ======================================================

    @FXML private Label lblDistrict;   // Upazilla can be added later

    // ======================================================
    //                DOCUMENT VERIFICATION SECTION
    // ======================================================

    @FXML private Button btnMedicalPdf;  // Opens medical certificate PDF
    @FXML private ImageView imgNidFront; // NID front image
    @FXML private ImageView imgNidBack;// NID back image

    @FXML private Button btnEditProfile, btnUpdatePhoto, btnUpdateDocuments;

    // ================= নতুন ডেট সেকশনের জন্য ভেরিয়েবল =================
    @FXML private Label lblLastDonateDate;      // শেষ ডোনেশনের তারিখ দেখানোর জন্য
    @FXML private Label lblNextEligibleDate;    // পরবর্তী এলিজিবল তারিখ দেখানোর জন্য
    @FXML private Label lblEligibilityStatus;   // এলিজিবল নাকি আন-এলিজিবল লেবেল
    @FXML private DatePicker dpHidden;          // লুকানো ডেটপিকার
    @FXML private Button btnPickDate;           // ডেটপিকার ওপেন করার বাটন

    // Default profile image if user has no image
    private final String DEFAULT_IMAGE_PATH = AppPaths.defaultImage().toString();
    private final String USER_FILE_PATH = AppPaths.userDetailsJson().toString(); // ফাইলের নাম


    // ======================================================
    //                INITIALIZE METHOD
    // Called automatically when FXML loads
    // ======================================================

    @FXML
    public void initialize() {
        loadUserData();   // Load current logged-in user data

        // ২. ডেটপিকারের লিসেনার যোগ করা
        // যখনই ইউজার ডেট সিলেক্ট করবে, এই কোডটি রান হবে
        dpHidden.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateDonationDate(newValue);
            }
        });
    }

    // ================= ডেটপিকার পপ-আপ ওপেন করা =================
    @FXML
    void openDatePicker(ActionEvent event) {
        dpHidden.show(); // লুকানো ক্যালেন্ডারটি ওপেন হবে
    }


    // ======================================================
    //                LOAD CURRENT USER DATA
    // Fetch data from LoginDetails and populate UI
    // ======================================================

    private void loadUserData() {

        User user = LoginDetails.getUser();

        if (user == null) {
            System.out.println("No logged-in user found!");
            return;
        }

        // Load profile picture
        setProfileImage(user.getProfilePicPath());

        // Header section
        lblUserName.setText(safe(user.getName()));
        lblHeaderDistrict.setText(safe(user.getDistrict()));

        // Personal Information
        lblFullName.setText(safe(user.getName()));
        lblAge.setText(safe(user.getAge()));
        lblGender.setText(safe(user.getGender()));
        lblBloodGroup.setText(safe(user.getBloodGroup()));
        lblPhone.setText(safe(user.getNumber()));
        lblEmail.setText(safe(user.getEmail()));

        // Address
        lblDistrict.setText(safe(user.getDistrict()));

        // Load document images
        loadImage(imgNidFront, user.getNidFrontPath());
        loadImage(imgNidBack, user.getNidBackPath());

        // Set PDF open action
        btnMedicalPdf.setOnAction(e -> openPdf(user.getMedicalPdfPath()));


        // --- নতুন: ডোনেশন ডেট চেক করা ---
        if (user.getLastDonateDate() != null && !user.getLastDonateDate().isEmpty()) {
            try {
                // স্ট্রিং থেকে LocalDate এ কনভার্ট করা
                LocalDate lastDate = LocalDate.parse(user.getLastDonateDate());

                // ক্যালকুলেশন এবং UI আপডেট করা
                calculateAndShowDates(lastDate);

            } catch (Exception e) {
                System.out.println("Date parse error: " + e.getMessage());
                setEligibilityUI(true); // এরর হলে বাই ডিফল্ট এলিজিবল
            }
        } else {
            // যদি ইউজার আগে কখনো ডেট সেট না করে থাকে
            lblLastDonateDate.setText("Not Set");
            lblNextEligibleDate.setText("Available Now");
            setEligibilityUI(true);
        }

    }

    // ================= ডেট আপডেট এবং সেভ করার লজিক =================
    private void updateDonationDate(LocalDate date) {
        // ১. ইউজার অবজেক্ট আপডেট করা
        User user = LoginDetails.getUser();
        user.setLastDonateDate(date.toString());

        // ২. UI তে তারিখ এবং স্ট্যাটাস আপডেট করা
        calculateAndShowDates(date);

        // ৩. JSON ফাইলে পার্মানেন্টলি সেভ করা
        saveUserToJson(user);
    }

    // ================= তারিখ হিসাব এবং লেবেল শো করা =================
    private void calculateAndShowDates(LocalDate lastDate) {
        // লাস্ট ডেট শো করা
        lblLastDonateDate.setText(lastDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));

        // ৪ মাস যোগ করে নেক্সট ডেট বের করা
        LocalDate nextDate = lastDate.plusMonths(4);
        lblNextEligibleDate.setText(nextDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));

        User user = LoginDetails.getUser();
        user.setNextDonateDate(nextDate.toString());

        // আজকের তারিখের সাথে তুলনা করা
        // যদি আজ >= নেক্সট ডেট হয়, তাহলে এলিজিবল
        boolean isEligible = LocalDate.now().isAfter(nextDate) || LocalDate.now().equals(nextDate);

        // কালার এবং টেক্সট সেট করা
        setEligibilityUI(isEligible);
    }

    // ================= এলিজিবিলিটি লেবেল ডিজাইন চেঞ্জ =================
    private void setEligibilityUI(boolean isEligible) {
        User user = LoginDetails.getUser();
        if (isEligible) {
            lblEligibilityStatus.setText("Eligible for Donation");
            // সবুজ ব্যাকগ্রাউন্ড
            lblEligibilityStatus.setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-background-radius: 15; -fx-padding: 5 15;");
            user.setEligibility("Eligible");
        } else {
            lblEligibilityStatus.setText("Not Eligible");
            // লাল ব্যাকগ্রাউন্ড
            lblEligibilityStatus.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -fx-background-radius: 15; -fx-padding: 5 15;");
            user.setEligibility("Not Eligible");
        }
    }

    // ================= JSON ফাইলে সেভ করা (GSON ব্যবহার করে) =================
    private void saveUserToJson(User updatedUser) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<User> userList = new ArrayList<>();

        // ১. ফাইল থেকে আগের সব ইউজার পড়া
        try (Reader reader = new FileReader(USER_FILE_PATH)) {
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            userList = gson.fromJson(reader, userListType);
            if (userList == null) userList = new ArrayList<>();
        } catch (FileNotFoundException e) {
            System.out.println("File not found, creating new.");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // ২. নির্দিষ্ট ইউজারকে খুঁজে বের করে আপডেট করা
        boolean found = false;
        for (int i = 0; i < userList.size(); i++) {
            // ইমেইল দিয়ে চেক করা হচ্ছে (ইউনিক আইডি হিসেবে)
            if (userList.get(i).getEmail().equals(updatedUser.getEmail())) {
                userList.set(i, updatedUser); // লিস্ট আপডেট করা হলো
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("User not found in JSON list!");
            return;
        }

        // ৩. ফাইল আবার রাইট করা (সেভ)
        try (Writer writer = new FileWriter(USER_FILE_PATH)) {
            gson.toJson(userList, writer);
            System.out.println("Donation date saved successfully to JSON.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // ======================================================
    //                SET PROFILE IMAGE (CIRCLE)
    // Uses ImagePattern to fill Circle
    // If no image -> default image used
    // ======================================================

    private void setProfileImage(String path) {
        try {
            File file = (path != null && !path.isEmpty()) ? new File(path) : null;

            if (file != null && file.exists()) {
                Image img = new Image("file:" + file.getAbsolutePath());
                profileCircle.setFill(new ImagePattern(img));
            } else {
                File defaultFile = new File(DEFAULT_IMAGE_PATH);
                if (defaultFile.exists()) {
                    Image img = new Image("file:" + defaultFile.getAbsolutePath());
                    profileCircle.setFill(new ImagePattern(img));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ======================================================
    //                LOAD IMAGE INTO IMAGEVIEW
    // Used for NID Front & Back images
    // ======================================================

    private void loadImage(ImageView view, String path) {
        try {
            if (path != null && !path.isEmpty()) {
                File file = new File(path);
                if (file.exists()) {
                    view.setImage(new Image("file:" + file.getAbsolutePath()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ======================================================
    //                OPEN MEDICAL PDF FILE
    // Uses Desktop API to open PDF externally
    // ======================================================

    private void openPdf(String path) {
        try {
            if (path != null && !path.isEmpty()) {
                File file = new File(path);
                if (file.exists()) {
                    Desktop.getDesktop().open(file);
                } else {
                    System.out.println("PDF not found!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ======================================================
    //                SAFE STRING CHECK
    // Prevents null or empty value display
    // ======================================================

    private String safe(String value) {
        return (value != null && !value.isEmpty()) ? value : "N/A";
    }


    @FXML
    void editprof(ActionEvent event)throws Exception{
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditProf.fxml"));
            Parent root = loader.load();

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));
            popupStage.setTitle("Edit Profile");

            popupStage.initOwner(btnEditProfile.getScene().getWindow());
            popupStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            popupStage.setResizable(false);

            popupStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void editPP(ActionEvent event)throws Exception{
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateProfilePic.fxml"));
            Parent root = loader.load();

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));
            popupStage.setTitle("Update Profile Picture");

            popupStage.initOwner(btnUpdatePhoto.getScene().getWindow());
            popupStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            popupStage.setResizable(false);

            popupStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void editdoc(ActionEvent event)throws Exception{
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditDocuments.fxml"));
            Parent root = loader.load();

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));
            popupStage.setTitle("Update Documents");

            popupStage.initOwner(btnUpdateDocuments.getScene().getWindow());
            popupStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            popupStage.setResizable(false);

            popupStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ======================================================
    //                SIDEBAR NAVIGATION METHODS
    // (COMPLETELY UNCHANGED)
    // ======================================================

    @FXML
    void ProftoHome(ActionEvent event)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Stage stage = (Stage) btnHome.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void ProftoNews(ActionEvent event)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Newsfeed.fxml"));
        Stage stage = (Stage) btnNewsFeed.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void ProftoNoti(ActionEvent event)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Notification.fxml"));
        Stage stage = (Stage) btnNotifications.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void ProftoChat(ActionEvent event)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Chat.fxml"));
        Stage stage = (Stage) btnChat.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void ProftoCreate(ActionEvent event)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("CreatePost.fxml"));
        Stage stage = (Stage) btnCreatePost.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void ProftoStng(ActionEvent event)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
        Stage stage = (Stage) btnSettings.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void logout_p(ActionEvent event) throws Exception {
        chkLogin.setlogout();

        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Stage stage = (Stage) btnLogOut.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
