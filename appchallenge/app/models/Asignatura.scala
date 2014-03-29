package models;

import java.util.Date;

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import scala.language.postfixOps

case class Asignatura(

  id: Pk[Long] = NotAssigned,
  name: String,
  credits: Int
)
  

object Asignatura {

  /**
   * Parse an Event from a ResultSet
   */
  val asignatura = {
      get[Pk[Long]]("asignatura.id") ~
      get[String]("asignatura.name") ~
      get[Int]("asignatura.credits") 
      map {
        case id ~ name ~ credits  => Asignatura(id, name, credits)
      }
  }

  /**
  * Retrieve all Alumnos.
  */
  def all(): List[Asignatura] = DB.withConnection {
    implicit c => SQL("select * from Aaignatura").as(asignatura *)
  }

  /**
  * Retrieve a event from the id.
  * @param id the event id
  */
  def findById(id: Long): Option[Asignatura] = {
    DB.withConnection { implicit connection =>
      SQL("select * from asignatura where id = {id}").on('id -> id).as(Asignatura.asignatura.singleOpt)
    }
  }

}
