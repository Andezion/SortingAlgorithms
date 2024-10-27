import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.JFrame;
import javax.sound.sampled.*;
import java.io.File;
import java.util.Objects;


public class Main extends JPanel
{

    private final ArrayList<Integer> array;
    private static final ArrayList<Integer> ORIGINAL_ARRAY = new ArrayList<>();  // Оригинальный массив для сброса
    private static Thread sortingThread;
    private SortStrategy defaultSortStrategy;

    static int currentIndex = -1;
    static int minIndex = -1;
    static int helper_for_shell = -1;

    public Main(ArrayList<Integer> array)
    {
        this.array = array;
    }

    public void visualizeSort(SortStrategy sortStrategy)
    {
        defaultSortStrategy = sortStrategy;

        if (sortingThread != null && sortingThread.isAlive())
        {
            sortingThread.interrupt();
        }

        sortingThread = new Thread(() -> sortStrategy.sort(array, this));
        sortingThread.start();
    }

    public void refresh()
    {
        repaint();
        try
        {
            Thread.sleep(50);

        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        setBackground(Color.BLACK);
        g.setColor(Color.WHITE);

        int width = 5;
        for (int i = 0; i < array.size(); i++)
        {
            int height = array.get(i) * 10;

            if (i == currentIndex)
            {
                g.setColor(Color.RED);
            }
            else if (i == minIndex || i == helper_for_shell)
            {
                g.setColor(Color.BLUE);
            }
            else
            {
                g.setColor(Color.WHITE);
            }

            g.fillRect(10 + i * (width + 5), 510 - height, width, height);
        }
    }

    public static void soundPlaying(String filename)
    {
        try
        {
            File audioFile = new File(filename);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);

            Clip clip = AudioSystem.getClip();

            clip.open(audioInputStream);
            clip.start();
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
        {
            e.printStackTrace();
        }
    }

    @FunctionalInterface
    interface SortStrategy
    {
        void sort(ArrayList<Integer> array, Main visualizer);
    }

    public static class SelectionSort implements SortStrategy // sound ok and display ok
    {
        @Override
        public void sort(ArrayList<Integer> array, Main visualizer)
        {
            int size = array.size();

            for (int i = 0; i < size - 1; i++)
            {
                currentIndex = i;
                int min_index = i;

                for (int j = i + 1; j < size; j++)
                {
                    minIndex = min_index;
                    if (array.get(j) < array.get(min_index))
                    {
                        min_index = j;
                    }
                }

                if (min_index != i)
                {
                    int temp = array.get(i);
                    array.set(i, array.get(min_index));
                    array.set(min_index, temp);
                    soundPlaying("sound.wav");
                }

                visualizer.refresh();
            }

            currentIndex = -1;
            minIndex = -1;
            visualizer.refresh();
        }
    }
    public static class BubbleSort implements SortStrategy // sound ok (ugly) display ok
    {
        @Override
        public void sort(ArrayList<Integer> array, Main visualizer)
        {
            int n = array.size();
            int swapped;
            for (int i = 0; i < n - 1; i++)
            {
                currentIndex = i;
                swapped = 0;
                for (int j = 0; j < n - i - 1; j++)
                {
                    if (array.get(j) > array.get(j + 1))
                    {
                        minIndex = j;
                        int temp = array.get(j);
                        array.set(j, array.get(j + 1));
                        array.set(j + 1, temp);

                        swapped = 1;

                        soundPlaying("sound.wav");
                        visualizer.refresh();
                    }
                }
                if(swapped == 0)
                {
                    break;
                }
            }

            currentIndex = -1;
            minIndex = -1;
            visualizer.refresh();
        }
    }
    public static class InsertionSort implements SortStrategy // sound ok
    {
        @Override
        public void sort(ArrayList<Integer> array, Main visualizer)
        {
            int size = array.size();

            for(int i = 0; i < size; i++)
            {
                currentIndex = i;
                int key_value = array.get(i);
                int j = i - 1;

                while(j >= 0 && array.get(j) > key_value)
                {
                    minIndex = j;
                    array.set(j + 1, array.get(j));
                    j = j - 1;

                    soundPlaying("sound.wav");
                    visualizer.refresh();
                }

                array.set(j + 1, key_value);

                visualizer.refresh();
            }

            currentIndex = -1;
            minIndex = -1;
            visualizer.refresh();
        }
    }
    public static class GnomeSort implements SortStrategy // sound ok (ugly) display ok
    {
        @Override
        public void sort(ArrayList<Integer> array, Main visualizer)
        {
            int size = array.size();
            int index = 0;

            while(index < size)
            {
                currentIndex = index;
                minIndex = -1;

                if(index == 0)
                {
                    index++;
                }
                else if(array.get(index) >= array.get(index - 1))
                {
                    index++;
                }
                else
                {
                    minIndex = index - 1;
                    int temp = array.get(index);
                    array.set(index, array.get(index - 1));
                    soundPlaying("sound.wav");
                    array.set(index - 1, temp);

                    visualizer.refresh();

                    index--;
                }
            }

            currentIndex = -1;
            minIndex = -1;
            visualizer.refresh();
        }
    }
    public static class HeapSort implements SortStrategy // sound ok display ok
    {
        @Override
        public void sort(ArrayList<Integer> array, Main visualizer)
        {
            int size = array.size();

            for (int i = size / 2 - 1; i >= 0; i--)
            {
                HelperHeap(array, size, i, visualizer);
            }

            for (int i = size - 1; i > 0; i--)
            {
                currentIndex = 0;
                minIndex = i;

                int temp = array.getFirst();
                array.set(0, array.get(i));
                array.set(i, temp);
                visualizer.refresh();

                currentIndex = -1;
                minIndex = -1;
                HelperHeap(array, i, 0, visualizer);
            }

            currentIndex = -1;
            minIndex = -1;
            visualizer.refresh();
        }

        void HelperHeap(ArrayList<Integer> array, int size, int i, Main visualizer)
        {
            int largest = i;
            int left = 2 * i + 1;
            int right = 2 * i + 2;

            if (left < size && array.get(left) > array.get(largest))
            {
                largest = left;
            }

            if (right < size && array.get(right) > array.get(largest))
            {
                largest = right;
            }

            if (largest != i)
            {
                currentIndex = i;
                minIndex = largest;

                int swap = array.get(i);
                array.set(i, array.get(largest));
                array.set(largest, swap);
                soundPlaying("sound.wav");
                visualizer.refresh();

                HelperHeap(array, size, largest, visualizer);
            }
        }
    }
    public static class QuickSort implements SortStrategy // sound ok display ok
    {
        public void QuickHelper(ArrayList<Integer> array, int left, int right, Main visualizer)
        {
            if (left < right)
            {
                int pivot = array.get(right);
                int i = left - 1;

                for (int j = left; j <= right - 1; j++)
                {
                    currentIndex = j;
                    minIndex = right;

                    if (array.get(j) < pivot)
                    {
                        i++;

                        int temp = array.get(i);
                        array.set(i, array.get(j));
                        soundPlaying("sound.wav");
                        array.set(j, temp);

                        currentIndex = i;
                        minIndex = j;

                        visualizer.refresh();
                    }
                }

                int temp = array.get(i + 1);
                array.set(i + 1, array.get(right));
                array.set(right, temp);

                currentIndex = i;
                minIndex = right;

                visualizer.refresh();

                int pi = i + 1;

                QuickHelper(array, left, pi - 1, visualizer);
                QuickHelper(array, pi + 1, right, visualizer);
            }

            currentIndex = -1;
            minIndex = -1;
            visualizer.refresh();
        }

        @Override
        public void sort(ArrayList<Integer> array, Main visualizer)
        {
            QuickHelper(array, 0, array.size() - 1, visualizer);
        }
    }
    public static class MergeSort implements SortStrategy // sound ok display ok
    {
        public void MergeHelper(ArrayList<Integer> array, int p, int q, int r, Main visualizer)
        {
            int n1 = q - p + 1;
            int n2 = r - q;

            int[] L = new int[n1];
            int[] M = new int[n2];

            for(int i = 0; i < n1; i++)
            {
                L[i] = array.get(p + i);
            }
            for(int j = 0; j < n2; j++)
            {
                M[j] = array.get(q + j + 1);
            }

            int i, j, k;
            i = 0;
            j = 0;
            k = p;

            while(i < n1 && j < n2)
            {
                if(L[i] <= M[j])
                {
                    currentIndex = i;
                    minIndex = j;

                    array.set(k, L[i]);
                    visualizer.refresh();
                    soundPlaying("sound.wav");
                    i++;
                }
                else
                {
                    currentIndex = k;
                    minIndex = j;

                    array.set(k, M[j]);
                    visualizer.refresh();
                    soundPlaying("sound.wav");
                    j++;
                }
                k++;
            }

            while(i < n1)
            {
                array.set(k, L[i]);
                visualizer.refresh();
                i++;
                k++;
            }
            while(j < n2)
            {
                array.set(k, M[j]);
                visualizer.refresh();
                j++;
                k++;
            }

            currentIndex = -1;
            minIndex = -1;
            visualizer.refresh();
        }
        public void Merge(ArrayList<Integer> array, int l, int r, Main visualizer)
        {
            if(l < r)
            {
                int m = (l + r) / 2;

                Merge(array, l, m, visualizer);
                Merge(array, m + 1, r, visualizer);

                MergeHelper(array, l, m, r, visualizer);
            }
        }

        @Override
        public void sort(ArrayList<Integer> array, Main visualizer)
        {
            Merge(array, 0, array.size() - 1, visualizer);
        }
    }
    public static class CountSort implements SortStrategy // sound ok display ok
    {
        @Override
        public void sort(ArrayList<Integer> array, Main visualizer)
        {
            int size = array.size();
            int maxValue = 0;

            for (Integer integer : array)
            {
                maxValue = Math.max(maxValue, integer);
            }

            int[] countArray = new int[maxValue + 1];
            for (int i = 0; i < size; i++)
            {
                countArray[array.get(i)]++;
            }

            int index = 0;
            for (int i = 0; i <= maxValue; i++)
            {
                currentIndex = i;
                while (countArray[i] > 0)
                {
                    minIndex = index;
                    array.set(index, i);
                    countArray[i]--;
                    index++;

                    visualizer.refresh();
                    soundPlaying("sound.wav");
                }
            }

            currentIndex = -1;
            minIndex = -1;
            visualizer.refresh();
        }
    }
    public static class CocktailSort implements SortStrategy // sound ok (ugly)
    {
        @Override
        public void sort(ArrayList<Integer> array, Main visualizer)
        {
            boolean swapped = true;
            int start = 0;
            int end = array.size();

            while (swapped)
            {
                swapped = false;

                for (int i = start; i < end - 1; ++i)
                {
                    currentIndex = i;
                    minIndex = i + 1;

                    if (array.get(i) > array.get(i + 1))
                    {
                        int temp = array.get(i);
                        array.set(i, array.get(i + 1));
                        array.set(i + 1, temp);

                        swapped = true;
                        soundPlaying("sound.wav");
                        visualizer.refresh();
                    }
                }

                if (!swapped)
                {
                    break;
                }

                swapped = false;

                end = end - 1;

                for (int i = end - 1; i >= start; i--)
                {
                    currentIndex = i;
                    minIndex = i + 1;

                    if (array.get(i) > array.get(i + 1))
                    {
                        int temp = array.get(i);
                        array.set(i, array.get(i + 1));
                        array.set(i + 1, temp);

                        swapped = true;
                        soundPlaying("sound.wav");
                        visualizer.refresh();
                    }
                }

                start = start + 1;
            }

            currentIndex = -1;
            minIndex = -1;
            visualizer.refresh();
        }
    }
    public static class RadixSort implements SortStrategy // sound ok display ok (ugly)
    {
        static int getMax(ArrayList<Integer> array, int n)
        {
            int mx = array.getFirst();
            for (int i = 1; i < n; i++)
            {
                if (array.get(i) > mx)
                {
                    mx = array.get(i);
                }
            }
            return mx;
        }
        static void countSort(ArrayList<Integer> array, int n, int exp, Main visualizer)
        {
            int[] output = new int[n];
            int i;
            int[] count = new int[10];
            Arrays.fill(count, 0);

            for (i = 0; i < n; i++)
            {
                currentIndex = i;
                count[(array.get(i) / exp) % 10]++;
                visualizer.refresh();
            }


            for (i = 1; i < 10; i++)
            {
                count[i] += count[i - 1];
            }

            for (i = n - 1; i >= 0; i--)
            {
                int digit = (array.get(i) / exp) % 10;
                currentIndex = i;
                minIndex = count[digit] - 1;
                output[minIndex] = array.get(i);
                count[digit]--;
                visualizer.refresh();
            }

            for (i = 0; i < n; i++)
            {
                currentIndex = i;
                array.set(i, output[i]);
                soundPlaying("sound.wav");
                visualizer.refresh();
            }

            currentIndex = -1;
            minIndex = -1;
            visualizer.refresh();
        }
        @Override
        public void sort(ArrayList<Integer> array, Main visualizer)
        {
            int n = array.size();
            int m = getMax(array, n);

            for (int exp = 1; m / exp > 0; exp *= 10)
            {
                countSort(array, n, exp, visualizer);
            }

            currentIndex = -1;
            minIndex = -1;
            visualizer.refresh();
        }
    }
    public static class BucketSort implements SortStrategy // sound ok display NOT ok -- repair
    {
        public static void insertionSort(ArrayList<Integer> array, Main visualizer)
        {
            for (int i = 1; i < array.size(); i++)
            {
                currentIndex = i;

                int key = array.get(i);
                int j = i - 1;
                while (j >= 0 && array.get(j) > key)
                {
                    minIndex = j;

                    array.set(j + 1, array.get(j));
                    soundPlaying("sound.wav");
                    visualizer.refresh();
                    j--;
                }
                array.set(j + 1, key);
                visualizer.refresh();
            }

            currentIndex = -1;
            minIndex = -1;
        }

        @Override
        public void sort(ArrayList<Integer> array, Main visualizer)
        {
            int n = array.size();
            int maxVal = Collections.max(array);

            ArrayList<ArrayList<Integer>> buckets = new ArrayList<>(n);
            for (int i = 0; i < n; i++)
            {
                buckets.add(new ArrayList<>());
            }

            for (int i = 0; i < n; i++)
            {
                int bi = (int) ((float) array.get(i) / maxVal * (n - 1));
                buckets.get(bi).add(array.get(i));
            }


            for (int i = 0; i < n; i++)
            {
                insertionSort(buckets.get(i), visualizer);
            }

            int index = 0;
            for (int i = 0; i < n; i++)
            {
                for (int j = 0; j < buckets.get(i).size(); j++)
                {
                    currentIndex = index;
                    minIndex = j;

                    array.set(index++, buckets.get(i).get(j));
                    soundPlaying("sound.wav");
                    visualizer.refresh();
                }
            }

            currentIndex = -1;
            minIndex = -1;
            visualizer.refresh();
        }
    }
    public static class BingoSort implements SortStrategy // sound ok display ok
    {
        static int bingo;
        static int nextBingo;

        static void maxMin(ArrayList<Integer> array, int n)
        {
            for (int i = 1; i < n; i++)
            {
                bingo = Math.min(bingo, array.get(i));
                nextBingo = Math.max(nextBingo, array.get(i));
            }
        }
        @Override
        public void sort(ArrayList<Integer> array, Main visualizer)
        {
            int n = array.size();
            bingo = array.getFirst();
            nextBingo = array.getFirst();

            maxMin(array, n);

            int largestEle = nextBingo;
            int nextElePos = 0;

            while (bingo < nextBingo)
            {
                int startPos = nextElePos;
                currentIndex = bingo;
                for (int i = startPos; i < n; i++)
                {
                    minIndex = i;
                    if (array.get(i) == bingo)
                    {
                        int temp = array.get(i);
                        array.set(i, array.get(nextElePos));
                        array.set(nextElePos, temp);

                        visualizer.refresh();
                        soundPlaying("sound.wav");

                        nextElePos = nextElePos + 1;
                    }

                    else if (array.get(i) < nextBingo)
                    {
                        nextBingo = array.get(i);
                    }
                }
                bingo = nextBingo;
                nextBingo = largestEle;
            }

            minIndex = -1;
            currentIndex = -1;
            visualizer.refresh();
        }
    }
    public static class ShellSort implements SortStrategy // sound ok display ok
    {
        @Override
        public void sort(ArrayList<Integer> array, Main visualizer)
        {
            int n = array.size();

            for (int gap = n/2; gap > 0; gap /= 2)
            {
                currentIndex = gap;
                for (int i = gap; i < n; i += 1)
                {
                    minIndex = i;
                    int temp = array.get(i);

                    int j;
                    for (j = i; j >= gap && array.get(j - gap) > temp; j -= gap)
                    {
                        helper_for_shell = j - gap;
                        array.set(j, array.get(j - gap));
                        visualizer.refresh();
                        soundPlaying("sound.wav");
                    }

                    array.set(j, temp);
                    visualizer.refresh();
                }
            }

            currentIndex = -1;
            minIndex = -1;
            helper_for_shell = -1;
            visualizer.refresh();
        }
    }
    public static class CombSort implements SortStrategy // sound ok display ok
    {
        int getNextGap(int gap)
        {
            gap = (gap * 10) / 13;
            return Math.max(gap, 1);
        }

        @Override
        public void sort(ArrayList<Integer> array, Main visualizer)
        {
            int n = array.size();

            int gap = n;

            boolean swapped = true;

            while (gap != 1 || swapped)
            {
                gap = getNextGap(gap);
                currentIndex = gap;
                swapped = false;

                for (int i = 0; i < n - gap; i++)
                {
                    minIndex = i;
                    if (array.get(i) > array.get(i + gap))
                    {
                        helper_for_shell = i + gap;
                        int temp = array.get(i);
                        array.set(i, array.get(i + gap));
                        array.set(i + gap, temp);

                        visualizer.refresh();
                        soundPlaying("sound.wav");
                        swapped = true;
                    }
                }
            }

            minIndex = -1;
            currentIndex = -1;
            helper_for_shell = -1;
            visualizer.refresh();
        }
    }
    public static class PigeonSort implements SortStrategy // sound ok display ok
    {
        @Override
        public void sort(ArrayList<Integer> array, Main visualizer)
        {
            int n = array.size();
            int min = array.getFirst();
            int max = array.getFirst();
            int range, i, j, index;

            for(int a = 0; a < n; a++)
            {
                if(array.get(a) > max)
                {
                    max = array.get(a);
                }
                if(array.get(a) < min)
                {
                    min = array.get(a);
                }
            }

            range = max - min + 1;
            int[] phole = new int[range];
            Arrays.fill(phole, 0);

            for(i = 0; i < n; i++)
            {
                currentIndex = i;
                phole[array.get(i) - min]++;
                visualizer.refresh();
            }


            index = 0;

            for(j = 0; j < range; j++)
            {
                currentIndex = j;
                while(phole[j]-->0)
                {
                    minIndex = index + 1;
                    array.set(index++, j + min);
                    visualizer.refresh();
                    soundPlaying("sound.wav");
                }
            }

            currentIndex = -1;
            minIndex = -1;
            visualizer.refresh();
        }
    }
    public static class CycleSort implements SortStrategy // sound ok
    {
        static void swap(ArrayList<Integer> array, int i, int correctpos)
        {
            int temp = array.get(i);
            array.set(i, array.get(correctpos));
            array.set(correctpos, temp);
        }

        @Override
        public void sort(ArrayList<Integer> array, Main visualizer)
        {
            int i = 0;
            int n = array.size();
            while (i < n)
            {
                currentIndex = i;
                int correctpos = array.get(i) - 1;
                minIndex = correctpos;
                if (array.get(i) < n && !Objects.equals(array.get(i), array.get(correctpos)))
                {
                    swap(array, i, correctpos);
                    visualizer.refresh();
                    soundPlaying("sound.wav");
                }
                else
                {
                    i++;
                }
            }

            minIndex = -1;
            currentIndex = -1;
            visualizer.refresh();
        }
    }

    public static void main(String[] args)
    {
        ArrayList<Integer> array = new ArrayList<>();
        for(int i = 1; i < 51; i++)
        {
            array.add(i);
        }
        Collections.shuffle(array);
        ORIGINAL_ARRAY.addAll(array);

        Main visualizer = new Main(array);

        JFrame frame = new JFrame("Sort Visualizer");

        final DefaultListModel<String> list = new DefaultListModel<>();
        list.addElement("Selection Sort");
        list.addElement("Bubble Sort");
        list.addElement("Insertion Sort");
        list.addElement("Gnome Sort");
        list.addElement("Heap Sort");
        list.addElement("Quick Sort");
        list.addElement("Merge Sort");
        list.addElement("Count Sort");
        list.addElement("Cocktail Sort");
        list.addElement("Radix Sort");
        list.addElement("Bucket Sort");
        list.addElement("Bingo Sort");
        list.addElement("Shell Sort");
        list.addElement("Comb Sort");
        list.addElement("Pigeonhole Sort");
        list.addElement("Cycle Sort");

        final JList<String> list1 = new JList<>(list);
        list1.setBounds(570, 60, 130, 380);
        list1.setFont(new Font("Arial", Font.PLAIN, 18));
        frame.add(list1);

        JButton newAlgorithm = new JButton("Restart");
        newAlgorithm.setBounds(570, 460, 130, 30);

        newAlgorithm.addActionListener(e -> {
            if (sortingThread != null && sortingThread.isAlive()) {
                sortingThread.interrupt();
            }

            // Сбрасываем массив к исходному состоянию
            array.clear();
            array.addAll(ORIGINAL_ARRAY);
            Collections.shuffle(array);

            // Очищаем визуальные индикаторы
            currentIndex = -1;
            minIndex = -1;
            helper_for_shell = -1;
            visualizer.refresh();
        });
        frame.add(newAlgorithm);

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        frame.add(visualizer);

        visualizer.setBounds(5, 60, 520, 500);

        frame.setVisible(true);


        JLabel l1 = new JLabel();
        l1.setBounds(10, 15, 120, 30);
        l1.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel l2 = new JLabel();
        l2.setBounds(140, 15, 90, 30);
        l2.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel l3 = new JLabel();
        l3.setBounds(230, 15, 65, 30);
        l3.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel l4 = new JLabel();
        l4.setBounds(325, 15, 90, 30);
        l4.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel l5 = new JLabel();
        l5.setBounds(405, 15, 65, 30);
        l5.setFont(new Font("Arial", Font.PLAIN, 16));

        list1.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
            {
                int selectedIndex = list1.getSelectedIndex();

                switch (selectedIndex) {
                    case 0 -> {
                        visualizer.visualizeSort(new SelectionSort());
                        l1.setText("Selection Sort");

                        l2.setText("Worst O(n): ");
                        l3.setText("n²");
                        l4.setText("Best O(n): ");
                        l5.setText("n²");
                    }
                    case 1 -> {
                        visualizer.visualizeSort(new BubbleSort());
                        l1.setText("Bubble Sort");

                        l2.setText("Worst O(n): ");
                        l3.setText("n²");
                        l4.setText("Best O(n): ");
                        l5.setText("n");
                    }
                    case 2 -> {
                        visualizer.visualizeSort(new InsertionSort());
                        l1.setText("Insertion Sort");

                        l2.setText("Worst O(n): ");
                        l3.setText("n²");
                        l4.setText("Best O(n): ");
                        l5.setText("n");
                    }
                    case 3 -> {
                        visualizer.visualizeSort(new GnomeSort());
                        l1.setText("Gnome Sort");

                        l2.setText("Worst O(n): ");
                        l3.setText("n²");
                        l4.setText("Best O(n): ");
                        l5.setText("n");
                    }
                    case 4 -> {
                        visualizer.visualizeSort(new HeapSort());
                        l1.setText("Heap Sort");

                        l2.setText("Worst O(n): ");
                        l3.setText("n * log(n)");
                        l4.setText("Best O(n): ");
                        l5.setText("n * log(n)");
                    }
                    case 5 -> {
                        visualizer.visualizeSort(new QuickSort());
                        l1.setText("Quick Sort");

                        l2.setText("Worst O(n): ");
                        l3.setText("n²");
                        l4.setText("Best O(n): ");
                        l5.setText("n * log(n)");
                    }
                    case 6 -> {
                        visualizer.visualizeSort(new MergeSort());
                        l1.setText("Merge Sort");

                        l2.setText("Worst O(n): ");
                        l3.setText("n * log(n)");
                        l4.setText("Best O(n): ");
                        l5.setText("n * log(n)");
                    }
                    case 7 -> {
                        visualizer.visualizeSort(new CountSort());
                        l1.setText("Count Sort");

                        l2.setText("Worst O(n): ");
                        l3.setText("n + k");
                        l4.setText("Best O(n): ");
                        l5.setText("n + k");
                    }
                    case 8 -> {
                        visualizer.visualizeSort(new CocktailSort());
                        l1.setText("Cocktail Sort");

                        l2.setText("Worst O(n): ");
                        l3.setText("n²");
                        l4.setText("Best O(n): ");
                        l5.setText("n");
                    }
                    case 9 -> {
                        visualizer.visualizeSort(new RadixSort());
                        l1.setText("Radix Sort");

                        l2.setText("Worst O(n): ");
                        l3.setText("d(n + k)");
                        l4.setText("Best O(n): ");
                        l5.setText("d(n + k)");
                    }
                    case 10 -> {
                        visualizer.visualizeSort(new BucketSort());
                        l1.setText("Bucket Sort");

                        l2.setText("Worst O(n): ");
                        l3.setText("n + k");
                        l4.setText("Best O(n): ");
                        l5.setText("n + k");
                    }
                    case 11 -> {
                        visualizer.visualizeSort(new BingoSort());
                        l1.setText("Bingo Sort");

                        l2.setText("Worst O(n): ");
                        l3.setText("n²");
                        l4.setText("Best O(n): ");
                        l5.setText("n");
                    }
                    case 12 -> {
                        visualizer.visualizeSort(new ShellSort());
                        l1.setText("Shell Sort");

                        l2.setText("Worst O(n): ");
                        l3.setText("n log²n");
                        l4.setText("Best O(n): ");
                        l5.setText("n");
                    }
                    case 13 -> {
                        visualizer.visualizeSort(new CombSort());
                        l1.setText("Comb Sort");

                        l2.setText("Worst O(n): ");
                        l3.setText("n²");
                        l4.setText("Best O(n): ");
                        l5.setText("n * log(n)");
                    }
                    case 14 -> {
                        visualizer.visualizeSort(new PigeonSort());
                        l1.setText("Pigeon Sort");

                        l2.setText("Worst O(n): ");
                        l3.setText("n + k");
                        l4.setText("Best O(n): ");
                        l5.setText("n + k");
                    }
                    case 15 -> {
                        visualizer.visualizeSort(new CycleSort());
                        l1.setText("Cycle Sort");

                        l2.setText("Worst O(n): ");
                        l3.setText("n²");
                        l4.setText("Best O(n): ");
                        l5.setText("n²");
                    }
                }
            }
        });

        frame.add(l1);
        frame.add(l2);
        frame.add(l3);
        frame.add(l4);
        frame.add(l5);
    }
}
