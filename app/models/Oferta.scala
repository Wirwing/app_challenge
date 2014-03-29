package models;

import java.util.Date;
import org.joda.time._

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import scala.language.postfixOps
import play.api.Logger

case class Oferta(

  idAsignatura: Pk[Long] = NotAssigned,
  idProfesor: Pk[Long]  = NotAssigned,
  periodo: Pk[Date] = NotAssigned,
  dia: Int,
  horaInicial: String,
  horaFinal: String

  )


object Oferta {

  /**
   * Parse an Event from a ResultSet
   */
   val oferta = {
    get[Pk[Long]]("oferta.idAsignatura") ~
    get[Pk[Long]]("oferta.idProfesor") ~
    get[Pk[Date]]("oferta.periodo") ~
    get[Int]("oferta.dia") ~
    get[String]("oferta.horaInicial") ~
    get[String]("oferta.horaFinal") map {
      case idAsignatura ~ idProfesor ~ periodo ~ dia ~ horaInicial ~ horaFinal  => Oferta(idAsignatura, idProfesor, periodo, dia, horaInicial, horaFinal)
    }
  }

  /**
  * Retrieve all Alumnos.
  */
  def all(): List[Oferta] = DB.withConnection {
    implicit c => SQL("select * from oferta").as(oferta *)
  }

  def allInLapse( lapse  :String ): List[Oferta] = DB.withConnection {
   implicit c => SQL("select * from oferta where periodo = {lapse}").on( 'lapse -> lapse ).as(oferta *)
 }

 def allGroupByTeacher(  ): List[Oferta] = {

      val data  = DB.withConnection {

       implicit c => SQL("select * from oferta").as(oferta *)
     }

     Logger.info( data.toString )

     return data.groupBy( x => (x.idProfesor.get.toInt, x.idAsignatura.get.toInt) ).map( x => x._2(0) ).to[List]
}

  /**
  * Retrieve a event from the id.
  * @param id the event id
  */
  def findById(idAsignatura: Long): Option[Oferta] = {
    DB.withConnection { implicit connection =>
      SQL("select * from oferta where idAsignatura = {idAsignatura}").on('idAsignatura -> idAsignatura).as(Oferta.oferta.singleOpt)
    }
  }

/**
  * Retrieve a event from the id.
  * @param id the event id
  */
  def allWithAsignatureId(idAsignatura: Long): List[Oferta] = {
    DB.withConnection { implicit connection =>
      SQL("select * from oferta where idAsignatura = {idAsignatura}").on('idAsignatura -> idAsignatura).as(oferta *)
    }
  }

  def findByIdAnd(idAsignatura: Long): Option[Oferta] = {
    DB.withConnection { implicit connection =>
      SQL("select * from oferta where idAsignatura = {idAsignatura}").on('idAsignatura -> idAsignatura).as(Oferta.oferta.singleOpt)
    }
  }

}
