package alliness.apartmentparser.implementation;

import alliness.apartmentparser.dto.AppConfig;
import alliness.apartmentparser.dto.Offer;
import alliness.apartmentparser.enums.DistrictsEnum;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            if (!getConfig().isCommissionAllow()) {
                put("search[filter_enum_commission]", "1");
            }
            put("search[order]", getConfig().getOrder());
        }};
    }

    @Override
    public List<Offer> parse(Document document) {
        ArrayList<Offer> offersList = new ArrayList<>();
        Elements         offers     = document.getElementById("offers_table").getElementsByAttribute("data-id");

        for (Element offerElement : offers) {
            if (!offerElement.parent().hasClass("promoted")) {
                Offer offer = new Offer();

                Element  link     = offerElement.getElementsByClass("detailsLink").get(0);
                Elements strongs  = offerElement.getElementsByTag("strong");
                Elements xNormals = offerElement.getElementsByClass("x-normal");

                offer.setOfferId(offerElement.attr("data-id"));
                offer.setLink(link.attr("href"));
                offer.setTitle(strongs.get(0).text());
                offer.setPrice(strongs.get(1).text());
                offer.setLocation(xNormals.get(2).text());
                offer.setDate(xNormals.get(1).text());
                offer.setImage(offerElement.getElementsByTag("img").attr("src"));
                if (offer.getLocation().contains("Сегодня")) {
                    offersList.add(offer);
                }
            }
        }


        return offersList;

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
