import org.json4s.Xml.toJson
import org.json4s.jackson.Serialization
import org.json4s.{Formats, NoTypeHints}

import scala.util.Try
import scala.xml.XML

object XML {
  /*
  def parseXML(rawXML: String): Unit = {
    implicit val formats: Formats = Serialization.formats(NoTypeHints)
    val xml = XML.loadString(rawXML.trim().replaceFirst("^([\\W]+)<", "<"))
    val json = toJson(xml)
    val action: String = Try((xml \\ "CIP01Rq" \\ "Action").text).toOption.getOrElse("No data")
    val jv = json \\ "CIP01Rq" \\ "ListOfServiceRequest" \\ "ServiceRequest"
    val data = jv.extract[SiebelData]
    val resultFromSiebel = if (data.SRThematicCode == ""
      && data.Channel == "Email"
      && data.EmailToAddress.isDefined)
      data.copy(SRThematicCode = emailToDep(data.EmailToAddress.get))
    else
      data
    val tiers = Try(xml \\ "CIP01Rq" \\ "ListOfServiceRequest" \\ "ServiceRequest" \\ "ListOfRequesters" \\ "Requester" \\ "TierLevel")
      .toOption.get.map(x => x.text)
    val topCard = if (tiers.isEmpty) "NONE" else if (tiers.contains("PLATINUM")) "PLATINUM" else if (tiers.contains("GOLD")) "GOLD" //todo переписать на матч кейс
    else if (tiers.contains("SILVER")) "SILVER" else if (tiers.contains("BASIC")) "BASIC" else "NONE"
    (action, resultFromSiebel, topCard)
  }
  */

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
