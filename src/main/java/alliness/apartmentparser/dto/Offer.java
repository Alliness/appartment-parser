package alliness.apartmentparser.dto;

import alliness.core.convertor.Serializable;

public class Offer extends Serializable {


    private String offerId;
    private String link;
    private String image;
    private String title;
    private String location;
    private String date;
    private String price;

    public String getOfferId() {
        return offerId;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }

    public Offer setOfferId(String offerId) {
        this.offerId = offerId;
        return this;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Offer setImage(String image) {
        this.image = image;
        return this;

    }

    public Offer setTitle(String title) {
        this.title = title;
        return this;

    }

    public Offer setLocation(String location) {
        this.location = location;
        return this;

    }

    public Offer setDate(String date) {
        this.date = date;
        return this;

    }

    public Offer setPrice(String price) {
        this.price = price;
        return this;

    }

    public String getParsedMessage() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("<b>%s</b>\n", title))
          .append(String.format("<b>%s</b>\n", price))
          .append(String.format("<b>%s</b>\n", date))
          .append(String.format("<b>%s</b>\n", location))
          .append(String.format("<a href=\"%s\">%s</a>", link, link));
        return sb.toString();
    }
}
