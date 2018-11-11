package de.comparus.opensource.longmap;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("unchecked")
public class LongMapImpl<V> implements LongMap<V> {
  private Node[] table;
  private int size;
  private int capacity;
  private int threshold;
  private Class<V> valueClazz;

  private final int MIN_CAPASITY = 16;
  private final int MAX_CAPASITY = Integer.MAX_VALUE - 2;
  private final float loadFactor = 0.75f;

  public static class Node<V> {
    final long key;
    V value;
    Node next;

    public Node(long key, V value) {
      this.key = key;
      this.value = value;
    }

    public V getValue() {
      return value;
    }

    public long getKey() {
      return key;
    }

    public Node getNext() {
      return next;
    }

    public void setNext(Node node) {
      next = node;
    }

    public void setValue(V value) {
      this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (!(obj instanceof Node)) return false;

      boolean isEqual;
      try {
        Node other = (Node) obj;
        isEqual = key == other.key && (value == other.value || value.equals(other.value));
      } catch (ClassCastException e) {
        return false;
      }
      return isEqual;
    }
  }

  public LongMapImpl(Class<V> clazz) {
    valueClazz = clazz;
    clear();
  }

  public V put(long key, V value) {
    int hash = getHash(key);
    int index = getIndex(hash);

    V oldValue = setValue(key, value, index);

    if (oldValue == null) {
      if (size == threshold) {
        resize(capacity * 2);
      }
      addNode(key, value, index, table);
      size++;
    }

    return oldValue;
  }

  public V get(long key) {
    V value = null;

    int index = getIndex(getHash(key));
    Node node = table[index];

    while (node != null) {
      if (node.key == key) {
        value = (V) node.value;
        break;
      }
      node = node.next;
    }

    return value;
  }

  public V remove(long key) {
    int index = getIndex(getHash(key));
    V removedValue = removeNode(key, index, table);
    if (removedValue != null) size--;
    return removedValue;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public boolean containsKey(long key) {
    return null != get(key);
  }

  public boolean containsValue(V value) {
    boolean isContains = false;

    outerLoop:
    for (Node node : table) {
      while (node != null) {
        if (node.value == value || node.value.equals(value)) {
          isContains = true;
          break outerLoop;
        }
        node = node.next;
      }
    }

    return isContains;
  }

  public long[] keys() {
    ArrayList<Node> nodes = getAllNodes(table);
    long[] keys = new long[nodes.size()];

    int count = 0;
    for (Node node : nodes) {
      keys[count++] = node.key;
    }

    return keys;
  }

  public V[] values() {
    ArrayList<Node> nodes = getAllNodes(table);
    V[] values = (V[]) Array.newInstance(valueClazz, nodes.size());

    int count = 0;
    for (Node node : nodes) {
      values[count++] = (V) node.value;
    }

    return values;
  }

  private ArrayList<Node> getAllNodes(Node[] table) {
    ArrayList<Node> nodes = new ArrayList<>();
    for (Node node : table) {
      while (node != null) {
        nodes.add(node);
        node = node.next;
      }
    }
    return nodes;
  }

  public long size() {
    return size;
  }

  public void clear() {
    table = (Node[]) Array.newInstance(Node.class, MIN_CAPASITY);
    size = 0;
    capacity = MIN_CAPASITY;
    threshold = getThreshold();
  }

  private int getThreshold() {
    return (int) (capacity * loadFactor);
  }

  private int getHash(long key) {
    return Long.hashCode(key);
  }

  private int getIndex(int hash) {
    return hash & (capacity - 1);
  }

  private V setValue(long key, V value, int index) {
    V oldValue = null;
    Node nodes = table[index];

    while (nodes != null) {
      if (nodes.key == key) {
        oldValue = (V) nodes.value;
        nodes.value = value;
        break;
      }
      nodes = nodes.next;
    }

    return oldValue;
  }

  private void addNode(long key, V value, int index, Node[] table) {
    Node node = new Node(key, value);
    node.next = table[index];
    table[index] = node;
  }

  private V removeNode(long key, int index, Node[] table) {
    V value = null;
    Node node = table[index];

    if (node != null) {
      if (node.key == key) {
        value = (V) node.value;
        table[index] = node.next;
      } else {
        while (node.next != null) {
          if (node.next.key == key) {
            value = (V) node.next.value;
            node.next = node.next.next;
            break;
          }
          node = node.next;
        }
      }
    }

    return value;
  }

  private void resize(int newCapacity) {
    if (capacity == MAX_CAPASITY) {
      threshold = MAX_CAPASITY;
      return;
    }

    newCapacity = newCapacity <= MAX_CAPASITY ? newCapacity : MAX_CAPASITY;
    Node[] newTable = (Node[]) Array.newInstance(Node.class, newCapacity);
    transfer(newTable);
    table = newTable;
    capacity = newCapacity;
    threshold = getThreshold();
  }

  private void transfer(Node[] newTable) {
    for (Node node : table) {
      int index;
      while (node != null) {
        index = getIndex(getHash(node.key));
        addNode(node.key, (V) node.value, index, newTable);
        node = node.next;
      }
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof LongMapImpl)) return false;

    LongMapImpl<V> longMap;
    try {
      longMap = (LongMapImpl<V>) obj;
    } catch (ClassCastException e) {
      return false;
    }

    boolean isEqual = true;
    try {
      Node[] table2 = null;

      Class cl = getClass();
      Field[] fields = cl.getDeclaredFields();
      Field field;
      String tableClassName = table.getClass().getCanonicalName();
      String fieldTypeName;
      for (int i = 0, length = fields.length; isEqual && i < length; i++) {
        field = fields[i];
        field.setAccessible(true);
        fieldTypeName = field.getType().getCanonicalName();
        if (fieldTypeName.equals(tableClassName)) {
          table2 = (Node[]) field.get(longMap);
        } else {
          isEqual = field.get(this) == field.get(longMap) || field.get(this).equals(field.get(longMap));
        }
      }

      isEqual = isEqual ? tableEquals(this.table, table2) : isEqual;

    } catch (IllegalAccessException e) {
      return false;
    }
    return isEqual;
  }

  private boolean tableEquals(Node[] table1, Node[] table2) {
    if (table1 == table2) return true;
    if (table1 == null || table2 == null || table1.length != table2.length) return false;

    boolean isEqual = true;
    Node node1, node2;
    for (int i = 0, size = table1.length; isEqual && i < size; i++) {
      node1 = table1[i];
      node2 = table2[i];

      if (node1 == null && node2 == null) {
        continue;
      }

      if (node1 == null || node2 == null) {
        isEqual = false;
      } else {
        while (node1 != null) {
          isEqual = node1.equals(node2);
          if (!isEqual) break;
          node1 = node1.next;
          node2 = node2.next;
        }
        isEqual = isEqual ? node2 == null : isEqual;
      }
    }

    return isEqual;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(super.toString());
    builder.append(":");
    builder.append("{");

    try {
      Class cl = getClass();
      Field[] fields = cl.getDeclaredFields();
      Field field;
      String tableClassName = table.getClass().getCanonicalName();
      String fieldTypeName;
      boolean isNotFirst = false;
      for (int i = 0, length = fields.length; i < length; i++) {
        if (isNotFirst) builder.append(", ");
        field = fields[i];
        field.setAccessible(true);
        fieldTypeName = field.getType().getCanonicalName();
        if (!fieldTypeName.equals(tableClassName)) {
          isNotFirst = true;
          builder.append(field.getName());
          builder.append("=");
          builder.append(field.get(this));
        }
      }
      builder.append(", keys=");
      builder.append(Arrays.toString(keys()));
      builder.append(", values=");
      builder.append(Arrays.toString(values()));
    } catch (IllegalAccessException e) {
      builder.append("}");
      return builder.toString();
    }
    builder.append("}");
    return builder.toString();
  }
}