import scalikejdbc._
import JDBCSettings._
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.ActorMaterializer

case class Arbeitgeber(title: String)

object JDBC {

  ConnectionPool.singleton(url, user, password, poolSettings)

  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem()
    implicit val mat: ActorMaterializer = ActorMaterializer()

    implicit val session: AutoSession.type = AutoSession

    sql"""CREATE TABLE IF NOT EXISTS arbeitgeber (id serial not null primary key, title varchar(64), created_at timestamp not null)""".execute.apply()

    val insertActor: ActorRef = system.actorOf(Props[InsertActor], "insertActor")
    insertActor ! Arbeitgeber("Sbertech")

  }
}
