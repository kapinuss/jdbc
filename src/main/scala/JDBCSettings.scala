import scalikejdbc.{ConnectionPoolSettings, GlobalSettings, LoggingSQLAndTimeSettings}

object JDBCSettings {
  GlobalSettings.loggingSQLAndTime = new LoggingSQLAndTimeSettings(
    enabled = true,
    singleLineMode = true,
    logLevel = 'DEBUG
  )

  Class.forName(Config.getString("db.oracle.driver"))
  val poolSettings = new ConnectionPoolSettings(initialSize = 100, maxSize = 100)
  val url = Config.getString("db.oracle.url")
  val user = Config.getString("db.oracle.user")
  val password = Config.getString("db.oracle.password")
}
