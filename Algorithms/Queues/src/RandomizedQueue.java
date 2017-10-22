import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item>
{
    private Item[] items;
    private int n;

    private class ArrayIterator implements Iterator<Item>
    {
        private final Item[] items;
        private final int[] indexes;
        private int current;
        private final int size;

        public ArrayIterator(Item[] items, int current, int size)
        {
            this.items = items;
            indexes = StdRandom.permutation(size);
            this.current = current;
            this.size = size;
        }

        public boolean hasNext()
        {
            return current != size;
        }

        public Item next()
        {
            if (!hasNext())
            {
                throw new NoSuchElementException();
            }
            return items[indexes[current++]];
        }
    }

    public RandomizedQueue()
    {
        items = (Item[]) new Object[1];
        n = 0;
    }

    public boolean isEmpty()
    {
        return n == 0;
    }

    public int size()
    {
        return n;
    }

    public void enqueue(Item item)
    {
        if (item == null)
        {
            throw new IllegalArgumentException();
        }
        if (n == items.length)
        {
            resize(2 * items.length);
        }
        items[n++] = item;
    }

    public Item dequeue()
    {
        if (n == 0)
        {
            throw new NoSuchElementException();
        }

        int index = StdRandom.uniform(n);
        Item item = items[index];
        items[index] = items[--n];
        items[n] = null;
        if (n == items.length / 4)
        {
            resize(Math.max(items.length / 2, 1));
        }
        return item;
    }

    public Item sample()
    {
        if (n == 0)
        {
            throw new NoSuchElementException();
        }

        return items[StdRandom.uniform(n)];
    }

    public Iterator<Item> iterator()
    {
        return new ArrayIterator(items, 0, n);
    }

    private void resize(int size)
    {
        if (size == items.length)
        {
            return;
        }
        Item[] newItems = (Item[]) new Object[size];
        for (int i = 0; i != n; ++i)
        {
            newItems[i] = items[i];
        }
        items = newItems;
    }
}