import akka.actor.Actor

/***
 * Classes used as messages used by the Actors to notify the services
 */
case class GetOrdersMessage(table: Int)
case class GetOrderMessage(table: Int, item: String)
case class CreateOrderMessage(table: Int, item: String)
case class DeleteOrderMessage(table: Int, item: String)

/***
 * Actor class responsible to handle the API messages and relaying to the business processes
 */
class OrdersActor extends Actor {

  // Value used to call the business services
  private implicit val orderService: OrderService = new OrderService

  /***
   * Defines the behavior of the actor upon receiving a message and sends answers to the senders
   * @return An answer to the sender
   */
  def receive: Receive = {
    // Case to get the orders for a specific table
    case message: GetOrdersMessage => sender() ! orderService.get_orders(message.table).getOrElse("Commands not found")

    // Case to create a order
    case message: CreateOrderMessage =>
      orderService.create_order(message.table, message.item)
      sender() ! "Command created"

    // Case to get a specific order for a specific table
    case message: GetOrderMessage => sender() ! orderService.get_specific_order(message.table, message.item).getOrElse("Command not found")

    // Case to delete a order
    case message: DeleteOrderMessage =>
      orderService.delete_order(message.table, message.item)
      sender() ! "Command deleted"
  }
}