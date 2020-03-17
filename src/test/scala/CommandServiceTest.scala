import org.scalatest._
import org.scalatestplus.junit.JUnitRunner
import org.junit.runner.RunWith

class CommandServiceTest extends FlatSpec {

  "CommandsService once filled" should "not be empty" in {
    val commandService: CommandService = new CommandService
    val command: Command = new Command
    command.set_cook_time(10)
    command.set_item("test")
    commandService.create_command(1, command)
    assert(!commandService.get_commands(1).isEmpty)
    assert(!commandService.get_command_specific(1, command.get_item()).isEmpty)
    commandService.delete_command(1, "test")
    assert(commandService.get_commands(1).isEmpty)
  }
}
