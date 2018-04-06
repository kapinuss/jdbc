import java.io.File

import akka.actor.Actor
import scalikejdbc._
import scala.io.BufferedSource
import JDBC.session

class DirManActor extends Actor {

  var allDirs: List[String] = List.empty
  var diffDirs: List[String] = List.empty

  val dir: String = Config.getString("dir.documents")

  override def preStart(): Unit = {
    val titles: List[String] = DB readOnly { implicit session =>
      sql"SELECT title FROM SUBDIR".map(rs => rs.string("title")).list.apply()
    }
    println(s"В базе хранятся данные про ${titles.size} папок.")
    allDirs = titles
  }

  def receive: PartialFunction[Any, Unit] = {
    case message: String =>
      println(message)
      val subs: List[String] = getSubs(new File(dir))
      diffDirs = subs.diff(allDirs)
      println(s"diffDirs $diffDirs")
      allDirs = subs
      println(s"allDirs $allDirs")

      diffDirs.foreach(sub => {
        println(sub)
        val notification = XMLUtils.parseXML(getTextFromFile(sub))
        saveSub(sub)
      })
  }

  def getSubs(dir: File): List[String] = dir.listFiles.filter(_.isDirectory).map(_.getName).toList

  def getTextFromFile(subdir: String): String = {
    val fileAddress = s"$dir/$subdir/notification.xml"

    val fileContent: String = if (new java.io.File(fileAddress).exists) {
      val source: BufferedSource = io.Source.fromFile(fileAddress, "windows-1251")
      try source.mkString finally source.close()
    } else ""
    fileContent
  }

  def saveSub(sub: String): Unit = sql"""INSERT INTO SUBDIR values ($sub, 2)""".execute.apply()

  def saveNotification(not: Notification): AnyVal = not.accepted match{
    case true => sql"""begin pkg_sedo.set_sedo_registered ('${not.ggeNumber.`xdms:number`}', '${not.ggeNumber.`xdms:date`}', '${not.minstroyNumber.`xdms:number`}', '${not.minstroyNumber.`xdms:date`}' end;""".execute.apply()
    case false =>
  }
}

