package models;

import java.util.Date;

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._
import com.github.nscala_time.time.Imports._
import org.joda.time.Days

import scala.language.postfixOps
import play.api.Logger


case class IA( x: Int, y: Int ){
  def calculate():Int = {
    val rude = x + (0.5*y)

    if( rude%1.0 >= 0.5 ){
      return math.ceil(rude).toInt
      }else{
        return math.floor(rude).toInt
      }

    }
  }

  case class Kardex(

    alumnoId: Pk[Long] = NotAssigned,
    asignaturaId: Pk[Long] = NotAssigned,
    periodo: Pk[Date] = NotAssigned,
    situacion: Int,
    tipo: Int

    ){

    def getDistanceFromDeadLine(  ): Long = {
      val date = deadline()
      return Days.daysBetween( LocalDate.now, date ).getDays
    }

    def deadline(  ):LocalDate = {
      val datePeriodo = LocalDate.fromDateFields( periodo.get )
      val month = datePeriodo.getMonthOfYear
      Logger( month.toString )
      if( month >= 8 ){
        return datePeriodo.plusMonths(24)
        }else{
          return datePeriodo.plusMonths( 18 )
        }

      }

    }
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
   val data = DB.withConnection { implicit connection =>
    SQL("select * from kardex where alumnoId = {studentId} and situacion = '1' order by periodo desc").on(
      'studentId -> studentId        
      ).as( kardex *)
  }

  //Logger.info( "all aproved="+data.toString )
  data
}

def getAllNotAproved( studentId: Int ): List[Kardex] = {
  val failed = DB.withConnection { implicit connection =>
    SQL("select * from kardex where alumnoId = {studentId} and situacion = '0' order by periodo desc").on(
      'studentId -> studentId        
      ).as( kardex *)
  }

  Logger.info( failed.toString )
  val passed = getAllAproved( studentId ).map( x  => x.asignaturaId.get.toInt )
  
  val failedIds = failed.map( x  => x.asignaturaId.get.toInt )
  Logger.info( "failedIds = "+failedIds.toString )

  val allNeverAprovedIds = (failedIds filterNot passed.contains)
  Logger.info( "allNeverAprovedIds = "+allNeverAprovedIds.toString )

  val allNeverAproved = failed.filter( x => allNeverAprovedIds.contains( x.asignaturaId.get.toInt ) )
  Logger.info( "allNeverAproved = "+allNeverAproved.toString )

  return allNeverAproved
}




def findByStudentId( studentId: Int ): List[Kardex] = {
 DB.withConnection { implicit connection =>
  SQL("select * from kardex where alumnoId = {studentId}").on(
    'studentId -> studentId        
    ).as( kardex *)
}
}

def calculateCAM( studentId: Int ):Int = {
  val kardex = findByStudentId( studentId )
    //Logger.info( kardex.toString )


    val kardexByGroup = kardex.groupBy( x => x.periodo.toString )
  //Logger.info( kardexByGroup.toString )

  val calculateIAperGroup = kardexByGroup.map( x => {
    val subjects = x._2
    Logger.info( "subjects "+subjects.toString )
    val passed = subjects.filter( subject => (subject.tipo == 1 && subject.situacion == 1) ).size
    val failed  = subjects.filter( subject => (subject.tipo == 0 && subject.situacion == 1) ).size
    IA( passed, failed )
  }
  )
  //Logger.info( "IAPergrpuo = "+calculateIAperGroup.toString )

  val iaPerPeriod = calculateIAperGroup.map( x=> x.calculate )
  //Logger.info( "iaPerPeriod = "+iaPerPeriod.toString )

  
  val cam = iaPerPeriod.max
  Logger.info( "max = "+cam )
  return cam

}

}
