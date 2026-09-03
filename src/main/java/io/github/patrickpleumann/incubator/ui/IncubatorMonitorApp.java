package io.github.patrickpleumann.incubator.ui;

import io.github.patrickpleumann.incubator.device.Incubator;
import io.github.patrickpleumann.incubator.device.SimulatedTemperatureSource;
import io.github.patrickpleumann.incubator.device.TemperatureSampler;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.Duration;
import java.util.Random;

/**
 * Entry point of the application.
 */
public class IncubatorMonitorApp extends Application {

    private static final String WINDOW_TITLE = "Incubator Monitor";
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private MonitorView monitorView;
    private final Incubator incubator = new Incubator(37.0,0.5);
    private final TemperatureSampler  sampler = new TemperatureSampler
            (incubator, new SimulatedTemperatureSource(new Random()), Duration.ofMillis(500));

    @Override
    public void start(Stage stage)
    {
        monitorView = new MonitorView();

        monitorView.setTemperature(incubator.getCurrentTemperature());
        monitorView.setWithinTolerance(incubator.isWithinTolerance());
        monitorView.setOnTargetSubmitted(incubator::setTargetTemperature);
        monitorView.setOnSimulationToggled(this::toggleSimulation);
        monitorView.setSimulationRunning(sampler.isRunning());

        stage.setTitle(WINDOW_TITLE);
        stage.setScene(new Scene(monitorView.getRoot(),WINDOW_WIDTH ,WINDOW_HEIGHT));
        stage.show();
    }

    private void toggleSimulation()
    {
        if(sampler.isRunning())
            sampler.stop();

        else
            sampler.start();

        monitorView.setSimulationRunning(sampler.isRunning());
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
