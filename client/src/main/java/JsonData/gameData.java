package JsonData;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class gameData {
    public final String status;
    public final JsonNode game;
    public gameData(@JsonProperty("status") String status,
                @JsonProperty("game") JsonNode game)
 {
        this.status = status;
        this.game = game;
    }
}