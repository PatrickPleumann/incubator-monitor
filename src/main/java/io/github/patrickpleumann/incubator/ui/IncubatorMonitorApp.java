package io.github.patrickpleumann.incubator.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Entry point of the application. For now it only opens an empty window.
 */
public class IncubatorMonitorApp extends Application {

    private static final String WINDOW_TITLE = "Incubator Monitor";
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private MonitorView monitorView;

    @Override
    public void start(Stage stage)
    {
        monitorView = new MonitorView();

        stage.setTitle(WINDOW_TITLE);
        stage.setScene(new Scene(monitorView.getRoot(),WINDOW_WIDTH ,WINDOW_HEIGHT));

        stage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
