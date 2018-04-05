import java.io.File
import scalikejdbc._
import JDBCSettings._
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.ActorMaterializer
import scala.io.BufferedSource
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

case class Arbeitgeber(title: String)

object JDBC {

  ConnectionPool.singleton(url, user, password, poolSettings)
  implicit val session: AutoSession.type = AutoSession
  implicit val system: ActorSystem = ActorSystem()
  implicit val mat: ActorMaterializer = ActorMaterializer()

  def main(args: Array[String]): Unit = {

    //sql"""CREATE TABLE IF NOT EXISTS arbeitgeber (id serial not null primary key, title varchar(64))""".execute.apply()

    val insertActor: ActorRef = system.actorOf(Props[InsertActor], "insertActor")
    val deleteActor: ActorRef = system.actorOf(Props[DeleteActor], "deleteActor")
    val countActor: ActorRef = system.actorOf(Props[CountActor], "countActor")
    val dirManActor: ActorRef = system.actorOf(Props[DirManActor], "dirManActor")

    system.scheduler.schedule(2 seconds, 5 seconds, dirManActor, "test")

    //insertActor ! Arbeitgeber("GP")
    //countActor ! "PERSONS"

    val subs: List[String] = getSubs(new File("/home/stanislav/newfolder"))
    subs.foreach(sub => XMLUtils.parseXML(getTextFromFile(sub)))
  }

  def getSubs(dir: File): List[String] = dir.listFiles.filter(_.isDirectory).map(_.getName).toList

  def getTextFromFile(dir: String): String = {
    val source: BufferedSource = io.Source.fromFile(s"/home/stanislav/newfolder/$dir/notification.xml", "windows-1251")
    val xml: String = try source.mkString finally source.close()
    xml
  }
}
