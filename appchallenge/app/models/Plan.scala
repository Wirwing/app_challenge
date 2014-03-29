package models;

import java.util.Date;

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import scala.language.postfixOps

case class Plan(

  id: Pk[Long] = NotAssigned,
  name: String
)
  

object Plan {

  /**
   * Parse an Event from a ResultSet
   */
  val plan = {
    get[Pk[Long]]("plan.id") ~
      get[String]("plan.name") 
      map {
        case id ~ name  => Plan(id, name)
      }
  }

  /**
  * Retrieve all Alumnos.
  */
  def all(): List[Plan] = DB.withConnection {
    implicit c => SQL("select * from plan").as(plan *)
  }

  /**
  * Retrieve a event from the id.
  * @param id the event id
  */
  def findById(id: Long): Option[Plan] = {
    DB.withConnection { implicit connection =>
      SQL("select * from plan where id = {id}").on('id -> id).as(Plan.plan.singleOpt)
    }
  }

}
