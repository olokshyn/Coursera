import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation
{
    public static void main(String[] args)
    {
        if (args.length < 1)
        {
            throw new IllegalArgumentException();
        }
        RandomizedQueue<String> randQueue = new RandomizedQueue<>();
        int k = Integer.parseInt(args[0]);
        for (int i = 0; i != k; ++i)
        {
            randQueue.enqueue(StdIn.readString());
        }
        for (String str: randQueue)
        {
            if (k == 0)
            {
                break;
            }
            --k;
            StdOut.println(str);
        }
    }
}
