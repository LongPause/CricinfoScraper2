package CricInfo
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.model.Element

class JsoupTable(header : List[String], data : List[List[Element]]) {
  def getHeader() = header
  def getColumn(col : Int) : List[Element] = data.map(_(col))
  def getColumn(colHeader : String): List[Element] = getColumn(header.indexOf(colHeader))
  def getRow(row : Int) = data(row)
  def getCell(row : Int, col : Int) = data(row)(col)

  def getSubTable(colHeaders : List[String]) = {
    val cols : List[Int] = colHeaders.map{header.indexOf(_)}
    new JsoupTable(colHeaders ,  data.map(r => cols.map(c => r(c))))
  }
}

/**
  * Created by mehedisa on 1/18/2017.
  */
object CricScraper extends App {


  def processTable (tableElm : Element) : JsoupTable = {
    val headers : List[String] = tableElm >> element("thead") >> texts("th") toList
    val data : List[List[Element]] = tableElm >> elementList("tbody > tr") map (_ >> elementList("td") toList)
    new JsoupTable(headers , data)
  }

  def getCricTable(link : String) = {
    val doc = JsoupBrowser().get(link)
    val itemFound: Option[Element] = doc >> elementList(".engineTable") find(_ >?> text("caption") getOrElse("") equals "Innings by innings list" )
    val tableBody: JsoupTable = itemFound match {
      case Some(elm) => processTable(elm)
      case  _=> new JsoupTable(List() , List(List()))
    }
    tableBody
  }

  val link = "http://stats.espncricinfo.com/ci/engine/player/35320.html?class=1;template=results;type=batting;view=innings"
  println()
  println()

  val table = getCricTable(link)
  val h = table.getHeader()
  println(h)

  val subTable = table.getSubTable(List("Runs", "Inns"))
  val subHeader = subTable.getHeader()
  val runs = subTable.getColumn("Runs")

  print(subHeader)
  print(runs map (text(_)))


}
