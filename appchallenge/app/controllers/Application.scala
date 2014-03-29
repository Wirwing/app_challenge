package controllers

import play.api._
import play.api.mvc._
import play.api.Logger

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def delete = Action { implicit request =>
    Logger.info("Push delete!")
    Ok("{status: \"Ok\"}")
  }

  def login = Action { implicit request =>

      request.headers.get("Authorization") match {
          case Some(header) => println(header)
          case None => {
              println("send user name and password")
              Unauthorized.withHeaders("WWW-Authenticate" -> "Basic realm=\"myrealm\"")
          }           
      }

      Ok("{status: \"Ok auth\"}")

  }

}