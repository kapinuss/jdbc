import scalikejdbc._

object JDBC {

  GlobalSettings.loggingSQLAndTime = new LoggingSQLAndTimeSettings(
    enabled = true,
    singleLineMode = true,
    logLevel = 'DEBUG
  )

  Class.forName("org.postgresql.Driver")
  val poolSettings = new ConnectionPoolSettings(initialSize = 100, maxSize = 100)
  val url = "jdbc:postgresql://localhost:5432/postgres"
  val user = "postgres"
  val password = "postgres"

  ConnectionPool.singleton(url, user, password, poolSettings)

  def main(args: Array[String]): Unit = {
    implicit val session: AutoSession.type = AutoSession
    sql"""create table test (id serial not null primary key, name varchar(64), created_at timestamp not null)""".execute.apply()
  }
}
