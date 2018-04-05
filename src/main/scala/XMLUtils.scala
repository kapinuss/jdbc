import org.json4s.Xml.toJson
import org.json4s.jackson.Serialization
import org.json4s.{Formats, JValue, NoTypeHints}

import scala.util.Try
import scala.xml.{Elem, XML}

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


  val accepted: String =
    """
      |<?xml version="1.0" encoding="windows-1251"?>
      |<xdms:communication xmlns:xdms="http://www.infpres.com/IEDMS" xdms:version="2.2">
      |  <xdms:header xdms:type="Уведомление" xdms:uid="b99ecde8-2582-42ea-be3a-065685884827" xdms:created="2018-03-30T10:41:12.502">
      |    <xdms:source xdms:uid="39b3deb1-9f60-49a5-8fb9-2af8020071ed">
      |      <xdms:organization>Министерство строительства и жилищно-коммунального хозяйства (Минстрой России)</xdms:organization>
      |    </xdms:source>
      |  </xdms:header>
      |  <xdms:notification xdms:type="Зарегистрирован" xdms:uid="a644d9ca-d2c1-c94e-8373-2f2883696a93">
      |    <xdms:documentAccepted>
      |      <xdms:time>2018-03-30T10:41:12.503</xdms:time>
      |      <xdms:foundation>
      |        <xdms:organization>Федеральное автономное учреждение "Главное управление государственной экспертизы"</xdms:organization>
      |        <xdms:num>
      |          <xdms:number>20-02-3/5046-ИЛ</xdms:number>
      |          <xdms:date>2018-03-28</xdms:date>
      |        </xdms:num>
      |      </xdms:foundation>
      |      <xdms:correspondent>
      |        <xdms:region>г. Москва</xdms:region>
      |        <xdms:organization>Министерство строительства и жилищно-коммунального хозяйства (Минстрой России)</xdms:organization>
      |      </xdms:correspondent>
      |      <xdms:num>
      |        <xdms:number>33306/МС</xdms:number>
      |        <xdms:date>2018-03-30</xdms:date>
      |      </xdms:num>
      |    </xdms:documentAccepted>
      |  </xdms:notification>
      |</xdms:communication>
    """.stripMargin

  val refused: String =
    """
      |<?xml version="1.0" encoding="windows-1251"?>
      |<xdms:communication xmlns:xdms="http://www.infpres.com/IEDMS" xdms:version="2.2">
      |  <xdms:header xdms:type="Уведомление" xdms:uid="af1a97a0-8f3f-4d14-8bfa-9b540c5ed7a9" xdms:created="2018-03-30T10:10:39.159">
      |    <xdms:source xdms:uid="39b3deb1-9f60-49a5-8fb9-2af8020071ed">
      |      <xdms:organization>Министерство строительства и жилищно-коммунального хозяйства (Минстрой России)</xdms:organization>
      |    </xdms:source>
      |  </xdms:header>
      |  <xdms:notification xdms:type="Отказано в регистрации" xdms:uid="e175cfcf-8ba9-6f4a-88df-eab5f390c0f2">
      |    <xdms:documentRefused>
      |      <xdms:time>2018-03-30T10:10:39.159</xdms:time>
      |      <xdms:foundation>
      |        <xdms:organization>Федеральное автономное учреждение "Главное управление государственной экспертизы"</xdms:organization>
      |        <xdms:num>
      |          <xdms:number>01-01-15/5160-ИМ</xdms:number>
      |          <xdms:date>2018-03-30</xdms:date>
      |        </xdms:num>
      |      </xdms:foundation>
      |      <xdms:correspondent>
      |        <xdms:region>г. Москва</xdms:region>
      |        <xdms:organization>Министерство строительства и жилищно-коммунального хозяйства (Минстрой России)</xdms:organization>
      |      </xdms:correspondent>
      |      <xdms:reason>Не подлежит регистрации</xdms:reason>
      |    </xdms:documentRefused>
      |    <xdms:comment>по договоренности</xdms:comment>
      |  </xdms:notification>
      |</xdms:communication>
      |
    """.stripMargin
}
