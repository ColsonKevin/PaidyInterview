// Class representing a command
class Command {

  // Class variables
  private var item = ""
  private var cook_time = 0

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
}
