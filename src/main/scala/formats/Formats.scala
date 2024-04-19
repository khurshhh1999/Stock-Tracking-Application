package formats

import models.Quote
import spray.json.DefaultJsonProtocol.{BigDecimalJsonFormat, JsValueFormat, LongJsonFormat, StringJsonFormat, immSeqFormat, jsonFormat7}
import spray.json.{JsNull, JsValue, RootJsonFormat, enrichAny}

import scala.language.{implicitConversions, postfixOps}

object Formats {
  final case class DataNotFoundException(message: String) extends Exception(message)

  implicit val quoteFormat: RootJsonFormat[Quote] = jsonFormat7(Quote)

  implicit object QuoteSeqFormat extends RootJsonFormat[Seq[Quote]]
  {
    override def read(json: JsValue): Seq[Quote] = {

      implicit def JsonToMap(json: JsValue):Map[String, JsValue] = json.asJsObject.fields

      def JsonToSeq(json: JsValue): Seq[JsValue] = json.convertTo[Seq[JsValue]]
      def JsonToDecimalList(json: JsValue):Seq[BigDecimal] = JsonToSeq(json).map(_.convertTo[BigDecimal])
      def JsToOption(nullable: JsValue): Option[JsValue] = nullable match {
        case JsNull => None
        case _ => Some(nullable)
      }


      JsToOption(json("chart")("result")) match {
        case Some(value) =>
          val result = JsonToSeq(value).head
          val timestamps:Seq[Long] = JsonToSeq(result("timestamp")).map(j => j.convertTo[Long])
          val quote = JsonToSeq(result("indicators")("quote")).head
          val openValues:Seq[BigDecimal] = JsonToDecimalList(quote("open"))
          val lowValues:Seq[BigDecimal] = JsonToDecimalList(quote("close"))
          val volumeValues:Seq[BigDecimal] = JsonToDecimalList(quote("volume"))
          val highValues:Seq[BigDecimal] = JsonToDecimalList(quote("high"))
          val closeValues:Seq[BigDecimal] = JsonToDecimalList(quote("close"))
          val adjCloseValues:Seq[BigDecimal] = JsonToDecimalList(JsonToSeq(result("indicators")("adjclose")).head("adjclose"))


          (timestamps zip openValues zip lowValues zip volumeValues zip highValues zip closeValues zip adjCloseValues)
            .map{case ((((((timestamp, open), low), volume), high), close), adjclose) =>
              Quote(timestamp, open, low, volume, high, close, adjclose)}

        case None =>
          val error = json("chart")("error")
          throw DataNotFoundException(error("description").convertTo[String])
      }

    }

    override def write(obj: Seq[Quote]): JsValue = obj.toJson
  }
}
