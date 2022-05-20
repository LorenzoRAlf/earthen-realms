package JsonData;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class coinsData {
    public final String status;
    public final int coins;
    public coinsData(@JsonProperty("status") String status,
                @JsonProperty("message") int coins)
 {
        this.status = status;
        this.coins = coins;
    }
}