import scalikejdbc.{ConnectionPoolSettings, GlobalSettings, LoggingSQLAndTimeSettings}

object JDBCSettings {
  GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(
    enabled = true,
    singleLineMode = true,
    logLevel = 'DEBUG
  )

  Class.forName(Config.getString("db.oracle.driver"))
  val poolSettings = new ConnectionPoolSettings(initialSize = 100, maxSize = 100)
  val url: String = Config.getString("db.oracle.url")
  val user: String = Config.getString("db.oracle.user")
  val password: String = Config.getString("db.oracle.password")
}
