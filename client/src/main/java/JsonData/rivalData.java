package JsonData;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class rivalData {
    public final String status;
    public final String weaponR;
    public rivalData(@JsonProperty("Status") String status,
                @JsonProperty("weapon") String weaponR)
 {
        this.status = status;
        this.weaponR = weaponR;
    }
}