package cn.howardliu.gear.commons.utils

import scala.util.control.Exception.allCatch

/**
 * <br>created at 16-3-24
 * @author liuxh
 * @since 1.0.0
 */
object StringUtilsEx {
  def isDigits(x: String) = (x != null) && (x forall Character.isDigit)

  def isLongNumber(s: String): Boolean = (allCatch opt s.toLong).isDefined

  def isDoubleNumber(s: String): Boolean = (allCatch opt s.toDouble).isDefined

  def format1Decimals(value: Double, suffix: String): String = {
    val p: String = String.valueOf(value)
    val ix: Int = p.indexOf('.') + 1
    val ex: Int = p.indexOf('E')
    val fraction: Char = p.charAt(ix)
    if (fraction == '0') {
      if (ex != -1) {
        p.substring(0, ix - 1) + p.substring(ex) + suffix
      }
      else {
        p.substring(0, ix - 1) + suffix
      }
    }
    else {
      if (ex != -1) {
        p.substring(0, ix) + fraction + p.substring(ex) + suffix
      }
      else {
        p.substring(0, ix) + fraction + suffix
      }
    }
  }

  def unCapitalize (str: String): String = changeFirstCharacterCase(str, capitalize = false)

  def changeFirstCharacterCase(str: String, capitalize: Boolean): String = {
    if (str == null || str.length == 0) {
      return str
    }
    val sb: StringBuilder = new StringBuilder(str.length)
    if (capitalize) {
      sb.append(Character.toUpperCase(str.charAt(0)))
    } else {
      sb.append(Character.toLowerCase(str.charAt(0)))
    }
    sb.append(str.substring(1))
    sb.toString()
  }

  def main(args: Array[String]) {
    println(isDigits("  1"))
    println(isLongNumber(" 1 "))
    println(isDoubleNumber(" 1.1a "))
    println(format1Decimals(100.001, "mb"))
    println(format1Decimals(100.011, "mb"))
    println(format1Decimals(100.111, "mb"))
    println(unCapitalize("".getClass.getSimpleName))
  }
}
