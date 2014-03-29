package models;

import java.util.Date;

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import scala.language.postfixOps

case class Oferta(

  idAsignatura: Pk[Long] = NotAssigned,
  idProfesor: Pk[Long]  = NotAssigned,
  periodo: Pk[Date] = NotAssigned

)
  

object Oferta {

  /**
   * Parse an Event from a ResultSet
   */
  val oferta = {
      get[Pk[Long]]("oferta.idAsignatura") ~
      get[Pk[Long]]("oferta.idProfesor") ~
      get[Pk[Date]]("oferta.periodo") map {
        case idAsignatura ~ idProfesor ~ periodo  => Oferta(idAsignatura, idProfesor, periodo)
      }
  }

  /**
  * Retrieve all Alumnos.
  */
  def all(): List[Oferta] = DB.withConnection {
    implicit c => SQL("select * from oferta").as(oferta *)
  }

  /**
  * Retrieve a event from the id.
  * @param id the event id
  */
  def findById(id: Long): Option[Oferta] = {
    DB.withConnection { implicit connection =>
      SQL("select * from oferta where id = {id}").on('id -> id).as(Oferta.oferta.singleOpt)
    }
  }

}
