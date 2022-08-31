package trackerservice.clients;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;

public interface BlockchainClient {
  Long getBalance(String btcAddress) throws URISyntaxException, IOException;
}
