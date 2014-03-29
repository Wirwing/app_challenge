package models

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import scala.collection.breakOut
import models._
import com.github.nscala_time.time.Imports._


import views._
import anorm._

import play.api.Logger

object BuscadorMaterias {

	val EXTRAORDINARY = 0
	val EXTRAORDINARY_CHANCES = 3
	val MANDATORY_SECOND_ORDINARY = You already burned your +EXTRAORDINARY_CHANCES.toString+ extraordinaries, TAKE THE ORDINARY


	def test( studentIdInt ) = Action{

		val failedSubjects = Kardex.getAllNotAproved( studentId )
		Logger.info( failedSubjects.toString )

		failedSubjects.foreach( x = Logger.info( hola+x.alumnoId.get.toString+,+x.asignaturaId+ +x.periodo.get+  +x.deadline().toString+ +x.getDistanceFromDeadLine().toString ) )


		Ok(Done)
	}

	def checkSatisfiedDependencies( alumnoId Int, asignaturaId Int ) = Action{
		implicit request =
			
			val isAproved = checkDependencies( alumnoId, asignaturaId )

			Logger.info( is aproved = +isAproved )

		Ok( isAproved = +isAproved )

	}


	def checkDependencies( alumnoId Int, asignaturaId Int )Boolean={
		val dependencies = Dependencia.findById( asignaturaId ).map( x = x.requisitoId.get.toInt )
		if( dependencies.isEmpty ){
			return true
		}else{
			val allAproved = Kardex.getAllAproved( alumnoId ).map( x = x.asignaturaId.get.toInt )
			val isAproved = (dependencies filterNot allAproved.contains).isEmpty
			return isAproved
		}
	}



	def getOfferByAproved( studentId Int ) = Action {
		 implicit request =
		 	val lapse = Date.month()
		 	val offerIds = Oferta.all().map( x = x.idAsignatura.get.toInt )

		 	val offerIds = Oferta.allGroupByTeacher().map( x = x.idAsignatura.get.toInt )
		 	Logger.info( offerIds = +offerIds )

		 	val aprovedSubjectsIds = Kardex.getAllAproved( studentId ).map( x = x.asignaturaId.get.toInt )
		 	Logger.info( aprovedSubjectsIds = +aprovedSubjectsIds.toString )

		 	val availableOfferIds = (offerIds filterNot aprovedSubjectsIds.contains)
		 	Logger.info( availableOfferIds =+availableOfferIds.toString )

		 	val suggestedSubjects = availableOfferIds.filter( x = checkDependencies( studentId, x ) ).
		 		map( x = Asignatura.findById( x ).get.name )


		 	Logger.info( suggestedSubjects.toString )

		 	Ok( suggestedSubjects.toString )

	}

	def getAvailableSubjects( studentIdInt )List[Asignatura] = {

		val offerIds = Oferta.allGroupByTeacher().map( x = x.idAsignatura.get.toInt )
		 	Logger.info( offerIds = +offerIds )

		 	val aprovedSubjectsIds = Kardex.getAllAproved( studentId ).map( x = x.asignaturaId.get.toInt )
		 	Logger.info( aprovedSubjectsIds = +aprovedSubjectsIds.toString )

		 	val availableOfferIds = (offerIds filterNot aprovedSubjectsIds.contains)
		 	Logger.info( availableOfferIds =+availableOfferIds.toString )

		 	val suggestedSubjects = availableOfferIds.filter( x = checkDependencies( studentId, x ) ).
		 		map( x = Asignatura.findById( x ).get )

		 return suggestedSubjects

	}

	def getOfferByFail( studentId Int ) = Action {
		 implicit request =
		 	val lapse = Date.month()
		 	val offerIds = Oferta.all().map( x = x.idAsignatura.get.toInt )

		 	 val month = LocalDateTime.now.month

		 	var suggest = List[Asignatura]()

		 	val cam = Kardex.calculateCAM( studentId )
		 	Logger.info( cam.toString )



		 	val failedSubjects = Kardex.getAllNotAproved( studentId )
		 	val failedSubjectsMap = failedSubjects.groupBy( x = x.asignaturaId.get.toInt )

		 	val keysFailedSubjects = failedSubjects.map( x = x.asignaturaId.get.toInt ).distinct


		 	if( keysFailedSubjects.size = cam ){
		 		keysFailedSubjects.foreach( x = {
		 				val asignatura = Asignatura.findById(x).get
		 				suggest = suggest+asignatura
		 			} )

		 		Logger.info( suggest=+suggest.toString )
		 		llenar con las asignaturas restantes
		 		val offerSubjects = getAvailableSubjects( studentId ).groupBy( x = x.id.get )
		 		val keySet = offerSubjects.keySet.to[List]
		 		Logger.info( getAvailableSubjects=+offerSubjects.toString )
		 		Logger.info( (cam-suggest.size).toString )

		 		var limit = cam-suggest.size-1
		 		if( keySet.size  limit ){
		 			limit = keySet.size-1
		 		}
		 		
		 		for( i - 0 to limit ){
		 			suggest = suggest+offerSubjects(keySet(i))(0)
		 			texto += suggest(i).toString+ ,
		 		}


		 		
		 	}

		 	

		 	

		 	Ok( suggest.toString )

	}


	def getOfferWithWarnings( studentId Int ) = Action {
		 implicit request =
		 	val lapse = Date.month()
		 	val offerIds = Oferta.all().map( x = x.idAsignatura.get.toInt )

		 	

		 	var suggest = List[(Asignatura, String)]()

		 	val cam = Kardex.calculateCAM( studentId )
		 	Logger.info( cam.toString )



		 	val failedSubjects = Kardex.getAllNotAproved( studentId )
		 	val failedSubjectsMap = failedSubjects.groupBy( x = x.asignaturaId.get.toInt )

		 	val keysFailedSubjects = failedSubjects.map( x = x.asignaturaId.get.toInt ).distinct

		 	if( keysFailedSubjects.size = cam ){
		 		keysFailedSubjects.foreach( x = {
		 				val asignatura = Asignatura.findById(x).get
		 				val extraordinariesBurned = failedSubjectsMap( x ).filter( subject = subject.tipo == EXTRAORDINARY )
		 				val daysToDoomsday = failedSubjectsMap( x ).map( subject = subject.getDistanceFromDeadLine() ).min

		 				var message = 
		 				if (extraordinariesBurned.size == EXTRAORDINARY_CHANCES){
		 					message = MANDATORY_SECOND_ORDINARY
		 				}

		 				suggest = suggest+(asignatura, doomsday in+daysToDoomsday.toString+ days take extraordinary now.+message)
		 			} )

		 		Logger.info( suggest=+suggest.toString )
		 		llenar con las asignaturas restantes
		 		val offerSubjects = getAvailableSubjects( studentId ).groupBy( x = x.id.get )
		 		val keySet = offerSubjects.keySet.to[List]
		 		Logger.info( getAvailableSubjects=+offerSubjects.toString )
		 		Logger.info( (cam-suggest.size).toString )

		 		var limit = cam-suggest.size-1
		 		if( keySet.size  limit ){
		 			limit = keySet.size-1
		 		}
		 		
		 		for( i - 0 to limit ){
		 			suggest = suggest+(offerSubjects(keySet(i))(0), OK no problem)
		 			texto += suggest(i).toString+ ,
		 		}


		 		
		 	}

		 	

		 	

		 	Ok( suggest.toString )

	}



	def getOfferWithWarnings( studentId Int ) = Action {
		 implicit request =
		 	val lapse = Date.month()
		 	val offerIds = Oferta.all().map( x = x.idAsignatura.get.toInt )

		 	 val month = LocalDateTime.now.month

		 	var suggest = List[Asignatura]()

		 	val cam = Kardex.calculateCAM( studentId )
		 	Logger.info( cam.toString )

		 	val failedSubjects = Kardex.getAllNotAproved( studentId ).foreach( x = Logger.info( x.getDistanceFromDeadLine().toString ) )
		 	
		 	val	failedSubjectsIds = failedSubjects.groupBy( x = x.asignaturaId.get.toInt )
		 	Logger.info( failedSubjectsIds.size.toString )

		 	if( failedSubjects.keySet.size = cam ){
		 		failedSubjects.keySet.foreach( x = {
		 				val asignatura = Asignatura.findById(x).get
		 				suggest = suggest+asignatura
		 			} )

		 		llenar con las asignaturas restantes
		 		val offerSubjects = getAvailableSubjects( studentId )
		 		Logger.info( (cam-suggest.size).toString )
		 		
		 		for( i - 0 to (cam-suggest.size-1) ){
		 			suggest = suggest+offerSubjects(i)
		 		}
		 	}

		 	

		 	

		 	Ok( suggest.toString )

	}
	
}