package models

final case class Quote(timestamp: Long,
                        open: Double,
                        low: Double,
                        volume: Double,
                        high: Double,
                        close: Double,
                        adjclose: Double)