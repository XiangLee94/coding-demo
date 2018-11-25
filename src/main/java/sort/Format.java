package sort;

public class Format {
    public static void createHeap(int[] a, int n, int h) {//一次调整变为最大堆 n要调整的数组长度，h为堆的根节点
        int i, j, flag;
        int temp;
        i = h;
        j = 2 * i + 1;
        temp = a[i];
        flag = 0;
        while (j < n && flag != 1) {
            if (j < n - 1 && a[j] < a[j + 1]) j++;

            if (temp > a[j])
                flag = 1;
            else {
                a[i] = a[j];
                i = j;
                j = 2 * i + 1;
            }
        }
        a[i] = temp;
    }

    //初始化循环建堆
    public static void initCreateHeap(int[] a) {
        int n = a.length;
        for (int i = (n - 1) / 2; i >= 0; i--) {
            createHeap(a, n, i);
        }
    }

    //堆排序主函数
    public static void heapSort(int[] a) {
        int temp;
        int n = a.length;

        initCreateHeap(a);
        for (int i = n - 1; i > 0; i--) {
            temp = a[0];
            a[0] = a[i];
            a[i] = temp;

            createHeap(a, i, 0);
        }
    }
}
