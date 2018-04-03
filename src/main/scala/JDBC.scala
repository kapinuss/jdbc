import scalikejdbc._
import JDBCSettings._
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.ActorMaterializer

case class Arbeitgeber(title: String)

object JDBC {

  ConnectionPool.singleton(url, user, password, poolSettings)
  implicit val session: AutoSession.type = AutoSession
  implicit val system: ActorSystem = ActorSystem()
  implicit val mat: ActorMaterializer = ActorMaterializer()

  def main(args: Array[String]): Unit = {

    val host: String = Config.getString("http.host")
    val port: Int = Config.getInt("http.port")

    //sql"""CREATE TABLE IF NOT EXISTS arbeitgeber (id serial not null primary key, title varchar(64))""".execute.apply()

    val insertActor: ActorRef = system.actorOf(Props[InsertActor], "insertActor")
    val deleteActor: ActorRef = system.actorOf(Props[DeleteActor], "deleteActor")
    val countActor: ActorRef = system.actorOf(Props[CountActor], "countActor")

    //insertActor ! Arbeitgeber("GP")
    countActor ! "PERSONS"

  }
}
