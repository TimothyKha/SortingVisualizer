package algorithms;


import visualization.VisualizationFrame;
import visualization.Visualizer;

public class SelectionSort implements Runnable {

    private Integer[] sortArray;
    private VisualizationFrame frame;

    public SelectionSort(Integer[] sortArray, VisualizationFrame frame) {
        this.sortArray = sortArray;
        this.frame = frame;
    }

    // Places the smallest element in the unsorted part of the array where it should be.
    public static void swap(Integer[] arr, int iteration, int minInd) {
        int temp = arr[iteration];
        arr[iteration] = arr[minInd];
        arr[minInd] = temp;
    }

    public void run() {
        sort();
        if (!Visualizer.running) {
            Visualizer.sorting = false;
            return;
        }
        frame.linearWipe(sortArray);
        Visualizer.sorting = false;
        Visualizer.running = false;
    }

    public void sort() {
        // In every iteration, the array would be sorted from 0 to index - 1.
        for (int iteration = 0; iteration < sortArray.length; iteration++) {
            int min = sortArray[iteration];
            int minInd = iteration;
            // Find the smallest element within the rest of the array that isn't sorted yet starting at index.
            for (int index = iteration + 1; index < sortArray.length; index++) {
                if (sortArray[index] < min) {
                    min = sortArray[index];
                    minInd = index;
                }
            }
            swap(sortArray, iteration, minInd);
            frame.reDraw(sortArray, iteration, minInd, -1);
            try {
                Thread.sleep(Visualizer.sleep * 2);
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
