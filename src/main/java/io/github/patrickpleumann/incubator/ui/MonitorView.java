package io.github.patrickpleumann.incubator.ui;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MonitorView
{
    private static final String HEADER_TITLE = "Incubator Monitor";
    private static final String HEADER_COLOR = "#3C1192";

    private final BorderPane root;

    public MonitorView()
    {
        root = new BorderPane();
        root.setTop(createHeader());
    }

    private Label createHeader()
    {
        Label header = new Label(HEADER_TITLE);
        header.setFont(Font.font("System", FontWeight.BOLD, 16));
        header.setTextFill(Color.WHITE);
        header.setStyle("-fx-background-color: " + HEADER_COLOR + ";");
        header.setMaxWidth(Double.MAX_VALUE);
        header.setPadding(new Insets(12));
        return header;
    }

    public Parent getRoot()
    {
        return root;
    }
    public void setTemperature()
    {

    }
}
