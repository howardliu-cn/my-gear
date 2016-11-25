package cn.howardliu.gear.commons.utils

import java.io.{InputStream, InputStreamReader}
import java.lang.management.{ManagementFactory, OperatingSystemMXBean, RuntimeMXBean}
import java.lang.reflect.Method
import java.nio.charset.Charset
import java.text.{DecimalFormat, DecimalFormatSymbols}
import java.util.{Locale, Map => JMap}

import Type._
import org.apache.commons.io.IOUtils

import scala.collection.immutable.HashMap

/**
 * <br/>created at 16-3-21
 * @author liuxh
 * @since 1.1.11
 */
object SystemInfo {
  val ONE_KB: Double = 1024.toDouble
  val ONE_MB: Double = ONE_KB * ONE_KB
  val ONE_GB: Double = ONE_KB * ONE_MB

  def getSystemInfo: JMap[String, Any] = {
    val os: OperatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean
    var info = HashMap("name" -> os.getName, "version" -> os.getVersion, "arch" -> os.getArch, "systemLoadAverage" -> os.getSystemLoadAverage)

    // com.sun.management.OperatingSystemMXBean
    info += ("committedVirtualMemorySize" -> addGetterIfAvaliable(os, "committedVirtualMemorySize"))
    info += ("freePhysicalMemorySize" -> addGetterIfAvaliable(os, "freePhysicalMemorySize"))
    info += ("freeSwapSpaceSize" -> addGetterIfAvaliable(os, "freeSwapSpaceSize"))
    info += ("processCpuTime" -> addGetterIfAvaliable(os, "processCpuTime"))
    info += ("totalPhysicalMemorySize" -> addGetterIfAvaliable(os, "totalPhysicalMemorySize"))
    info += ("totalSwapSpaceSize" -> addGetterIfAvaliable(os, "totalSwapSpaceSize"))

    // com.sun.management.UnixOperatingSystemMXBean
    info += ("openFileDescriptorCount" -> addGetterIfAvaliable(os, "openFileDescriptorCount"))
    info += ("maxFileDescriptorCount" -> addGetterIfAvaliable(os, "maxFileDescriptorCount"))

    try {
      if (!os.getName.toLowerCase(Locale.ROOT).startsWith("windows")) {
        info += ("uname" -> execute("uname -a"))
        info += ("uptime" -> execute("uptime"))
      }
    } catch {
      case _: Exception =>
    }
    >>(info)
  }

  def getJvmInfo: JMap[String, Any] = {
    val javaVersion: String = System.getProperty("java.specification.version", "unknown")
    val javaVendor: String = System.getProperty("java.specification.vendor", "unknown")
    val javaName: String = System.getProperty("java.specification.name", "unknown")
    val jreVersion: String = System.getProperty("java.version", "unknown")
    val jreVendor: String = System.getProperty("java.vendor", "unknown")
    val vmVersion: String = System.getProperty("java.vm.version", "unknown")
    val vmVendor: String = System.getProperty("java.vm.vendor", "unknown")
    val vmName: String = System.getProperty("java.vm.name", "unknown")

    var jvm: HashMap[String, Any] = HashMap("version" -> (jreVersion + " " + vmVersion), "name" -> (jreVendor + " " + vmName))
    jvm += ("spec" -> HashMap("vendor" -> javaVendor, "name" -> javaName, "version" -> javaVersion))
    jvm += ("jre" -> HashMap("vendor" -> jreVendor, "version" -> jreVersion))
    jvm += ("vm" -> HashMap("vendor" -> vmVendor, "name" -> vmName, "version" -> vmVersion))

    val runtime: Runtime = Runtime.getRuntime
    val free: Long = runtime.freeMemory
    val max: Long = runtime.maxMemory
    val total: Long = runtime.totalMemory
    val used: Long = total - free
    val percentUsed: Double = (used.toDouble / max.toDouble) * 100

    jvm += ("processors" -> runtime.availableProcessors)
    val df = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ROOT))
    var mem: HashMap[String, Any] = HashMap("free" -> humanReadableUnits(free, df), "used" -> (humanReadableUnits(used, df) + " (%" + df.format(percentUsed) + ")"), "total" -> humanReadableUnits(total, df), "max" -> humanReadableUnits(max, df))
    mem += ("raw" -> HashMap("free" -> free, "used" -> used, "used%" -> percentUsed, "total" -> total, "max" -> max))
    jvm += ("memory" -> mem)

    val mx: RuntimeMXBean = ManagementFactory.getRuntimeMXBean
    jvm += ("jmx" -> HashMap("bootclasspath" -> mx.getBootClassPath, "classpath" -> mx.getClassPath, "commandLineArgs" -> mx.getInputArguments, "startTime" -> mx.getStartTime, "upTimeMS" -> mx.getUptime))
    >>(jvm)
  }

  private def humanReadableUnits(bytes: Long, df: DecimalFormat) = if ((bytes / ONE_GB).toLong > 0) df.format(bytes / ONE_GB) + "GB" else if ((bytes / ONE_MB).toLong > 0) df.format(bytes / ONE_MB) + "MB" else if ((bytes / ONE_KB).toLong > 0) df.format(bytes / ONE_KB) + "KB" else df.format(bytes) + "bytes"

  private def addGetterIfAvaliable(obj: AnyRef, getter: String): Any = {
    try {
      val n: String = Character.toUpperCase(getter.charAt(0)) + getter.substring(1)
      val m: Method = obj.getClass.getMethod("get" + n)
      m.setAccessible(true)
      m.invoke(obj, null.asInstanceOf[Array[AnyRef]])
    } catch {
      case ignored: Exception => ""
    }
  }

  private def execute(cmd: String): String = {
    var in: InputStream = null
    var process: Process = null
    try {
      process = Runtime.getRuntime.exec(cmd)
      in = process.getInputStream
      IOUtils.toString(new InputStreamReader(in, Charset.defaultCharset))
    } catch {
      case ex: Exception => "(error executing: " + cmd + ")"
      case err: Error =>
        if (err.getMessage != null && (err.getMessage.contains("posix_spawn") || err.getMessage.contains("UNIXProcess"))) {
          return "(error executing: " + cmd + ")"
        }
        throw err
    } finally {
      if (process != null) {
        IOUtils.closeQuietly(process.getOutputStream)
        IOUtils.closeQuietly(process.getInputStream)
        IOUtils.closeQuietly(process.getErrorStream)
      }
    }
  }
}
