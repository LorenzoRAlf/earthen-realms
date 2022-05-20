package JsonData;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class rankingData {
    public final String status;
    public final JsonNode users;
    public rankingData(@JsonProperty("status") String status,
                @JsonProperty("userList") JsonNode users)
 {
        this.status = status;
        this.users = users;
    }
}