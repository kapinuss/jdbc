import org.json4s.Xml.toJson
import org.json4s.jackson.Serialization
import org.json4s.{Formats, NoTypeHints}

import scala.xml.XML

case class Num (`xdms:number`: String, `xdms:date`: String)
case class Feedback(`xdms:reason`: String, `xdms:comment`: String)

object XMLUtils {

  implicit val formats: Formats = Serialization.formats(NoTypeHints)

  def parseXML(rawXML: String): Unit = {
    val json = toJson(XML.loadString(rawXML.trim))
    val accepted: Boolean = (json \\ "xdms:documentAccepted").children.nonEmpty
    if (accepted) println("Принято")
    val refused: Boolean = (json \\ "xdms:documentRefused").children.nonEmpty
    if (refused) println("Отклонено")
    val ggeNumber = (json \\ "xdms:foundation" \\ "xdms:num").extract[Num]
    val minstroyNumber = if (accepted) (json \\ "xdms:foundation" \\ "xdms:num").extract[Num]
    val reason = if (refused) (json \\ "xdms:reason").extract[String]
    val comment = if (refused) (json \\ "xdms:comment").extract[String]
    println("Причина: " + reason + "\nКомментарий: " + comment)
    println("ggeNumber " + ggeNumber)
  }
}
