package eg.edu.alexu.csd.filestructure.btree;

public interface IBTreeNodeExtended<K extends Comparable<K>, V> extends IBTreeNode {
    public void addKey(K key);

    public void addKey(int i, K key);

    public void setKey(int i, K key);

    public K getKey(int i);

    public void deleteKey(int i);

    public void deleteKey(Object key);

    public void addValue(V value);

    public void addValue(int i, V value);

    public void setValue(int i, V value);

    public V getValue(int i);

    public void deleteValue(int i);

    public void deleteValue(Object value);

    public void addChild(IBTreeNode child);

    public void addChild(int i, IBTreeNode child);

    public void setChild(int i, IBTreeNode child);

    public IBTreeNodeExtended getChild(int i);

    public void deleteChild(int i);

    public void deleteChild(Object child);

    public void setParent(IBTreeNodeExtended node);

    public IBTreeNodeExtended getParent();
}
