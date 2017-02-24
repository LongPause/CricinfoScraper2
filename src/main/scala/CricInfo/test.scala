package CricInfo

import net.ruippeixotog.scalascraper.browser.JsoupBrowser

/**
  * Created by mehedisa on 2/8/2017.
  */
object test extends App {
  val link = "http://www.google.com"


  try
  {
    val doc = JsoupBrowser().get(link)
    println(doc.toHtml)
  }
  catch
    {

      case _: Throwable => println("Got some other kind of exception")
    }
  finally
  {
    // your scala code here, such as to close a database connection
    println("Goodbye !!")

  }

}
