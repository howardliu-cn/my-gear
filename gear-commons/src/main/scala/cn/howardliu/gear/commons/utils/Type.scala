package cn.howardliu.gear.commons.utils

import java.util.{HashMap => JHashMap, Map => JMap}

/**
 * <br>created at 16-3-30
 * @author liuxh
 * @since 1.0.0
 */
object Type {
  def >>(map: Map[String, Any]): JMap[String, Any] = {
    val result: JMap[String, Any] = new JHashMap()
    map.foreach(kv => {
      val (k, v) = kv
      val nv = v match {
        case m: Map[String, Any] => >>(m.asInstanceOf[Map[String, Any]])
        case _ => v
      }
      result.put(k, nv)
    })
    result
  }
}
