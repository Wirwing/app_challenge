package models;

import java.util.Date;

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import scala.language.postfixOps

case class Dependencia(

  asignaturaId: Pk[Long] = NotAssigned,
  requisitoId: Pk[Long] = NotAssigned
  
)

object Dependencia {

  /**
   * Parse a Dependencia from a ResultSet
   */
  val dependencia = {
      get[Pk[Long]]("dependencia.asignaturaId") ~
      get[Pk[Long]]("dependencia.requisitoId") map {
        case asignaturaId ~ requisitoId => Dependencia(asignaturaId, requisitoId)
      }
  }

  /**
  * Retrieve a Dependencia from the id.
  * @param id the asignature id
  */
  def findById(asignaturaId: Long): List[Dependencia] = {
    DB.withConnection { implicit connection =>
      SQL("select * from dependencia where asignaturaId = {asignaturaId}").on(
        'asignaturaId -> asignaturaId
        ).as(dependencia *)
    }
  }

}
