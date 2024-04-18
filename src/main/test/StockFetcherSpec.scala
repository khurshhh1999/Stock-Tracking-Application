package test

import actors.StockFetcher
import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import akka.http.scaladsl.model._
import messages._
import models._
import org.scalatest.wordspec.AnyWordSpecLike
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout

import java.time.{LocalDate, ZonedDateTime}
import scala.concurrent.duration._
import scala.concurrent.Future

class StockFetcherSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {


  override implicit val timeout: Timeout = Timeout(3.seconds)

  "StockFetcher" should {
    "successfully fetch stock quotes" in {
      val stockFetcher = spawn(StockFetcher())
      val testProbe = createTestProbe[FetcherMessage]()

      val quoteRequest = QuoteRequest("AAPL", ZonedDateTime.now.minusDays(10), ZonedDateTime.now)
      stockFetcher ! quoteRequest


    }

    "handle failures in fetching stock data" in {
      val stockFetcher = spawn(StockFetcher())
      val testProbe = createTestProbe[FetcherMessage]()

      val quoteRequest = QuoteRequest("INVALID", ZonedDateTime.now.minusDays(10), ZonedDateTime.now)
      stockFetcher ! quoteRequest


    }
  }
}

