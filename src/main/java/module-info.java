module edu.ITSolutions.Export {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    exports edu.ITSolutions.Export;
    exports edu.ITSolutions.Export.Controller;
    exports edu.ITSolutions.Export.ui;
    exports edu.ITSolutions.Export.util;

}
