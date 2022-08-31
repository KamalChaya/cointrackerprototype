package trackerservice.guice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.mockito.Mock;
import org.mockito.Mockito;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import trackerservice.clients.BlockChainInfoAPIClient;
import trackerservice.clients.BlockchainClient;
import trackerservice.dbmodels.Balance;

@SuppressWarnings("unchecked")
public class TestModule extends AbstractModule {
  public static String EMPTY_RESP_ADDRESS = "3G4ARGjm2pmpxvFRRMZ7Y2CSwtF52oxNfK";
  public static String INVALID_RESP_ADDRESS = "3G4ARGjm2pmpxvFRRMZ7Y2CSwtF52oxN11";
  public static String VALID_RESP_ADDRESS = "3G4ARGjm2pmpxvFRRMZ7Y2CSwtF52oxN12";

  private static String VALID_ADDRESS_JSON_RESP = "{\n"
      + "    \"3G4ARGjm2pmpxvFRRMZ7Y2CSwtF52oxN12\": {\n"
      + "        \"final_balance\": 1229874,\n"
      + "        \"n_tx\": 0,\n"
      + "        \"total_received\": 0\n"
      + "    }\n"
      + "}";
  private static String INVALID_ADDRESS_JSON_RESP = "{\n"
      + "    \"3G4ARGjm2pmpxvFRRMZ7Y2CSwtF52oxNfK\": {\n"
      + "        \"final_balance\": 0,\n"
      + "        \"n_tx\": 0,\n"
      + "        \"total_received\": 0\n"
      + "    }\n"
      + "}";

  @Override
  public void configure() {
    bind(new TypeLiteral<DynamoDbTable<Balance>>() {})
        .toInstance((DynamoDbTable<Balance>) Mockito.mock(DynamoDbTable.class));
  }

  private HttpResponse createMockResponse(String jsonString) throws IOException {
    HttpEntity mockHttpEntity = mock(HttpEntity.class);
    when(mockHttpEntity.getContent()).thenReturn(new ByteArrayInputStream(jsonString.getBytes()));
    HttpResponse mockHttpResponse = mock(HttpResponse.class);
    when(mockHttpResponse.getEntity()).thenReturn(mockHttpEntity);
    return mockHttpResponse;
  }

  @Provides
  @Singleton
  public HttpClient getMockHttpClient() throws IOException {
    HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
    when(mockHttpClient.execute(any(HttpGet.class))).thenAnswer(args -> {
      HttpGet httpGet = args.getArgument(0);
      System.out.println(httpGet.getURI().getQuery());
      String address = httpGet.getURI().getQuery().split("=")[1];

      if (address.equals(VALID_RESP_ADDRESS)) {
        return createMockResponse(VALID_ADDRESS_JSON_RESP);
      } else if (address.equals(INVALID_RESP_ADDRESS)) {
        return createMockResponse(INVALID_ADDRESS_JSON_RESP);
      } else {
        return createMockResponse("{}");
      }
    });

    return mockHttpClient;
  }

  @Provides
  public BlockchainClient getMockBlockchainClient(HttpClient mockHttpClient) {
    return new BlockChainInfoAPIClient(mockHttpClient);
  }
}
