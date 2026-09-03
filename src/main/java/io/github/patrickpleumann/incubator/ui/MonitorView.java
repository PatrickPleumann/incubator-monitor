package io.github.patrickpleumann.incubator.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Locale;
import java.util.function.DoubleConsumer;

public class MonitorView
{
    private static final String HEADER_TITLE = "Incubator Monitor";
    private static final String HEADER_COLOR = "#3C1192";
    private static final Color STATUS_OK = Color.web("#2E7D32");
    private static final Color STATUS_WARN = Color.web("#FFBF00");


    private final BorderPane root;
    private final Label temperatureLabel = new Label();
    private final Circle statusDot = new Circle(10);
    private final Label statusLabel = new Label();
    private final TextField targetField = new TextField();
    private final Button applyButton = new Button("Apply");
    private DoubleConsumer targetSubmittedHandler;

    private final Button simulationButton = new Button();
    private Runnable simulationToggleHandler;

    public MonitorView()
    {
        root = new BorderPane();

        root.setTop(createHeader());

        root.setCenter(createDisplay());

        root.setBottom(createControls());
    }

    private Node createHeader()
    {
        Label header = new Label(HEADER_TITLE);
        header.setFont(Font.font("System", FontWeight.BOLD, 16));
        header.setTextFill(Color.BEIGE);
        header.setStyle("-fx-background-color: " + HEADER_COLOR + ";");
        header.setMaxWidth(Double.MAX_VALUE);
        header.setPadding(new Insets(12));
        return header;
    }

    private Node createDisplay()
    {
        temperatureLabel.setFont(Font.font(48));

        VBox vbox = new VBox(12);
        vbox.setAlignment(Pos.CENTER);

        HBox statusRow = new HBox(8);
        statusRow.setAlignment(Pos.CENTER);
        statusRow.getChildren().addAll(statusDot, statusLabel);

        vbox.getChildren().addAll(temperatureLabel, statusRow);
        return vbox;
    }
    private Node createControls()
    {
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(12));
        Label label = new Label("Target (°C)");
        applyButton.setOnAction(event -> submitTarget());
        simulationButton.setOnAction(event ->
        {
            if (simulationToggleHandler != null) simulationToggleHandler.run();
        });

        controls.getChildren().addAll(label,targetField, applyButton, simulationButton);
        return controls;
    }

    public void setWithinTolerance(boolean withinTolerance)
    {
        if(withinTolerance)
        {
            statusDot.setFill(STATUS_OK);
            statusLabel.setText("Within tolerance");
        }
        else
        {
            statusDot.setFill(STATUS_WARN);
            statusLabel.setText("Out of tolerance");
        }

    }

    public Parent getRoot()
    {
        return root;
    }

    public void setTemperature(double celsius)
    {
        temperatureLabel.setText(String.format(Locale.ROOT, "%.2f °C", celsius));
    }

    public void setOnTargetSubmitted(DoubleConsumer handler)
    {
        this.targetSubmittedHandler = handler;
    }

    private void submitTarget()
    {
        try
        {
            var text = targetField.getText();
            double value = Double.parseDouble(text);

            if(targetSubmittedHandler != null)
            {
                targetSubmittedHandler.accept(value);
            }
            targetField.setStyle("");
        }
        catch(IllegalArgumentException ex)
        {
            targetField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        }
    }

    public void setOnSimulationToggled(Runnable handler)
    {
        this.simulationToggleHandler = handler;
    }
    public void setSimulationRunning(boolean running)
    {
        if(running)
            simulationButton.setText("Stop");

        else
            simulationButton.setText("Start");
    }
}
