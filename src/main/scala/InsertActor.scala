import akka.actor.Actor
import scalikejdbc._
import JDBC.session

class InsertActor extends Actor {

  def receive: PartialFunction[Any, Unit] = {
    case a: Arbeitgeber => sql"""INSERT INTO arbeitgeber(title) values (${a.title})""".execute.apply()
  }
}
