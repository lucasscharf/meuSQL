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

      Statement statement = Statement.fromCommand(command);

      switch (statement) {
        case EXIT:
          System.exit(0);
          break;

        case SELECT:
          System.out.println("Selecionou");
          break;

        case INSERT:
          System.out.println("INseriu");
          break;
        case NOT_FOUND:
          System.out.println(String.format("Command %s not valid", command));
          break;
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

  public static Statement fromCommand(String command) {
    Statement[] statements = Statement.values();
    for (Statement statement : statements) {
      if (statement != NOT_FOUND && command.toLowerCase().startsWith(statement.command))
        return statement;
    }
    return NOT_FOUND;
  }
}