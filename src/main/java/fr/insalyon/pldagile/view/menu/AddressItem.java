package fr.insalyon.pldagile.view.menu;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.insalyon.pldagile.view.Fonts;

public class AddressItem extends Region {
    private static final double addressItemHeightWithoutTour = 75.0;
    private static final double addressItemHeightWithTour = 100.0;
    private static final double depotHeight = 50;
    private Date date;
    private String type;
    private int duration;
    private int stepIndex;
    private long requestNumber;
    private TextField value;

    public AddressItem(Date arrivalDate, int duration, long requestNumber, String type, int stepIndex, boolean editable) {
        this.date = arrivalDate;
        this.type = type;
        this.duration = duration;
        this.requestNumber = requestNumber;
        this.stepIndex = stepIndex;


        GridPane maingp = new GridPane();
        maingp.setAlignment(Pos.BASELINE_CENTER);
        maingp.setHgap(5);
        maingp.setVgap(5);

        DateFormat dateFormat = new SimpleDateFormat("HH 'h' mm");

        if(requestNumber >= 0) {
            Label titleLabel = new Label(type + " n°" + (requestNumber + 1));
            titleLabel.setFont(Fonts.getBoldBodyFont());
            maingp.add(titleLabel, 0, 0, 4, 1);
            GridPane.setHalignment(titleLabel, HPos.LEFT);
            GridPane.setMargin(titleLabel, new Insets(10, 0, 5, 0));


            if(arrivalDate != null){
                Label arrivalLabel = new Label("Arrival time : " + dateFormat.format(arrivalDate));
                titleLabel.setFont(Fonts.getBodyFont());
                maingp.add(arrivalLabel, 0, 1, 4, 1);
                GridPane.setHalignment(arrivalLabel, HPos.LEFT);
                GridPane.setMargin(arrivalLabel, new Insets(5, 5, 5, 0));
            }

            Label timeLabel = new Label(type + " duration : ");
            timeLabel.setFont(Fonts.getBodyFont());
            maingp.add(timeLabel, 0, 2, 1, 1);
            GridPane.setHalignment(timeLabel, HPos.LEFT);
            GridPane.setMargin(timeLabel, new Insets(5, 5, 5, 0));


            value = new TextField(String.valueOf(duration));
            value.setFont(Fonts.getBodyFont());
            maingp.add(value, 1, 2, 1, 1);
            GridPane.setHalignment(value, HPos.LEFT);
            GridPane.setMargin(value, new Insets(5, 5, 5, 0));
            value.setMaxWidth(50);
            value.setEditable(false);
            value.setDisable(false);

            Label unity = new Label(" seconds");
            unity.setFont(Fonts.getBodyFont());
            maingp.add(unity, 2, 2, 1, 1);
            GridPane.setHalignment(unity, HPos.LEFT);
            GridPane.setMargin(unity, new Insets(5, 5, 5, 0));


            if(editable){
                value.setEditable(true);
            }
        } else {
            //depot
            Label titleLabel = new Label(type + " - " + dateFormat.format(arrivalDate));
            titleLabel.setFont(Fonts.getBoldBodyFont());
            maingp.add(titleLabel, 0, 0, 4, 1);
            GridPane.setHalignment(titleLabel, HPos.LEFT);
            GridPane.setMargin(titleLabel, new Insets(10, 0, 5, 0));

        }
        this.getChildren().add(maingp);

    }

    public double getAddressItemHeight() {
        if (date == null) {
            return addressItemHeightWithoutTour;
        }
        if (requestNumber >= 0) {
            return depotHeight;
        }
        return addressItemHeightWithTour;
    }

    public long getRequestNumber() {
        return requestNumber;
    }


    public String getType() {
        return type;
    }

    public int getStepIndex(){
        return stepIndex;
    }

    public String getValue() {
        return this.value.getText();
    }

    public void setEditable(boolean b) {
        this.value.setEditable(b);
    }

    public void enforceHeight() {
        this.setPrefHeight(this.getAddressItemHeight());
    }

}
