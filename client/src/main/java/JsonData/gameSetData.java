package JsonData;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class gameSetData {
    public final String status;
    public final JsonNode glist;
    public gameSetData(@JsonProperty("status") String status,
                @JsonProperty("game") JsonNode glist)
 {
        this.status = status;
        this.glist = glist;
    }
}