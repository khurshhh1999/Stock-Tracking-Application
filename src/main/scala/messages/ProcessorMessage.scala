package messages

import models.Quote

sealed trait ProcessorMessage

final case class Quotes(symbol:String, quotes:Seq[Quote]) extends ProcessorMessage