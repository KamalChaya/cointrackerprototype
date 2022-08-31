package trackerservice.dbmodels;

import java.math.BigInteger;
import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class Balance {
  private String btcAddress;
  private Long balance;

  @DynamoDbPartitionKey
  @DynamoDbAttribute("btc_address")
  public String getBtcAddress() {
    return this.btcAddress;
  }

  public void setBtcAddress(String btcAddress) {
    this.btcAddress = btcAddress;
  }

  @DynamoDbAttribute("balance")
  public Long getBalance() {
    return this.balance;
  }

  public void setBalance(Long balance) {
    this.balance = balance;
  }
}
