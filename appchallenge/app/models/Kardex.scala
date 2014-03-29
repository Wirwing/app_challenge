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

  /**
  *possible deprecated
  */

  def isAproved( alumnoId: Int, asignaturaId:Int ):Boolean = {
    val aprovedDependencies = DB.withConnection { implicit connection =>
      SQL("select * from kardex where alumnoId = {alumnoId} and asignaturaId = {asignaturaId} and situacion = 1").on(
        'alumnoId -> alumnoId,
        'asignaturaId -> asignaturaId
        ).as( kardex * )
    }

    ! aprovedDependencies.isEmpty
  }


  def getAllAproved( studentId: Int ): List[Kardex] = {
     DB.withConnection { implicit connection =>
      SQL("select * from kardex where alumnoId = {studentId} and situacion = '1'").on(
        'studentId -> studentId        
        ).as( kardex *)
    }
  }


  def findByStudentId( studentId: Int ): List[Kardex] = {
       DB.withConnection { implicit connection =>
      SQL("select * from kardex where alumnoId = {studentId}").on(
        'studentId -> studentId        
        ).as( kardex *)
    }




  }

}
