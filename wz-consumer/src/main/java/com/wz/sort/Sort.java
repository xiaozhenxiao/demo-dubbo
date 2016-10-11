package com.wz.sort;

import java.util.Arrays;

/**
 * Created by Administrator on 2016/6/26.
 * Date:2016-06-26
 */
public class Sort {
    public static void main(String[] args) {
        int [] array = {1, 5, 8, 2, 7, 4, 3, 6, 9, 15, 10, 11, 13};
        printArray("排序前：",  array);
        radixSort(array, 10, 4);
//        bubbleSort(array);
//        kuaipai(array, 0, array.length-1);
        printArray("排序：", array);
    }

    public static void kuaipai(int[] a, int start, int end){
        if(start<end){
            int i = start, j = end;
            int base = a[start];
            while(i<=j){
                while(i < end && a[i] < base)
                    i++;
                while(j > start && a[j] > base)
                    j--;
                if (i<=j){
                    swap(a, i, j);
                    i++;
                    j--;
                }
            }
            if(i < end)
                kuaipai(a, i, end);
            if(j > start)
                kuaipai(a, start, j);
        }
    }

    public static void printArray(String print, int[] numbers){
        System.out.println(print);
        for (int i : numbers) {
            System.out.print(i + " - ");
        }
        System.out.println();
    }
    public static void swap(int a[], int i, int j){
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    /**
     * 代码
     * <p>
     * 冒泡法排序<br/>
     * <p>
     * <li>比较相邻的元素。如果第一个比第二个大，就交换他们两个。</li>
     * <li>对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对。在这一点，最后的元素应该会是最大的数。</li>
     * <li>针对所有的元素重复以上的步骤，除了最后一个。</li>
     * <li>持续每次对越来越少的元素重复上面的步骤，直到没有任何一对数字需要比较。</li>
     *
     * @param numbers 需要排序的整型数组
     */
    public static void bubbleSort(int[] numbers) {
        int size = numbers.length; // 数组大小
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                if (numbers[i] < numbers[j]) { // 交换两数的位置
                    swap(numbers, i, j);
                }
            }
        }
    }

    /**
     * 快速排序<br/>
     * <ul>
     * <li>从数列中挑出一个元素，称为“基准”</li>
     * <li>重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。在这个分割之后，
     * 该基准是它的最后位置。这个称为分割（partition）操作。</li>
     * <li>递归地把小于基准值元素的子数列和大于基准值元素的子数列排序。</li>
     * </ul>
     *
     * @param numbers
     * @param start
     * @param end
     */
    public static void quickSort(int[] numbers, int start, int end) {
        if (start < end) {
            int base = numbers[start]; // 选定的基准值（第一个数值作为基准值）
            int i = start, j = end;
            do {
                while ((numbers[i] < base) && (i < end))
                    i++;
                while ((numbers[j] > base) && (j > start))
                    j--;
                if (i <= j) {
                    swap(numbers, i, j);
                    i++;
                    j--;
                }
            } while (i <= j);
            if (start < j)
                quickSort(numbers, start, j);
            if (end > i)
                quickSort(numbers, i, end);
        }
    }

    /**
     * 选择排序<br/>
     * <li>在未排序序列中找到最小元素，存放到排序序列的起始位置</li>
     * <li>再从剩余未排序元素中继续寻找最小元素，然后放到排序序列末尾。</li>
     * <li>以此类推，直到所有元素均排序完毕。</li>
     *
     * @param numbers
     */
    public static void selectSort(int[] numbers) {
        int size = numbers.length;
        for (int i = 0; i < size; i++) {
            int k = i;
            for (int j = size - 1; j >i; j--)  {
                if (numbers[j] < numbers[k])  k = j;
            }
            swap(numbers, i, k);
        }
    }

    /**
     * 插入排序<br/>
     * <ul>
     * <li>从第一个元素开始，该元素可以认为已经被排序</li>
     * <li>取出下一个元素，在已经排序的元素序列中从后向前扫描</li>
     * <li>如果该元素（已排序）大于新元素，将该元素移到下一位置</li>
     * <li>重复步骤3，直到找到已排序的元素小于或者等于新元素的位置</li>
     * <li>将新元素插入到该位置中</li>
     * <li>重复步骤2</li>
     * </ul>
     *
     * @param numbers
     */
    public static void insertSort(int[] numbers) {
        int size = numbers.length, temp, j;
        for(int i=1; i<size; i++) {
            temp = numbers[i];
            for(j = i; j > 0 && temp < numbers[j-1]; j--)
                numbers[j] = numbers[j-1];
            numbers[j] = temp;
        }
    }

    /** 归并排序<br/>
     * <ul>
     * <li>申请空间，使其大小为两个已经排序序列之和，该空间用来存放合并后的序列</li>
     * <li>设定两个指针，最初位置分别为两个已经排序序列的起始位置</li>
     * <li>比较两个指针所指向的元素，选择相对小的元素放入到合并空间，并移动指针到下一位置</li>
     * <li>重复步骤3直到某一指针达到序列尾</li>
     * <li>将另一序列剩下的所有元素直接复制到合并序列尾</li>
     * </ul>
     *
     * @param numbers
     */
    public static void mergeSort(int[] numbers, int left, int right) {
        int t = 1;// 每组元素个数
        int size = right - left + 1;
        while (t < size) {
            int s = t;// 本次循环每组元素个数
            t = 2 * s;
            int i = left;
            while (i + (t - 1) < size) {
                merge(numbers, i, i + (s - 1), i + (t - 1));
                i += t;
            }
            if (i + (s - 1) < right)
                merge(numbers, i, i + (s - 1), right);
        }
    }
    /**
     * 归并算法实现
     *
     * @param data
     * @param p
     * @param q
     * @param r
     */
    private static void merge(int[] data, int p, int q, int r) {
        int[] B = new int[data.length];
        int s = p;
        int t = q + 1;
        int k = p;
        while (s <= q && t <= r) {
            if (data[s] <= data[t]) {
                B[k] = data[s];
                s++;
            } else {
                B[k] = data[t];
                t++;
            }
            k++;
        }
        if (s == q + 1)
            B[k++] = data[t++];
        else
            B[k++] = data[s++];
        for (int i = p; i <= r; i++)
            data[i] = B[i];
    }

    /**
     * 将待排数据里德排序关键字拆分成多个排序关键字；第1个排序关键字，第2个排序关键字，第3个排序关键字......
     * 然后，根据子关键字对待排序数据进行排序
     * 多关键字排序时有两种解决方案：
     * 最高位优先法（MSD）(Most Significant Digit first)
     * 最低位优先法（LSD）(Least Significant Digit first)
     * 例如，对如下数据序列进行排序。192,221,12,23
     * 可以观察到它的每个数据至多只有3位，因此可以将每个数据拆分成3个关键字：百位（高位）、十位、个位（低位）。
     * @param data
     * @param radix
     * @param d
     */
    public static void radixSort(int[] data, int radix, int d) {
        // 缓存数组
        int[] tmp = new int[data.length];
        // buckets用于记录待排序元素的信息
        // buckets数组定义了max-min个桶
        int[] buckets = new int[radix];

        for (int i = 0, rate = 1; i < d; i++) {

            // 重置count数组，开始统计下一个关键字
            Arrays.fill(buckets, 0);
            // 将data中的元素完全复制到tmp数组中
            System.arraycopy(data, 0, tmp, 0, data.length);

            // 计算每个待排序数据的子关键字
            for (int j = 0; j < data.length; j++) {
                int subKey = (tmp[j] / rate) % radix;
                buckets[subKey]++;
            }

            for (int j = 1; j < radix; j++) {
                buckets[j] = buckets[j] + buckets[j - 1];
            }

            // 按子关键字对指定的数据进行排序
            for (int m = data.length - 1; m >= 0; m--) {
                int subKey = (tmp[m] / rate) % radix;
                data[--buckets[subKey]] = tmp[m];
            }
            rate *= radix;
        }

    }
}