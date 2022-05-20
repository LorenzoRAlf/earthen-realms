package JsonData;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class armorData {
    public final String status;
    public final JsonNode alist;
    public armorData(@JsonProperty("status") String status,
                @JsonProperty("armor_list") JsonNode alist)
 {
        this.status = status;
        this.alist = alist;
    }
}