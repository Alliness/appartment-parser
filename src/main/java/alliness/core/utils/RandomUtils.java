package alliness.core.utils;

import java.util.List;
import java.util.Random;

/**
 * Created by zhitnikov on 7/10/2017.
 */
public class RandomUtils {


    /**
     * get Random int from(min) to(max)
     *
     * @param from int
     * @param to   int
     * @return int
     */
    public static int getRandomInt(int from, int to) {
        return new Random().nextInt(to + 1 - from) + from;
    }

    /**
     * Return random object from list
     * @param list {@link List}
     * @return Object
     */
    public static Object getFromList(List<?> list) {
        return list.get(getRandomInt(0, list.size() - 1));
    }
}
