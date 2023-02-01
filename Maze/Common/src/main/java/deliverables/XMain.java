package deliverables;

import static deliverables.XBad.xBad;
import static deliverables.XBad.xBadWithObserver;
import static deliverables.XBad2.xBad2;
import static deliverables.XBad2.xBad2WithMultiGoal;
import static deliverables.XBad2.xBad2WithObserver;
import static deliverables.XBoard.xBoard;
import static deliverables.XGame.xGameWithObserver;
import static deliverables.XServerClient.xClients;
import static deliverables.XServerClient.xServer;
import static deliverables.XState.xState;
import static deliverables.XChoice.xChoice;
import static deliverables.XGame.xGame;

public class XMain {
  public static void main(String[] args) {
    if(args.length == 0) {
      System.out.println("XMain needs an argument to specify task");
      return;
    }
    int task = Integer.parseInt(args[0]);
    switch(task) {
      case 3:
        xBoard();
        break;
      case 4:
        xState();
        break;
      case 5:
        xChoice();
        break;
      case 6:
        xGame();
        break;
      case 7:
        xGameWithObserver();
        break;
      case 8:
        xBad();
        break;
      case 9:
        xBadWithObserver();
        break;
      case 10:
        xBad2();
        break;
      case 11:
        xBad2WithObserver();
        break;
      case 12:
        xServer(args);
        break;
      case 13:
        xClients(args);
        break;
      case 14:
        xBad2WithMultiGoal();
      default:
        System.out.println("Task number not found");
    }
  }
}
