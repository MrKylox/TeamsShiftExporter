package edu.ITSolutions.Export.ui;

import edu.ITSolutions.Export.Member;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.stream.Collectors;

public class MemberSelectionUI extends HBox {
    private final  ObservableList<Member> members;

    public MemberSelectionUI(ObservableList<Member> members) {
        this.members = members;

        ListView<Member> memberListView = new ListView<>(members);
        memberListView.setCellFactory((ListView<Member> param) -> new ListCell<>() {
            private final CheckBox checkBox;
            {
                checkBox = new CheckBox();
                checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                    if (getItem() != null) {
                        getItem().setSelected(isNowSelected);
                        GroupShift.refreshMemberList();
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
        this.getChildren().add(memberListView);
    }

    public List<String> getSelectedMembers() {
        return members.stream()
                .filter(Member::isSelected)
                .map(Member::getName)
                .collect(Collectors.toList());
    }
}
