package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class ModifyView extends Region {
    private static Controller controller = null;
    private static final ObservableList<RequestItem> pickupItems = FXCollections.observableArrayList();
    private Button addRequest;
    private Button deleteRequest;
    private Button backMainMenu;
    private Button confirmMainMenu;


    protected static final String BACK = "Cancel";
    protected static final String CONFIRM = "Confirm";


    private final String[] buttonTexts = new String[]{BACK, CONFIRM};

    public ModifyView(Controller controller) {
        this.controller = controller;

        //TODO Move outside of constructor with function calls
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label titleLabel = new Label("Modify");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(titleLabel, 0,0,2,1);
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        GridPane.setMargin(titleLabel, new Insets(0, 0,10,0));

        addRequest = new Button("Add Request");
        deleteRequest = new Button("Delete Request");


        Label titleLabelRequests = new Label("Requests");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(titleLabelRequests, 0, 2, 1, 1);
        GridPane.setHalignment(titleLabelRequests, HPos.CENTER);
        GridPane.setMargin(titleLabelRequests, new Insets(20, 0, 20, 0));
        ListView<RequestItem> pickupList = new ListView<RequestItem>();
        pickupList.setItems(pickupItems);
        pickupList.setOrientation(Orientation.VERTICAL);
        pickupList.setMaxHeight(Control.USE_PREF_SIZE);
        gridPane.add(pickupList, 0, 3, 1, 1);

        backMainMenu = new Button("Cancel");
        confirmMainMenu = new Button(CONFIRM);
        gridPane.add(addRequest, 0, 1, 1, 1);
        gridPane.add(deleteRequest, 1, 1, 1, 1);
        gridPane.add(backMainMenu, 0, 6, 1, 1);
        gridPane.add(confirmMainMenu, 1, 6, 1, 1);

        backMainMenu.setOnAction(this::actionPerformed);
        confirmMainMenu.setOnAction(this::actionPerformed);

        this.getChildren().add(gridPane);
    }

    private void actionPerformed(ActionEvent event) {
        switch (((Button) event.getTarget()).getText()){
            case BACK:
                controller.cancel(); break;
            case CONFIRM:
                controller.confirm(); break;
        }
    }

    public static void setPickupItems(List<RequestItem> requestList) {
        clearItems();
        pickupItems.addAll(requestList);
    }

    public static void clearItems() {
        pickupItems.clear();
    }

}
