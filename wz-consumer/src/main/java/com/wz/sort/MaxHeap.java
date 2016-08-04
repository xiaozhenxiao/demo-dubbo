package com.wz.sort;

/**
 * Created by wangzhen on 2016-08-03.
 */
public class MaxHeap {
    int[] heap;
    int heapsize;
    public MaxHeap(int[] array)
    {
        this.heap=array;
        this.heapsize=heap.length;
    }
    public void BuildMaxHeap(){
        for(int i=heapsize/2-1;i>=0;i--)
        {
            Maxify(i);//依次向上将当前子树最大堆化
        }
    }
    public void HeapSort(){
        for(int i=0;i<heap.length;i++)
        {
            //执行n次，将每个当前最大的值放到堆末尾
            int tmp=heap[0];
            heap[0]=heap[heapsize-1];
            heap[heapsize-1]=tmp;
            heapsize--;
            Maxify(0);
        }
    }
    public void Maxify(int i){
        int l=Left(i);
        int r=Right(i);
        int largest;

        if(l<heapsize&&heap[l]>heap[i])
            largest=l;
        else
            largest=i;
        if(r<heapsize&&heap[r]>heap[largest])
            largest=r;
        if(largest==i||largest>=heapsize)//如果largest等于i说明i是最大元素 largest超出heap范围说明不存在比i节点大的子女
            return ;
        int tmp=heap[i];//交换i与largest对应的元素位置，在largest位置递归调用maxify
        heap[i]=heap[largest];
        heap[largest]=tmp;
        Maxify(largest);
    }

    public void IncreaseValue(int i,int val){
        heap[i]=val;
        if(i>=heapsize||i<=0||heap[i]>=val)
            return;
        int p=Parent(i);
        if(heap[p]>=val)
            return;
        heap[i]=heap[p];
        IncreaseValue(p, val);
    }

    private int Parent(int i){
        return (i-1)/2;
    }
    private int Left(int i){
        return 2*(i+1)-1;
    }
    private int Right(int i){
        return 2*(i+1);
    }
}
