package com.example.projectredpulsenew;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;

public class PostOwnerProfileView_Controller {

    @FXML private Button btnClose;
    @FXML private Button btnMedicalPdf;

    @FXML private Circle profileCircle;
    @FXML private Label lblUserName;
    @FXML private Label lblHeaderDistrict;

    @FXML private Label lblBloodGroup;
    @FXML private Label lblAge;
    @FXML private Label lblGender;
    @FXML private Label lblPhone;
    @FXML private Label lblEmail;

    @FXML private ImageView imgNidFront;
    @FXML private ImageView imgNidBack;

    private String medicalPdfPath;

    // ==========================================================
    @FXML
    public void initialize() {
        btnMedicalPdf.setOnAction(e -> openPdf());
    }

    // ==========================================================
    // Receive Data From Newsfeed Controller
    // ==========================================================
    public void setPostOwnerData(User postOwner) {

        if (postOwner == null) return;

        lblUserName.setText(safe(postOwner.getName()));
        lblHeaderDistrict.setText(safe(postOwner.getDistrict()));
        lblBloodGroup.setText(safe(postOwner.getBloodGroup()));
        lblAge.setText(safe(postOwner.getAge()));
        lblGender.setText(safe(postOwner.getGender()));
        lblPhone.setText(safe(postOwner.getNumber()));
        lblEmail.setText(safe(postOwner.getEmail()));

        this.medicalPdfPath = postOwner.getMedicalPdfPath();

        setCircleImage(profileCircle, postOwner.getProfilePicPath());
        setImageView(imgNidFront, postOwner.getNidFrontPath());
        setImageView(imgNidBack, postOwner.getNidBackPath());
    }

    // ==========================================================
    // Image Handling
    // ==========================================================
    private void setCircleImage(Circle circle, String path) {
        try {
            if (path == null || path.isEmpty()) return;

            File file = new File(path);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                circle.setFill(new ImagePattern(image));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setImageView(ImageView view, String path) {
        try {
            if (path == null || path.isEmpty()) return;

            File file = new File(path);
            if (file.exists()) {
                view.setImage(new Image(file.toURI().toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================================================
    // Open Medical PDF
    // ==========================================================
    private void openPdf() {
        try {
            if (medicalPdfPath == null || medicalPdfPath.isEmpty()) return;

            File file = new File(medicalPdfPath);
            if (file.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================================================
    // Close Popup
    // ==========================================================
    @FXML
    void closeWindow(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    private String safe(String s) {
        return (s == null || s.isEmpty()) ? "N/A" : s;
    }
}
