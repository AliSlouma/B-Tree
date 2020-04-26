package eg.edu.alexu.csd.filestructure.btree;

import javax.management.RuntimeErrorException;
import java.util.LinkedList;
import java.util.Queue;

public class BTree<K extends Comparable<K>, V> implements IBTree, IBTreeDisplay {
    private int minimumDegree = 2;
    private IBTreeNodeExtended<K, V> root;
    private int maxKeys = 2 * this.minimumDegree - 1;
    private int minKeys = this.minimumDegree - 1;
    private int size = 0; // just for debug
    private int foundAgain = 0; // just for debug

    public BTree() {}
    public BTree(int minimumDegree) {
        if(minimumDegree < 2)
            throw new RuntimeErrorException(null);
        this.minimumDegree = minimumDegree;
        this.maxKeys = 2 * this.minimumDegree - 1;
        this.minKeys = this.minimumDegree - 1;
    }

    @Override
    public int getMinimumDegree() {
        return this.minimumDegree;
    }

    @Override
    public IBTreeNode getRoot() {
        return this.root;
    }

    @Override
    public void insert(Comparable key, Object value) {
        if(key == null || value == null) throw new RuntimeErrorException(null);
        if(root != null) {
            IBTreeNodeExtended node = this.B_Tree_Search2(this.root, key);
            if(node != null) {
                node.setValue(node.getKeys().indexOf(key), value);
                ++this.foundAgain;
                return;
            }
        }
        if(root == null) this.B_Tree_Create();
        ++this.size;
        if(this.root.getNumOfKeys() == this.maxKeys) {
            IBTreeNodeExtended s = new BTreeNode();
            s.setLeaf(false);
            s.addChild(this.root);
            this.root = s;
            this.B_Tree_Split(this.root, 1);
        }
        this.B_Tree_Insert_NonFull(this.root, key, value);
    }

    private void B_Tree_Create() {
        this.root = new BTreeNode<>();
    }

    private void B_Tree_Insert_NonFull(IBTreeNodeExtended root, Comparable key, Object value) {
        int i = root.getNumOfKeys();
        if(root.isLeaf()) {
            while(i >= 1 && key.compareTo(root.getKey(i)) < 0)
                --i;
            ++i;
            root.addKey(i, key);
            root.addValue(i, value);
            root.setNumOfKeys(root.getNumOfKeys() + 1);
        } else {
            while(i >= 1 && key.compareTo(root.getKey(i)) < 0)
                --i;
            ++i;
            if(root.getChild(i).getNumOfKeys() == this.maxKeys) {
                B_Tree_Split(root, i);
                if(key.compareTo(root.getKey(i)) > 0)
                    ++i;
            }
            this.B_Tree_Insert_NonFull(root.getChild(i), key, value);
        }
    }


    private void B_Tree_Split(IBTreeNodeExtended root, int i) {
        IBTreeNodeExtended newSibling = new BTreeNode();
        IBTreeNodeExtended wantedChild = root.getChild(i);
        newSibling.setNumOfKeys(this.minKeys);
        newSibling.setLeaf(wantedChild.isLeaf());
        for (int j = this.minKeys; j >= 1; --j) {
            newSibling.addKey(1, wantedChild.getKey(j + this.minimumDegree));
            newSibling.addValue(1, wantedChild.getValue(j + this.minimumDegree));
            wantedChild.deleteKey(j + this.minimumDegree);
            wantedChild.deleteValue(j + this.minimumDegree);
        }
        if(!wantedChild.isLeaf()) {
            for(int j = this.minimumDegree; j >= 1; --j) {
                newSibling.addChild(1, wantedChild.getChild(j + this.minimumDegree));
                wantedChild.deleteChild(j + this.minimumDegree);
            }
        }
        wantedChild.setNumOfKeys(this.minKeys);
        root.addKey(i, wantedChild.getKey(this.minimumDegree));
        root.addValue(i, wantedChild.getValue(this.minimumDegree));
        wantedChild.deleteKey(this.minimumDegree);
        wantedChild.deleteValue(this.minimumDegree);
        root.addChild(i+1, newSibling);
        root.setNumOfKeys(root.getNumOfKeys() + 1);
    }

    @Override
    public Object search(Comparable key) {
        return this.B_Tree_Search(this.root, key);
    }

    private Object B_Tree_Search(IBTreeNodeExtended<K, V> root, Comparable key) {
        if(root == null || key == null) throw new RuntimeErrorException(null);
        int i = 1;
        while(i <= root.getNumOfKeys() && key.compareTo(root.getKey(i)) > 0)
            ++i;
        if(i <= root.getNumOfKeys() && key.compareTo(root.getKey(i)) == 0)
            return root.getValue(i);
        else if(root.isLeaf())
            return null;
        else
            return this.B_Tree_Search(root.getChild(i), key);
    }

    private IBTreeNodeExtended B_Tree_Search2(IBTreeNodeExtended root, Comparable key) {
        if(root == null || key == null) throw new RuntimeErrorException(null);
        int i = 1;
        while(i <= root.getNumOfKeys() && key.compareTo(root.getKey(i)) > 0)
            ++i;
        if(i <= root.getNumOfKeys() && key.compareTo(root.getKey(i)) == 0)
            return root;
        else if(root.isLeaf())
            return null;
        else
            return this.B_Tree_Search2(root.getChild(i), key);
    }

    @Override
    public boolean delete(Comparable key) {
        return false;
    }

    public IBTreeNodeExtended minimumNode(IBTreeNodeExtended node) {
        while(node != null && !node.isLeaf()) {
            node = node.getChild(1);
        }
        return node;
    }

    public IBTreeNodeExtended maximumNode(IBTreeNodeExtended node) {
        while(node != null && !node.isLeaf()) {
            node = node.getChild(node.getNumOfKeys() + 1);
        }
        return node;
    }

    @Override
    public void B_Tree_Display(IBTreeNodeExtended root) {
        Queue<IBTreeNodeExtended> waitedNodes = new LinkedList<>();
        waitedNodes.add(root);
        while(!waitedNodes.isEmpty()) {
            IBTreeNodeExtended current = waitedNodes.remove();
            if(!current.isLeaf())
                waitedNodes.addAll(current.getChildren().subList(1, current.getNumOfKeys() + 2));
            if(current != null)
                System.out.println(current.getKeys().subList(1, current.getNumOfKeys() + 1));
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
