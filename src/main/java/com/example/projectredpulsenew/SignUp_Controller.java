package com.example.projectredpulsenew;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SignUp_Controller implements Initializable {

    // ================= EXISTING FIELDS =================
    @FXML
    private TextField name, age, email, number;

    @FXML
    private PasswordField password, confirm_pass;

    @FXML
    private ComboBox<String> genderItems, bloodGroupOption, district_items;

    @FXML
    private Label signupMassage;

    @FXML
    private Button signupBtn, btnCancel;

    // ================= NEW FIELDS =================
    @FXML
    private Button chooseProfilePicBtn, chooseMedicalPdfBtn, chooseNidFrontBtn, chooseNidBackBtn;

    @FXML
    private Label profilePicStatus, medicalPdfStatus, nidFrontStatus, nidBackStatus;

    // File references to store selected files
    private File profilePicFile, medicalPdfFile, nidFrontFile, nidBackFile;

    private Runnable onSignUpSuccess;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Gender options
        genderItems.setItems(FXCollections.observableArrayList("Male", "Female"));

        // Blood Group options
        bloodGroupOption.setItems(FXCollections.observableArrayList("O+", "A+", "B+", "AB+", "O-", "A-", "B-", "AB-"));

        // District options
        district_items.setItems(FXCollections.observableArrayList(
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

        // ================= FILE CHOOSER BUTTONS =================
        chooseProfilePicBtn.setOnAction(this::chooseProfilePic);
        chooseMedicalPdfBtn.setOnAction(this::chooseMedicalPdf);
        chooseNidFrontBtn.setOnAction(this::chooseNidFront);
        chooseNidBackBtn.setOnAction(this::chooseNidBack);
    }

    // ================= FILE CHOOSER HANDLERS =================
    private void chooseProfilePic(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        profilePicFile = fileChooser.showOpenDialog(signupBtn.getScene().getWindow());
        if (profilePicFile != null) {
            profilePicStatus.setText(profilePicFile.getName());
        } else {
            profilePicStatus.setText("No file chosen");
        }
    }

    private void chooseMedicalPdf(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload Medical Certificate PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        medicalPdfFile = fileChooser.showOpenDialog(signupBtn.getScene().getWindow());
        if (medicalPdfFile != null) {
            medicalPdfStatus.setText(medicalPdfFile.getName());
        } else {
            medicalPdfStatus.setText("No file chosen");
        }
    }

    private void chooseNidFront(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select NID Front Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        nidFrontFile = fileChooser.showOpenDialog(signupBtn.getScene().getWindow());
        if (nidFrontFile != null) {
            nidFrontStatus.setText(nidFrontFile.getName());
        } else {
            nidFrontStatus.setText("No file chosen");
        }
    }

    private void chooseNidBack(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select NID Back Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        nidBackFile = fileChooser.showOpenDialog(signupBtn.getScene().getWindow());
        if (nidBackFile != null) {
            nidBackStatus.setText(nidBackFile.getName());
        } else {
            nidBackStatus.setText("No file chosen");
        }
    }

    // ================= SAVE USER TO JSON =================
    private void saveUserToJson(User user) {
        try {
            File file = AppPaths.userDetailsJson().toFile();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<User> userList;

            if (file.exists()) {
                Reader reader = new FileReader(file);
                Type listType = new TypeToken<List<User>>() {}.getType();
                userList = gson.fromJson(reader, listType);
                if (userList == null) userList = new ArrayList<>();
                reader.close();
            } else {
                userList = new ArrayList<>();
            }


            userList.add(user);

            // Write pretty JSON
            Writer writer = new FileWriter(file);
            gson.toJson(userList, writer);
            writer.close();

            System.out.println("User saved successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= SIGN UP BUTTON =================
    @FXML
    void signup(ActionEvent event) throws Exception {
        try {
            String nameInput = name.getText();
            String ageInput = age.getText();
            String genderInput = genderItems.getValue();
            String bloodInput = bloodGroupOption.getValue();
            String districtInput = district_items.getValue();
            String emailInput = email.getText();
            String numberInput = number.getText();
            String passwordInput = password.getText();
            String confirmPasswordInput = confirm_pass.getText();

            // ================= VALIDATION =================
            if (nameInput.isEmpty()) { showError("Please Enter your Name!"); return; }
            if (!isValidName(nameInput)) {
                showError("Name must contain only letters (min 3 characters).");
                return;
            }

            if (ageInput.isEmpty()) { showError("Please Enter your Age!"); return; }
            if (!isValidAge(ageInput)) {
                showError("Age must be between 18 - 65.");
                return;
            }

            if (genderInput == null || genderInput.isEmpty()) { showError("Please Select your Gender!"); return; }
            if (bloodInput == null || bloodInput.isEmpty()) { showError("Please Select your Blood Group!"); return; }
            if (districtInput == null || districtInput.isEmpty()) { showError("Please Enter your Address!"); return; }

            if (emailInput.isEmpty()) { showError("Please Enter a valid Email!"); return; }
            if (!isValidEmail(emailInput)) {
                showError("Invalid Email! Must be Gmail or Yahoo, no consecutive dots, valid TLD.");
                return;
            }
            if (isEmailAlreadyExists(emailInput)) {
                showError("Email already registered!");
                return;
            }

            if (numberInput.isEmpty()) { showError("Please Enter Phone Number!"); return; }
            if (!isValidPhone(numberInput)) {
                showError("Invalid Phone Number! Use 01XXXXXXXXX format.");
                return;
            }

            if (passwordInput == null || passwordInput.isEmpty()) { showError("Please enter a Valid Password!"); return; }
            if (!isStrongPassword(passwordInput)) {
                showError("Password must be 6+ chars with letters & numbers.");
                return;
            }

            if (confirmPasswordInput == null || confirmPasswordInput.isEmpty()) { showError("Please Write again to Confirm the Password!"); return; }
            if (!passwordInput.equals(confirmPasswordInput)) { showError("Passwords do not match!"); return; }

            // Optional: Check if files are uploaded
            if (profilePicFile == null) { showError("Please upload Profile Picture!"); return; }
            if (medicalPdfFile == null) { showError("Please upload Medical Certificate!"); return; }
            if (nidFrontFile == null) { showError("Please upload NID Front Image!"); return; }
            if (nidBackFile == null) { showError("Please upload NID Back Image!"); return; }







            // ================= CREATE USER =================
            User newUser = new User(nameInput, ageInput, genderInput, bloodInput, districtInput, numberInput, emailInput, passwordInput, profilePicFile.getAbsolutePath(), medicalPdfFile.getAbsolutePath(), nidFrontFile.getAbsolutePath(), nidBackFile.getAbsolutePath());

            // Save files paths
            saveUserToJson(newUser);


            if (onSignUpSuccess != null) {
                onSignUpSuccess.run();   // 🔥 Caller page refresh হবে
            }

            Stage stage = (Stage) signupBtn.getScene().getWindow();
            stage.close();

//            // Redirect to login
//            Parent root = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
//            Stage stage = (Stage) signupBtn.getScene().getWindow();
//            stage.setScene(new Scene(root));
//            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
    private boolean isValidAge(String age) {
        try {
            int a = Integer.parseInt(age);
            return a >= 18 && a <= 65;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;

        // Regex explanation:
        // ^[A-Za-z0-9]+([._%+-]?[A-Za-z0-9]+)* --> username part (dots, _, %, +, - allowed but not consecutively or at start/end)
        // @(gmail|yahoo)\\.com$                --> only gmail.com or yahoo.com allowed
        String regex = "^[A-Za-z0-9]+([._%+-]?[A-Za-z0-9]+)*@(gmail|yahoo)\\.com$";

        // check for consecutive dots
        if (email.contains("..")) return false;

        return email.matches(regex);
    }
    private boolean isEmailAlreadyExists(String emailInput) {
        try {
            File file = AppPaths.userDetailsJson().toFile();

            if (!file.exists()) return false;

            Gson gson = new Gson();
            Reader reader = new FileReader(file);
            Type listType = new TypeToken<List<User>>() {}.getType();
            List<User> users = gson.fromJson(reader, listType);
            reader.close();

            if (users == null) return false;

            for (User user : users) {
                if (user.getEmail().equalsIgnoreCase(emailInput)) {
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    private boolean isValidPhone(String phone) {
        if (phone == null) return false;

        // Remove leading/trailing spaces
        phone = phone.trim();

        // Bangladesh mobile number format: 01XXXXXXXXX (11 digits)
        String regex = "^01[3-9]\\d{8}$";

        return phone.matches(regex);
    }
    private boolean isStrongPassword(String password) {
        // Minimum 6 chars, 1 letter, 1 number
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$");
    }



    // ================= HELPER METHOD =================
    private void showError(String message) {
        signupMassage.setText(message);
        signupMassage.setStyle("-fx-text-fill: red;");
    }


    // ================= CANCEL =================
    @FXML
    void handleCancel(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }


    public void setOnSignUpSuccess(Runnable onLoginSuccess) {
        this.onSignUpSuccess = onLoginSuccess;
    }
}
