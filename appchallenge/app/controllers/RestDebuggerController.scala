package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import scala.collection.breakOut
import models._

import views._
import anorm._

import play.api.Logger

object AsignaturasRestController extends Controller{

  def checkSatisfiedDependencies( alumnoId: Int, asignaturaId: Int ) = Action{
    implicit request =>
      
      val isAproved = checkDependencies( alumnoId, asignaturaId )

      Logger.info( "is aproved = "+isAproved )

    Ok( "isAproved = "+isAproved )

  }


  def checkDependencies( alumnoId: Int, asignaturaId: Int ):Boolean={
    val dependencies = Dependencia.findById( asignaturaId ).map( x => x.requisitoId.get.toInt )
    if( dependencies.isEmpty ){
      return true
    }else{
      val allAproved = Kardex.getAllAproved( alumnoId ).map( x => x.asignaturaId.get.toInt )
      val isAproved = (dependencies filterNot allAproved.contains).isEmpty
      return isAproved
    }
  }



  def getOfferByAproved( studentId: Int ) = Action {
     implicit request =>
      //val lapse = Date.month()
      //val offerIds = Oferta.all().map( x => x.idAsignatura.get.toInt )

      val offerIds = Oferta.all().map( x => x.idAsignatura.get.toInt )
      Logger.info( "offerIds = "+Oferta.all().toString )

      val aprovedSubjectsIds = Kardex.getAllAproved( studentId ).map( x => x.asignaturaId.get.toInt )
      Logger.info( "aprovedSubjectsIds = "+aprovedSubjectsIds.toString )

      val availableOfferIds = (offerIds filterNot aprovedSubjectsIds.contains)
      Logger.info( "availableOfferIds ="+availableOfferIds.toString )

      val suggestedSubjects = availableOfferIds.filter( x => checkDependencies( studentId, x ) ).
        map( x => Asignatura.findById( x ).get.name )


      Logger.info( suggestedSubjects.toString )

      Ok( suggestedSubjects.toString )

  }

}