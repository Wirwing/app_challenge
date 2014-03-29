import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import java.util.Date;

import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class KardexModelSpec extends Specification {

  import models._

  // -- Date helpers
  def dateIs(date: java.util.Date, str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date) == str

  "Kardex model" should {

    "validate list not empty" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val kardex = Kardex.all
        kardex.size should be > 0

        val one = kardex(1)


      }
    }

  }
}
