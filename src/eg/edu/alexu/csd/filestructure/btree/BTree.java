package eg.edu.alexu.csd.filestructure.btree;

import javax.management.RuntimeErrorException;
import java.util.LinkedList;
import java.util.Queue;

public class BTree<K extends Comparable<K>, V> implements IBTree, IBTreeDisplay {
    private int minimumDegree = 2;
    private IBTreeNodeExtended<K, V> root;
    private int maxKeys = 2 * this.minimumDegree - 1;
    private int minKeys = this.minimumDegree - 1;

    public BTree() {}
    public BTree(int minimumDegree) {
        if(minimumDegree < 2) // checks for valid minimumDegrees
            throw new RuntimeErrorException(null);
        this.minimumDegree = minimumDegree;
        this.maxKeys = 2 * this.minimumDegree - 1;
        this.minKeys = this.minimumDegree - 1;
    }

    /**
     * @return minimumDegree of the tree
     */
    @Override
    public int getMinimumDegree() {
        return this.minimumDegree;
    }

    /**
     * @return the root of this tree
     */
    @Override
    public IBTreeNode getRoot() {
        return this.root;
    }

    /**
     * insert key and value in a specific root
     * @param key
     * @param value
     */
    @Override
    public void insert(Comparable key, Object value) {
        if(key == null || value == null) throw new RuntimeErrorException(null);
        if(root != null) {
            IBTreeNodeExtended node = this.B_Tree_Search2(this.root, key);
            if(node != null) {
                node.setValue(node.getKeys().indexOf(key), value);
                return;
            }
        }
        if(root == null) this.B_Tree_Create();
        if(this.root.getNumOfKeys() == this.maxKeys) {
            IBTreeNodeExtended s = new BTreeNode();
            s.setLeaf(false);
            s.addChild(this.root);
            this.root.setParent(s);            // exclusively added watch out
            this.root = s;
            this.B_Tree_Split(this.root, 1);
        }
        this.B_Tree_Insert_NonFull(this.root, key, value);
    }

    private void B_Tree_Create() {
        this.root = new BTreeNode<>();
    }

    /**
     * this procedure makes the actual insertion
     * @param root
     * @param key
     * @param value
     */
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

    /**
     * split wanted node that is " root.getChild(i) is full node " and root is guarantied to be nonFull node
     * @param root
     * @param i
     */
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
                newSibling.getChild(1).setParent(newSibling);    // exclusively added
                wantedChild.deleteChild(j + this.minimumDegree);
            }
        }
        wantedChild.setNumOfKeys(this.minKeys);
        root.addKey(i, wantedChild.getKey(this.minimumDegree));
        root.addValue(i, wantedChild.getValue(this.minimumDegree));
        wantedChild.deleteKey(this.minimumDegree);
        wantedChild.deleteValue(this.minimumDegree);
        root.addChild(i+1, newSibling);
        newSibling.setParent(root);      // exclusively added
        root.setNumOfKeys(root.getNumOfKeys() + 1);
    }

    /**
     *
     * @param key seaerch for key.
     * @return value corresponding to that key.
     * useus an uxiliary procedure B_Tree_Search that uses recursion on nodes.
     */
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

    /**
     * Search for key and returns wanted node
     * @param root start search from this root of this subtree
     * @param key search for this key
     * @return node contains the searched key
     */
    public IBTreeNodeExtended B_Tree_Search2(IBTreeNodeExtended root, Comparable key) {
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
        if(key == null || this.root == null)
            throw new RuntimeErrorException(null);
        if(this.root.isLeaf()) {
            if (this.root.getNumOfKeys() == 0)
                return false;

        }
        if(this.B_Tree_Search2(this.root, key) == null)
            return false;
        boolean operationIsDone = this.B_Tree_Delete(this.root, key);
        if(this.root.getChildren().size() == 1) {
            this.root.getChild(1).setParent(null);
            this.root = this.root.getChild(1);
        }
        return operationIsDone;
    }

    private boolean B_Tree_Delete(IBTreeNodeExtended<K,V> root, Comparable key) {
        int i;

        for(i = 1; i <= root.getNumOfKeys() && key.compareTo(root.getKey(i)) > 0; ++i);
        if(i <= root.getNumOfKeys() && key.compareTo(root.getKey(i)) == 0) {
            if(root.isLeaf()) {
                root.deleteKey(i);
                root.deleteValue(i);
                root.setNumOfKeys(root.getNumOfKeys() - 1);
                return true;
            }
            else {
                if(root.getChild(i).getNumOfKeys() >= this.minimumDegree) {
                    Object[] pre = this.predecessor(root, i);
                    IBTreeNodeExtended auxiliary = (IBTreeNodeExtended) pre[0];
                    int index = (int) pre[1];
                    root.setKey(i, (K) auxiliary.getKey(index));
                    root.setValue(i, (V) auxiliary.getValue(index));
                    return this.B_Tree_Delete(root.getChild(i), root.getKey(i));
                } else if(root.getChild(i+1).getNumOfKeys() >= this.minimumDegree) {
                    Object[] pre = this.successor(root, i);
                    IBTreeNodeExtended auxiliary = (IBTreeNodeExtended) pre[0];
                    int index = (int) pre[1];
                    root.setKey(i, (K) auxiliary.getKey(index));
                    root.setValue(i, (V) auxiliary.getValue(index));
                    return this.B_Tree_Delete(root.getChild(i+1), root.getKey(i));
                } else {
                    this.merge(root, root.getChild(i), i);
                    this.B_Tree_Delete(root.getChild(i), key);
                }
            }
        } else if (!root.isLeaf()){
            IBTreeNodeExtended wantedChild = root.getChild(i);
            if(wantedChild.getNumOfKeys() > this.minKeys)
                return this.B_Tree_Delete(wantedChild, key);
            else {
                if (root.getChild(i - 1) != null && root.getChild(i - 1).getNumOfKeys() > this.minKeys) {
                    IBTreeNodeExtended leftSibling = root.getChild(i - 1);
                    wantedChild.addKey(1, root.getKey(i - 1));
                    root.setKey(i - 1, (K) leftSibling.getKey(leftSibling.getNumOfKeys()));
                    leftSibling.deleteKey(leftSibling.getNumOfKeys());
                    wantedChild.addValue(1, root.getValue(i - 1));
                    root.setValue(i - 1, (V) leftSibling.getValue(leftSibling.getNumOfKeys()));
                    leftSibling.deleteValue(leftSibling.getNumOfKeys());
                    if (!wantedChild.isLeaf()) {
                        leftSibling.getChild(leftSibling.getNumOfKeys() + 1).setParent(wantedChild);
                        wantedChild.addChild(1, leftSibling.getChild(leftSibling.getNumOfKeys() + 1));
                        leftSibling.deleteChild(leftSibling.getNumOfKeys() + 1);
                    }
                    wantedChild.setNumOfKeys(wantedChild.getNumOfKeys() + 1);
                    leftSibling.setNumOfKeys(leftSibling.getNumOfKeys() - 1);
                } else if (i <= root.getNumOfKeys() && root.getChild(i + 1).getNumOfKeys() > this.minKeys) {
                    IBTreeNodeExtended rightSibling = root.getChild(i + 1);
                    wantedChild.addKey(root.getKey(i));
                    root.setKey(i, (K) rightSibling.getKey(1));
                    rightSibling.deleteKey(1);
                    wantedChild.addValue(root.getValue(i));
                    root.setValue(i, (V) rightSibling.getValue(1));
                    rightSibling.deleteValue(1);
                    if (!rightSibling.isLeaf()) {
                        rightSibling.getChild(1).setParent(wantedChild);
                        wantedChild.addChild(rightSibling.getChild(1));
                        rightSibling.deleteChild(1);
                    }
                    wantedChild.setNumOfKeys(wantedChild.getNumOfKeys() + 1);
                    rightSibling.setNumOfKeys(rightSibling.getNumOfKeys() - 1);
                } else {
                    if(i > root.getNumOfKeys())
                        --i;
                    this.merge(root, root.getChild(i), i);
                }
                return this.B_Tree_Delete(root.getChild(i), key);
            }
        } else
            return false;
        return true;
    }

    private void merge(IBTreeNodeExtended root, IBTreeNodeExtended augmentedNode, int i) {
        augmentedNode.addKey(root.getKey(i));
        augmentedNode.setKeys(root.getChild(i+1).getKeys());
        augmentedNode.addValue(root.getValue(i));
        augmentedNode.setValues(root.getChild(i+1).getValues());
        augmentedNode.setChildren(root.getChild(i+1).getChildren());
        for(Object x : root.getChild(i+1).getChildren())
            ((IBTreeNodeExtended) x).setParent(augmentedNode);
        augmentedNode.setNumOfKeys(this.maxKeys);
        root.deleteKey(i);
        root.deleteValue(i);
        root.getChild(i+1).setParent(null);
        root.deleteChild(i+1);
        root.setNumOfKeys(root.getNumOfKeys() - 1);
    }

    /**
     * This procedure used to find the successor of a specific key such that " inputKey = node.getKey(i) "
     * @param node  start searching from the root of this subtree
     * @param i index of the key in node
     * @return Object array contains " array[0] = node that has the successor ans array[1] = index of the key "
     * such that array[0].getKey(array[i]) = the successor
     */
    public Object[] successor(IBTreeNodeExtended node, int i) {
        K key = (K) node.getKey(i);
        Object[] wantedNode = new Object[2];
        if(!node.isLeaf()) {
            wantedNode[0] = this.minimumNode(node.getChild(i + 1));
            wantedNode[1] = 1;
        } else if(node.getNumOfKeys() > i) {
            wantedNode[0] = node;
            wantedNode[1] = i + 1;
        }
        else {
            IBTreeNodeExtended auxiliary = node.getParent();
            while(auxiliary != null) {
                int index = auxiliary.getChildren().indexOf(node) + 1;
                if(index > auxiliary.getNumOfKeys()) {
                    node = auxiliary;
                    auxiliary = auxiliary.getParent();
                } else if(auxiliary.getKey(index).compareTo(key) > 0) {
                    wantedNode[0] = auxiliary;
                    wantedNode[1] = index;
                    break;
                } else break;
            }
        }
        return wantedNode;
    }

    /**
     * This procedure used to find the predecessor of a specific key such that " inputKey = node.getKey(i) "
     * @param node  start searching from the root of this subtree
     * @param i index of the key in node
     * @return Object array contains " array[0] = node that has the predecessor ans array[1] = index of the key "
     * such that array[0].getKey(array[i]) = the predecessor
     */
    public Object[] predecessor(IBTreeNodeExtended node, int i) {
        K key = (K) node.getKey(i);
        Object[] wantedNode = new Object[2];
        if(!node.isLeaf()) {
            IBTreeNodeExtended dummy = this.maximumNode(node.getChild(i));
            wantedNode[0] = dummy;
            wantedNode[1] = dummy.getNumOfKeys();
        } else if(i > 1) {
            wantedNode[0] = node;
            wantedNode[1] = i - 1;
        }
        else {
            IBTreeNodeExtended auxiliary = node.getParent();
            while(auxiliary != null) {
                int index = auxiliary.getChildren().indexOf(node) + 1;
                if(index == 1) {
                    node = auxiliary;
                    auxiliary = auxiliary.getParent();
                } else if(auxiliary.getKey(index - 1).compareTo(key) < 0) {
                    wantedNode[0] = auxiliary;
                    wantedNode[1] = index - 1;
                    break;
                } else break;
            }
        }
        return wantedNode;
    }

    /**
     * This procedure used to find the node that has the minimum key
     * @param node root of the subtree wanted to find its min element
     * @return node has min element
     * you can find the minimum key by using " returnedNode.getKey(1) "
     */
    private IBTreeNodeExtended minimumNode(IBTreeNodeExtended node) {
        while(node != null && !node.isLeaf()) {
            node = node.getChild(1);
        }
        return node;
    }

    /**
     * This procedure used to find the node that has the maximum key
     * @param node root of the subtree wanted to find its max element
     * @return node has max element
     * you can find the maximum key by using " returnedNode.getKey(returnedNode.getNumOfKeys()) "
     */
    private IBTreeNodeExtended maximumNode(IBTreeNodeExtended node) {
        while(node != null && !node.isLeaf()) {
            node = node.getChild(node.getNumOfKeys() + 1);
        }
        return node;
    }

    /**
     * This procedure using BFS algorithm to traverse through the B_Tree
     * Use it just for debugging
     * @param root root of the wanted subtree to be be displayed
     */
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
}
