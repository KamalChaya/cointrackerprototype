package trackerservice.clients;

import com.google.inject.Inject;
import io.dropwizard.setup.Bootstrap;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.util.EntityUtils;
import org.apache.thrift.scheme.SchemeFactory;
import org.glassfish.jersey.client.JerseyClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import trackerservice.dbmodels.Transaction;
import trackerservice.dbmodels.TransactionRecipient;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class BlockChainInfoAPIClient implements BlockchainClient {

  private final HttpClient httpClient;

  private static final String HTTPS_SCHEME = "https";
  private static final String HOST = "blockchain.info";
  private static final String BALANCE_PATH = "balance";
  private static final String BALANCE_QUERY_PARAM = "active";
  private static final String RAW_ADDRESS_PATH = "rawaddr";
  private static final String OFFSET_QUERY_PARAM = "offset";

  private static final String FINAL_BALANCE_RESPONSE_KEY = "final_balance";
  private static final String RAW_ADDR_TXS_RESPONSE_KEY = "txs";
  private static final String TRANSACTIONS_OUTPUTS_RESPONSE_KEY = "out";
  private static final String TRANSACTIONS_FEE_RESPONSE_KEY = "fee";
  private static final String OUTPUT_SPENT_RESPONSE_KEY = "spent";
  private static final String OUTPUT_VALUE_RESPONSE_KEY = "value";
  private static final String OUTPUT_RECIPIENT_ADDRESS_RESPONSE_KEY = "address";

  @Override
  public Long getSatoshiBalance(String btcAddress) throws URISyntaxException, IOException {
    HttpGet httpGet = new HttpGet(new URIBuilder()
        .setHost(HOST)
        .setScheme(HTTPS_SCHEME)
        .setPath(BALANCE_PATH)
        .addParameter(BALANCE_QUERY_PARAM, btcAddress)
        .build());

    HttpResponse response = httpClient.execute(httpGet);

    JSONObject responseJson =
        (JSONObject) JSONValue.parse(EntityUtils.toString(response.getEntity()));
    JSONObject addressJson = (JSONObject) responseJson.get(btcAddress);
    return (Long) addressJson.get(FINAL_BALANCE_RESPONSE_KEY);
  }

  @Override
  public List<Transaction> getTransactions(String btcAddress, int startOffset)
      throws URISyntaxException, IOException {
    URIBuilder getTransactionsUriBuilder = new URIBuilder()
        .setHost(HOST)
        .setScheme(HTTPS_SCHEME)
        .setPath(String.format("%s/%s", RAW_ADDRESS_PATH, btcAddress));

    int currOffset = startOffset;
    List<Transaction> finalTransactions = new ArrayList<>();

    /*
      Each call returns up to 50 transactions by default. Keep calling the API until
      we get all the transactions starting from the current offset. When we get an empty
      response, we know we got all the transactions.
     */
    while (true) {
      getTransactionsUriBuilder.addParameter(OFFSET_QUERY_PARAM, String.valueOf(currOffset));
      HttpGet httpGet = new HttpGet(getTransactionsUriBuilder.build());
      HttpResponse httpResponse = httpClient.execute(httpGet);

      JSONObject responseJson = (JSONObject)
          JSONValue.parse(EntityUtils.toString(httpResponse.getEntity()));


      JSONArray transactions = (JSONArray) responseJson.get(RAW_ADDR_TXS_RESPONSE_KEY);
      if (transactions.isEmpty()) {
        break;
      }

      /*
        For each transaction we get the fee, and set the offset of that transaction
        from the API response. The offset is needed so we don't re-read the same
        transactions from the API. For example, if transactions 0, 1, 2, 3 are returned,
        then the next time we call the API, we want to read from transaction 4 onwards.

        We also get all the spent outputs for each transaction so that we can save
        how much BTC was sent to each address.
       */
      for (int transactionIdx = 0;
          transactionIdx < transactions.size();
          transactionIdx++) {

        JSONObject transaction = (JSONObject) transactions.get(transactionIdx);
        Long satoshiFee = (Long) transaction.get(TRANSACTIONS_FEE_RESPONSE_KEY);
        JSONArray outputs = (JSONArray) transaction.get(TRANSACTIONS_OUTPUTS_RESPONSE_KEY);

        Transaction currTransaction = new Transaction();
        currTransaction.setBtcAddress(btcAddress);
        currTransaction.setSatoshiFee(satoshiFee);
        currTransaction.setBlockchainApiOffset(currOffset + transactionIdx);

        List<TransactionRecipient> recipients = new ArrayList<>();
        for (int outputIdx = 0; outputIdx < outputs.size(); outputIdx++) {
          JSONObject output = (JSONObject) outputs.get(outputIdx);
          Boolean notChangeAddress = (Boolean) output.get(OUTPUT_SPENT_RESPONSE_KEY);

          if (notChangeAddress) {
            Long satoshisSent = (Long) output.get(OUTPUT_VALUE_RESPONSE_KEY);
            String recipientAddress = (String) output.get(OUTPUT_RECIPIENT_ADDRESS_RESPONSE_KEY);
            TransactionRecipient transactionRecipient = new TransactionRecipient();
            transactionRecipient.setSatoshisSent(satoshisSent);
            transactionRecipient.setRecipientAddress(recipientAddress);

            recipients.add(transactionRecipient);
          }
        }
        finalTransactions.add(currTransaction);
        currTransaction.setTransactionRecipients(recipients);
      }
      currOffset += transactions.size();
    }

    return finalTransactions;
  }
}
