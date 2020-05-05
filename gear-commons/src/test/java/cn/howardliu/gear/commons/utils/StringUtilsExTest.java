package cn.howardliu.gear.commons.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * <br>created at 2020/5/5
 *
 * @author liuxh
 * @since 1.0.0
 */
public class StringUtilsExTest {
    @Test
    public void isDigits() {
        Assert.assertFalse(StringUtilsEx.isDigits("  1"));
        Assert.assertTrue(StringUtilsEx.isDigits("1"));
    }

    @Test
    public void format1Decimals() {
        Assert.assertEquals("100mb", StringUtilsEx.format1Decimals(100.001, "mb"));
        Assert.assertEquals("100mb", StringUtilsEx.format1Decimals(100.011, "mb"));
        Assert.assertEquals("100.1mb", StringUtilsEx.format1Decimals(100.111, "mb"));
    }

    @Test
    public void unCapitalize() {
        Assert.assertEquals("string", StringUtilsEx.unCapitalize("".getClass().getSimpleName()));
    }

    @Test
    public void changeFirstCharacterCase() {
        Assert.assertEquals("string", StringUtilsEx.changeFirstCharacterCase("".getClass().getSimpleName(), false));
        Assert.assertEquals("String", StringUtilsEx.changeFirstCharacterCase("".getClass().getSimpleName(), true));
    }
}
