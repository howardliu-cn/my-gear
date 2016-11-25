package cn.howardliu.gear.commons.utils

import scala.util.control.Exception.allCatch

/**
 * <br/>created at 16-3-24
 * @author liuxh
 * @since 1.0.0
 */
object StringUtilsEx {
  def isDigits(x: String) = (x != null) && (x forall Character.isDigit)

  def isLongNumber(s: String): Boolean = (allCatch opt s.toLong).isDefined

  def isDoubleNumber(s: String): Boolean = (allCatch opt s.toDouble).isDefined

  def main(args: Array[String]) {
    println(isDigits("  1"))
    println(isLongNumber(" 1 "))
    println(isDoubleNumber(" 1.1a "))
  }
}
