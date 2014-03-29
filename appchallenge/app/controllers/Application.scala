package controllers

import play.api._
import models._
import play.api.mvc._
import play.api.Logger
import com.github.nscala_time.time.Imports._
import play.api.libs.json.Json


class UserNotFoundException(msg: String) extends RuntimeException(msg)

object Application extends Controller {

  def decodeBasicAuth(auth: String) = {
    val baStr = auth.replaceFirst("Basic ", "")
    var Array(user, pass) = new String(new sun.misc.BASE64Decoder().decodeBuffer(baStr), "UTF-8").split(":")
    (user, pass)
  }

  def index = Action { implicit request =>
    Ok(views.html.index("Your new application is ready."))
  }

  // def index = Action {

  //   // val month = LocalDateTime.now.month
  //   // Logger.debug(month.get.toString)

  //    // val formatter = DateTimeFormat.forPattern("HH:mm");
  //    // val start = new LocalTime(formatter.parseDateTime("10:00"))
  //    // val end = new LocalTime(formatter.parseDateTime("12:00"))

  //    // val between = new LocalTime(formatter.parseDateTime("10:00"))

  //    // val interval = new LocalTimeInterval(start, end);

  //    // val result = interval.contains(between)

  //    // Logger.debug(result.toString)

  //   Ok(views.html.index("Your new application is ready."))
  // }

  def delete = Action { implicit request =>
    Logger.info("Push delete!")
    Ok("{status: \"Ok\"}")
  }

  def login = Action { implicit request =>

    try{

      request.headers.get("Authorization").map{ basicAuth =>

          val (name, pass) = decodeBasicAuth(basicAuth)

          Logger.debug(s"Searching for $name with $pass")

          Alumno.findByNameAndPassword(name, pass).map{
          user => 
          Ok(Json.obj("message" -> s"Hello $user identified by $pass"))
          }getOrElse(Ok(Json.obj("message" -> "No lo encontramos! :(")))

      }.getOrElse{
           NotFound(Json.obj("error" -> "user not found"))
      }

      }catch{
          case e: Exception => 
          {
            Logger.debug(e.getMessage)
            NotFound(Json.obj("error" -> "user not found"))
          }
        }
      }

    }