package trackerservice.guice;

import com.google.inject.AbstractModule;
import io.dropwizard.setup.Environment;
import lombok.RequiredArgsConstructor;
import trackerservice.config.TrackerServiceConfig;

@RequiredArgsConstructor
public class TrackerServiceAppModule extends AbstractModule {
    private final TrackerServiceConfig trackerServiceConfig;
    private final Environment environment;

    @Override
    public void configure() {

    }
}
