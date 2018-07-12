package alliness.apartmentparser.implementation;

import alliness.apartmentparser.dto.AppConfig;
import alliness.apartmentparser.dto.Offer;
import alliness.apartmentparser.enums.DistrictsEnum;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class OlxDistributor extends BaseDistributor {


    public OlxDistributor(AppConfig.Distributors config) {
        super(config);
    }

    @Override
    public HashMap<String, String> getQueryMapping() {
        return new HashMap<String, String>() {{
            put("search[filter_float_price:from]", getConfig().getMinPrice());
            put("search[filter_float_price:to]", getConfig().getMaxPrice());
            put("search[filter_float_number_of_rooms:from]", getConfig().getMinRooms());
            put("search[filter_float_number_of_rooms:to]", getConfig().getMaxRooms());
            if(!getConfig().isCommissionAllow()){
                put("search[filter_enum_commission]", "1");
            }
            put("search[order]", getConfig().getOrder());
        }};
    }

    @Override
    public Offer parse(Document element) {
        return null;
    }

    public HashMap<DistrictsEnum, URI> getQueries() {
        try {
            HashMap<DistrictsEnum, URI> map = new HashMap<>();

            if (getConfig().getDistricts() != null) {
                URIBuilder builder = new URIBuilder(getConfig().getUrl());
                builder.setPath(getConfig().getPath());

                setParams(builder, getQueryMapping());

                for (String s : getConfig().getDistricts()) {
                    map.put(DistrictsEnum.valueOf(s),
                            builder.setParameter(
                                    "search[district_id]",
                                    String.valueOf(DistrictsEnum.valueOf(s).olxRegionId))
                                   .build());
                }
            }
            return map;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }


}
