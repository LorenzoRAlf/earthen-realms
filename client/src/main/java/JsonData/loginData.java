package JsonData;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class loginData {
    public final String status, sessionToken, message;
    public final JsonNode user;
    public loginData(@JsonProperty("status") String status,
                @JsonProperty("session_token") String sessionToken,
                @JsonProperty("user") JsonNode user,
                @JsonProperty("message") String message)
 {
        this.status = status;
        this.sessionToken = sessionToken;
        this.user = user;
        this.message = message;
    }
}
