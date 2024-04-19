package messages

import models.PerformanceIndicators

sealed trait CsvWriterProtocol

final case class Write(symbol:String, indicators: PerformanceIndicators) extends CsvWriterProtocol
