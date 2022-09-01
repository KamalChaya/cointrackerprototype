package trackerservice.apis;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import trackerservice.clients.BlockchainClient;
import trackerservice.dbmodels.ApiOffset;
import trackerservice.dbmodels.Balance;
import trackerservice.dbmodels.Transaction;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Path("/address")
public class AddBtcAddressResourceImpl implements AddBtcAddressResource{
    private final int MIN_BTC_ADDRESS_LENGTH = 25;
    private final int MAX_BTC_ADDRESS_LENGTH = 34;

    private final BlockchainClient blockchainClient;
    private final DynamoDbTable<Balance> balanceDynamoDbTable;
    private final DynamoDbTable<ApiOffset> apiOffsetDynamoDbTable;
    private final DynamoDbTable<Transaction> transactionDynamoDbTable;

    @Path("/{btcAddress}")
    @PUT
    @Override
    public Response addAddress(@PathParam("btcAddress") @NotBlank @NotNull String btcAddress) {
        if (!validBtcAddress(btcAddress)) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "BTC addresses must be from 25 - 34 characters" +
                    "must start with a 1, and not contain O, 0, I, l").build();
        }

        try {
            Long satoshiBalance = blockchainClient.getSatoshiBalance(btcAddress);
            
        } catch (URISyntaxException | IOException | DynamoDbException e) {
            log.error("Error adding address", e);
        }

        return Response.ok().build();
    }

    /*
        Followed the valid address rules from here:
        https://en.bitcoinwiki.org/wiki/Bitcoin_address#What.27s_in_a_Bitcoin_address
     */
    private boolean validBtcAddress(String btcAddress) {
        if (btcAddress.length() < MIN_BTC_ADDRESS_LENGTH || btcAddress.length() > MAX_BTC_ADDRESS_LENGTH) {
            return false;
        }

        char[] btcAddressChars = btcAddress.toCharArray();
        if (btcAddressChars[0] != '1' && btcAddressChars[0] != '3') {
            return false;
        }

        if (btcAddress.contains("0") || btcAddress.contains("O") || btcAddress.contains("I") || btcAddress.contains("l")) {
            return false;
        }

        return true;
    }



}
