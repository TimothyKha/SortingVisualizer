package algorithms;

import visualization.VisualizationFrame;
import visualization.Visualizer;


public class InsertionSort implements Runnable {
    private Integer[] sortArray;
    private VisualizationFrame frame;
    private boolean fast;

    public InsertionSort(Integer[] sortArray, VisualizationFrame frame, boolean fast) {
        this.sortArray = sortArray;
        this.frame = frame;
        this.fast = fast;
    }

    public static void swap(Integer[] arr, int index, int toSwap) {
        int temp = arr[index];
        arr[index] = arr[toSwap];
        arr[toSwap] = temp;
    }

    public void sort() {
        for (int iteration = 1; iteration < sortArray.length; iteration++) {
            for (int index = iteration; index > 0 && sortArray[index] < sortArray[index - 1]; index--) {
                swap(sortArray, index, index - 1);
                frame.reDraw(sortArray, iteration, -1, index);
                try {
                    Thread.sleep(Visualizer.sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                checkPause();
            }
        }
    }

    public void sortFast() {
        int old, insert = 0;
        for (int iteration = 1; iteration < sortArray.length; iteration++) {
            insert = iteration;
            for (int j = iteration - 1; j >= 0; j--) {
                if (sortArray[iteration] < sortArray[j]) {
                    insert = j;
                    if (j == 0) {
                        break;
                    }
                } else {
                    break;
                }
            }
            old = sortArray[iteration];
            for (int j = iteration; j > insert; j--) {
                sortArray[j] = sortArray[j - 1];
                checkPause();
            }
            sortArray[insert] = old;
            frame.reDraw(sortArray, iteration, -1, -1);
            try {
                Thread.sleep(Visualizer.sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkPause() {
        synchronized (Visualizer.threadPauser) {
            while (Visualizer.isPaused) {
                try {
                    Visualizer.threadPauser.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void run() {
        if (fast) {
            sortFast();
        } else {
            sort();
        }
        frame.linearWipe(sortArray);
        Visualizer.sorting = false;
    }
}
