package br.aletorio;

import java.util.Scanner;

import picocli.CommandLine.Command;

@Command(name = "greeting", mixinStandardHelpOptions = true)
public class GreetingCommand implements Runnable {

  Scanner scanner = new Scanner(System.in);

  @Override
  public void run() {
    System.out.println("SQLite done for study... type ctrl+c to exit");

    while (true) {
      System.out.print(">>>");
      String command = scanner.nextLine();

      if ("exit".equalsIgnoreCase(command)) {
        System.exit(0);
        ;
      }

      if (!command.isBlank()) {
        System.out.println(String.format("Command %s not valid", command));
      }
    }
  }

}

enum Statement {
  EXIT("exit"), SELECT("select"), INSERT("insert"), NOT_FOUND(null);

  final String command;

  Statement(String command) {
    this.command = command;
  }

  public Statement fromCommand(String command) {
    Statement[] statements = Statement.values();
    for (Statement statement : statements) {
      if (statement != NOT_FOUND && command.toLowerCase().startsWith(statement.command))
        return statement;
    }
    return NOT_FOUND;
  }
}