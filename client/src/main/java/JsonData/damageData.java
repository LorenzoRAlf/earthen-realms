package JsonData;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class damageData {
    public final String status;
    public final int damageTaken;
    public damageData(@JsonProperty("status") String status,
                @JsonProperty("playerDamage") int damageTaken)
 {
        this.status = status;
        this.damageTaken = damageTaken;
    }
}