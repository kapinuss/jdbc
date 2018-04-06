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
    val titles = DB readOnly { implicit session =>
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
        XMLUtils.parseXML(getTextFromFile(sub))
      })
  }

  def getSubs(dir: File): List[String] = dir.listFiles.filter(_.isDirectory).map(_.getName).toList

  def getTextFromFile(dir: String): String = {
    val fileAddress = s"/home/stanislav/newfolder/$dir/notification.xml"

    val fileContent: String = if (new java.io.File(fileAddress).exists) {
      val source: BufferedSource = io.Source.fromFile(fileAddress, "windows-1251")
      try source.mkString finally source.close()
    } else ""
    fileContent
  }
}

