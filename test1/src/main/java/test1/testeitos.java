package test1;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import JsonData.rankingData;

public class testeitos {
	public static void main(String[] args) {
		try {
			URL url = new URL("https://final-project-ams.herokuapp.com/usersAndPoints");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("accept", "application/json");
			InputStream responseStream = connection.getInputStream();
			ObjectMapper mapper = new ObjectMapper();
			rankingData gdmgdata = mapper.readValue(responseStream, rankingData.class);
			for (int i = 0; i < gdmgdata.users.size(); i++) {
				System.out.println(gdmgdata.users.get(i));
			}
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
