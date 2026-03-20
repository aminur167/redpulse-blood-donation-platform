module com.example.projectredpulsenew {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.base;
    requires javafx.graphics;
    requires java.desktop;

    // এই লাইনটি এখন কাজ করবে কারণ pom.xml এ Gson আছে
    requires com.google.gson;

    opens com.example.projectredpulsenew to javafx.fxml, com.google.gson;

    exports com.example.projectredpulsenew;
}