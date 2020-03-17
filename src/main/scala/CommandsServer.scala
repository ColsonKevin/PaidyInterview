import scala.util.{Failure, Random, Success}
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._

import scala.concurrent.duration._
import akka.stream.Materializer
import akka.util.Timeout
import akka.http.scaladsl.server.RouteResult._

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import scala.language.postfixOps


object CommandsServer {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("commands-system")
    implicit val materializer: Materializer = Materializer(system)
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher
    implicit val result: ActorRef = system.actorOf(Props(new CommandsActor))
    implicit val timeout: Timeout = Timeout(2 seconds)

    val route = {
      concat(
        path("getCommands") {
          get {
            parameters("table") {
              table: String =>
                val maybeCommands = ask(result, GetCommandsMessage(table.toInt))
                onComplete(maybeCommands) {
                  case Success(commands) => complete(HttpEntity(ContentTypes.`application/json`, commands.toString))
                  case Failure(t) => complete(HttpEntity(ContentTypes.`application/json`, "Commands not found"))}
            }
          }
        },
        path("createCommand") {
          get {
            parameters(("table", "item")) {
              (table: String, item: String) =>
              val maybeCreated = ask(result, CreateCommandMessage(table.toInt, item))
              onComplete(maybeCreated) {
                case Success(created) => complete(HttpEntity(ContentTypes.`application/json`, created.toString))
                case Failure(t) => complete(HttpEntity(ContentTypes.`application/json`, "Command not created"))
              }
            }
          }
        },
        path("deleteCommand") {
          get {
            parameters(("table", "item")) {
              (table: String, item: String) =>
                val maybeDeleted = ask(result, DeleteCommandMessage(table.toInt, item))
                onComplete(maybeDeleted) {
                  case Success(deleted) => complete(HttpEntity(ContentTypes.`application/json`, deleted.toString))
                  case Failure(t) => complete(HttpEntity(ContentTypes.`application/json`, "Command not deleted"))
                }
            }
          }
        },
        path("getCommand") {
          get {
            parameters(("table", "item")) {
              (table: String, item: String) =>
                val maybeCommand = ask(result, GetCommandMessage(table.toInt, item))
                onComplete(maybeCommand) {
                  case Success(command) => complete(HttpEntity(ContentTypes.`application/json`, command.toString))
                  case Failure(t) => complete(HttpEntity(ContentTypes.`application/json`, "Command not found"))
                }
            }
          }
        }
      )
    }
    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}