package models;

import java.util.Date;

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import scala.language.postfixOps

case class Alumno(

  id: Pk[Long] = NotAssigned,
  name: String,
  password: String,
  planId: Pk[Long] = NotAssigned
  
)

object Alumno {

  /**
   * Parse an Alumn from a ResultSet
   */
  val alumno = {
    get[Pk[Long]]("alumno.id") ~
      get[String]("alumno.name") ~
      get[String]("alumno.password")~
      get[Pk[Long]]("alumno.planId") map {
        case id ~ name ~ password ~ planId => Alumno(id, name, password, planId)
      }
  }

  /**
  * Retrieve an alumn from his id and password.
  */
  def findByNameAndPassword(id: Long, password: String): Option[Alumno] = {
    DB.withConnection { implicit connection =>
      SQL("select * from alumno where id = {alumno_id} and password = {password}").on(
        'alumno_id -> id,
        'password -> password
        ).as(Alumno.alumno.singleOpt)
    }
  }

}
