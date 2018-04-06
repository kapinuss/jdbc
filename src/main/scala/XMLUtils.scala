import org.json4s.Xml.toJson
import org.json4s.jackson.Serialization
import org.json4s.{Formats, NoTypeHints}

import scala.xml.XML

case class Num (`xdms:number`: String, `xdms:date`: String)
case class Feedback(`xdms:reason`: String, `xdms:comment`: String)
case class Notification(accepted: Boolean, refused: Boolean, ggeNumber: Num, minstroyNumber: Num, reason: String, comment: String)

object XMLUtils {

  implicit val formats: Formats = Serialization.formats(NoTypeHints)
  def parseXML(rawXML: String): Notification = if (rawXML.nonEmpty) {
    val json = toJson(XML.loadString(rawXML.trim))
    val accepted: Boolean = (json \\ "xdms:documentAccepted").children.nonEmpty
    val refused: Boolean = (json \\ "xdms:documentRefused").children.nonEmpty
    val ggeNumber = (json \\ "xdms:foundation" \\ "xdms:num").extract[Num]
    val minstroyNumber = if (accepted) (json \\ "xdms:foundation" \\ "xdms:num").extract[Num] else Num("", "")
    val reason = if (refused) (json \\ "xdms:reason").extract[String] else ""
    val comment = if (refused) (json \\ "xdms:comment").extract[String] else ""
    Notification(accepted, refused, ggeNumber, minstroyNumber, reason, comment)
  } else Notification.apply(false, false, Num("", ""), Num("", ""), "", "")
}
