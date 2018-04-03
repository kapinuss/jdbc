import akka.NotUsed
import akka.actor.Actor
import akka.stream.scaladsl.Source
import scalikejdbc._


abstract class SQLActor extends Actor {
  import JDBC.session

}
