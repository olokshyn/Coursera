import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item>
{
    private Node first;
    private Node last;
    private int size;

    private class Node
    {
        Item item;
        Node next;
        Node prev;

        Node(Item item)
        {
            this.item = item;
            next = null;
            prev = null;
        }
    }

    private class ListIterator implements Iterator<Item>
    {
        private Node current;

        ListIterator(Node current)
        {
            this.current = current;
        }

        public boolean hasNext()
        {
            return current != null;
        }

        public Item next()
        {
            if (!hasNext())
            {
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public Deque()
    {
        first = null;
        last = null;
    }

    public boolean isEmpty()
    {
        return first == null;
    }

    public int size()
    {
        return size;
    }

    public void addFirst(Item item)
    {
        if (item == null)
        {
            throw new IllegalArgumentException();
        }
        Node newFirst = new Node(item);
        newFirst.next = first;
        if (first != null)
        {
            first.prev = newFirst;
        }
        first = newFirst;
        if (last == null)
        {
            last = first;
        }
        ++size;
    }

    public void addLast(Item item)
    {
        if (item == null)
        {
            throw new IllegalArgumentException();
        }
        Node newLast = new Node(item);
        newLast.prev = last;
        if (last != null)
        {
            last.next = newLast;
        }
        last = newLast;
        if (first == null)
        {
            first = last;
        }
        ++size;
    }

    public Item removeFirst()
    {
        if (first == null)
        {
            throw new NoSuchElementException();
        }
        Item item = first.item;
        if (first == last)
        {
            last = null;
        }
        else
        {
            first.next.prev = null;
        }
        first = first.next;
        --size;
        return item;
    }

    public Item removeLast()
    {
        if (last == null)
        {
            throw new NoSuchElementException();
        }
        Item item = last.item;
        if (first == last)
        {
            first = null;
        }
        else
        {
            last.prev.next = null;
        }
        last = last.prev;
        --size;
        return item;
    }

    public Iterator<Item> iterator()
    {
        return new ListIterator(first);
    }
}
