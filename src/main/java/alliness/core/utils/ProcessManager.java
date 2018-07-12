package alliness.core.utils;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Win-System Process manager
 */
public class ProcessManager {

    private static final Logger log = Logger.getLogger(ProcessManager.class);

    private static final String TASKLIST = "tasklist";
    private static final String KILL     = "taskkill /F /IM ";
    public static final String CHROME_DRIVER_PROCESS = "chromedriver.exe";


    /**
     *
     * @return list of system process data
     */
    public static List<String> getProcesses() {
        List<String> processes = new ArrayList<>();

        try {
            Process p = Runtime.getRuntime().exec(TASKLIST);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String       line;
            while ((line = reader.readLine()) != null) {
                processes.add(line);
            }
            return processes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return processes;
    }

    /**
     *
     * @param serviceName {@link String}
     * @return is serviceName is running
     */
    public static boolean isProcessRunning(String serviceName) {
        try {
            if (getProcesses() != null)
                for (String s : getProcesses()) {
                    if (s.contains(serviceName)) {
                        return true;
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *  kill process serviceName
     * @param serviceName {@link String}
     */
    public static void killProcess(String serviceName) throws Exception {
        if (isProcessRunning(serviceName)) {
            log.info("[ProcessManager] Process "+ serviceName + " was found.");
            Runtime.getRuntime().exec(KILL + serviceName);
            log.info("[ProcessManager] Process "+ serviceName + " was killed.");
        }
    }

    /**
     * kill chromedriver.exe
     */
    public static void killChromeDriver() {
        try {
            killProcess(CHROME_DRIVER_PROCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
