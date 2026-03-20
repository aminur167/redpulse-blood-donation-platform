package com.example.projectredpulsenew;
;

public class chkLogin {
        private static boolean loggedIn = false;

        public static boolean isLoggedIn() {
            return loggedIn;
        }

        public static void setlogin() {
            loggedIn = true;
        }

        public static void setlogout() {
            loggedIn = false;
            LoginDetails.temp = null;
        }

        public static void alert() {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Login Required");
            alert.setHeaderText(null);
            alert.setContentText("Please login first to perform this action.");
            alert.showAndWait();
        }
}
