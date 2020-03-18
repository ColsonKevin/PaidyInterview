/***
 * Allows to test the client/server interaction with one client only
 */
object SmoothClientServerTest {

  def main(args: Array[String]): Unit = {

    // Threads initialization
    val thread1 = new Thread {override def run(): Unit = OrdersServer.main(null)}
    val thread2 = new Thread {override def run(): Unit = OrdersClient.main(null)}

    // Threads start
    thread1.start()
    thread2.start()
  }
}