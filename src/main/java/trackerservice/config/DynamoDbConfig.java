package trackerservice.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class DynamoDbConfig {
    @NotBlank
    private String balanceTable;

    @NotBlank
    private String transactionsTable;

    @NotBlank
    private String apiOffsetTable;

    @JsonProperty
    public String getApiOffsetTable() {
        return this.apiOffsetTable;
    }

    @JsonProperty
    public void setApiOffsetTable(String apiOffsetTable) {
        this.apiOffsetTable = apiOffsetTable;
    }

    @JsonProperty
    public void setBalanceTable(String balanceTable) {
        this.balanceTable = balanceTable;
    }

    @JsonProperty
    public String getBalanceTable() {
        return balanceTable;
    }

    @JsonProperty
    public String getTransactionsTable() {
        return this.transactionsTable;
    }

    @JsonProperty
    public void setTransactionsTable(String transactionsTable) {
        this.transactionsTable = transactionsTable;
    }
}
