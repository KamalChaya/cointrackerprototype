package trackerservice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import trackerservice.apis.AddBtcAddressResource;
import trackerservice.config.TrackerServiceConfig;
import trackerservice.guice.TrackerServiceAppModule;

public class TrackerServiceApp extends Application<TrackerServiceConfig> {
    public static void main(String... args) throws Exception {
        new TrackerServiceApp().run(args);
    }

    @Override
    public void run(TrackerServiceConfig configuration, Environment environment) throws Exception {
        Injector injector =
                Guice.createInjector(new TrackerServiceAppModule(configuration, environment));
        environment.jersey().register(injector.getInstance(AddBtcAddressResource.class));
    }
}
