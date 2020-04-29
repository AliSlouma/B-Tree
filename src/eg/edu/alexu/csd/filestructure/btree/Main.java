package eg.edu.alexu.csd.filestructure.btree;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        BTree<Integer, String> ibTree = new BTree<>(3);

        Integer[] arr = {1, 3, 7, 10, 11, 13, 14, 15, 18, 16, 19, 24, 25, 26, 21, 4, 5, 20, 22, 2, 17, 12, 6};
        Integer[] arr2 = {3, 19, 15, 6, 20, 26, 14, 21, 13, 25, 1, 7, 5, 16, 4, 11, 18, 22, 10, 2, 12, 24, 17};

        for (Integer x : arr) {
            ibTree.insert(x, "Soso" + x);
        }

        for(Integer x : arr2) {
            System.out.println(ibTree.delete(x));
        }
    }
}
