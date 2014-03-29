package controllers

import models._
import views._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.Logger
import com.github.nscala_time.time.Imports._
import play.api.libs.json.Json

import java.util.Date;

class UserNotFoundException(msg: String) extends RuntimeException(msg)

case class SuggestForm(initialHour: String, finalHour: String)

object Application extends Controller {

  val suggestForm = Form(
    mapping(
      "initialHour" -> text,
      "finalHour" -> text)(SuggestForm.apply)(SuggestForm.unapply))

  def decodeBasicAuth(auth: String) = {
    val baStr = auth.replaceFirst("Basic ", "")
    var Array(user, pass) = new String(new sun.misc.BASE64Decoder().decodeBuffer(baStr), "UTF-8").split(":")
    (user, pass)
  }

  def index = Action { implicit request =>
    Ok(views.html.suggestEntry(suggestForm))
  }

  /**
  * Handle the 'new event form' submission.
  */
  def next(): Action[AnyContent] = Action { implicit request =>

    try{

      suggestForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(html.suggestEntry(formWithErrors))
          },
          suggestForm => {

            val formatter = DateTimeFormat.forPattern("HH:mmaa");
            val start = new LocalTime(formatter.parseDateTime(suggestForm.initialHour))
            val end = new LocalTime(formatter.parseDateTime(suggestForm.finalHour))

            Logger.info(start.toString)
            Logger.info(end.toString)

            Redirect(routes.Application.index)
            })

      } catch {

        case e: Exception =>
        Redirect(routes.Application.index).flashing("error" -> "Error en los datos del formulario!")

      }

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