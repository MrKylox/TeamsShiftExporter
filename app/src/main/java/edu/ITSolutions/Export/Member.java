package edu.ITSolutions.Export;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Member {
    private final String name;
    private final String email;
    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    public Member(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }
}
