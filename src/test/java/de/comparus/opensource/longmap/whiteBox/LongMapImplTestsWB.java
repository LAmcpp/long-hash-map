package de.comparus.opensource.longmap.whiteBox;

import de.comparus.opensource.longmap.LongMapImpl;
import de.comparus.opensource.longmap.LongMapImpl.Node;
import de.comparus.opensource.longmap.helpers.LongMapTestHelper;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertArrayEquals;

@SuppressWarnings("unchecked")
public class LongMapImplTestsWB {
  private LongMapImpl<String> longMap;
  private final int defaulCapacity = 16;
  private Class<?> clazz = String.class;

//  private ArrayList<String> values;
//  private ArrayList<Long> keys;

  private Node[] table;
  LongMapTestHelper helper = new LongMapTestHelper();

  @Before
  public void setUp() {
    longMap = new LongMapImpl<>(String.class);
  }

  @Test
  public void putTest_One() throws NoSuchFieldException, IllegalAccessException {
    //Given
    long key = 0L;
    String value = helper.getRandomValue();
    Node[] expTable = new Node[defaulCapacity];
    expTable[0] = new Node(key, value);
    LongMapImpl<String> expMap = (LongMapImpl<String>) helper.createDefaultLongMapWithTable(expTable, 1, clazz);

    //When
    String act = longMap.put(key, value);

    //Then
    assertNull(act);
    assertEquals(expMap, longMap);
  }

  @Test
  public void putTest_Two() throws NoSuchFieldException, IllegalAccessException {
    //Given
    long
      key0 = 0L,
      key1 = 1L;
    String
      value0 = helper.getRandomValue(),
      value1 = helper.getRandomValue();
    Node[] expTable = new Node[defaulCapacity];
    expTable[0] = new Node(key0, value0);
    expTable[1] = new Node(key1, value1);
    LongMapImpl<String> expMap = (LongMapImpl<String>) helper.createDefaultLongMapWithTable(expTable, 2, clazz);

    //When
    String act0 = longMap.put(key0, value0);
    String act1 = longMap.put(key1, value1);

    //Then
    assertNull(act0);
    assertNull(act1);
    assertEquals(expMap, longMap);
  }

  @Test
  public void putTest_Many() throws NoSuchFieldException, IllegalAccessException {
    //Given
    int count = 7;
    long[] keys = new long[count];
    String[] values = new String[count];
    Node[] expTable = new Node[defaulCapacity];
    String value;

    for (int i = 0; i < count; i++) {
      keys[i] = i;
      value = helper.getRandomValue();
      values[i] = value;
      expTable[i] = new Node(i, value);
    }

    LongMapImpl<String> expMap = (LongMapImpl<String>) helper.createDefaultLongMapWithTable(expTable, count, clazz);
    String[] exp = {null, null, null, null, null, null, null};

    //When
    String[] act = new String[count];
    for (int i = 0; i < count; i++) {
      act[i] = longMap.put(keys[i], values[i]);
    }

    //Then
    assertArrayEquals(exp, act);
    assertEquals(expMap, longMap);
  }

  @Test
  public void putTest_Zero_EqualIndex_DifferentKeys() throws NoSuchFieldException, IllegalAccessException {
    //Given
    long
      key0 = 0L,
      key1 = defaulCapacity;
    String
      value0 = helper.getRandomValue(),
      value1 = helper.getRandomValue();
    Node[] expTable = new Node[defaulCapacity];
    Node node0 = new Node(key0, value0);
    Node node1 = new Node(key1, value1);
    node1.setNext(node0);
    expTable[0] = node1;
    LongMapImpl<String> expMap = (LongMapImpl<String>) helper.createDefaultLongMapWithTable(expTable, 2, clazz);

    //When
    String act0 = longMap.put(key0, value0);
    String act1 = longMap.put(key1, value1);

    //Then
    assertNull(act0);
    assertNull(act1);
    assertEquals(expMap, longMap);
  }

  @Test
  public void putTest_End_EqualIndex_DifferentKeys() throws NoSuchFieldException, IllegalAccessException {
    //Given
    long
      key0 = defaulCapacity,
      key1 = defaulCapacity * 2;
    String
      value0 = helper.getRandomValue(),
      value1 = helper.getRandomValue();
    Node[] expTable = new Node[defaulCapacity];
    Node node0 = new Node(key0, value0);
    Node node1 = new Node(key1, value1);
    node1.setNext(node0);
    expTable[0] = node1;
    LongMapImpl<String> expMap = (LongMapImpl<String>) helper.createDefaultLongMapWithTable(expTable, 2, clazz);

    //When
    String act0 = longMap.put(key0, value0);
    String act1 = longMap.put(key1, value1);

    //Then
    assertNull(act0);
    assertNull(act1);
    assertEquals(expMap, longMap);
  }

  @Test
  public void putTest_Middle_EqualIndex_DifferentKeys() throws NoSuchFieldException, IllegalAccessException {
    //Given
    long
      key0 = defaulCapacity / 2,
      key1 = defaulCapacity + key0;
    String
      value0 = helper.getRandomValue(),
      value1 = helper.getRandomValue();
    Node
      node0 = new Node(key0, value0),
      node1 = new Node(key1, value1);
    node1.setNext(node0);

    Node[] expTable = new Node[defaulCapacity];
    expTable[defaulCapacity / 2] = node1;
    LongMapImpl<String> expMap = (LongMapImpl<String>) helper.createDefaultLongMapWithTable(expTable, 2, clazz);

    //When
    String act0 = longMap.put(key0, value0);
    String act1 = longMap.put(key1, value1);

    //Then
    assertNull(act0);
    assertNull(act1);
    assertEquals(expMap, longMap);
  }

  @Test
  public void putTest_EqualKeys_One() throws NoSuchFieldException, IllegalAccessException {
    //Given
    int count = 2;
    long[] keys = new long[count];
    String[] values = new String[count];
    Node[] expTable = new Node[defaulCapacity];
    String value;

    for (int i = 0; i < count; i++) {
      keys[i] = count;
      value = helper.getRandomValue();
      values[i] = value;
    }
    expTable[count] = new Node(count, values[values.length - 1]);

    LongMapImpl<String> expMap = (LongMapImpl<String>) helper.createDefaultLongMapWithTable(expTable, 1, clazz);
    String[] exp = {null, values[0]};

    //When
    String[] act = new String[count];
    for (int i = 0; i < count; i++) {
      act[i] = longMap.put(keys[i], values[i]);
    }

    //Then
    assertArrayEquals(exp, act);
    assertEquals(expMap, longMap);
  }

  @Test
  public void putTest_EqualKeys_Many() throws NoSuchFieldException, IllegalAccessException {
    //Given
    int count = 7;
    long[] keys = new long[count];
    String[] values = new String[count];
    Node[] expTable = new Node[defaulCapacity];
    String value;

    for (int i = 0; i < count; i++) {
      keys[i] = count;
      value = helper.getRandomValue();
      values[i] = value;
    }
    expTable[count] = new Node(count, values[values.length - 1]);

    LongMapImpl<String> expMap = (LongMapImpl<String>) helper.createDefaultLongMapWithTable(expTable, 1, clazz);
    String[] exp = {null, values[0], values[1], values[2], values[3], values[4], values[5]};

    //When
    String[] act = new String[count];
    for (int i = 0; i < count; i++) {
      act[i] = longMap.put(keys[i], values[i]);
    }

    //Then
    assertArrayEquals(exp, act);
    assertEquals(expMap, longMap);
  }

  @Test
  public void putTest_WithResize_DifferentKeys() throws NoSuchFieldException, IllegalAccessException {
    //Given
    int count = 13;
    long[] keys = new long[count];
    String[] values = new String[count];
    Node[] expTable = new Node[defaulCapacity * 2];
    String value;

    for (int i = 0; i < count; i++) {
      keys[i] = i;
      value = helper.getRandomValue();
      values[i] = value;
      expTable[i] = new Node(i, value);
    }

    String[] fieldsNames = {
      "size",
      "capacity",
      "threshold",

    };
    int[] fieldsValues = {
      count,
      defaulCapacity * 2,
      defaulCapacity * 2 - defaulCapacity / 2,
    };
    LongMapImpl<String> expMap = (LongMapImpl<String>) helper.createParametrizedLongMapWithTable(expTable, fieldsNames, fieldsValues, clazz);
    String[] exp = new String[count];

    //When
    String[] act = new String[count];
    for (int i = 0; i < count; i++) {
      act[i] = longMap.put(keys[i], values[i]);
    }

    //Then
    assertArrayEquals(exp, act);
    assertEquals(expMap, longMap);
  }

  @Test
  public void putTest_WithoutResize_EqualLastKey() throws NoSuchFieldException, IllegalAccessException {
    //Given
    int count = 13;
    long[] keys = new long[count];
    String[] values = new String[count];
    Node[] expTable = new Node[defaulCapacity];
    String value;

    for (int i = 0; i < count - 1; i++) {
      keys[i] = i;
      value = helper.getRandomValue();
      values[i] = value;
      expTable[i] = new Node(i, value);
    }
    keys[count - 1] = keys[count - 2];
    values[count - 1] = helper.getRandomValue();
    expTable[count - 2] = new Node(keys[count - 1], values[count - 1]);

    String[] fieldsNames = {"size"};
    int[] fieldsValues = {count - 1};
    LongMapImpl<String> expMap = (LongMapImpl<String>) helper.createParametrizedLongMapWithTable(expTable, fieldsNames, fieldsValues, clazz);
    String[] exp = new String[count];
    exp[count - 1] = values[count - 2];

    //When
    String[] act = new String[count];
    for (int i = 0; i < count; i++) {
      act[i] = longMap.put(keys[i], values[i]);
    }

    //Then
    assertArrayEquals(exp, act);
    assertEquals(expMap, longMap);
  }

  @Test
  public void removeTest_EmptyTable() throws NoSuchFieldException, IllegalAccessException {
    //Given
    Node[] expTable = new Node[defaulCapacity];
    LongMapImpl<String> expMap = (LongMapImpl<String>) helper.createDefaultLongMapWithTable(expTable, 0, clazz);

    //When
    String act = longMap.remove(0);

    //Then
    assertNull(act);
    assertEquals(expMap, longMap);
  }

  @Test
  public void removeTest_One() throws NoSuchFieldException, IllegalAccessException {
    //Given
    long key = 0L;
    String value = helper.getRandomValue();

    Node[] expTable = new Node[defaulCapacity];
    LongMapImpl<String> expMap = (LongMapImpl<String>) helper.createDefaultLongMapWithTable(expTable, 0, clazz);

    longMap.put(key, value);

    String exp = value;

    //When
    String act = longMap.remove(key);

    //Then
    assertEquals(exp, act);
    assertEquals(expMap, longMap);
  }

  @Test
  public void removeTest_Two_One() throws NoSuchFieldException, IllegalAccessException {
    //Given
    long key0 = 0L;
    String value0 = helper.getRandomValue();
    long key1 = 1L;
    String value1 = helper.getRandomValue();

    Node[] expTable = new Node[defaulCapacity];
    expTable[1] = new Node(key1, value1);
    LongMapImpl<String> expMap = (LongMapImpl<String>) helper.createDefaultLongMapWithTable(expTable, 1, clazz);

    longMap.put(key0, value0);
    longMap.put(key1, value1);

    String exp = value0;

    //When
    String act = longMap.remove(key0);

    //Then
    assertEquals(exp, act);
    assertEquals(expMap, longMap);
  }

  @Test
  public void removeTest_Two_Two() throws NoSuchFieldException, IllegalAccessException {
    //Given
    long key0 = 0L;
    String value0 = helper.getRandomValue();
    long key1 = 1L;
    String value1 = helper.getRandomValue();

    Node[] expTable = new Node[defaulCapacity];
    LongMapImpl<String> expMap = (LongMapImpl<String>) helper.createDefaultLongMapWithTable(expTable, 0, clazz);

    longMap.put(key0, value0);
    longMap.put(key1, value1);

    String exp0 = value0;
    String exp1 = value1;

    //When
    String act0 = longMap.remove(key0);
    String act1 = longMap.remove(key1);

    //Then
    assertEquals(exp0, act0);
    assertEquals(exp1, act1);
    assertEquals(expMap, longMap);
  }

  @Test
  public void removeTest_Many_One() throws NoSuchFieldException, IllegalAccessException {
    //Given
    int count = 7;
    long[] keys = new long[count];
    String[] values = new String[count];
    Node[] expTable = new Node[defaulCapacity];
    String value;

    for (int i = 0; i < count; i++) {
      keys[i] = i;
      value = helper.getRandomValue();
      values[i] = value;
      if (i != 0) expTable[i] = new Node(i, value);
    }

    LongMapImpl<String> expMap = (LongMapImpl<String>) helper.createDefaultLongMapWithTable(expTable, count - 1, clazz);
    String[] exp = {values[0]};
    for (int i = 0; i < count; i++) {
      longMap.put(keys[i], values[i]);
    }

    //When
    String[] act = new String[1];
    for (int i = 0; i < 1; i++) {
      act[i] = longMap.remove(keys[i]);
    }

    //Then
    assertArrayEquals(exp, act);
    assertEquals(expMap, longMap);
  }

  @Test
  public void removeTest_Many_All() throws NoSuchFieldException, IllegalAccessException {
    //Given
    int count = 7;
    long[] keys = new long[count];
    String[] values = new String[count];
    Node[] expTable = new Node[defaulCapacity];
    String value;

    for (int i = 0; i < count; i++) {
      keys[i] = i;
      value = helper.getRandomValue();
      values[i] = value;
    }

    LongMapImpl<String> expMap = (LongMapImpl<String>) helper.createDefaultLongMapWithTable(expTable, 0, clazz);
    String[] exp = values;
    for (int i = 0; i < count; i++) {
      longMap.put(keys[i], values[i]);
    }

    //When
    String[] act = new String[count];
    for (int i = 0; i < count; i++) {
      act[i] = longMap.remove(keys[i]);
    }

    //Then
    assertArrayEquals(exp, act);
    assertEquals(expMap, longMap);
  }

  @Test
  public void removeTest_Many_One_EqualKeys() throws NoSuchFieldException, IllegalAccessException {
    //Given
    int count = 7;
    long[] keys = new long[count];
    String[] values = new String[count];
    Node[] expTable = new Node[defaulCapacity];
    String value;

    for (int i = 0; i < count; i++) {
      keys[i] = count + defaulCapacity * i;
      value = helper.getRandomValue();
      values[i] = value;
      if (i != 0) {
        Node node = new Node(keys[i], values[i]);
        node.setNext(expTable[count]);
        expTable[count] = node;
      }
    }

    LongMapImpl<String> expMap = (LongMapImpl<String>) helper.createDefaultLongMapWithTable(expTable, count - 1, clazz);
    String[] exp = {values[0]};
    for (int i = 0; i < count; i++) {
      longMap.put(keys[i], values[i]);
    }

    //When
    String[] act = {longMap.remove(keys[0])};

    //Then
    assertArrayEquals(exp, act);
    assertEquals(expMap, longMap);
  }

  @Test
  public void removeTest_Many_All_EqualKeys() throws NoSuchFieldException, IllegalAccessException {
    //Given
    int count = 7;
    long[] keys = new long[count];
    String[] values = new String[count];
    Node[] expTable = new Node[defaulCapacity];
    String value;

    for (int i = 0; i < count; i++) {
      keys[i] = count + count * i;
      value = helper.getRandomValue();
      values[i] = value;
    }

    LongMapImpl<String> expMap = (LongMapImpl<String>) helper.createDefaultLongMapWithTable(expTable, 0, clazz);
    String[] exp = values;
    for (int i = 0; i < count; i++) {
      longMap.put(keys[i], values[i]);
    }

    //When
    String[] act = new String[count];
    for (int i = 0; i < count; i++) {
      act[i] = longMap.remove(keys[i]);
    }

    //Then
    assertArrayEquals(exp, act);
    assertEquals(expMap, longMap);
  }

  @Test
  public void equalsTest_EmptyTable() {
    //Given
    LongMapImpl<String> map2 = new LongMapImpl<>(String.class);

    //When
    boolean act1 = longMap.equals(map2);
    boolean act2 = map2.equals(longMap);

    //Then
    assertTrue(act1);
    assertTrue(act2);
  }

  @Test
  public void equalsTest_EmptyTableNotEqual() throws NoSuchFieldException, IllegalAccessException {
    //Given
    LongMapImpl<String> map2 = new LongMapImpl<>(String.class);
    Field field = helper.getMapFieldWithAccess(map2, "threshold");
    field.setInt(map2, 0);

    //When
    boolean act1 = longMap.equals(map2);
    boolean act2 = map2.equals(longMap);

    //Then
    assertFalse(act1);
    assertFalse(act2);
  }

  @Test
  public void toStringTest() throws NoSuchFieldException, IllegalAccessException {
    //Given
    int count = 7;
    long[] keys = new long[count];
    String[] values = new String[count];
    Node[] expTable = new Node[defaulCapacity];
    String value;

    for (int i = 0; i < count; i++) {
      keys[i] = i;
      value = "value" + i;
      values[i] = value;
      expTable[i] = new Node(keys[i], values[i]);
    }

    LongMapImpl<String> expMap = (LongMapImpl<String>) helper.createDefaultLongMapWithTable(expTable, count, clazz);

    String exp = expMap.getClass().getName() + "{size=7, capacity=16, threshold=12, valueClazz=class java.lang.String, MIN_CAPASITY=16, MAX_CAPASITY=2147483645, loadFactor=0.75, keys=[0, 1, 2, 3, 4, 5, 6], values=[value0, value1, value2, value3, value4, value5, value6]}";

    //When
    String strMap = expMap.toString();
    String[] st = strMap.split("[:@]");
    String act = st[0] + st[2];

    //Then
    assertEquals(exp, act);
  }
}
