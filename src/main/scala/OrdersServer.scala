import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import akka.util.Timeout
import akka.http.scaladsl.server.RouteResult._

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import scala.language.postfixOps
import scala.concurrent.duration._
import scala.util.{Failure, Success}

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.databind.ObjectMapper

/***
 * Runnable object taking care of the endpoints asking actors to take care the queries
 */
object OrdersServer {

  /***
   * Main function
   * @param args: Array[String]
   */
  def main(args: Array[String]): Unit = {

    // Class values
    implicit val system: ActorSystem = ActorSystem("orders-system")
    implicit val materializer: Materializer = Materializer(system)
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher
    implicit val result: ActorRef = system.actorOf(Props(new OrdersActor)) // Actors initialization
    implicit val timeout: Timeout = Timeout(2 seconds) // Defines the timeout for the queries to enter failure)

    // Endpoints definition
    val route = {
      concat(

        // Endpoint to get all orders of a table
        path("getOrders") {
          get {
            parameters("table") {
              table: String =>
                onComplete(ask(result, GetOrdersMessage(table.toInt))) {
                  case Success(orders) => complete(HttpResponse(entity = orders.toString))
                  case Failure(t) => complete(HttpResponse(entity = "Orders not found"))}
            }
          }
        },

        // Endpoint to create an order
        path("createOrder") {
          post {
            entity(as[String]) {
              jsonMap =>
                val map = new ObjectMapper().registerModule(DefaultScalaModule).readValue(jsonMap, classOf[Map[String, String]])
                onComplete(ask(result, CreateOrderMessage(map("table").toInt, map("item")))) {
                  case Success(created) => complete(HttpResponse(entity = created.toString))
                  case Failure(t) => complete(HttpResponse(entity = "Command not created"))
                }
            }
          }
        },

        // EndPoint to delete an order
        path("deleteOrder") {
          post {
            entity(as[String]) {
              jsonMap =>
                val map = new ObjectMapper().registerModule(DefaultScalaModule).readValue(jsonMap, classOf[Map[String, String]])
                onComplete(ask(result, DeleteOrderMessage(map("table").toInt, map("item")))) {
                  case Success(deleted) => complete(HttpResponse(entity = deleted.toString))
                  case Failure(t) => complete(HttpResponse(entity = "Command not deleted"))
                }
            }
          }
        },

        // Endpoint to get a specific order for a table
        path("getOrder") {
          get {
            parameters(("table", "item")) {
              (table: String, item: String) =>
                val maybeCommand = ask(result, GetOrderMessage(table.toInt, item))
                onComplete(maybeCommand) {
                  case Success(command) => complete(HttpResponse(entity = command.toString))
                  case Failure(t) => complete(HttpResponse(entity = "Command not found"))
                }
            }
          }
        }
      )
    }

  // Running the endpoints
    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}