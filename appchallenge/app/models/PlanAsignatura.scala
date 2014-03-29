package models;

import java.util.Date;

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import scala.language.postfixOps

case class PlanAsignatura(

  idAsignatura: Pk[Long] = NotAssigned,
  idPlan: Pk[Long] = NotAssigned

)
  

object PlanAsignatura {

  /**
   * Parse an Event from a ResultSet
   */
  val planAsignatura = {
    get[Pk[Long]]("planAsignatura.idAsignatura") ~
    get[Pk[Long]]("planAsignatura.idPlan")  map {
        case idAsignatura ~ idPlan   => PlanAsignatura(idAsignatura, idPlan)
    }
  }

  /**
  * Retrieve all Alumnos.
  */
  def all(): List[PlanAsignatura] = DB.withConnection {
    implicit c => SQL("select * from planasignatura").as(planAsignatura *)
  }

  /**
  * Retrieve a event from the id.
  * @param id the event id
  */
  def findById(idAsignatura: Int, idPlan: Int): Option[PlanAsignatura] = {
    DB.withConnection { implicit connection =>
      SQL("""select * from planasignatura where idAsignatura = {id}
            and idPlan = {idPlan}    
          """).on(
          'idPlan -> idPlan,
          'idAsignatura -> idAsignatura
          ).as(PlanAsignatura.planAsignatura.singleOpt)
    }
  }

}
