public class Main
{

    public static void main(String[] args)
    {
//        Deque<Integer> deque = new Deque<>();
//        deque.addFirst(10);
//        deque.addFirst(15);
//        deque.addLast(5);
//        deque.removeLast();
//        deque.removeFirst();
//        deque.removeLast();
//        deque.addFirst(20);
//        deque.addLast(25);
//
//        for (int a: deque)
//        {
//            System.out.println(a);
//        }

        RandomizedQueue<Integer> randQueue = new RandomizedQueue<>();
        randQueue.enqueue(28);
        randQueue.dequeue();
        randQueue.enqueue(35);
//        randQueue.enqueue(1);
//        randQueue.enqueue(2);
//        randQueue.enqueue(3);
//        randQueue.enqueue(4);
//        randQueue.enqueue(5);
//        randQueue.dequeue();
//        randQueue.dequeue();
//        randQueue.dequeue();
//        randQueue.enqueue(6);
//        randQueue.enqueue(7);
//        randQueue.enqueue(8);
//        randQueue.dequeue();
//        randQueue.dequeue();
//        randQueue.dequeue();
//        randQueue.dequeue();
//        randQueue.enqueue(9);
//        randQueue.enqueue(10);
//        randQueue.enqueue(11);
//        randQueue.enqueue(12);

        for (int a : randQueue)
        {
            System.out.println(a);
        }
    }
}
