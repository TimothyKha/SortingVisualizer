package visualization;

import java.util.ArrayList;
import java.util.Collections;


import algorithms.*;

public class Visualizer {
    public final static Object threadPauser = new Object();
    public static Thread sortingThread;
    public static VisualizationFrame frame;
    public static Integer[] toSort;
    public static boolean sorting = false;
    public static int numRectangles = 100;
    public static int sleep = 20;
    public static int blockWidth;
    public static boolean fast = false;
    public static boolean stepped = false;
    public static boolean isPaused = false;

    public static void main(String[] args) {
        frame = new VisualizationFrame();
        resetArray();
    }

    public static void resetArray() {
        if (sorting) return;
        toSort = new Integer[numRectangles];
        blockWidth = (int) Math.max(Math.floor(600 / numRectangles), 1);
        if (stepped) {
            ArrayList<Integer> toShuffle = new ArrayList<>();
            for (int i = 0; i < numRectangles; i++) {
                toShuffle.add(i, i);
            }
            Collections.shuffle(toShuffle);
            toSort = toShuffle.toArray(toSort);
        } else {
            for (int i = 0; i < numRectangles; i++) {
                toSort[i] = (int) (numRectangles * Math.random());
            }
        }
        frame.initialDraw(toSort);
    }

    public static void startSort(String sortName) {
        if (sortingThread == null || !sorting) {
            sorting = true;
            switch (sortName) {
                case "merge":
                    sortingThread = new Thread(new MergeSort2(toSort, frame));
                    break;
                case "bubble":
                    sortingThread = new Thread(new BubbleSort(toSort, frame, fast));
                    break;
                case "insertion":
                    sortingThread = new Thread(new InsertionSort(toSort, frame, fast));
                    break;
                case "selection":
                    sortingThread = new Thread(new SelectionSort(toSort, frame));
                    break;
                case "quick":
                    sortingThread = new Thread(new QuickSort(toSort, frame));
                    break;
                default:
                    sorting = false;
                    return;
            }
            sortingThread.start();
        }
    }
}
