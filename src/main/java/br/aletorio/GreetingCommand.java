package br.aletorio;

import java.util.Scanner;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "greeting", mixinStandardHelpOptions = true)
public class GreetingCommand implements Runnable {

  @Parameters(paramLabel = "<name>", defaultValue = "picocli", description = "Your name.")
  String name;

  Scanner scanner = new Scanner(System.in);
  @Override
  public void run() {
    System.out.printf("Hello %s, go go commando!%n", name);
  }

}
