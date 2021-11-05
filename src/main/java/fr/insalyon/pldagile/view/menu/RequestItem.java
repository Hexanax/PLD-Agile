package fr.insalyon.pldagile.view.menu;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestItem extends Region {
    private long requestNumber;
    private String type;
    private int stepIndex;
    private TextField value;

    public RequestItem(Date arrivalDate, int duration, long requestNumber, String type, int stepIndex, boolean editable) {

        //TODO test if its a depot

        this.type = type;
        this.requestNumber = requestNumber;
        this.stepIndex = stepIndex;

        GridPane maingp = new GridPane();
        maingp.setAlignment(Pos.BASELINE_CENTER);
        maingp.setPadding(new Insets(5, 5, 5, 5));
        maingp.setHgap(5);
        maingp.setVgap(5);

        DateFormat dateFormat = new SimpleDateFormat("HH 'h' mm");

        if(requestNumber >= 0) {
            Label titleLabel = new Label(type + " n°" + (requestNumber + 1));
            titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            maingp.add(titleLabel, 0, 0, 4, 1);
            GridPane.setHalignment(titleLabel, HPos.LEFT);
            GridPane.setMargin(titleLabel, new Insets(10, 0, 5, 0));


            if(!editable){
                Label arrivalLabel = new Label("Arrival time : " + dateFormat.format(arrivalDate));
                titleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
                maingp.add(arrivalLabel, 0, 1, 4, 1);
                GridPane.setHalignment(arrivalLabel, HPos.LEFT);
                GridPane.setMargin(arrivalLabel, new Insets(5, 5, 5, 0));
            }

            Label timeLabel = new Label(type + " duration : "); //TODO Display time: dateFormat.format(pickupTime)
            timeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
            maingp.add(timeLabel, 0, 2, 1, 1);
            GridPane.setHalignment(timeLabel, HPos.LEFT);
            GridPane.setMargin(timeLabel, new Insets(5, 5, 5, 0));


            value = new TextField(String.valueOf(duration));
            value.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
            maingp.add(value, 1, 2, 1, 1);
            GridPane.setHalignment(value, HPos.LEFT);
            GridPane.setMargin(value, new Insets(5, 5, 5, 0));
            value.setEditable(false);
            value.setDisable(false);

            Label unity = new Label(" seconds");
            unity.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
            maingp.add(unity, 2, 2, 1, 1);
            GridPane.setHalignment(unity, HPos.LEFT);
            GridPane.setMargin(unity, new Insets(5, 5, 5, 0));


            if(editable){
                value.setEditable(true);
            }
        } else {
            //depot
            Label titleLabel = new Label(type + " - " + dateFormat.format(arrivalDate));
            titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            maingp.add(titleLabel, 0, 0, 4, 1);
            GridPane.setHalignment(titleLabel, HPos.LEFT);
            GridPane.setMargin(titleLabel, new Insets(10, 0, 5, 0));

        }

        this.getChildren().add(maingp);
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


}
