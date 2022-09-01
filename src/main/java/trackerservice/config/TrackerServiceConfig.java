package trackerservice.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.HttpClientConfiguration;
import org.apache.http.client.HttpClient;

import javax.validation.constraints.NotNull;

public class TrackerServiceConfig extends Configuration {
    @NotNull
    private DynamoDbConfig dynamoDbProd;

    @NotNull
    private HttpClientConfiguration httpClient;

    @JsonProperty
    public DynamoDbConfig getDynamoDbProd() {
        return this.dynamoDbProd;
    }

    @JsonProperty
    public void setDynamoDbProd(DynamoDbConfig dynamoDbProd) {
        this.dynamoDbProd = dynamoDbProd;
    }

    @JsonProperty
    public HttpClientConfiguration getHttpClient() {
        return this.httpClient;
    }

    @JsonProperty
    public void setHttpClient(HttpClientConfiguration httpClient) {
        this.httpClient = httpClient;
    }
}
