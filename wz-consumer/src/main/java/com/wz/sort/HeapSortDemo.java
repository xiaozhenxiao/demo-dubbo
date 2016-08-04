package com.wz.sort;

/**
 * Created by wangzhen on 2016-08-03.
 */
public class HeapSortDemo {
    private static int[] queue = new int[10];
    public static void main(String[] args)
    {
        int[] array=new int[]{11,32,13,4,70,38,29,10,54,16};
        /*MaxHeap heap=new MaxHeap(array);
        System.out.println("执行最大堆化前堆的结构：");
        printHeapTree(heap.heap);
        heap.BuildMaxHeap();
        System.out.println("执行最大堆化后堆的结构：");
        printHeapTree(heap.heap);
        heap.HeapSort();
        System.out.println("执行堆排序后数组的内容");
        printHeapTree(heap.heap);
        printHeap(-1, heap.heap);*/
        for (int i = 0; i < array.length; i++) {
            siftUp(i, array[i]);
        }
        printHeapTree(queue);
        printHeap(-1,queue);

        for (int i = 9; i >= 0; i--) {
            siftDownHeap(i);
        }
        System.out.println("\n排序结果:");
        printHeap(-1,queue);

    }

    private static void siftDownHeap(int index) {
        int last;
        last = queue[index];
        queue[index] = queue[0];
        siftDown(0,last, index);
        System.out.println("\n元素沉降"+ (10-index) +"次以后");
        printHeapTree(queue);
        printHeap(-1,queue);
    }

    private static void siftUp(int k, int key){
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            int e = queue[parent];
            if (key > e)
                break;
            queue[k] = e;
            k = parent;
        }
        queue[k] = key;
    }
    private static void siftDown(int k, int key, int size) {
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            int c = queue[child];
            int right = child + 1;
            if (right < size && c > queue[right])
                c = queue[child = right];
            if (key < c)
                break;
            queue[k] = c;
            k = child;
        }
        queue[k] = key;
    }
    private static void printHeapTree(int[] array){
        for(int i=1;i<array.length;i=i*2){
            for(int k=i-1;k<2*(i)-1&&k<array.length;k++){
                System.out.print(array[k]+" ");
            }
            System.out.println();
        }
    }
    private static void printHeap(int k, int[] array){
        for(int i=0;i<array.length;i++){
            if(i==k && array[i]!=0){
                System.out.print("<"+ array[i]+"> ");
            }else if(array[i]!=0){
                System.out.print(array[i]+" ");
            }

        }
    }
}
