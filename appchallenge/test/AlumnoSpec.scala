import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import java.util.Date;

import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class AlumnoModelSpec extends Specification {

  import models._

  // -- Date helpers
  def dateIs(date: java.util.Date, str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date) == str

  "Alumno model" should {

    "find by id and password" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val Some(alumno) = Alumno.findByNameAndPassword(1, "password")
        alumno.name must equalTo("Irving")

      }
    }

  }
}
