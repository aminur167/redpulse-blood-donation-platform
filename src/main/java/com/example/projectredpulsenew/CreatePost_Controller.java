package com.example.projectredpulsenew;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreatePost_Controller implements Initializable {

    @FXML private Label appNameLabel;

    @FXML private Button btnChat;
    @FXML private Button btnCreatePost;
    @FXML private Button btnLogOut;
    @FXML private Button btnNewsFeed;
    @FXML private Button btnHome;
    @FXML private Button btnNotifications;
    @FXML private Button btnProfile;
    @FXML private Button btnSettings;

    @FXML private VBox sidebar;
    @FXML private VBox sidebarButtons;

    @FXML private HBox sidebarHeader;
    @FXML private HBox topBar;
    @FXML private HBox topBarRight;

    // Form fields
    @FXML private TextField patientNameField;
    @FXML private ComboBox<String> bloodGroupField;
    @FXML private TextField unitNeededField;
    @FXML private TextField locationField;
    @FXML private DatePicker dateNeededField;
    @FXML private TextField phoneField;
    @FXML private TextArea notesArea;
    @FXML private Button submitBtn;
    @FXML private ComboBox<String> districtField;

    private final String filePath = System.getProperty("user.dir") + File.separator + "D:\\project-redpulse-new\\src\\main\\resources\\com\\example\\projectredpulsenew\\PostDetails.json";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        districtField.setItems(FXCollections.observableArrayList(
                "Bagerhat", "Bandarban", "Barguna", "Barishal", "Bhola", "Bogura",
                "Brahmanbaria", "Chandpur", "Chapainawabganj", "Chattogram", "Chuadanga",
                "Cox's Bazar", "Cumilla", "Dhaka", "Dinajpur", "Faridpur", "Feni",
                "Gaibandha", "Gazipur", "Gopalganj", "Habiganj", "Jamalpur", "Jashore",
                "Jhalokati", "Jhenaidah", "Joypurhat", "Khagrachhari", "Khulna",
                "Kishoreganj", "Kurigram", "Kushtia", "Lakshmipur", "Lalmonirhat",
                "Madaripur", "Magura", "Manikganj", "Meherpur", "Moulvibazar",
                "Munshiganj", "Mymensingh", "Naogaon", "Narail", "Narayanganj",
                "Narsingdi", "Natore", "Netrokona", "Nilphamari", "Noakhali", "Pabna",
                "Panchagarh", "Patuakhali", "Pirojpur", "Rajbari", "Rajshahi",
                "Rangamati", "Rangpur", "Satkhira", "Shariatpur", "Sherpur",
                "Sirajganj", "Sunamganj", "Sylhet", "Tangail", "Thakurgaon"
        ));
    }

    // ------------------- Navigation Buttons -------------------

    @FXML
    void CreatetoNews(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Newsfeed.fxml"));
        Stage stage = (Stage) btnNewsFeed.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void CreatetoHome(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Stage stage = (Stage) btnHome.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void CreatetoNoti(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Notification.fxml"));
        Stage stage = (Stage) btnNotifications.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void CreatetoChat(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Chat.fxml"));
        Stage stage = (Stage) btnChat.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void CreatetoProf(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        Stage stage = (Stage) btnProfile.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void CreatetoStng(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
        Stage stage = (Stage) btnSettings.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // ------------------- Save Data to JSON -------------------

    private void savePostToJson(PostDetails post) {
        // ফিক্স: রিলেটিভ পাথ ব্যবহার করা হয়েছে যেন নিউজফিডের সাথে ফাইল লোকেশন মিলে যায়
        String filePath = "D:\\project-redpulse-new\\src\\main\\resources\\com\\example\\projectredpulsenew\\PostDetails.json";



        List<PostDetails> posts = new ArrayList<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();


        try {
            File file = new File(filePath);
            System.out.println("Writing to file at: " + file.getAbsolutePath());
            if (file.exists() && file.length() > 0) {
                try (Reader reader = new FileReader(file)) {
                    Type listType = new TypeToken<List<PostDetails>>() {}.getType();
                    List<PostDetails> existingPosts = gson.fromJson(reader, listType);
                    if (existingPosts != null) posts = existingPosts;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error reading existing posts: " + e.getMessage());
        }

        posts.add(post);

        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(posts, writer);
            System.out.println("Post saved successfully for user: " + post.getUserName());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving post: " + e.getMessage());
        }
    }

    // ------------------- Submit Button -------------------

    @FXML
    public void submitPost(ActionEvent event) {
        try {
            String patient = patientNameField.getText();
            String blood = bloodGroupField.getValue();
            String unit = unitNeededField.getText();
            String loc = locationField.getText();
            String date = dateNeededField.getValue() != null ? dateNeededField.getValue().toString() : "";
            String phone = phoneField.getText();
            String district = districtField.getValue();
            String notes = notesArea.getText();

            // Validation
            if (patient.isEmpty()) {
                showAlert("Error", "Please Enter Patient Name!");
                return;
            }
            if (!isValidName(patient)) {
                showAlert("Error", "Patient Name must contain only letters and spaces (3-50 chars).");
                return;
            }

            if (blood == null || blood.isEmpty()) {
                showAlert("Error", "Please select a blood group.");
                return;
            }

            if (!isValidUnit(unit)) {
                showAlert("Error", "Unit Needed must be a positive number.");
                return;
            }

            if (!isValidLocation(loc)) {
                showAlert("Error", "Hospital name must have at least 3 characters.");
                return;
            }

            if (!isValidDate(dateNeededField)) {
                showAlert("Error", "Please select a valid date (today or future).");
                return;
            }

            if (phone.isEmpty()) {
                showAlert("Error", "Please Enter Phone Number!");
                return;
            }
            if (!isValidPhone(phone)) {
                showAlert("Error", "Phone must be 11 digits, Bangladesh format (01XXXXXXXXX).");
                return;
            }

            if (district == null || district.isEmpty()) {
                showAlert("Error", "Please select a district.");
                return;
            }

            if (!isValidNotes(notes)) {
                showAlert("Error", "Notes cannot exceed 200 characters.");
                return;
            }



            // Get current user's name
            String currentUserName = "Anonymous";
            String currentUserEmail = "N/A";
            String currentUserPassword = "N/A";
            String currentUserProfilePic = "N/A";

            try {
                User temp2 = LoginDetails.getUser();
                if (temp2 != null) {
                    if (temp2.getName() != null) currentUserName = temp2.getName();
                    if (temp2.getEmail() != null) currentUserEmail = temp2.getEmail();
                    if (temp2.getPassword() != null) currentUserPassword = temp2.getPassword();
                    if (temp2.getProfilePicPath() != null) currentUserProfilePic = temp2.getProfilePicPath();
                }
            } catch (Exception e) {
                System.out.println("Could not get user info: " + e.getMessage());
            }

            // Create post
            PostDetails post = new PostDetails(
                    currentUserName,
                    currentUserEmail,
                    currentUserPassword,
                    currentUserProfilePic,
                    patient,
                    blood,
                    unit,
                    loc,
                    date,
                    phone,
                    district,
                    notes
            );

            System.out.println("Creating post by: " + currentUserName);

            savePostToJson(post);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Post Created!");
            alert.setContentText("Your blood donation request has been posted to the newsfeed.");
            alert.showAndWait();

            clearForm();

            // Navigate to newsfeed
            Parent root = FXMLLoader.load(getClass().getResource("Newsfeed.fxml"));
            Stage stage = (Stage) submitBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Failed to create post: " + e.getMessage());
            alert.show();
        }
    }

    private void clearForm() {
        patientNameField.clear();
        bloodGroupField.setValue(null);
        unitNeededField.clear();
        locationField.clear();
        dateNeededField.setValue(null);
        phoneField.clear();
        districtField.setValue(null);
        notesArea.clear();
    }

    // ================= VALIDATION HELPERS =================
    private boolean isValidName(String name) {
        if (name == null) return false;

        // Trim leading/trailing spaces
        name = name.trim();

        // Regex: Only letters and single spaces between words, 3-50 chars
        String regex = "^[A-Za-z]+( [A-Za-z]+)*$";

        return name.matches(regex) && name.length() >= 3 && name.length() <= 50;
    }
    private boolean isValidUnit(String unit) {
        if (unit == null || unit.isEmpty()) return false;
        try {
            int u = Integer.parseInt(unit);
            return u > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private boolean isValidLocation(String loc) {
        return loc != null && loc.length() >= 3;
    }
    private boolean isValidDate(DatePicker datePicker) {
        if (datePicker.getValue() == null) return false;
        return !datePicker.getValue().isBefore(java.time.LocalDate.now());
    }
    private boolean isValidNotes(String notes) {
        return notes.length() <= 200;
    }
    private boolean isValidPhone(String phone) {
        if (phone == null) return false;

        // Remove leading/trailing spaces
        phone = phone.trim();

        // Bangladesh mobile number format: 01XXXXXXXXX (11 digits)
        String regex = "^01[3-9]\\d{8}$";

        return phone.matches(regex);
    }




    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    void logout_c(ActionEvent event) throws Exception {
        chkLogin.setlogout();

        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Stage stage = (Stage) btnLogOut.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}