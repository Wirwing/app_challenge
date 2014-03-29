package models;

import java.util.Date;

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import scala.language.postfixOps

case class Kardex(

  alumnoId: Pk[Long] = NotAssigned,
  asignaturaId: Pk[Long] = NotAssigned,
  periodo: Pk[Date] = NotAssigned,
  situacion: Int,
  tipo: Int
  
)

object Kardex {

  /**
   * Parse an Event from a ResultSet
   */
  val Kardex = {
      get[Pk[Long]]("kardex.id") ~
      get[String]("kardex.name") ~
      get[Int]("kardex.credits") 
      map {
        case id ~ name ~ credits  => Kardex(id, name, credits)
      }
  }

  /**
  * Retrieve all Alumnos.
  */
  def all(): List[Kardex] = DB.withConnection {
    implicit c => SQL("select * from Aaignatura").as(kardex *)
  }

  /**
  * Retrieve a event from the id.
  * @param id the event id
  */
  def findById(id: Long): Option[Kardex] = {
    DB.withConnection { implicit connection =>
      SQL("select * from kardex where id = {id}").on('id -> id).as(kardex.asignatura.singleOpt)
    }
  }

}
