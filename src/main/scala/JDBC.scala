import scalikejdbc._
import JDBCSettings._
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.ActorMaterializer
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

case class Arbeitgeber(title: String)

object JDBC {

  ConnectionPool.singleton(url, user, password, poolSettings)
  implicit val session: AutoSession.type = AutoSession
  implicit val system: ActorSystem = ActorSystem()
  implicit val mat: ActorMaterializer = ActorMaterializer()

  def main(args: Array[String]): Unit = {

    val insertActor: ActorRef = system.actorOf(Props[InsertActor], "insertActor")
    val deleteActor: ActorRef = system.actorOf(Props[DeleteActor], "deleteActor")
    val countActor: ActorRef = system.actorOf(Props[CountActor], "countActor")
    val dirManActor: ActorRef = system.actorOf(Props[DirManActor], "dirManActor")

    system.scheduler.schedule(2 seconds, 5 seconds, dirManActor, "heartBeat")
  }


}
