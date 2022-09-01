package trackerservice.dbmodels;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class ApiOffset {
    private String btcAddress;
    private int apiOffset;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("btc_address")
    public String getBtcAddress() {
        return this.btcAddress;
    }

    public void setBtcAddress(String btcAddress) {
        this.btcAddress = btcAddress;
    }

    @DynamoDbAttribute("blockchain_info_offset")
    public int getApiOffset() {
        return this.apiOffset;
    }

    public void setApiOffset(int apiOffset) {
        this.apiOffset = apiOffset;
    }
}
