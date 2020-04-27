package eg.edu.alexu.csd.filestructure.btree;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        BTree<String, String> ibTree = new BTree<>();
        String str[] = {"F", "S", "Q", "K", "C", "L", "H", "T", "V", "W", "M", "R", "N", "P", "A", "B", "X", "Y", "D", "Z", "E"};
        for (String x : str) {
            ibTree.insert(x, x);
        }
        for(String x : str) {
            System.out.print(x);
            System.out.print(" ");
        }
        System.out.println();
        for(String x : str) {
            IBTreeNodeExtended node = ibTree.B_Tree_Search2((IBTreeNodeExtended) ibTree.getRoot(), x);
            int t = node.getKeys().indexOf(x) + 1;
            Object arr[] = ibTree.predecessor(node, t);
            int i;
            IBTreeNodeExtended m;
            if (arr[0] != null || arr[1] != null) {
                i = (Integer) arr[1];
                m = (IBTreeNodeExtended) arr[0];
                System.out.print(m.getKey(i));
                System.out.print(" ");
            }else System.out.print("  ");
        }
    }
}
