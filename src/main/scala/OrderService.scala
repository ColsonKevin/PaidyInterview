import java.util

import scala.util.Random

/**
 * Class used to handle the business operations on the orders
 */
class OrderService {

    // Storage variable containing the orders
    private var orders = Map.empty[Int, util.ArrayList[Order]]

    /***
     * Creates an order based on a table number and an item then stores it
     * @param table: Int
     * @param item: String
     */
    def create_order(table: Int, item: String): Unit = {
        println(s"create_order function called with parameters: $table, $item")

        // Order creation
        val order: Order = new Order
        order.set_item(item)
        order.set_cook_time(5 + Random.nextInt(10))

        // Order storage
        orders.get(table) match {
            case Some(x) => x.add(order)
            case None =>
                val orderList = new util.ArrayList[Order]
                orderList.add(order)
                orders = orders + (table -> orderList)
        }
        println(s"Stored orders: $orders")
    }

    /***
     * Gets all the orders for a table
     * @param table: Int
     * @return An optional List of Orders
     */
    def get_orders(table: Int): Option[util.ArrayList[Order]] = {
        println(s"get_orders function called with parameters: $table")

        // Gets the orders if exists
        orders.get(table).orElse(None)
    }

    /***
     * Gets a specific order for a specific table
     * @param table: Int
     * @param item: String
     * @return The first order meeting the requirements
     */
    def get_specific_order(table: Int, item: String): Option[Order] = {
        println(s"get_specific_order function called with parameters: $table, $item")

        // Gets the specific order if exists
        orders.get(table) match {
            case Some(x) => Option(x.stream().filter(_.equals(item)).findFirst().orElse(null))
            case None => None
        }
    }

    /***
     * Deletes a the first order meeting the requirements
     * @param table: Int
     * @param item: String
     */
    def delete_order(table: Int, item: String): Unit = {
        println(s"delete_order function called with parameters: $table, $item")

        // Retrieves the first matching order and removes it
        orders.get(table) match {
            case Some(x) =>
                val orderToRemove: Order = x.stream().filter(_.equals(item)).findFirst().orElse(null)
                if (orderToRemove != null) x.remove(orderToRemove)
            case None =>
        }
        println(s"Stored orders: $orders")
    }

    // Getter and setters
    def get_orders(): Map[Int, util.ArrayList[Order]] = {
        orders
    }
}