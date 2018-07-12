package alliness.apartmentparser.dto;

import alliness.core.convertor.Serializable;

import java.util.List;

public class AppConfig extends Serializable {
    private List<Distributors> distributors;

    private Bots bots;

    private long interval;

    private String agent;


    public List<Distributors> getDistributors() {
        return distributors;
    }

    public Bots getBots() {
        return bots;
    }

    public long getInterval() {
        return interval;
    }

    public String getAgent() {
        return agent;
    }

    public static class Distributors {
        private String name;

        private String url;

        private String path;

        private String minPrice;

        private String maxPrice;

        private String minRooms;

        private String maxRooms;

        private String order;

        private boolean commissionAllow;

        private List<String> districts;

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public String getPath() {
            return path;
        }

        public String getMinPrice() {
            return minPrice;
        }

        public String getMaxPrice() {
            return maxPrice;
        }

        public String getMinRooms() {
            return minRooms;
        }

        public String getMaxRooms() {
            return maxRooms;
        }

        public String getOrder() {
            return order;
        }

        public boolean isCommissionAllow() {
            return commissionAllow;
        }

        public List<String> getDistricts() {
            return districts;
        }
    }

    public static class Bots {
        private String telegram;

        public String getTelegram() {
            return telegram;
        }
    }
}
