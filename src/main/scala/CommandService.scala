import java.util
import java.util.stream.Collectors

class CommandService {
    private var commands = new util.ArrayList[util.ArrayList[Command]]

    def create_command(table: Int, command: Command): Unit = {
        commands.get(table).add(command)
    }

    def get_commands(table: Int): util.ArrayList[Command] = {
        commands.get(table)
    }

    def get_command_specific(table: Int, command: Command): util.ArrayList[Command] = {
        commands.get(table).stream().filter(_.get_item().equals(command.get_item()))
          .collect(Collectors.toCollection(util.ArrayList[Command]))
    }

    def delete_command(table: Int, command: Command): Unit = {
        commands.get(table).remove(command)
    }
}
