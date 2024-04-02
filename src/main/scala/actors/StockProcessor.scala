package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import messages.{ProcessorMessage, Quotes}

object StockProcessor {
  def apply(): Behavior[ProcessorMessage] = Behaviors receive {
    case (context, Quotes(symbol, quotes)) =>
      context.log.info(symbol+"\t"+quotes.mkString("\t"))
      Behaviors.same
  }

}
