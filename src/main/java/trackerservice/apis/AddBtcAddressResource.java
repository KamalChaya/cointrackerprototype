package trackerservice.apis;

import javax.ws.rs.core.Response;

public interface AddBtcAddressResource {
    Response addAddress(String btcAddress);
}
