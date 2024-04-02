package actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.Get
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.unmarshalling.Unmarshal
import formats.Formats._
import messages._
import models.Quote

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions
import scala.util.{Failure, Success}

object StockFetcher {

  def apply():Behavior[FetcherMessage] = Behaviors setup { context =>

    lazy val processor = context.spawn(StockProcessor(), "processor")

    Behaviors receive {
          case (context, QuoteRequest(symbol, from, to)) =>
            val url = s"https://query1.finance.yahoo.com/v8/finance/chart/$symbol?symbol=$symbol&period1=${from.toEpochSecond}&period2=${to.toEpochSecond}&interval=1d&events=div|split"
            val response = Http(context.system).singleRequest(Get(url))
            context.pipeToSelf(response){
              case Success(response) => Response(symbol, response)
              case Failure(exception) => FetchFailed(symbol, exception)
            }
            Behaviors.same

          case (context, Response(symbol, response)) =>
            implicit val system: ActorSystem[Nothing] = context.system
            val parsed = Unmarshal(response).to[Seq[Quote]]
            context.pipeToSelf(parsed){
              case Success(quoteList: Seq[Quote]) => Parsed(symbol, quoteList)
              case Failure(error:Throwable) => ParseFailed(symbol, error)
            }
            Behaviors.same

          case (context, Parsed(symbol, parsed)) =>
            processor ! Quotes(symbol, parsed)
            Behaviors.same

          case (context, FetchFailed(symbol, exception)) =>
            context.log.error(s"Unable to fetch data for $symbol", exception)
            Behaviors.same

          case (context, ParseFailed(symbol, exception)) =>
            context.log.error(s"Unable to parse data for $symbol.", exception)
            Behaviors.same
        }
  }
}
