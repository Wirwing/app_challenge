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
  val kardex= {
      get[Pk[Long]]("kardex.alumnoId") ~
      get[Pk[Long]]("kardex.asignaturaId") ~
      get[Pk[Date]]("kardex.periodo") ~
      get[Int]("kardex.situacion") ~
      get[Int]("kardex.tipo") map {
        case alumnoId ~ asignaturaId ~ periodo ~ situacion ~ tipo => Kardex(alumnoId, asignaturaId, periodo, situacion, tipo)
      }
  }

  /**
  * Retrieve all Alumnos.
  */
  def all(): List[Kardex] = DB.withConnection {
    implicit c => SQL("select * from kardex").as(kardex *)
  }

  /**
  * Retrieve a event from the id.
  * @param id the event id
  */
  def findById(alumnoId: Long, asignaturaId: Long): Option[Kardex] = {
    DB.withConnection { implicit connection =>
      SQL("select * from kardex where alumnoId = {alumnoId} and asignaturaId = {asignaturaId}").on(
        'alumnoId -> alumnoId,
        'asignaturaId -> asignaturaId
        ).as(Kardex.kardex.singleOpt)
    }
  }

  def isAproved( alumnoId: Int, asignaturaId:List[Dependencia] ){
    val aprovedDependencies = DB.withConnection { implicit connection =>
      SQL("select * from kardex where alumnoId = {alumnoId} and asignaturaId = {asignaturaId} and situacion = 1").on(
        'alumnoId -> alumnoId,
        'asignaturaId -> asignaturaId
        ).as(Kardex.kardex.singleOpt)
    }

    ! aprovedDependencies.isEmpty
  }

}
