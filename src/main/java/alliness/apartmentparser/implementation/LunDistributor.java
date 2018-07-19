package alliness.apartmentparser.implementation;

import alliness.apartmentparser.dto.AppConfig;
import alliness.apartmentparser.dto.Offer;
import alliness.apartmentparser.enums.DistributorResponseType;
import alliness.apartmentparser.enums.DistrictsEnum;
import alliness.apartmentparser.interfaces.DistributorMethodType;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LunDistributor extends BaseDistributor {

    public LunDistributor(AppConfig.Distributors config) {
        super(config);
    }

    @Override
    public HashMap<DistrictsEnum, URI> getQueries() {
        URI uri = null;

        try {
            uri = new URIBuilder(getConfig().getUrl()).setPath(getConfig().getPath()).build();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        URI finalUri = uri;
        return new HashMap<DistrictsEnum, URI>() {{
            put(DistrictsEnum.All, finalUri);
        }};
    }

    @Override
    public HashMap<String, String> getQueryMapping() {
        return null;
    }

    @Override
    public List<Offer> parse(Document document) {
        JSONObject       response   = new JSONObject(document.body().text());
        ArrayList<Offer> offersList = new ArrayList<>();

        for (Object o : response.getJSONObject("data").getJSONArray("items")) {
            JSONObject item  = (JSONObject) o;
            if(item.has("doShowPriceSqmAvg")){
                continue;
            }
            Offer      offer = new Offer();

            offer.setOfferId(String.valueOf(item.get("pageId")));
            offer.setTitle(item.getString("headerFromSource"));
            offer.setDate(item.getString("addTime"));
            offer.setLocation(item.getJSONObject("geo").getJSONObject("district").getString("name"));
            offer.setLink(getConfig().getUrl() + item.getString("singleRealtyUrl"));
            offer.setPrice(item.getString("price"));
            offer.setImage(item.getJSONObject("photo").getString("url"));
            offersList.add(offer);
        }

        return offersList;
    }

    @Override
    public DistributorResponseType getResponseType() {
        return DistributorResponseType.JSON;
    }

    @Override
    public DistributorMethodType getMethodType() {
        return DistributorMethodType.POST;
    }

    @Override
    public String getRequestData() {
        JSONArray rooms  = new JSONArray();
        JSONArray subway = new JSONArray();

        subway.put("19")
              .put("20")
              .put("21")
              .put("22")
              .put("23")
              .put("24")
              .put("25")
              .put("26")
              .put("27")
              .put("28")
              .put("29")
              .put("30")
              .put("48")
              .put("49")
              .put("50")
              .put("51")
              .put("52");

        for (int i = 0; i < Integer.parseInt(getConfig().getMaxRooms()); i++) {
            rooms.put(String.valueOf(i + 1));
        }


        return new JSONObject()
                .put("language", "ru")
                .put("searchParams", new JSONObject().put("city", "1")
                                                     .put("contractType", "2")
                                                     .put("page", "1")
                                                     .put("realtyType", "1")
                                                     .put("priceMax", getConfig().getMaxPrice()).put("roomCount", rooms)
                                                     .put("subway", subway)
                                                     .put("updateTime", getConfig().getOrder())

                )
                .toString();
    }
}
