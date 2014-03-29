package controllers

import play.api._
import play.api.mvc._
import play.api.Logger
import com.github.nscala_time.time.Imports._

object Application extends Controller {

  def index = Action {

    // val month = LocalDateTime.now.month
    // Logger.debug(month.get.toString)

     // val formatter = DateTimeFormat.forPattern("HH:mm");
     // val start = new LocalTime(formatter.parseDateTime("10:00"))
     // val end = new LocalTime(formatter.parseDateTime("12:00"))

     // val between = new LocalTime(formatter.parseDateTime("10:00"))

     // val interval = new LocalTimeInterval(start, end);

     // val result = interval.contains(between)

     // Logger.debug(result.toString)

    Ok(views.html.index("Your new application is ready."))
  }

  def delete = Action { implicit request =>
    Logger.info("Push delete!")
    Ok("{status: \"Ok\"}")
  }

  def login = Action { implicit request =>

    request.headers.get("Authorization") match {
      case Some(header) => {
        println(header)
        Ok("{status: \"Ok auth\"}")
      }
      case None => {
        println("send user name and password")
        Unauthorized.withHeaders("WWW-Authenticate" -> "Basic realm=\"myrealm\"")
      }           
    }

  }

}