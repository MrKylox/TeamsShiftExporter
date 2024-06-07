module edu.mnsu.it.teams.shift {
    requires javafx.controls;
    requires javafx.fxml;

    opens edu.mnsu.it.teams.shift to javafx.fxml;
    exports edu.mnsu.it.teams.shift;
}
