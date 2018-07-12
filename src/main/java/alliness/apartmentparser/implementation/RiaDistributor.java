package alliness.apartmentparser.implementation;

import alliness.apartmentparser.dto.AppConfig;
import alliness.apartmentparser.dto.Offer;
import alliness.apartmentparser.enums.DistrictsEnum;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class RiaDistributor extends BaseDistributor{


    public RiaDistributor(AppConfig.Distributors config) {
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
        return new HashMap<String, String>(){{
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

    @Override
    public Offer parse(Document element) {
        return null;
    }

}
