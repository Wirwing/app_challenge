package helpers

object AuthHelper{

  def decodeBasicAuth(auth: String) = {
    val baStr = auth.replaceFirst("Basic ", "")
    var Array(user, pass) = new String(new sun.misc.BASE64Decoder().decodeBuffer(baStr), "UTF-8").split(":")
    (user, pass)
  }

}
