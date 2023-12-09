module ca.bcit.comp2522.termproject.lyxz {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens ca.bcit.comp2522.termproject.lyxz to javafx.fxml;
    exports ca.bcit.comp2522.termproject.lyxz;
}