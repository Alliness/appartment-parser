package alliness.apartmentparser.implementation;

import alliness.apartmentparser.dto.AppConfig;
import alliness.apartmentparser.dto.Offer;
import alliness.apartmentparser.enums.DistrictsEnum;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RiaDistributor extends BaseDistributor {


    public RiaDistributor(AppConfig.Distributors config) throws IOException {
        super(config);
    }

    @Override
    public HashMap<DistrictsEnum, URI> getQueries() {
        try {
            HashMap<DistrictsEnum, URI> map = new HashMap<>();

            if (getConfig().getDistricts() != null) {
                for (String s : getConfig().getDistricts()) {
                    URIBuilder builder = new URIBuilder(getConfig().getUrl());
                    builder.setPath(getConfig().getPath());

                    setParams(builder, getQueryMapping());

                    map.put(DistrictsEnum.valueOf(s),
                            builder.setParameter(
                                    String.format("district_id[%s]", DistrictsEnum.valueOf(s).riaRegionId),
                                    String.valueOf(DistrictsEnum.valueOf(s).riaRegionId))
                                   .build());
                }
            }
            return map;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HashMap<String, String> getQueryMapping() {
        return new HashMap<String, String>() {{
            put("category", "1");
            put("realty_type", "2");
            put("operation_type", "3");
            put("state_id", "10");
            put("city_id[10]", "10");
            put("characteristic[209][from]", getConfig().getMinRooms());
            put("characteristic[209][to]", getConfig().getMaxRooms());
            put("characteristic[235][from]", getConfig().getMinPrice());
            put("characteristic[235][to]", getConfig().getMaxPrice());
            put("sort", "inspected_sort");
            put("city_id[15]", "10");
            put("roomss", "on");
            put("period", getConfig().getOrder());
        }};
    }

    //https://dom.ria.com/node/searchEngine/v2/view/realty/14434267?lang_id=2
    @Override
    public List<Offer> parse(Document document) {
        JSONObject response = new JSONObject(document.body().text());
        ArrayList<Offer> offersList = new ArrayList<>();

        try {

            for (Object item : response.getJSONArray("items")) {

                URI rq = new URIBuilder(getConfig().getUrl())
                        .setPath(String.format("%s/view/realty/%s", getConfig().getPath(), item))
                        .setParameter("lang_id", "2")
                        .build();

                String raw = Jsoup.connect(String.valueOf(rq))
                                  .ignoreContentType(true)
                                  .header("Content-type", "application/json")
                                  .get()
                                  .body()
                                  .text();

                Offer       offer    = new Offer();
                JSONObject  result   = new JSONObject(raw);

                offer.setOfferId(result.getString("_id").replace("realty-", ""));
                offer.setTitle(String.format("%s, %s, %s-к.",
                        result.getString("city_name"),
                        result.getString("street_name"),
                        result.getInt("rooms_count")
                ));
                offer.setDate(result.getString("created_at"));
                offer.setLocation(result.getString("district_name"));
                offer.setLink(String.format("%s/ru/%s",
                        getConfig().getUrl(),
                        result.getString("beautiful_url")
                ));

                offer.setPrice(String.valueOf(result.getInt("price")));
                offersList.add(offer);
            }
        } catch (URISyntaxException | IOException e) {
            log.error(e.getMessage(), e);
            System.exit(-1);
        } catch (JSONException ignored) {
        }

        return offersList;

    }

}
