package JsonData;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class rivalDataArmor {
    public final String status;
    public final String armorR;
    public rivalDataArmor(@JsonProperty("Status") String status,
                @JsonProperty("armor") String armorR)
 {
        this.status = status;
        this.armorR = armorR;
    }
}