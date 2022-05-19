package JsonData;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class weaponsData {
    public final String status;
    public final JsonNode wlist;
    public weaponsData(@JsonProperty("status") String status,
                @JsonProperty("weapon_list") JsonNode wlist)
 {
        this.status = status;
        this.wlist = wlist;
    }
}