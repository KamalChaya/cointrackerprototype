package trackerservice.guice;

import com.google.inject.AbstractModule;
import io.dropwizard.setup.Environment;
import lombok.RequiredArgsConstructor;
import trackerservice.config.TrackerServiceConfig;

@RequiredArgsConstructor
public class ResourceModule extends AbstractModule {
    private final TrackerServiceConfig config;
    private final Environment environment;

    @Override
    public void configure() {
        install(new DynamoDbModule(config));
        install(new BlockchainClientModule(config, environment));


    }
}
