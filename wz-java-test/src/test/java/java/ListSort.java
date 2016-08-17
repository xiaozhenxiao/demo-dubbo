package java;

import com.alibaba.fastjson.JSON;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ListSort {
    public static void main(String[] args) {

        System.out.println("**************************************************************");
        String json = "[{\"total\":\"2000\"}, {\"total\":\"10000\"}, {\"total\":\"6000\"}, {\"total\":\"14000\"}, {\"total\":\"12000\"}]";
        List<Map<String, String>> list = JSON.parseObject(json, List.class);
        for (Map<String, String> map : list) {
            String total = map.get("total");
            System.out.println(total);
        }
        Comparator<Map<String, String>> comparator = new Comparator<Map<String, String>>() {
            public int compare(Map<String, String> arg0, Map<String, String> arg1) {
                int result = 0;
                String stt1 = arg0.get("total");
                String stt2 = arg1.get("total");
                Double tt1 = (stt1 != null) ? Double.valueOf(stt1) : 0;
                Double tt2 = (stt2 != null) ? Double.valueOf(stt2) : 0;

                result = tt1.compareTo(tt2);
                return result;
            }
        };
        System.out.println("---------------------------------------------------");
        Collections.sort(list, comparator);
        for (Map<String, String> map : list) {
            String total = map.get("total");
            System.out.println(total);
        }


        int[] a = {2, 5, 3, 7, 1, 8, 3, 9, 15, 6};
        quickSort(a, 0, 9);
    }

    public static void quickSort(int[] a, int start, int end) {

        if (start < end) {
            int base = a[start];
            int i = start, j = end;
            while (i <= j) {
                while (i < end && base > a[i])
                    i++;
                while (j > start && base < a[j])
                    j--;
                if (i <= j) {
                    int temp = a[i];
                    a[i] = a[j];
                    a[j] = temp;
                    i++;
                    j--;
                }
                for (int aa : a) {
                    System.out.print(aa + "\t");
                }
                System.out.println("\n" + i + "-------------------------------------------" + j);
            }
            if (start < j)
                quickSort(a, start, j);
            if (end > i)
                quickSort(a, i, end);
        }


    }

}
