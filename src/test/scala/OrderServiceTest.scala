import org.scalatest._

class OrderServiceTest extends FlatSpec {

  "CommandsService once filled" should "not be empty" in {
    val commandService: OrderService = new OrderService
    val command: Order = new Order
    command.set_cook_time(10)
    command.set_item("test")
    assert(commandService.get_orders(1).isEmpty)
    assert(commandService.get_specific_order(1, command.get_item())!= null)
    commandService.delete_order(1, "test")
    assert(commandService.get_orders(1).isEmpty)
  }
}
