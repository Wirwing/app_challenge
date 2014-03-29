package controllers

import helpers._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import scala.collection.breakOut
import models._

import play.api.libs.json._
import play.api.libs.functional.syntax._

import views._
import anorm._
import com.github.nscala_time.time.Imports._

import play.api.Logger

object RestDebuggerController extends Controller{

  case class Suggest(periodo: String, iam: Double, materias: Seq[Materia])
  case class Materia(nombre: String, profesor: String, situacion: String, horarios: Seq[MateriaHorario])
  case class MateriaHorario(id: Int, hora_inicio: String, hora_final: String)

  case class Horario ( hora_inicio: String, hora_final: String)

 /**
 * Companion object to support JSON transformations.
 */
 object Horario {
  implicit val reads: Reads[Horario] = (
    (__ \ "hora_inicio").read[String] and
    (__ \ "hora_final").read[String]
    )(Horario.apply _)
  }

  object Suggest {
    implicit val writes: Writes[Suggest] = (
      (__ \ "periodo").write[String] and
      (__ \ "iam").write[Double] and
      (__ \ "materias").write[Seq[Materia]]
      )(unlift(Suggest.unapply))
    }

    object Materia {
      implicit val writes: Writes[Materia] = (
        (__ \ "nombre").write[String] and
        (__ \ "profesor").write[String] and
        (__ \ "situacion").write[String] and
        (__ \ "horarios").write[Seq[MateriaHorario]]
        )(unlift(Materia.unapply))
      }

      object MateriaHorario {
        implicit val writes: Writes[MateriaHorario] = (
          (__ \ "id").write[Int] and
          (__ \ "hora_inicio").write[String] and
          (__ \ "hora_final").write[String]
          )(unlift(MateriaHorario.unapply))
        }

  /**
 * Validates and returns the Task provided, or displays errors.
 * @return
 */
 def suggest = Action(parse.json) { request =>

   try{

    request.headers.get("Authorization").map{ basicAuth =>

      val (name, pass) = AuthHelper.decodeBasicAuth(basicAuth)
      val alumnoId = Alumno.findByNameAndPassword(name, pass).get.id.get

      request.body.validate[Horario].map{
        case horario => 
        Logger.info( horario.toString )

        val asignaturas = BuscadorMaterias.getOfferWithWarnings(alumnoId.toInt)

        Logger.debug("Asignaturassss: " + asignaturas.toString)

        val today = DateTime.now

        var periodo = ""

        if(today.month.get >= 8){
          periodo = "Agosto-Diciembre "
        }else{
          periodo = "Enero-Julio "
        }

        periodo += today.year.get.toString

        val cam = Kardex.calculateCAM(alumnoId.toInt)

        var materias: List[Materia] = List()

        asignaturas.foreach(asignatura =>{

          var ofertas = Oferta.allWithAsignatureId(asignatura._1.id.get)
          var horarios: List[MateriaHorario] = List()

          var idProf = ""

          ofertas.map(oferta => {
             //Horario de materia
            val materiaHorario = MateriaHorario(oferta.dia, oferta.horaInicial, oferta.horaFinal)
            horarios = horarios ::: List(materiaHorario)
            idProf = oferta.idProfesor.get.toString
          })

          Logger.debug("Ofertasss: " + ofertas.toString)

          Logger.debug(asignatura._1.id.get.toString)
          Logger.debug(alumnoId.toString)

          var materia = Materia(asignatura._1.name, idProf, asignatura._2, horarios.toSeq)

          Logger.debug("materia neww " + materia.toString)

          materias = materias ::: List(materia)
          
        })

        

        val suggestion = Suggest(periodo, cam.toLong, materias.toSeq)

        // val suggestion = Suggest(
        //   periodo,
        //   cam.toLong,
        //   Seq(
        //     Materia("Algebra Lineal 1", "A", 1, Seq(
        //       MateriaHorario(1, "15:00", "16:20"),
        //       MateriaHorario(3, "15:00", "16:20"),
        //       MateriaHorario(5, "15:00", "16:20")
        //       )),
        //     Materia("Algebra Lineal 1", "B", 2, Seq(
        //       MateriaHorario(2, "12:00", "13:20"),
        //       MateriaHorario(3, "12:00", "13:20"),
        //       MateriaHorario(4, "12:00", "13:20")
        //       )),
        //     Materia("Análisis númerico", "A", 1, Seq(
        //       MateriaHorario(2, "12:00", "13:20"),
        //       MateriaHorario(3, "12:00", "13:20"),
        //       MateriaHorario(4, "12:00", "13:20")
        //       ))
        //     )
        //   )

        val json = Json.toJson(suggestion)

        Ok(json)
        }.recoverTotal{
          e => BadRequest("Detected error:"+ JsError.toFlatJson(e))
        }
      }getOrElse{
        NotFound(Json.obj("error" -> "user not found"))
      }
      }catch{
        case e: Exception => 
        {
          Logger.debug(e.getMessage)
          NotFound(Json.obj("error" -> "user not found"))
        }

      }
    }
  }
