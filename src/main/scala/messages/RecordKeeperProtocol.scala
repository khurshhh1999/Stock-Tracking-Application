package messages

import models.PerformanceIndicators

sealed trait RecordKeeperProtocol

final case class Latest(symbol:String, indicators: PerformanceIndicators) extends RecordKeeperProtocol