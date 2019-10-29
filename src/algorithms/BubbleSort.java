package algorithms;


import visualization.Visualizer;
import visualization.VisualizationFrame;

public class BubbleSort implements Runnable {

    private Integer[] sortArray;
    private VisualizationFrame frame;
    private boolean fast;

    public BubbleSort(Integer[] sortArray, VisualizationFrame frame, boolean fast) {
        this.sortArray = sortArray;
        this.frame = frame;
        this.fast = fast;
    }

    public static void swap(Integer[] arr, int first, int second) {
        int temp = arr[first];
        arr[first] = arr[second];
        arr[second] = temp;
    }


    public void run() {
        if (fast) {
            sortFast();
        } else {
            sort();
        }
        if (!Visualizer.running) {
            Visualizer.sorting = false;
            return;
        }
        frame.linearWipe(sortArray);
        Visualizer.sorting = false;
        Visualizer.running = false;
    }

    public void sortFast() {
        int temp = 0;
        for (int i = 0; i < sortArray.length - 1; i++) {
            for (int j = 1; j < sortArray.length - i; j++) {
                if (sortArray[j - 1] > sortArray[j]) {
                    BubbleSort.swap(sortArray, j-1, j);
                }
            }
            frame.reDraw(sortArray, sortArray.length - 1 - i, -1, -1);
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
        frame.reDraw(sortArray, 0, -1, -1);
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

    public void sort() {
        int temp = 0;
        boolean swapped = false;
        for (int i = 0; i < sortArray.length - 1; i++) {
            swapped = false;
            for (int j = 1; j < sortArray.length - i; j++) {
                if (sortArray[j - 1] > sortArray[j]) {
                    BubbleSort.swap(sortArray, j-1, j);
                    swapped = true;
                }
                frame.reDraw(sortArray, j, j + 1, -1);
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
            if (!swapped) break;
        }
        frame.initialDraw(sortArray);
    }

}
