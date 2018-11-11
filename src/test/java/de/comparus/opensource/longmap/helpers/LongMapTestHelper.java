package de.comparus.opensource.longmap.helpers;

import de.comparus.opensource.longmap.LongMap;
import de.comparus.opensource.longmap.LongMapImpl;
import de.comparus.opensource.longmap.LongMapImpl.Node;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

public class LongMapTestHelper {
  private static Random random = new Random();

  public static class LongMapData {
    public LongMap<String> longMap;
    public ArrayList<String> values;
    public ArrayList<Long> keys;

    LongMapData() {
      longMap = new LongMapImpl<>(String.class);
      keys = new ArrayList<>();
      values = new ArrayList<>();
    }
  }

  public static LongMapData init(long quantity) {
    long key;
    String value;
    LongMapData data = new LongMapData();
    for (int i = 0; i < quantity; i++) {
      key = random.nextLong();
      value = "value" + key;
      data.longMap.put(key, value);
      data.values.add(value);
      data.keys.add(key);
    }
    return data;
  }

  public long getNotExistsKey(ArrayList<Long> keys) {
    long key = random.nextLong();
    while (keys.contains(key)) {
      key = random.nextLong();
    }
    return key;
  }

  public String getNotExistsValue(ArrayList<String> values) {
    String value = "value" + random.nextLong();
    while (values.contains(value)) {
      value = "value" + random.nextLong();
    }
    return value;
  }

  public String getRandomValue() {
    return "value" + random.nextLong();
  }

  public LongMapImpl.Node[] getTable(LongMapImpl map) throws NoSuchFieldException, IllegalAccessException {
    Field tableField = getMapFieldWithAccess(map, "table");
    tableField.setAccessible(true);
    return (LongMapImpl.Node[]) tableField.get(map);
  }

  public Field getMapFieldWithAccess(LongMapImpl map, String name) throws NoSuchFieldException {
    Class cl = map.getClass();
    Field field = cl.getDeclaredField(name);
    field.setAccessible(true);
    return field;
  }

  public LongMapImpl<?> createDefaultLongMapWithTable(Node[] table, int size, Class<?> clazz) throws NoSuchFieldException, IllegalAccessException {
    if (table == null || table.length != 16 || clazz == null || size < 0) return null;
    LongMapImpl<?> map = new LongMapImpl<>(clazz);
    Class cl = LongMapImpl.class;
    Field field = cl.getDeclaredField("table");
    field.setAccessible(true);
    field.set(map, table);
    field = cl.getDeclaredField("size");
    field.setAccessible(true);
    field.set(map, size);
    return map;
  }

  public LongMapImpl<?> createParametrizedLongMapWithTable(Node[] table, String[] fieldsNames, int[] values, Class<?> clazz) throws NoSuchFieldException, IllegalAccessException {
    if (clazz == null) return null;
    LongMapImpl<?> map = new LongMapImpl<>(clazz);
    Class cl = LongMapImpl.class;
    Field field = cl.getDeclaredField("table");
    field.setAccessible(true);
    field.set(map, table);

    if (fieldsNames != null && values != null && fieldsNames.length == values.length) {
      for (int i = 0, size = fieldsNames.length; i < size; i++) {
        field = cl.getDeclaredField(fieldsNames[i]);
        field.setAccessible(true);
        field.set(map, values[i]);
      }
    }

    return map;
  }
}
