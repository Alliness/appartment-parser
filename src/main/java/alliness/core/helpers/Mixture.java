package alliness.core.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * DataProvider helper
 * Created by zhitnikov on 7/12/2017.
 */
public class Mixture {

    /**
     * @param array1 (any Object[][])
     * @param array2 (any Object[][])
     * @return Object[][]
     * for each element of array 1 add each element of array2
     */
    public static Object[][] mix(Object[][] array1, Object[][] array2) {

        int maxRows = array1.length * array2.length;
        int maxCols = array1[0].length + array2[0].length;

        Object[][] endResult = new Object[maxRows][maxCols];

        int count = 0;

        for (Object[] anArray1 : array1) {

            for (Object[] anArray2 : array2) {

                System.arraycopy(anArray1, 0, endResult[count], 0, anArray1.length);
                System.arraycopy(anArray2, 0, endResult[count], anArray1.length, anArray2.length);
                count++;
            }
        }
        return endResult;
    }


    /**
     * Return all combinations from multiple string arrays e.g
     * getCombinations(String[]{1,2}, String[]{A,B} will return list of lists
     * [0  : [1,A],
     *  1  : [1,B],
     *  2  : [2,A],
     *  3  : [2,B]
     * ]
     *
     * @param data arrays
     * @return List of lists of combinations
     */
    public static List<List<String>> getCombinations(String[]... data) {

        List<List<String>> lists = new ArrayList<>();

        for (String[] array : data) {
            lists.add(Arrays.asList(array));
        }
        List<List<String>> result = new ArrayList<>();
        _generatePermutateComb(lists, result, 0, new ArrayList<>());

        return result;
    }


    private static void _generatePermutateComb(List<List<String>> Lists, List<List<String>> result, int depth, ArrayList<String> current) {
        if (depth == Lists.size()) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = 0; i < Lists.get(depth).size(); ++i) {
            current.add(Lists.get(depth).get(i));
            _generatePermutateComb(Lists, result, depth + 1, current);
            current.remove(current.get(depth));
        }

    }

    private static void _generatePermutateCombArray(List<Object[]> input, List<Object[]> output, int depth, ArrayList<Object> current) {
        if (depth == input.size()) {
            output.add(current.toArray());
            return;
        }
        for (int i = 0; i < input.get(depth).length; ++i) {
            current.add(input.get(depth)[i]);
            _generatePermutateCombArray(input, output, depth + 1, current);
            current.remove(current.get(depth));
        }

    }

    public static List<Object[]> getCombinationsObjects(String[]... data) {

        List<Object[]> lists  = new ArrayList<>();
        List<Object[]> result = new ArrayList<>();

        lists.addAll(Arrays.asList(data));
        _generatePermutateCombArray(lists, result, 0, new ArrayList<>());

        return result;
    }

    public static List<Object[]> getCombinationsObjects(Object[]... data) {

        List<Object[]> lists = new ArrayList<>();
        lists.addAll(Arrays.asList(data));
        List<Object[]> result = new ArrayList<>();
        _generatePermutateCombArray(lists, result, 0, new ArrayList<>());

        return result;
    }


    /**
     * Convert map to Object[][]
     * @param map {@link Map}
     * @return Object[][]
     */
    public static Object[][]toObject(Map<?, ?> map) {

        int x = map.size();
        map.entrySet().toArray();
        Object[][] obj     = new Object[x][2];
        int        counter = 0;
        for (Map.Entry<?, ?> o : map.entrySet()) {
            obj[counter][0] = o.getKey();
            obj[counter][1] = o.getValue();
            counter++;
        }
        return obj;
    }
}
