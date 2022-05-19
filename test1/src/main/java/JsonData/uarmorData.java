package JsonData;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class uarmorData {
    public final String status;
    public final JsonNode alist;
    public uarmorData(@JsonProperty("status") String status,
                @JsonProperty("armors") JsonNode alist)
 {
        this.status = status;
        this.alist = alist;
    }
}