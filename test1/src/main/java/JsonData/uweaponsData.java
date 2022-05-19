package JsonData;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class uweaponsData {
    public final String status;
    public final JsonNode wlist;
    public uweaponsData(@JsonProperty("status") String status,
                @JsonProperty("weapons") JsonNode wlist)
 {
        this.status = status;
        this.wlist = wlist;
    }
}