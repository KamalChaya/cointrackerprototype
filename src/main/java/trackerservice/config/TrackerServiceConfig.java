package trackerservice.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

public class TrackerServiceConfig extends Configuration {
    @NotNull
    private DynamoDbConfig dynamoDbProd;

    @JsonProperty
    public DynamoDbConfig getDynamoDbProd() {
        return this.dynamoDbProd;
    }

    @JsonProperty
    public void setDynamoDbProd(DynamoDbConfig dynamoDbProd) {
        this.dynamoDbProd = dynamoDbProd;
    }
}
