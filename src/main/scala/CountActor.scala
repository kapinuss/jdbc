import akka.actor.Actor
import scalikejdbc._

class CountActor extends Actor {

  def receive: PartialFunction[Any, Unit] = {
    case table: String => //sql"""SELECT count(*) FROM $table""".execute.apply()
      val number: Option[Long] = DB readOnly { implicit session =>
        sql"""SELECT count(*) as num FROM T_DOC_INV_DETAILS"""
          .map(rs => rs.long("num"))
          .single
          .apply()
      }

      number match {
        case Some(x) => println(x)
        case None =>
      }
  }
}