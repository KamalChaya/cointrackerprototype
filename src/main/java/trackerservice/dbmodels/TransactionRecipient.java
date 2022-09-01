package trackerservice.dbmodels;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbFlatten;

@DynamoDbBean
public class TransactionRecipient {
  private String recipientAddress;
  private Long satoshisSent;

  @DynamoDbAttribute("recipient_address")
  public String getRecipientAddress() {
    return this.recipientAddress;
  }

  public void setRecipientAddress(String recipientAddress) {
    this.recipientAddress = recipientAddress;
  }

  @DynamoDbAttribute("satoshis_sent")
  public Long getSatoshisSent() {
    return this.satoshisSent;
  }

  public void setSatoshisSent(Long satoshisSent) {
    this.satoshisSent = satoshisSent;
  }
}
