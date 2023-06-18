module com.flashlearn {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.flashlearn to javafx.fxml;
    exports com.flashlearn;
}