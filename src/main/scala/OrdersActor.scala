import akka.actor.Actor

import scala.util.Random

case class GetCommandsMessage(table: Int)
case class GetCommandMessage(table: Int, item: String)
case class CreateCommandMessage(table: Int, item: String)
case class DeleteCommandMessage(table: Int, item: String)

class CommandsActor extends Actor {

  private implicit val commandService: OrderService = new OrderService

  def receive: Receive = {
    case message: GetCommandsMessage => sender() ! commandService.get_orders(message.table).getOrElse("Commands not found")
    case message: CreateCommandMessage => commandService.create_order(message.table, message.item)
      sender() ! "Command created"
    case message: GetCommandMessage => sender() ! commandService.get_specific_order(message.table, message.item).getOrElse("Command not found")
    case message: DeleteCommandMessage =>
      commandService.delete_order(message.table, message.item)
      sender() ! "Command deleted"
  }
}