/***
 * Class representing the Order entity
 */
class Order {

  // Class variables
  private var item: String = _
  private var cook_time: Int = _

  // Getters and setters
  def set_item(item_val: String): Unit = {
    item = item_val
  }
  def get_item(): String = {
    item
  }
  def set_cook_time(cook_time_val: Int): Unit = {
    cook_time = cook_time_val
  }
  def get_cook_type(): Int = {
    cook_time
  }

  // Allows to compare orders easier
  def equals(obj: String): Boolean = {
    item.equals(obj)
  }

  // Allows to print an order nicely
  override def toString: String = new String(s"Order: $item; $cook_time")
}
