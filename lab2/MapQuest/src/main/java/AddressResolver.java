

import org.apache.http.client.utils.URIBuilder;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.io.IOException;
import java.net.URISyntaxException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AddressResolver {
    URIBuilder uriBuilder;
    TqsHttpClient httpClient;

    public AddressResolver() {
        this.httpClient = new TqsHttpBasic();
    }




    public Address findAddressForLocation(double lat, double lng) throws URISyntaxException, IOException, ParseException {
        uriBuilder= new URIBuilder("http://open.mapquestapi.com/geocoding/v1/reverse");

        uriBuilder.addParameter("key","onVNuhocy65IDvmu6t0KZGs2ashvtWzF");
        String location = String.format("%.6f,%.6f", lat,lng);
        uriBuilder.addParameter("location",location);
        uriBuilder.addParameter("includeRoadMetadata", "true");

        System.out.println(uriBuilder.build().toString());
        JSONObject data = (JSONObject) new JSONParser().parse(httpClient.get(uriBuilder.build().toString()));
        data = (JSONObject) ((JSONArray)((JSONObject) ((JSONArray) data.get("results")).get(0))
                .get("locations")).get(0);
        return new Address(data.get("street").toString(),data.get("adminArea5").toString(),
                data.get("adminArea3").toString(),data.get("postalCode").toString(),"");
    }


}
