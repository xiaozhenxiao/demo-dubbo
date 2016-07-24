/**
 * Created by Administrator on 2016/7/23.
 * Date:2016-07-23
 */
public class JavaTest {
    public static void main(String[] args) {
        int [] array = {1, 5, 8, 2, 7, 4, 3, 6, 9, 15, 10, 11, 13};
        quickSort(array, 0, array.length-1);
        printArray("排序后", array);
    }

    public static void printArray(String print, int[] numbers){
        System.out.println(print);
        for (int i : numbers) {
            System.out.print(i + " - ");
        }
        System.out.println();
    }

    public static void quickSort(int[] a, int start, int end){
        if(start<end){
            int i = start, j = end;
            int base = a[start];
            int temp;
            while (i<=j) {
                while (a[i] < base && i < end)
                    i++;
                while (a[j] > base && j > start)
                    j--;
                if (i <= j) {
                    temp = a[i];
                    a[i] = a[j];
                    a[j] = temp;
                    i++;
                    j--;
                }
            }
            if (i<=end)
                quickSort(a, i, end);
            if(j>=start)
                quickSort(a, start, j);
        }
    }
}
