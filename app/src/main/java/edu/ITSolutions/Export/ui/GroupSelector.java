package edu.ITSolutions.Export.ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class GroupSelector<T> {
    private final BooleanProperty check = new SimpleBooleanProperty(false);
    private final ObjectProperty<T> item = new SimpleObjectProperty<>();

    public GroupSelector() {
    }

    public GroupSelector(T item) {
        this.item.set(item);
    }

    public GroupSelector(T item, Boolean check) {
        this.item.set(item);
        this.check.set(check);
    }

    public BooleanProperty checkProperty() {
        return check;
    }

    public Boolean getCheck() {
        return check.getValue();
    }

    public void setCheck(Boolean value) {
        check.set(value);
    }

    public ObjectProperty<T> itemProperty() {
        return item;
    }

    public T getItem() {
        return item.getValue();
    }

    public void setItem(T value) {
        item.setValue(value);
    }

    @Override
    public String toString() {
        return item.getValue().toString();
    }
}
