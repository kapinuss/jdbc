import akka.NotUsed
import akka.actor.Actor
import akka.stream.scaladsl.Source
import scalikejdbc._
import JDBC.session

class DeleteActor extends Actor {

  def receive: PartialFunction[Any, Unit] = {
    case geber: Arbeitgeber => sql"""DELETE FROM arbeitgeber WHERE title = ${geber.title}""".execute.apply()
    case geberId: Int => sql"""DELETE FROM arbeitgeber WHERE id = $geberId""".execute.apply()
  }
}