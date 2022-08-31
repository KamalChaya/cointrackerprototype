package trackerservice.clients;

import com.google.inject.Inject;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.glassfish.jersey.client.JerseyClient;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class BlockChainInfoAPIClient implements BlockchainClient {

  private final HttpClient httpClient;

  private static final String HOST = "blockchain.info";
  private static final String BALANCE_PATH = "balance";
  private static final String BALANCE_QUERY_PARAM = "active";

  private static final String FINAL_BALANCE_RESPONSE_KEY = "final_balance";

  @Override
  public Long getBalance(String btcAddress) throws URISyntaxException, IOException {
    HttpGet httpGet = new HttpGet(new URIBuilder()
        .setHost(HOST)
        .setScheme("https")
        .setPath(BALANCE_PATH)
        .addParameter(BALANCE_QUERY_PARAM, btcAddress)
        .build());

    HttpResponse response = httpClient.execute(httpGet);

    JSONObject responseJson =
        (JSONObject) JSONValue.parse(EntityUtils.toString(response.getEntity()));
    JSONObject addressJson = (JSONObject) responseJson.get(btcAddress);
    return (Long) addressJson.get(FINAL_BALANCE_RESPONSE_KEY);
  }
}
