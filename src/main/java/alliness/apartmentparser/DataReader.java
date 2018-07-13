package alliness.apartmentparser;

import alliness.apartmentparser.dto.AppConfig;
import alliness.core.helpers.FWriter;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DataReader {
    private static DataReader ourInstance = new DataReader();

    public static DataReader getInstance() {
        return ourInstance;
    }

    private File                dir;
    private File                recipients;
    private Map<String, File> offers = Collections.synchronizedMap(new HashMap<>());

    @SuppressWarnings("all")
    private DataReader() {
        dir = new File("data");
        if (!dir.exists()) {
            dir.mkdir();
        }

        for (AppConfig.Distributors distributors : Config.get().getDistributors()) {
            File f = new File("data/offers_" + distributors.getName() + ".json");
            if (!f.exists()) {
                try {
                    f.createNewFile();
                    FWriter.writeToFile(new JSONArray().toString(), f, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            offers.put(distributors.getName(), f);
        }

        recipients = new File("data/recipients.json");
        if (!recipients.exists()) {
            try {
                recipients.createNewFile();
                FWriter.writeToFile(new JSONArray().toString(), recipients, false);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public File getDir() {
        return dir;
    }

    public Map<String, File> getOffersFiles() {
        return offers;
    }

    public File getFileForDistributor(String distributor) {
        try {
            return (File) offers.get(distributor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public File getRecipientsFile() {
        return recipients;
    }
}
