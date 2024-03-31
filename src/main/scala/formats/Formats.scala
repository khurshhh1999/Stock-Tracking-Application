package formats

import models.Quote
import spray.json.DefaultJsonProtocol.{DoubleJsonFormat, JsValueFormat, LongJsonFormat, immSeqFormat, jsonFormat7}
import spray.json.{JsValue, RootJsonFormat, enrichAny}

import scala.language.implicitConversions

object Formats {

  implicit val quoteFormat: RootJsonFormat[Quote] = jsonFormat7(Quote)
  implicit object QuoteSeqFormat extends RootJsonFormat[Seq[Quote]]
  {
    override def read(json: JsValue): Seq[Quote] = {

      implicit def JsonToMap(json: JsValue):Map[String, JsValue] = json.asJsObject.fields
      def JsonToSeq(json: JsValue): Seq[JsValue] = json.convertTo[Seq[JsValue]]
      implicit def JsonToLong(json: JsValue): Long = json.convertTo[Long]
      implicit def JsonToDouble(json: JsValue): Double = json.convertTo[Double]

      val result = JsonToSeq(json("chart")("result")).head
      val timestamps:Seq[JsValue] = JsonToSeq(result("timestamp"))
      val quote = JsonToSeq(result("indicators")("quote")).head
      val openValues:Seq[JsValue] = JsonToSeq(quote("open"))
      val lowValues:Seq[JsValue] = JsonToSeq(quote("close"))
      val volumeValues:Seq[JsValue] = JsonToSeq(quote("volume"))
      val highValues:Seq[JsValue] = JsonToSeq(quote("high"))
      val closeValues:Seq[JsValue] = JsonToSeq(quote("close"))
      val adjCloseValues:Seq[JsValue] = JsonToSeq(JsonToSeq(result("indicators")("adjclose")).head("adjclose"))

      (timestamps zip openValues zip lowValues zip volumeValues zip highValues zip closeValues zip adjCloseValues)
        .map{case ((((((timestamp, open), low), volume), high), close), adjclose) =>
          Quote(timestamp, open, low, volume, high, close, adjclose)}
    }

    override def write(obj: Seq[Quote]): JsValue = obj.toJson
  }

}
