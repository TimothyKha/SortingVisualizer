package algorithms;

import visualization.VisualizationFrame;
import visualization.Visualizer;

public class MergeSort implements Runnable {
    private Integer[] sortArray;
    private VisualizationFrame frame;

    public MergeSort(Integer[] sortArray, VisualizationFrame frame) {
        this.sortArray = sortArray;
        this.frame = frame;
    }

    public void run() {
        sort(sortArray, 0, sortArray.length-1);
        if (!Visualizer.running) {
            Visualizer.sorting = false;
            return;
        }
        frame.linearWipe(sortArray);
        Visualizer.sorting = false;
        Visualizer.running = false;
    }


    private void sort(Integer[] arr, int first, int last) {
        if (!Visualizer.running) {
            Visualizer.sorting = false;
            return;
        }
        int mid, left, right, tmp;
        if (first >= last)
            return;
        mid = (first + last) / 2;
        sort(arr, first, mid);
        sort(arr, mid + 1, last);
        left = first;
        right = mid + 1;
        if (arr[mid] <= arr[right])
            return;
        while (left <= mid && right <= last) {
            if (arr[left] <= arr[right])
                left++;
            else {
                tmp = arr[right];
                for (int i = right - left; i > 0; i--) {
                    arr[left + i] = arr[left + i - 1];
                }
                arr[left] = tmp;
                right++;
                left++;
                mid++;
            }
            frame.reDraw(arr, mid, right, left);
            try {
                Thread.sleep(Visualizer.sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            checkPause();
            if (!Visualizer.running) {
                Visualizer.sorting = false;
                return;
            }
        }
    }

    private void checkPause() {
        synchronized (Visualizer.threadPauser) {
            while (Visualizer.isPaused) {
                try {
                    Visualizer.threadPauser.wait();
                } catch (InterruptedException e) {

                }
            }
        }
    }
}
