package trackerservice.guice;

import com.google.inject.AbstractModule;
import io.dropwizard.setup.Environment;
import lombok.RequiredArgsConstructor;
import trackerservice.apis.AddBtcAddressResource;
import trackerservice.apis.AddBtcAddressResourceImpl;
import trackerservice.config.TrackerServiceConfig;

@RequiredArgsConstructor
public class TrackerServiceAppModule extends AbstractModule {
    private final TrackerServiceConfig trackerServiceConfig;
    private final Environment environment;

    @Override
    public void configure() {
        install(new DynamoDbModule(trackerServiceConfig));
        install(new BlockchainClientModule(trackerServiceConfig, environment));

        bind(AddBtcAddressResource.class).to(AddBtcAddressResourceImpl.class);
    }
}
