package ass1;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;


public class TestString {

  public static final String CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+=-}{][><.,';\":/?\\|`~ ";

  public static final String[][] dataset={
    {"abcde", "abcdf", "abcdg", "abcdh"},
    {"abcdh", "abcdg", "abcdf", "abcde"},
    {"rkodpcrgzb", "nyyvzjlhoh", "kkbdujcclb", "mtxyfujhks", "xpoexbbjcu", "gmwmqkxlmy", "cbhxvgylsg", "yheuiltiyd", "gterwqkuix", "uczmncqvfy"},
    {"edq9ic4hhh", "ao8zwqaq6x", "2lhxtqwape", "w28xxkma4k", "f75xjnh2ub", "tvozq7ut22", "mwx0dmzd0p", "eh9azskzt0", "6i6rpwzd3w", "gj1om93lue"},
    {"4910143310", "9890253698", "5178010671", "7386677997", "5958960642", "6291763921", "0552614775", "2305352706", "8473187927", "0081788281"},
    {"", "0", "!@#$%^&*()", ")(*&^%$#@!"},
    {},
    manyOrdered(10000, CHARSET),
    manyReverse(10000, CHARSET),
    manyRandom(10000, 1, 100, CHARSET)
  };
  static private String[] manyRandom(int size, int minStringLength, int maxStringLength, String charset) {
    Random r=new Random(0);
    String[] result=new String[size];
    for(int i=0;i<size;i++){
      int strLength = r.nextInt(maxStringLength - minStringLength) + minStringLength;
      char[] str = new char[strLength];

      for(int j = 0; j < strLength; j++)
        str[j] = charset.charAt(r.nextInt(charset.length()));

      result[i]= new String(str);
    }
    return result;
  }
  static private String[] manyReverse(int size, String charset) {
    String[] result=manyOrdered(size, charset);
    Collections.reverse(Arrays.asList(result));
    return result;
  }
  static private String[] manyOrdered(int size, String charset) {
    char[] charsetArr = charset.toCharArray();
    Arrays.sort(charsetArr);
    charset = new String(charsetArr);

    String[] result=new String[size];

    int[] currentStr = new int[(int)(Math.log(size) / Math.log(charset.length())) + 1];

    for(int c = 0; c < size; ++c) {
      StringBuilder sb = new StringBuilder();
      for(int i = 0; i < currentStr.length; ++i) {
        sb.append(charset.charAt(currentStr[i]));
      }
      result[c] = sb.toString();

      for (int i = currentStr.length - 1; i >= 0; --i) {
        ++currentStr[i];

        if (currentStr[i] < charset.length()) {
          break;
        }
        currentStr[i] = 0;
      }
    }
    return result;
  }

  @Test
  public void testISequentialSorter() {
    Sorter s=new ISequentialSorter();
    for(String[]l:dataset){TestHelper.testData(l,s);}
  }
  @Test
  public void testMSequentialSorter() {
    Sorter s=new MSequentialSorter();
    for(String[]l:dataset){TestHelper.testData(l,s);}
  }
  @Test
  public void testMParallelSorter1() {
    Sorter s=new MParallelSorter1();
    for(String[]l:dataset){TestHelper.testData(l,s);}
  }
  @Test
  public void testMParallelSorter2() {
    Sorter s=new MParallelSorter2();
    for(String[]l:dataset){TestHelper.testData(l,s);}
  }
  @Test
  public void testMParallelSorter3() {
    Sorter s=new MParallelSorter3();
    for(String[]l:dataset){TestHelper.testData(l,s);}
  }
}
