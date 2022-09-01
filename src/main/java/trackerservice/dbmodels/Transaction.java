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
  private Long satoshiFee;
  private List<TransactionRecipient> recipients;

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

  @DynamoDbAttribute("satoshi_fee")
  public long getSatoshiFee() {
    return this.satoshiFee;
  }

  public void setSatoshiFee(Long satoshiFee) {
    this.satoshiFee = satoshiFee;
  }

  @DynamoDbAttribute("transaction_recipients")
  public List<TransactionRecipient> getTransactionRecipients() {
    return this.recipients;
  }

  public void setTransactionRecipients(List<TransactionRecipient> recipients) {
    this.recipients = recipients;
  }
}
