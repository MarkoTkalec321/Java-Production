module com.example.tkalec7 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;

    opens com.example.tkalec7 to javafx.fxml;
    exports com.example.tkalec7;
}