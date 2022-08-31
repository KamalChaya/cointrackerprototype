package trackerservice.clients;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.List;
import trackerservice.dbmodels.Transaction;

public interface BlockchainClient {
  Long getSatoshiBalance(String btcAddress) throws URISyntaxException, IOException;

  List<Transaction> getTransactions(String btcAddress, int startOffset)
      throws URISyntaxException, IOException;
}
