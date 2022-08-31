package trackerservice.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.setup.Environment;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.HttpClient;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import trackerservice.clients.BlockChainInfoAPIClient;
import trackerservice.clients.BlockchainClient;
import trackerservice.config.TrackerServiceConfiguration;

@RequiredArgsConstructor
public class BlockchainClientModule extends AbstractModule {

  private final TrackerServiceConfiguration config;
  private final Environment environment;

  @Singleton
  @Provides
  public HttpClient getHttpClient() {
    return new HttpClientBuilder(environment)
        .using(config.getHtpClientConfiguration())
        .build(environment.getName());
  }

  @Override
  public void configure() {
    bind(BlockchainClient.class).to(BlockChainInfoAPIClient.class);
  }
}
