package eg.edu.alexu.csd.filestructure.btree;

import java.util.ArrayList;
import java.util.List;

public class BTreeNode<K extends Comparable<K>, V> implements IBTreeNodeExtended {
    private List<K> keys = new ArrayList<>();
    private List<V> values = new ArrayList<>();
    private List<IBTreeNodeExtended<K, V>> children = new ArrayList<>();
    private int numOfKeys = 0;
    private boolean leaf = true;

    public BTreeNode() {
        this.keys.add(null);
        this.values.add(null);
        this.children.add(null);
    }

    @Override
    public int getNumOfKeys() {
        return this.numOfKeys;
    }

    @Override
    public void setNumOfKeys(int numOfKeys) {
        this.numOfKeys = numOfKeys;
    }

    @Override
    public boolean isLeaf() {
        return this.leaf;
    }

    @Override
    public void setLeaf(boolean isLeaf) {
        this.leaf = isLeaf;
    }

    @Override
    public List getKeys() {
        if(this.numOfKeys == 0)
            return new ArrayList();
        return this.keys.subList(1, this.numOfKeys + 1);
    }

    @Override
    public void setKeys(List keys) {
        this.keys.addAll(keys);
    }

    @Override
    public List getValues() {
        if(this.numOfKeys == 0)
            return new ArrayList();
        return this.values.subList(1, this.numOfKeys + 1);
    }

    @Override
    public void setValues(List values) {
        this.values.addAll(values);
    }

    @Override
    public List<IBTreeNodeExtended<K, V>> getChildren() {
        if(this.children.size() == 1)
            return new ArrayList<>();
        return this.children.subList(1, this.numOfKeys + 2);
    }

    @Override
    public void setChildren(List children) {
        this.children.addAll(children);
    }

    @Override
    public void addKey(Comparable key) {
        this.keys.add((K) key);
    }

    @Override
    public void addKey(int i, Comparable key) {
        this.keys.add(i, (K) key);
    }

    @Override
    public void setKey(int i, Comparable key) {
        this.keys.set(i, (K) key);
    }

    @Override
    public Comparable getKey(int i) {
        return this.keys.get(i);
    }

    @Override
    public void deleteKey(int i) {
        this.keys.remove(i);
    }

    @Override
    public void deleteKey(Object key) {
        this.keys.remove(key);
    }

    @Override
    public void addValue(Object value) {
        this.values.add((V) value);
    }

    @Override
    public void addValue(int i, Object value) {
        this.values.add(i, (V) value);
    }

    @Override
    public void setValue(int i, Object value) {
        this.values.set(i, (V) value);
    }

    @Override
    public Object getValue(int i) {
        return this.values.get(i);
    }

    @Override
    public void deleteValue(int i) {
        this.values.remove(i);
    }

    @Override
    public void deleteValue(Object value) {
        this.values.remove(value);
    }

    @Override
    public void addChild(IBTreeNode child) {
        this.children.add((IBTreeNodeExtended) child);
    }

    @Override
    public void addChild(int i, IBTreeNode child) {
        this.children.add(i,(IBTreeNodeExtended) child);
    }

    @Override
    public void setChild(int i, IBTreeNode child) {
        this.children.set(i, (IBTreeNodeExtended) child);
    }

    @Override
    public IBTreeNodeExtended getChild(int i) {
        return this.children.get(i);
    }

    @Override
    public void deleteChild(int i) {
        this.children.remove(i);
    }

    @Override
    public void deleteChild(Object child) {
        this.children.remove(child);
    }
}
