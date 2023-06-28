module com.flashlearn {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;


    opens com.flashlearn to javafx.fxml;
    exports com.flashlearn;
}