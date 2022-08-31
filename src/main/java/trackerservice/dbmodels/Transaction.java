package trackerservice.dbmodels;

import java.util.List;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class Transaction {
  private String btcAddress;
  private int blockchainApiOffset;
  private long satoshisSent;
  private long satoshiFee;
  private List<String> destinationAddresses;

  @DynamoDbPartitionKey
  @DynamoDbAttribute("btc_address")
  public String getBtcAddress() {
    return this.btcAddress;
  }

  public void setBtcAddress(String btcAddress) {
    this.btcAddress = btcAddress;
  }

  @DynamoDbSortKey
  @DynamoDbAttribute("blockchaininfo_api_offset")
  public int getBlockchainApiOffset() {
    return this.blockchainApiOffset;
  }

  public void setBlockchainApiOffset(int blockchainApiOffset) {
    this.blockchainApiOffset = blockchainApiOffset;
  }

  @DynamoDbAttribute("satoshis_sent")
  public long getSatoshisSent() {
    return this.satoshisSent;
  }

  public void setSatoshisSent(long satoshisSent) {
    this.satoshisSent = satoshisSent;
  }

  @DynamoDbAttribute("satoshi_fee")
  public long getSatoshiFee() {
    return this.satoshiFee;
  }

  public void setSatoshiFee(long satoshiFee) {
    this.satoshiFee = satoshiFee;
  }

  @DynamoDbAttribute("destination_address")
  public List<String> getDestinationAddress() {
    return this.destinationAddresses;
  }

  public void setDestinationAddress(List<String> destinationAddress) {
    this.destinationAddresses = destinationAddress;
  }
}
