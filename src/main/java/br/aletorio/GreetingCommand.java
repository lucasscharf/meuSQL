package br.aletorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import picocli.CommandLine.Command;

@Command(name = "greeting", mixinStandardHelpOptions = true)
public class GreetingCommand implements Runnable {

  Scanner scanner = new Scanner(System.in);
  Db db = new Db();

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
          Row row = createInsertStatement(command);
          db.insert(row);
          break;
        case PRINT:
          System.out.println(db.toString());
          break;
        case NOT_FOUND:
          System.out.println(String.format("Command %s not valid", command));
          break;
      }
    }
  }

  private Row createInsertStatement(String command) {
    String[] tokens = command.split("\\s+");
    if (tokens.length < 4 || !tokens[0].equals("insert")) {
      throw new RuntimeException("Erro ao fazer parse do insert");
    }

    int id;
    try {
      id = Integer.parseInt(tokens[1]);
    } catch (NumberFormatException e) {
      throw new RuntimeException("Erro ao fazer parse do insert", e);
    }
    String username = tokens[2];
    String email = tokens[3];

    return new Row(id, username, email);
  }

}

enum Statement {
  PRINT("print"), EXIT("exit"), SELECT("select"), INSERT("insert"), NOT_FOUND(null);

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

class Row {
  private final int id;
  private final String name;
  private final String email;

  public Row(int id, String name, String email) {
    this.id = id;
    this.name = name;
    this.email = email;
  }

  public int size() {
    return name.length() + email.length() + Integer.BYTES;
  }

  @Override
  public String toString() {
    return "{" + id + ";" + name + ";" + email + "'}";
  }

}

class Page {
  private final List<Row> data = new ArrayList<>();
  private int id = UUID.randomUUID().hashCode();
  private int size = 0;
  private int MAX_SIZE = 4096;

  public void insert(Row row) {
    data.add(row);
    size += row.size();
  }

  public boolean willFit(Row row) {
    return size + row.size() < MAX_SIZE;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(id + "\n");
    for (Row row : data) {
      sb.append(row.toString() + "\n");
    }

    return sb.toString();
  }
}

class Db {
  private List<Page> pages;
  private int MAX_PAGES = 100;

  public Db() {
    pages = new ArrayList<>();
    pages.add(new Page());
  }

  public void insert(Row row) {
    Page lastPage = pages.getLast();
    if (!lastPage.willFit(row)) {
      if (pages.size() >= MAX_PAGES) {
        throw new RuntimeException("Maximum page reached");
      }

      Page page = new Page();
      pages.add(page);
      lastPage = page;
    }

    lastPage.insert(row);
  }


  @Override 
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("# Pages: " + pages.size());
    for (Page page : pages) {
      sb.append("\n" + page.toString());
    }

    return sb.toString();
  }
}
