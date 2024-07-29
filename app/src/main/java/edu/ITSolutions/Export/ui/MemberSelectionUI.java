package edu.ITSolutions.Export.ui;

import edu.ITSolutions.Export.Member;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.stream.Collectors;

public class MemberSelectionUI {
    private final ObservableList<Member> members;

    public MemberSelectionUI(ObservableList<Member> members) {
        this.members = members;
    }

    public HBox createMemberSelectionLayout() {

        ListView<Member> memberListView = new ListView<>(members);
        memberListView.setCellFactory((ListView<Member> param) -> new ListCell<>() {
            private final CheckBox checkBox;

            {
                checkBox = new CheckBox();
                checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                    if (getItem() != null) {
                        getItem().setSelected(isNowSelected);
                    }
                });
            }

            @Override
            protected void updateItem(Member item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    checkBox.setText(item.getName());
                    checkBox.setSelected(item.isSelected());
                    setGraphic(checkBox);
                } else {
                    setGraphic(null);
                }
            }
        });

        Button showSelectedButton = new Button("Show Selected Members");
        TextArea selectedMembersArea = new TextArea();
        selectedMembersArea.setEditable(false);

        showSelectedButton.setOnAction(e -> {
            String selectedMembers = members.stream()
                .filter(Member::isSelected)
                .map(Member::getName)
                .collect(Collectors.joining(", "));
            selectedMembersArea.setText(selectedMembers.isEmpty() ? "No members selected" : selectedMembers);
        });

        HBox layout = new HBox(10, memberListView, selectedMembersArea, showSelectedButton);
        return layout;
    }

    public List<String> getSelectedMembers() {
        return members.stream()
                .filter(Member::isSelected)
                .map(Member::getName)
                .collect(Collectors.toList());
    }
}
