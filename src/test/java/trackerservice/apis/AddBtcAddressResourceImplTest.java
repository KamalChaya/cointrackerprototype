package trackerservice.apis;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.inject.Guice;
import com.google.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.junit.Assert;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import trackerservice.clients.BlockchainClient;
import trackerservice.dbmodels.ApiOffset;
import trackerservice.dbmodels.Balance;
import trackerservice.dbmodels.Transaction;
import trackerservice.guice.TestModule;

public class AddBtcAddressResourceImplTest {

  @Inject private BlockchainClient mockBlockChainInfoClient;
  @Inject private HttpClient mockHttpClient;
  @Inject private DynamoDbTable<Balance> mockBalanceTable;
  @Inject private DynamoDbTable<ApiOffset> mockApiOffsetTable;
  @Inject private DynamoDbTable<Transaction> mockTransactionTable;
  private AddBtcAddressResource addBtcAddressResource;

  @BeforeEach
  public void before() {
    Guice.createInjector(new TestModule()).injectMembers(this);
    addBtcAddressResource = new AddBtcAddressResourceImpl(
            mockBlockChainInfoClient,
            mockBalanceTable,
            mockApiOffsetTable,
            mockTransactionTable);
  }

  @Test
  public void testInvalidBtcAddress() {
    String tooShortAddress = "sdafs";
    Response tooShortResponse = addBtcAddressResource.addAddress(tooShortAddress);
    Assert.assertEquals(tooShortResponse.getStatus(), Status.BAD_REQUEST.getStatusCode());

    String tooLongAddress = "awefewfwefewfawefaewf21211312321rf21ed2d12e";
    Response tooLongResponse = addBtcAddressResource.addAddress(tooLongAddress);
    Assert.assertEquals(tooLongResponse.getStatus(), Status.BAD_REQUEST.getStatusCode());
  }

  @Test
  public void testEmptyBtcAddress() {
    Response emptyAddressResp = addBtcAddressResource.addAddress("");
    Assert.assertEquals(emptyAddressResp.getStatus(), Status.BAD_REQUEST.getStatusCode());

    Response blankAddressResp = addBtcAddressResource.addAddress("   ");
    Assert.assertEquals(blankAddressResp.getStatus(), Status.BAD_REQUEST.getStatusCode());
  }

  @Test
  public void testErrorBlockchainClient() {
    Response invalidAddressResp =
        addBtcAddressResource.addAddress(TestModule.INVALID_RESP_ADDRESS);
    Assert.assertEquals(invalidAddressResp.getStatus(),
        Status.INTERNAL_SERVER_ERROR.getStatusCode());
  }

  @Test
  public void testDynamoError() {
    doThrow(DynamoDbException.builder().build())
        .when(mockBalanceTable)
        .putItem(any(Balance.class));
    Response response = addBtcAddressResource.addAddress(TestModule.VALID_RESP_ADDRESS);
    Assert.assertEquals(response.getStatus(),
        Status.INTERNAL_SERVER_ERROR.getStatusCode());
  }

  @Test
  public void testCallBlockchainAndDynamo() throws URISyntaxException, IOException {
    Response response =
        addBtcAddressResource.addAddress(TestModule.VALID_RESP_ADDRESS);

    Assert.assertEquals(response.getStatus(), Status.OK.getStatusCode());

    verify(mockBalanceTable, times(1)).putItem(any(Balance.class));
    verify(mockHttpClient, times(1)).execute(any(HttpGet.class));
  }
}
