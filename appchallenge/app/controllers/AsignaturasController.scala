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

object AsignaturasController extends Controller{

	def checkSatisfiedDependencies( alumnoId: Int, asignaturaId: Int ) = Action{
		implicit request =>
			
			val isAproved = checkDependencies( alumnoId, asignaturaId )

			Logger.info( "is aproved = "+isAproved )

		Ok( "isAproved = "+isAproved )

	}


	def checkDependencies( alumnoId: Int, asignaturaId: Int ):Boolean={
		val dependencies = Dependencia.findById( asignaturaId )
		if( dependencies.isEmpty ){
			return true
		}else{
			val isAproved = Kardex.isAproved( alumnoId, dependencies )	
			return isAproved
		}
	}



	def getOfferByAproved( studentId: Int ) = Action {
		 implicit request =>
		 	val offerIds = Oferta.all().map( x => x.idAsignatura.get.toInt )
		 	Logger.info( "offerIds = "+Oferta.all().toString )

		 	val aprovedSubjectsIds = Kardex.getAllAproved( studentId ).map( x => x.asignaturaId.get.toInt )
		 	Logger.info( "aprovedSubjectsIds = "+aprovedSubjectsIds.toString )

		 	val availableOfferIds = offerIds filterNot aprovedSubjectsIds.contains
		 	Logger.info( "availableOfferIds ="+availableOfferIds.toString )

		 	val suggestedSubjects = availableOfferIds.filter( x => checkDependencies( studentId, x ) ).
		 		map( x => Asignatura.findById( x ).get.name )


		 	Logger.info( suggestedSubjects.toString )

		 	Ok( suggestedSubjects.toString )

	}

}