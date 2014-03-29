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
			val dependencies = Dependencia.findById( asignaturaId )
			val isAproved = Kardex.isAproved( alumnoId, dependencies )

		Ok( "isAproved = "+isAproved )

	}

}