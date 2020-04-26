package eg.edu.alexu.csd.filestructure.btree;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        BTree<Integer, String> btree = new BTree<>();
        try {
            Random r = new Random();
            TreeSet<Integer> set = new TreeSet<>();
            for (int i = 0; i < 1000000; i++) {
                int key = r.nextInt(Integer.MAX_VALUE);
                btree.insert(key, "Soso" + key);
                set.add(key);
            }
            for(Integer x : set) {
                System.out.println(x);
            }
        } catch (Throwable e) {

        }
    }
}
