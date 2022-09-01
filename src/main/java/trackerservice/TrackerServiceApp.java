package trackerservice;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import trackerservice.config.TrackerServiceConfig;

public class TrackerServiceApp extends Application<TrackerServiceConfig> {
    public static void main(String... args) throws Exception {
        new TrackerServiceApp().run(args);
    }

    @Override
    public void run(TrackerServiceConfig configuration, Environment environment) throws Exception {

    }
}
