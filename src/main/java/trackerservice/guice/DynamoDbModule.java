package trackerservice.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import trackerservice.config.TrackerServiceConfig;
import trackerservice.dbmodels.Balance;

@RequiredArgsConstructor
public class DynamoDbModule extends AbstractModule {
    private final TrackerServiceConfig config;

    @Provides
    @Singleton
    public DynamoDbEnhancedClient getDynamoClient() {
        DynamoDbClient dynamoDbClient =
                DynamoDbClient.builder().region(Region.US_EAST_1).build();
        return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
    }

    @Provides
    public DynamoDbTable<Balance> getBalanceTable(DynamoDbEnhancedClient dynamoEnhancedClient) {
        dynamoEnhancedClient.table(config.getDynamoDbProd().)
    }
}
