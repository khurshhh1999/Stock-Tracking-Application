package app

import actors.{StockFetcher, StockProcessor}
import akka.actor.typed.ActorSystem
import messages.QuoteRequest

import java.time.{ZoneId, ZonedDateTime}

object Run extends App {

  val fetcher = ActorSystem(StockFetcher(), "fetcher")
  val from = ZonedDateTime.of(2024, 3, 28, 0, 0, 0, 0, ZoneId.systemDefault())
  val to = ZonedDateTime.now()
  fetcher ! QuoteRequest("GOOG", from, to)

}
