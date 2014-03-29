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
  

object Alumno {

  /**
   * Parse an Event from a ResultSet
   */
  val alumno = {
    get[Pk[Long]]("alumno.id") ~
      get[String]("alumno.name") ~
      get[String]("alumno.password") map {
        case id ~ name ~ password => Alumno(id, name, password)
      }
  }

  /**
  * Retrieve all Alumnos.
  */
  def all(): List[Alumno] = DB.withConnection {
    implicit c => SQL("select * from alumno").as(event *)
  }

  /**
  * Retrieve a event from the id.
  * @param id the event id
  */
  def findById(id: Long): Option[Event] = {
    DB.withConnection { implicit connection =>
      SQL("select * from event where id = {id}").on('id -> id).as(Event.event.singleOpt)
    }
  }

  /**
  * Retrieve an event from his user id.
  */
  def findByUserId(userId: Long): List[Event] = {
    DB.withConnection { implicit connection =>
      SQL("select * from event where user_id = {userId}").on('userId -> userId).as(event *)
    }
  }

  /**
  * Retrieve a list of events from their name.
  */
  def allWithName(eventName: String): List[Event] = {
    DB.withConnection { implicit connection =>
      SQL("select * from event where name like {name}").on(
        'name -> ("%" + eventName + "%")).as(event *)
    }
  }

  /**
  * Create an event.
  *
  * @param event Event to persist.
  * @param userId User owner of the event
  */
  def create(event: Event, userId: Long): Long = {
    DB.withConnection { implicit c =>
      SQL("insert into event (name, event_date, description, user_id)  values ({name}, {event_date}, {description}, {userId})").
        on(
          'name -> event.name,
          'event_date -> event.date,
          'description -> event.description,
          'userId -> userId).executeInsert(scalar[Long] single)
    }
  }

  /**
  * Update an event.
  *
  * @param id The event id.
  * @param event The event values.
  */
  def update(id: Long, event: Event) {
    DB.withConnection { implicit connection =>
      SQL(
        """
        update event
        set name = {name}, event_date = {date}, description = {description}
        where id = {id}
        """).on(
          'id -> id,
          'name -> event.name,
          'date -> event.date,
          'description -> event.description).executeUpdate()
    }
  }

  /**
  * Delete an event.
  *
  * @param id The event id to delete
  */
  def delete(id: Long) {
    DB.withConnection { implicit connection =>
      SQL("delete from event where id = {id}").on('id -> id).executeUpdate()
    }

  }

}
