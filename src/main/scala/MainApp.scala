/***
 * class simulation clients requestion the API through threads
 */
object MainApp {

  /***
   * Main function
   * @param args: Array[String]
   */
  def main(args: Array[String]): Unit = {

    // Threads initialization
    val thread1 = new Thread {override def run(): Unit = OrdersServer.main(null)}
    val thread2 = new Thread {override def run(): Unit = OrdersClient.main(null)}
    val thread3 = new Thread {override def run(): Unit = OrdersClient.main(null)}
    val thread4 = new Thread {override def run(): Unit = OrdersClient.main(null)}
    val thread5 = new Thread {override def run(): Unit = OrdersClient.main(null)}
    val thread6 = new Thread {override def run(): Unit = OrdersClient.main(null)}
    val thread7 = new Thread {override def run(): Unit = OrdersClient.main(null)}
    val thread8 = new Thread {override def run(): Unit = OrdersClient.main(null)}
    val thread9 = new Thread {override def run(): Unit = OrdersClient.main(null)}
    val thread10 = new Thread {override def run(): Unit = OrdersClient.main(null)}

    // Threads start
    thread1.start()
    thread2.start()
    thread3.start()
    thread4.start()
    thread5.start()
    thread6.start()
    thread7.start()
    thread8.start()
    thread9.start()
    thread10.start()
  }
}
