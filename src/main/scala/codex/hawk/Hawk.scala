package codex.hawk

import java.io._
import scalaj.http._
import spray.json._
import DefaultJsonProtocol._

case class customException(smth: String) extends Exception(smth)

/**
  * Hawk Catcher for Scala
  */
class HawkCatcher (val token: String, val apiUrl: String = "https://hawk.so/catcher/scala") {

  /**
    * Convert Stack Trace object to String
    *
    * @param t – trace object
    * @return string representation of the stack trace
    */
  def getStackTraceAsString(t: Throwable): String = {
    val sw = new StringWriter
    t.printStackTrace(new PrintWriter(sw))
    sw.toString
  }

  /**
    * Get current time in ISO format
    *
    * @return current time
    */
  def getCurrentTime(): String = {
    import java.time.OffsetDateTime
    import java.time.format.DateTimeFormatter
    val dtf = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    OffsetDateTime.now.format(dtf)
  }

  /**
    * Prepare error data for sending and send the to the Hawk Catcher API
    *
    * @param e – Exception object
    * @param comment – custom string comment
    */
  def catchException(e: Exception, comment: String = ""): Either[String, Exception] = {

    val stack: Seq[Map[String, String]] = e.getStackTrace.map(
      x => Map("line" -> x.getLineNumber.toString, "file" -> x.getFileName, "full" -> x.toString)
    )

    send(stack, e.getMessage, comment)
  }

  /**
    * Send information about Exception to the Hawk API
    *
    * @param stack - sequence of parsed stack lines
    * @return String answer from API or Exception
    */
  def send(stack: Seq[Map[String, String]], errorMessage: String, comment: String): Either[String, Exception] = {
    try {
        val data = Map(
          "tag" -> "fatal",
          "message" -> errorMessage,
          "stack" -> stack.toJson.toString,
          "time" -> getCurrentTime(),
          "token" -> token,
          "comment" -> comment
        )
      val response: HttpResponse[String] = Http(apiUrl).postData(data.toJson.toString).header("content-type", "application/json").asString
      Left(response.body.toString)
    } catch {
      case e: HttpStatusException => Right(customException("API return response with code: " + e.code))
      case e: Exception => Right(customException("Exception: " + e))
    }
  }

}