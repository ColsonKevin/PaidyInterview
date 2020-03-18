import org.scalatest._

/***
 * OrderService test class
 */
class OrderServiceTest extends FlatSpec {

  // Class to test
  private val orderService: OrderService = new OrderService

  "new orderService" should "be empty" in {
    assert(orderService.get_orders().isEmpty)
  }

  "get_orders in empty orders" should "return None" in {
    assert(orderService.get_orders(1).isEmpty)
  }

  "get_specific_order in empty orders" should "return None" in {
    assert(orderService.get_specific_order(1, "test").isEmpty)
  }

  "delete_orders on empty orders" should "return None" in {
    orderService.delete_order(1, "test")
    assert(orderService.get_orders().isEmpty)
  }

  "create_order on empty orders" should "increase the size by 1" in {
    orderService.create_order(1, "test")
    assert(orderService.get_orders().size == 1)
  }

  "get_orders on the initialized table" should "return an order list" in {
    orderService.create_order(1, "test2")
    orderService.create_order(2, "test")
    assert(orderService.get_orders(1).get.size() == 2)
    assert(orderService.get_orders(2).get.size() == 1)
  }

  "get_specific_order on the initialized table" should "return a specific order or None" in {
    orderService.create_order(1, "test")
    assert(orderService.get_specific_order(1, "test").get.equals("test"))
    assert(orderService.get_specific_order(1, "test2").get.equals("test2"))
    assert(orderService.get_specific_order(2, "test").get.equals("test"))
    assert(orderService.get_specific_order(2, "test2").isEmpty)
  }

  "delete_order on specific order" should "remove that order from the orders list" in {
    orderService.delete_order(1,"test")
    orderService.delete_order(1, "test3")
    assert(orderService.get_orders(1).get.size() == 2)
  }
}
