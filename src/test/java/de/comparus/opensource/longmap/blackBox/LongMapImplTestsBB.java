package de.comparus.opensource.longmap.blackBox;

import de.comparus.opensource.longmap.LongMap;
import de.comparus.opensource.longmap.helpers.LongMapTestHelper;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static de.comparus.opensource.longmap.helpers.LongMapTestHelper.LongMapData;
import static de.comparus.opensource.longmap.helpers.LongMapTestHelper.init;
import static junit.framework.TestCase.*;

@RunWith(Parameterized.class)
public class LongMapImplTestsBB {
  private LongMap<String> longMap;
  private int currentSize;
  private ArrayList<String> values;
  private ArrayList<Long> keys;

  LongMapTestHelper helper = new LongMapTestHelper();
  private static Random random = new Random();

  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
      {init(0), 0},
      {init(1), 1},
      {init(2), 2},
      {init(7), 7}
    });
  }

  public LongMapImplTestsBB(LongMapData data, int currentSize) {
    longMap = data.longMap;
    values = data.values;
    keys = data.keys;
    this.currentSize = currentSize;
  }

  @After
  public void clearSetUp() {
    values = new ArrayList<>();
    keys = new ArrayList<>();
  }

  @Test
  public void sizeTest() {
    //Given
    long exp = currentSize;

    //When
    long act = longMap.size();

    //Then
    assertEquals(exp, act);
  }

  @Test
  public void getTest_IsExists() {
    //Given
    if (currentSize != 0) {
      int index = random.nextInt(currentSize);
      long key = keys.get(index);
      String exp = values.get(index);

      //When
      String act = longMap.get(key);

      //Then
      assertEquals(exp, act);
    }
  }

  @Test
  public void getTest_IsNotExists() {
    //Given
    long key = helper.getNotExistsKey(keys);

    //When
    String act = longMap.get(key);

    //Then
    assertNull(act);
  }

  @Test
  public void isEmptyTest() {
    //Given
    boolean exp = currentSize == 0;

    //When
    boolean act = longMap.isEmpty();

    //Then
    assertEquals(exp, act);
  }

  @Test
  public void containsKey_IsExists() {
    //Given
    if (currentSize != 0) {
      int index = random.nextInt(currentSize);
      long key = keys.get(index);

      //When
      boolean act = longMap.containsKey(key);

      //Then
      assertTrue(act);
    }
  }

  @Test
  public void containsKey_IsNotExists() {
    //Given
    long key = helper.getNotExistsKey(keys);

    //When
    boolean act = longMap.containsKey(key);

    //Then
    assertFalse(act);
  }

  @Test
  public void containsValue_IsExists() {
    //Given
    if (currentSize != 0) {
      int index = random.nextInt(currentSize);
      String value = values.get(index);

      //When
      boolean act = longMap.containsValue(value);

      //Then
      assertTrue(act);
    }
  }

  @Test
  public void containsValue_IsNotExists() {
    //Given
    String value = helper.getNotExistsValue(values);

    //When
    boolean act = longMap.containsValue(value);

    //Then
    assertFalse(act);
  }

  @Test
  public void keysTest() {
    //Given
    //When
    long[] act = longMap.keys();

    //Then
    assertEquals(keys.size(), act.length);

    for (long key : act) {
      assertTrue(keys.contains(key));
    }
  }

  @Test
  public void valuesTest() {
    //Given
    //When
    String[] act = longMap.values();

    //Then
    assertEquals(values.size(), act.length);

    for (String key : act) {
      assertTrue(values.contains(key));
    }
  }
}
