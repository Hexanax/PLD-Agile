package fr.insalyon.pldagile.view.menu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

import java.util.Date;

public class PickupView extends Region {

    private Button addRequestButton;

    public PickupView() {
        GridPane maingp = new GridPane();
        maingp.setAlignment(Pos.CENTER);
        maingp.setPadding(new Insets(40, 40, 40, 40));
        maingp.setHgap(10);
        maingp.setVgap(10);

        Label titleLabel = new Label("Requests");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        maingp.add(titleLabel, 0,0,1,1);
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        GridPane.setMargin(titleLabel, new Insets(20, 0,20,0));


        ListView<PickupItem> list = new ListView<PickupItem>();
        ObservableList<PickupItem> items = FXCollections.observableArrayList (new PickupItem("Avenue des boulangers", new Date(), 65487)
        , new PickupItem("Rue de la Soie", new Date(), 794258), new PickupItem("Quai du Rhone", new Date(), 123456));
        list.setItems(items);
        list.setOrientation(Orientation.VERTICAL);
        list.setMaxHeight(Control.USE_PREF_SIZE);
        maingp.add(list, 0, 2, 1, 1);


        addRequestButton = new Button("Add Request");
        addRequestButton.setPrefHeight(40);
        addRequestButton.setDefaultButton(true);
        addRequestButton.setPrefWidth(100);
        maingp.add(addRequestButton, 0, 3, 1, 1);
        GridPane.setHalignment(addRequestButton, HPos.CENTER);
        GridPane.setMargin(addRequestButton, new Insets(20, 0,20,0));

        this.getChildren().add(maingp);

        addRequestButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
    }
}
