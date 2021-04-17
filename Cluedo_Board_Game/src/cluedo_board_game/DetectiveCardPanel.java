/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cluedo_board_game;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;
import javafx.util.Pair;

/**
 *
 * @author Anilz
 */
public class DetectiveCardPanel {

    private String[] suspectNames = {"Miss Scarlett", "Colonel Mustard", "Mrs.White", "Mr.Green", "Mrs.Peacock", "Professor Plum"};
    private String[] weaponNames = {"Dagger", "Candlestick", "Revolver", "Rope", "Leadpiping", "Spanner"};
    private String[] roomNames = {"Lounge", "Diningroom", "Kitchen", "Ballroom", "Conservatory", "Billiardroom", "Library", "Hall", "Study"};
    //Collection of HBoxes including itemLabels and corresponding ChoiceBoxes
    private ArrayList<HBox> boxesAndLabels = new ArrayList<HBox>();

    //Hashmap and notes store
    private HashMap<String, Boolean> detectiveCard = new HashMap<String, Boolean>();
    private String detectiveNotes;

    private TextArea detectiveNotesTextArea;
    private Button saveButton;

    /**
     *
     * @return the content of detectiveCheckList
     */
    public Parent createContent() {
        //VBox to contain suspect names and corresponding boxes
        VBox suspectList = new VBox();
        //Heading label,size of label created and put into VBox
        Label suspects = new Label("------Suspects------");
        suspectList.getChildren().add(suspects);
        suspects.setScaleX(1);
        //Loop through suspect names and for each suspect name;
        for (String suspectName : suspectNames) {
            //Create label and checkbox,then put them into HBox
            Label suspectText = new Label(suspectName);
            CheckBox suspectCheckBox = new CheckBox();
            HBox suspectCheckListLine = new HBox(suspectCheckBox, suspectText);
            //Add the HBox to defined arrayList
            boxesAndLabels.add(suspectCheckListLine);

            //detectiveCard.put(suspectName, suspectCheckBox.isSelected());
            //Set Spacings between label and checkbox 
            suspectCheckListLine.setSpacing(10);
            //Finally add HBox into VBox
            suspectList.getChildren().add(suspectCheckListLine);

        }
        //VBox to contain room names and corresponding boxes
        VBox roomList = new VBox();
        //Heading label,size of label created and put into VBox
        Label rooms = new Label("------Rooms------");
        roomList.getChildren().add(rooms);
        rooms.setScaleX(1);
        //Loop through room names and for each room name;
        for (String roomName : roomNames) {
            //Create label and checkbox,then put them into HBox
            Label roomLabel = new Label(roomName);
            CheckBox roomCheckBox = new CheckBox();
            HBox roomCheckListLine = new HBox(roomCheckBox, roomLabel);
            //Add the HBox to defined arrayList
            boxesAndLabels.add(roomCheckListLine);

            //detectiveCard.put(roomName, roomCheckBox.isSelected());
            //Set Spacings between label and checkbox 
            roomCheckListLine.setSpacing(10);
            //Finally add HBox into VBox
            roomList.getChildren().add(roomCheckListLine);
        }

        //VBox to contain weapon names and corresponding boxes
        VBox weaponList = new VBox();
        //Heading label,size of label created and put into VBox
        Label weapons = new Label("------Weapons------");
        roomList.getChildren().add(weapons);
        weapons.setScaleX(1);
        //Loop through weapon names and for each room name;
        for (String weaponName : weaponNames) {
            //Create label and checkbox,then put them into HBox
            Label weaponLabel = new Label(weaponName);
            CheckBox weaponCheckBox = new CheckBox();
            HBox weaponCheckListLine = new HBox(weaponCheckBox, weaponLabel);
            //Add the HBox to defined arrayList
            boxesAndLabels.add(weaponCheckListLine);

            //detectiveCard.put(weaponName, weaponCheckBox.isSelected());
            //Set Spacings between label and checkbox 
            weaponCheckListLine.setSpacing(10);
            //Finally add HBox into VBox
            weaponList.getChildren().add(weaponCheckListLine);
        }
        //Final VBox is created for complete checklist
        VBox completeCheckList = new VBox(suspectList, roomList, weaponList);

        //A text area is created for a player to take notes
        detectiveNotesTextArea = new TextArea(detectiveNotes);
        detectiveNotesTextArea.setPromptText("Case Notes...");
        detectiveNotesTextArea.setWrapText(true);
        detectiveNotesTextArea.setPrefSize(200, 500);
        Pane detectiveNotesPane = new Pane(detectiveNotesTextArea);

        saveButton = new Button("Save CheckList!");
        //Finally checklist and detective note area is combined into HBox
        HBox detectiveCheckList = new HBox(completeCheckList, detectiveNotesPane, saveButton);

        return detectiveCheckList;
    }

    /**
     * selects / unselects the label
     *
     * @param itemName
     */
    private boolean toggleSelect(String itemName) {
        detectiveCard.replace(itemName, !detectiveCard.get(itemName));
        return !detectiveCard.get(itemName);
    }

    /**
     * Returns updated detective card when a checkbox is selected
     *
     * @return detectiveCard
     */
    public HashMap getCardUpdates() {

        for (HBox boxAndName : boxesAndLabels) {
            // child 0 is selection box and child 1 is label
            CheckBox selectionBox = (CheckBox) boxAndName.getChildren().get(0);
            Label selectionLabel = (Label) boxAndName.getChildren().get(1);
            //if value not selected not select , if selected select
            if (detectiveCard.get(selectionLabel.getText()) == Boolean.TRUE) {
                selectionBox.setSelected(true);
            } else {
                selectionBox.setSelected(false);
            }
            //On click a print is provided

            selectionBox.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //if selected print selected , else print not selected
                    toggleSelect(selectionLabel.getText());
                }
            });

        }
        //detectiveNotes = detectiveNotesTextArea.getText();
        return detectiveCard;
    }

    public HashMap<String, Boolean> getDetectiveCard() {
        return detectiveCard;
    }

    public void setDetectiveCard(HashMap<String, Boolean> detectiveCard) {
        this.detectiveCard = detectiveCard;
    }

    public String getDetectiveNotes() {
        return detectiveNotes;
    }

    public void setDetectiveNotes(String detectiveNotes) {
        this.detectiveNotes = detectiveNotes;
    }

    public TextArea getDetectiveNotesTextArea() {
        return detectiveNotesTextArea;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    
    
}
