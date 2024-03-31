package actors

import akka.actor.typed.Behavior
import akka.actor.typed.receptionist.ServiceKey
import akka.actor.typed.scaladsl.Behaviors
import messages.{ProcessorMessage, Quotes}

object StockProcessor {
  val serviceKey: ServiceKey[ProcessorMessage] = ServiceKey[ProcessorMessage]("stock processor")
  def apply(): Behavior[ProcessorMessage] = Behaviors receive {
    case (context, Quotes(symbol, quotes)) =>
      context.log.info(symbol)
      context.log.info(quotes.mkString("\n"))
      Behaviors.same
  }

}
