import org.json4s.Xml.toJson
import org.json4s.jackson.Serialization
import org.json4s.{Formats, NoTypeHints}
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import scala.xml.XML

case class Num (`xdms:number`: String, `xdms:date`: String)
object Num {
  def apply(`xdms:number`: String, `xdms:date`: String): Num = {
    val parsedDate = LocalDate.parse(`xdms:date`, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val oracleString: String = parsedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    new Num (`xdms:number`, oracleString)
  }
}
case class Feedback(`xdms:reason`: String, `xdms:comment`: String)
case class Notification(accepted: Boolean, refused: Boolean, ggeNumber: Num, minstroyNumber: Num, reason: String, comment: String)

object XMLUtils {

  implicit val formats: Formats = Serialization.formats(NoTypeHints)
  def parseXML(rawXML: String): Notification = if (rawXML.nonEmpty) {
    val json = toJson(XML.loadString(rawXML.trim))
    val accepted: Boolean = (json \\ "xdms:documentAccepted").children.nonEmpty
    val refused: Boolean = (json \\ "xdms:documentRefused").children.nonEmpty
    val ggeNumber = (json \\ "xdms:foundation" \\ "xdms:num").extract[Num]
    println("GGeNum = " + ggeNumber)
    val minstroyNumber = if (accepted) (json \\ "xdms:foundation" \\ "xdms:num").extract[Num] else Num("", "2001-09-11")
    println("MinStroyNum = " + minstroyNumber)
    val reason = if (refused) (json \\ "xdms:reason").extract[String] else ""
    val comment = if (refused) (json \\ "xdms:comment").extract[String] else ""
    Notification(accepted, refused, ggeNumber, minstroyNumber, reason, comment)
  } else Notification.apply(false, false, Num("", "2001-09-11"), Num("", "2001-09-11"), "", "")

  //def performDate
}
