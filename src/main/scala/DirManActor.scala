import java.io.File

import akka.actor.Actor
import scalikejdbc._
import scala.io.BufferedSource
import JDBC.session

class DirManActor extends Actor with akka.actor.ActorLogging {

  var allDirs: List[String] = List.empty
  var diffDirs: List[String] = List.empty
  val dir: String = Config.getString("dir.documents")

  override def preStart(): Unit = {
    val titles: List[String] = DB readOnly { implicit session =>
      sql"SELECT title FROM SUBDIR".map(rs => rs.string("title")).list.apply()
    }
    JDBC.system.log.info(s"В базе хранятся данные про ${titles.size} папок.")
    allDirs = titles
  }

  def receive: PartialFunction[Any, Unit] = {
    case message: String if message.nonEmpty =>
      val subs: List[String] = getSubs(new File(dir))
      diffDirs = subs.diff(allDirs)
      allDirs = subs
      diffDirs.foreach(sub => {
        JDBC.system.log.info("Работаем с субдиректорией " + sub)
        val notification: Notification = XMLUtils.parseXML(getTextFromFile(sub))
        saveSub(sub,notification)
      })
    case _ => JDBC.system.log.info("DirManActor recieved some shit.")
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

  def saveSub(sub: String, notification: Notification): Unit = notification match {
    case Notification(true,_,_,_,_,_) =>
      println(notification)
      sql"""INSERT INTO SUBDIR values ($sub, 1)""".execute.apply ()
      //val sql = s"begin pkg_sedo.set_sedo_registered ('${notification.ggeNumber.`xdms:number`}', '${notification.ggeNumber.`xdms:date`}', '${notification.minstroyNumber.`xdms:number`}', '${notification.minstroyNumber.`xdms:date`}' end;"
      //println(sql)
      //sql"""$sql""".execute.apply()
    case Notification(_,true,_,_,_,_) => sql"""INSERT INTO SUBDIR values ($sub, 0)""".execute.apply ()
    case Notification(false,false,_,_,_,_) => sql"""INSERT INTO SUBDIR values ($sub, 2)""".execute.apply () //TODO do we need this insert?
    case _ =>
  }
  def saveNotification(not: Notification): AnyVal = not.accepted match {
    case true => sql"""begin pkg_sedo.set_sedo_registered ('${not.ggeNumber.`xdms:number`}', '${not.ggeNumber.`xdms:date`}', '${not.minstroyNumber.`xdms:number`}', '${not.minstroyNumber.`xdms:date`}' end;""".execute.apply()
    case false =>
  }
}

