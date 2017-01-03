package cn.howardliu.gear.commons.utils

import java.io.{InputStreamReader, InputStream}
import java.nio.charset.Charset

import org.apache.commons.io.IOUtils

/**
 * <br>created at 16-12-27
 * @author liuxh
 * @since 1.0.1
 */
object Processor {
  def execute(cmd: String): String = {
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

  def execute(cmd: Array[String]): String = {
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
