package fr.insalyon.pldagile.view.menu;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestItem extends Region {
    private int requestNumber;
    private Button destroy;
    private Button up;
    private Button down;

    public RequestItem(String requestName, String requestCommentary, long requestNumber) {

        GridPane maingp = new GridPane();
        maingp.setAlignment(Pos.BASELINE_CENTER);
        maingp.setPadding(new Insets(5, 5, 5, 5));
        maingp.setHgap(5);
        maingp.setVgap(5);

        Label titleLabel = new Label(requestName);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        maingp.add(titleLabel, 0,0,4,1);
        GridPane.setHalignment(titleLabel, HPos.LEFT);
        GridPane.setMargin(titleLabel, new Insets(10, 0,5,0));

        DateFormat dateFormat = new SimpleDateFormat("HH'h'mm");

        Label timeLabel = new Label(requestCommentary); //TODO Display time: dateFormat.format(pickupTime)
        timeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
        maingp.add(timeLabel, 0,1,1,1);
        GridPane.setHalignment(timeLabel, HPos.LEFT);
        GridPane.setMargin(timeLabel, new Insets(5, 5,5,0));

        Label requestLabel = new Label("Request n°" + requestNumber);
        requestLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
        maingp.add(requestLabel, 0,2,1,1);
        GridPane.setHalignment(requestLabel, HPos.LEFT);
        GridPane.setMargin(requestLabel, new Insets(5, 5,5,0));

        /*up = new Button("^");
        up.setPrefHeight(30);
        up.setPrefWidth(30);
        maingp.add(up, 1, 1, 1, 2);
        GridPane.setHalignment(up, HPos.CENTER);
        GridPane.setMargin(up, new Insets(0, 5,0,5));

        down = new Button("v");
        down.setPrefHeight(30);
        down.setPrefWidth(30);
        maingp.add(down, 2, 1, 1, 2);
        GridPane.setHalignment(down, HPos.CENTER);
        GridPane.setMargin(down, new Insets(0, 5,0,5));

        destroy = new Button("destroy");
        destroy.setPrefHeight(30);
        destroy.setPrefWidth(30);
        maingp.add(destroy, 3, 1, 1, 2);
        GridPane.setHalignment(destroy, HPos.CENTER);
        GridPane.setMargin(destroy, new Insets(0, 0,0,5));*/

        this.getChildren().add(maingp);

    }


}
