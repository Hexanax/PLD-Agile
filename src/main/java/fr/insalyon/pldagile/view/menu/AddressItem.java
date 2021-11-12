package fr.insalyon.pldagile.view.menu;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.insalyon.pldagile.view.Fonts;

/**
 * Address item {@link Region} meant to be displayed in the request list to show the
 * request entity's information, it can either be a {@link fr.insalyon.pldagile.model.Pickup}, {@link fr.insalyon.pldagile.model.Delivery}
 * or a {@link fr.insalyon.pldagile.model.Depot}
 */
public class AddressItem extends Region {

    private static final double ADDRESS_ITEM_HEIGHT_WITHOUT_TOUR = 75.0;
    private static final double ADDRESS_ITEM_HEIGHT_WITH_TOUR = 100.0;
    private static final double DEPOT_HEIGHT = 50;
    private final Date arrivalDate;
    private final String type;
    private final int duration;
    private final int stepIndex;
    private final long requestNumber;
    private TextField value;

    public AddressItem(Date arrivalDate, int duration, long requestNumber, String type, int stepIndex, boolean editable) {
        this.arrivalDate = arrivalDate;
        this.type = type;
        this.duration = duration;
        this.requestNumber = requestNumber;
        this.stepIndex = stepIndex;
        render(editable);
    }

    /**
     * Render the address item and set it editable in function of the parameter
     * @param editable
     */
    public void render(boolean editable) {
        GridPane mainGridPane = new GridPane();
        mainGridPane.setAlignment(Pos.BASELINE_CENTER);
        mainGridPane.setHgap(5);
        mainGridPane.setVgap(5);

        DateFormat dateFormat = new SimpleDateFormat("HH 'h' mm");
        if (requestNumber >= 0) { //Has a request number ?
            Label titleLabel = new Label(type + " n°" + (requestNumber + 1));
            titleLabel.setFont(Fonts.getBoldBodyFont());
            mainGridPane.add(titleLabel, 0, 0, 4, 1);
            GridPane.setHalignment(titleLabel, HPos.LEFT);
            GridPane.setMargin(titleLabel, new Insets(10, 0, 5, 0));

            //Only display arrival time if there's one
            if (arrivalDate != null) {
                Label arrivalLabel = new Label("Arrival time : " + dateFormat.format(arrivalDate));
                titleLabel.setFont(Fonts.getBodyFont());
                mainGridPane.add(arrivalLabel, 0, 1, 4, 1);
                GridPane.setHalignment(arrivalLabel, HPos.LEFT);
                GridPane.setMargin(arrivalLabel, new Insets(5, 5, 5, 0));
            }

            //The duration label
            Label timeLabel = new Label(type + " duration : ");
            timeLabel.setFont(Fonts.getBodyFont());
            mainGridPane.add(timeLabel, 0, 2, 1, 1);
            GridPane.setHalignment(timeLabel, HPos.LEFT);
            GridPane.setMargin(timeLabel, new Insets(5, 5, 5, 0));

            //The editable text field for the duration
            value = new TextField(String.valueOf(duration));
            value.setFont(Fonts.getBodyFont());
            mainGridPane.add(value, 1, 2, 1, 1);
            GridPane.setHalignment(value, HPos.LEFT);
            GridPane.setMargin(value, new Insets(5, 5, 5, 0));
            value.setMaxWidth(50);
            value.setEditable(editable);
            value.setDisable(false);

            //The time unit label
            Label timeUnit = new Label(" seconds");
            timeUnit.setFont(Fonts.getBodyFont());
            mainGridPane.add(timeUnit, 2, 2, 1, 1);
            GridPane.setHalignment(timeUnit, HPos.LEFT);
            GridPane.setMargin(timeUnit, new Insets(5, 5, 5, 0));
        } else { //Otherwise depot rendering
            Label titleLabel = new Label(type + " - " + dateFormat.format(arrivalDate));
            titleLabel.setFont(Fonts.getBoldBodyFont());
            mainGridPane.add(titleLabel, 0, 0, 4, 1);
            GridPane.setHalignment(titleLabel, HPos.LEFT);
            GridPane.setMargin(titleLabel, new Insets(10, 0, 5, 0));
        }
        this.getChildren().add(mainGridPane);
    }


    /**
     * get the item height in javafx
     * @return address height
     */
    public double getAddressItemHeight() {
        if (requestNumber < 0) {
            return DEPOT_HEIGHT;
        }
        if (arrivalDate == null) {
            return ADDRESS_ITEM_HEIGHT_WITHOUT_TOUR;
        }
        return ADDRESS_ITEM_HEIGHT_WITH_TOUR;
    }

    public long getRequestNumber() {
        return requestNumber;
    }

    public String getType() {
        return type;
    }

    public int getStepIndex() {
        return stepIndex;
    }

    public String getValue() {
        return this.value.getText();
    }

    public void setEditable(boolean b) {
        this.value.setEditable(b);
    }

    /**
     * Resize the item's height
     */
    public void enforceHeight() {
        this.setPrefHeight(this.getAddressItemHeight());
    }

}
