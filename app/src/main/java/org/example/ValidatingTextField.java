package org.example;

import java.util.function.Predicate;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TextField;

public class ValidatingTextField extends TextField {
    private final Predicate<String> validation;
    private BooleanProperty isValidProperty = new SimpleBooleanProperty();

    public ValidatingTextField(Predicate<String> validation) {
        this.validation = validation;

        textProperty().addListener((o, oldValue, newText) -> {
            isValidProperty.set(validation.test(newText));
        });
        
        isValidProperty.set(validation.test(""));
    }

    public BooleanProperty isValidProperty() {
        return isValidProperty;
    }
}
