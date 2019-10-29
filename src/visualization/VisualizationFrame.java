package visualization;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

public class VisualizationFrame extends JFrame {
    private AtomicBoolean firstPause = new AtomicBoolean(true);

    private int sizeModifier;

    private JPanel wrapper;
    private JPanel arrayPanel;
    private JPanel buttonPanel;
    private JPanel[] rectangles;
    private JCheckBox fast;
    private JCheckBox steppedVals;

    private JButton insertion;
    private JButton selection;
    private JButton merge;
    private JButton quick;
    private JButton bubble;
    private JButton generate;
    private JButton pause;

    private GridBagConstraints c;


    public VisualizationFrame() {
        super("Sorting Visualizer");

        insertion = new JButton("Insertion Sort");//creating instance of JButton
        //insertion.setBounds(screenWidth-(int)(1.1*buttonWidth),410,buttonWidth, 70);
        selection = new JButton("Selection Sort");//creating instance of JButton
        //selection.setBounds(screenWidth-(int)(1.1*buttonWidth),340,buttonWidth, 70);
        merge = new JButton("Merge Sort");//creating instance of JButton
        //merge.setBounds(screenWidth-(int)(1.1*buttonWidth),270,buttonWidth, 70);
        quick = new JButton("Quick Sort");//creating instance of JButton
        //quick.setBounds(screenWidth-(int)(1.1*buttonWidth),200,buttonWidth, 70);
        bubble = new JButton("Bubble Sort");//creating instance of JButton
        //bubble.setBounds(screenWidth-(int)(1.1*buttonWidth),130,buttonWidth, 70);
        generate = new JButton("Generate Array");//creating instance of JButton
        //generate.setBounds(screenWidth-(int)(1.1*buttonWidth),60,buttonWidth, 70);
        pause = new JButton("Pause Sorting");//creating instance of JButton
        //generate.setBounds(screenWidth-(int)(1.1*buttonWidth),60,buttonWidth, 70);

        buttonPanel = new JPanel();
        arrayPanel = new JPanel();
        wrapper = new JPanel();
        fast = new JCheckBox("Fast Mode", false);

        steppedVals = new JCheckBox("Stepped Values");
        c = new GridBagConstraints();
        arrayPanel.setLayout(new GridBagLayout());
        wrapper.setLayout(new BorderLayout());
        c.insets = new Insets(0, 0, 0, 2);
        c.anchor = GridBagConstraints.EAST;

        steppedVals.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Visualizer.stepped = !Visualizer.stepped;
                Visualizer.resetArray();
            }
        });
        fast.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Visualizer.fast = !Visualizer.fast;
            }
        });

        insertion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Visualizer.startSort("insertion");
            }
        });

        selection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Visualizer.startSort("selection");
            }
        });

        merge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Visualizer.startSort("merge");
            }
        });

        quick.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Visualizer.startSort("quick");
            }
        });

        bubble.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Visualizer.startSort("bubble");
            }
        });

        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Visualizer.resetArray();
            }
        });
        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (firstPause.get() && Visualizer.sorting) {
                    Visualizer.isPaused = true;
                    firstPause.set(false);
                } else {
                    synchronized (Visualizer.threadPauser) {
                        Visualizer.threadPauser.notify();
                        Visualizer.isPaused = false;
                    }
                    firstPause.set(true);
                }
            }
        });

        buttonPanel.add(selection);
        buttonPanel.add(insertion);
        buttonPanel.add(merge);
        buttonPanel.add(quick);
        buttonPanel.add(bubble);
        buttonPanel.add(generate);
        buttonPanel.add(pause);
        buttonPanel.add(fast);
        buttonPanel.add(steppedVals);

        wrapper.add(buttonPanel, BorderLayout.SOUTH);
        wrapper.add(arrayPanel);

        add(wrapper);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }


    public void initialDraw(Integer[] squares) {
        rectangles = new JPanel[Visualizer.numRectangles];
        arrayPanel.removeAll();
        sizeModifier = (int) ((getHeight()*0.8) / (Visualizer.numRectangles));
        for (int i = 0; i < Visualizer.numRectangles; i++) {
            rectangles[i] = new JPanel();
            rectangles[i].setPreferredSize(new Dimension(Visualizer.blockWidth, squares[i] * sizeModifier));
            rectangles[i].setBackground(Color.BLACK);
            arrayPanel.add(rectangles[i], c);
        }
        repaint();
        validate();
    }

    public void reDraw(Integer[] squares, int working, int comparing, int reading) {
        arrayPanel.removeAll();
        for (int i = 0; i < rectangles.length; i++) {
            rectangles[i].setPreferredSize(new Dimension(Visualizer.blockWidth, squares[i] * sizeModifier));
            if (i == working) {
                rectangles[i].setBackground(Color.CYAN);
            } else if (i == comparing) {
                rectangles[i].setBackground(Color.red);
            } else if (i == reading) {
                rectangles[i].setBackground(Color.yellow);
            } else {
                rectangles[i].setBackground(Color.BLACK);
            }
            arrayPanel.add(rectangles[i], c);
        }
        repaint();
        validate();
    }

    public void pivotDrawArray(Integer[] squares, int pivot, int currentIndex) {
        arrayPanel.removeAll();
        for (int i = 0; i < rectangles.length; i++) {
            rectangles[i].setPreferredSize(new Dimension(Visualizer.blockWidth, squares[i] * sizeModifier));
            if (i == pivot) {
                rectangles[i].setBackground(Color.CYAN);
            } else if (i > currentIndex) {
                rectangles[i].setBackground(Color.BLACK);
            } else if (i < pivot) {
                rectangles[i].setBackground(Color.yellow);
            } else {
                rectangles[i].setBackground(Color.red);
            }
            arrayPanel.add(rectangles[i], c);
        }
        repaint();
        validate();
    }

    public void linearWipe(Integer[] squares) {
        for (int i = 0; i < rectangles.length; i++) {
            linearHelper(squares, i);
            try {
                Thread.sleep(Visualizer.sleep / 2);
            } catch (InterruptedException e) {
            }
        }
    }

    private void linearHelper(Integer[] squares, int currentRec) {
        arrayPanel.removeAll();
        for (int i = 0; i < rectangles.length; i++) {
            rectangles[i].setPreferredSize(new Dimension(Visualizer.blockWidth, squares[i] * sizeModifier));
            if (i == currentRec) {
                rectangles[i].setBackground(Color.CYAN);
            } else {
                rectangles[i].setBackground(Color.BLACK);
            }
            arrayPanel.add(rectangles[i], c);
        }
        repaint();
        validate();
    }

}
