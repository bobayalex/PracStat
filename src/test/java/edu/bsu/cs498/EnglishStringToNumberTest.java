package edu.bsu.cs498;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnglishStringToNumberTest {

    EnglishStringToNumber englishStringToNumber = new EnglishStringToNumber();

    @Test
    public void convert() throws Exception {
        int actual1 = englishStringToNumber.convert("seventeen");
        int actual2 = englishStringToNumber.convert("fourteen");

        Assert.assertEquals(17, actual1);
        Assert.assertEquals(14, actual2);
    }

    @Test
    public void convertNonNumberTest() throws Exception {
        int actual = englishStringToNumber.convert("set");

        Assert.assertEquals(-1, actual);
    }

    @Test
    public void zeroConversionTest() throws Exception {
        int actual = englishStringToNumber.convert("zero");

        Assert.assertEquals(0, actual);
    }

    @Test
    public void oneConversionTest() throws Exception {
        int actual = englishStringToNumber.convert("one");

        Assert.assertEquals(1, actual);
    }

    @Test
    public void tenConversionTest() throws Exception {
        int actual = englishStringToNumber.convert("ten");

        Assert.assertEquals(10, actual);
    }

    @Test
    public void twentyConversionTest() throws Exception {
        int actual = englishStringToNumber.convert("twenty");

        Assert.assertEquals(20, actual);
    }
}