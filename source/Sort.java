import java.lang.reflect.Array;
import java.util.ArrayList;

public class Sort
{
    Sort() {}

    public int sortSelection(ArrayList<Integer> array)
    {
        int size = array.size();
        int counter = 0;

        for (int i = 0; i < size - 1; i++)
        {
            int min_index = i;

            for (int j = i + 1; j < size; j++)
            {
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

                counter++;
            }
        }
        return counter;
    }

    public int sortBubble(ArrayList<Integer> array)
    {
        int size = array.size();
        int swapped;

        int counter = 0;
        for(int i = 0; i < size - 1; i++)
        {
            swapped = 0;
            for(int j = 0; j < size - i - 1; j++)
            {
                if(array.get(j) > array.get(j + 1))
                {
                    int temp = array.get(j);
                    array.set(j, array.get(j + 1));
                    array.set(j + 1, temp);
                    swapped = 1;

                    counter++;
                }
            }

            if(swapped == 0)
            {
                break;
            }
        }

        return counter;
    }

    public int sortInsertion(ArrayList<Integer> array)
    {
        int size = array.size();
        int counter = 0;

        for(int i = 0; i < size; i++)
        {
            int key_value = array.get(i);
            int j = i - 1;

            while(j >= 0 && array.get(j) > key_value)
            {
                array.set(j + 1, array.get(j));
                j = j - 1;

                counter++;
            }

            array.set(j + 1, key_value);

            counter++;
        }

        return counter;
    }



    static void sortMerge(int[] array, int left, int right)
    {
        if (left < right)
        {
            int m = left + (right - left) / 2;

            sortMerge(array, left, m);
            sortMerge(array, m + 1, right);

            int n1 = m - left + 1;
            int n2 = right - m;

            int[] Left = new int[n1];
            int[] Right = new int[n2];

            System.arraycopy(array, left, Left, 0, n1);
            for (int j = 0; j < n2; ++j)
            {
                Right[j] = array[m + 1 + j];
            }

            int i = 0, j = 0;
            int k = left;

            while (i < n1 && j < n2)
            {
                if (Left[i] <= Right[j])
                {
                    array[k] = Left[i];
                    i++;
                }
                else
                {
                    array[k] = Right[j];
                    j++;
                }
                k++;
            }

            while (i < n1)
            {
                array[k] = Left[i];
                i++;
                k++;
            }

            while (j < n2)
            {
                array[k] = Right[j];
                j++;
                k++;
            }
        }
    }

    static void sortQuick(ArrayList<Integer> array, int left, int right)
    {
        if (left < right)
        {
            int pivot = array.get(right);
            int i = left - 1;

            for (int j = left; j <= right - 1; j++)
            {
                if (array.get(j) < pivot)
                {
                    int temp = array.get(i);
                    array.set(i, array.get(j));
                    array.set(j, temp);

                    i++;
                }
            }

            int temp = array.get(i + 1);
            array.set(i + 1, array.get(right));
            array.set(right, temp);

            int pi = i + 1;

            sortQuick(array, left, pi - 1);
            sortQuick(array, pi + 1, right);
        }
    }



    int counter_for_heap = 0;
    public int sortHeap(ArrayList<Integer> array)
    {
        int size = array.size();

        for (int i = size / 2 - 1; i >= 0; i--)
        {
            HelperHeap(array, size, i);
        }

        for (int i = size - 1; i > 0; i--)
        {
            int temp = array.getFirst();
            array.set(0, array.get(i));
            array.set(i, temp);

            counter_for_heap++;

            HelperHeap(array, i, 0);
        }

        return counter_for_heap;
    }
    void HelperHeap(ArrayList<Integer> array, int size, int i)
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
            int swap = array.get(i);
            array.set(i, array.get(largest));
            array.set(largest, swap);

            counter_for_heap++;

            HelperHeap(array, size, largest);
        }
    }
    public int sortGnome(ArrayList<Integer> array)
    {
        int size = array.size();
        int index = 0;

        int counter = 0;

        while(index < size)
        {
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
                int temp = array.get(index);
                array.set(index, array.get(index - 1));
                array.set(index - 1, temp);
                counter++;
                index--;
            }
        }
        return counter;
    }
}
