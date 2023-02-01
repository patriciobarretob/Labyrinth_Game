package players;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import components.SingleGoalRefereeState;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.awt.event.ActionListener;
import java.util.AbstractMap.SimpleEntry;

import javax.swing.*;
import json.JsonRefereeStateSerializer;

//The View for the Observer. Displays a graphical interface consisting of a RefereeState,
//and two buttons. The first button will display the next state, if any. The second button
//will save the current RefereeState in a file of the user’s choice.
//Inspired by https://course.ccs.neu.edu/cs3500f19/lec_gui_basics_notes.html
public class ObserverView extends JFrame implements ActionListener {
  Observer observerModel;
  Optional<SingleGoalRefereeState> currentState;
  public ObserverView(Observer observerModel) {
    super();
    this.observerModel = observerModel;
    this.currentState = Optional.empty();

    this.setTitle("Observer");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.getContentPane().setLayout(new BorderLayout());

    JPanel statePanel = createWinPanel(false);
    statePanel.setPreferredSize(new Dimension(500, 500));

    JScrollPane scrollPane = new JScrollPane(statePanel);
    this.getContentPane().add(scrollPane, 0);
    pack();

    addButtons();

    this.setSize(750, 500);
    setVisible(true);
  }

  //Creates the "next" and "toFile" buttons.
  //Adds them to the bottom of the contentPane.
  private void addButtons() {
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(1,2));

    JButton previous = new JButton("Previous");
    previous.setActionCommand("previous");
    previous.addActionListener(this);

    JButton next = new JButton("Next");
    next.setActionCommand("next");
    next.addActionListener(this);

    JButton toFile = new JButton("Export To File");
    toFile.setActionCommand("toFile");
    toFile.addActionListener(this);
    buttonPanel.add(previous);
    buttonPanel.add(next);
    buttonPanel.add(toFile);

    this.getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
  }

  //This has two actions that it is concerned with: "next" and "toFile".
  // next asks the model to retrieve the next RefereeState, if available.
  // toFile saves a JSON representation of the state to a file of the user's choosing.
  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "previous":
        handlePrevious();
        break;
      case "next":
        handleNext();
        break;
      case "toFile":
        handleToFile();
        break;
      default:
        throw new IllegalArgumentException("Action not found");
    }
  }

  private void handlePrevious() {
    Optional<SimpleEntry<SingleGoalRefereeState, Map<Color, String>>> state = observerModel.getPreviousState();
    JScrollPane scrollPane;
    if(state.isPresent()) {
      currentState = Optional.of(state.get().getKey());
      scrollPane = new JScrollPane(new JRefereeStatePanel(state.get().getKey(),
          state.get().getValue()));
    } else {
      scrollPane = new JScrollPane(createWinPanel(observerModel.isGameOver));
    }
    this.getContentPane().remove(0);
    this.getContentPane().add(scrollPane, 0);
    this.getContentPane().revalidate();
    this.getContentPane().repaint();
  }

  //Handles the "Next" command. If there is a next state, display it.
  // If the game is over, display a "game over" screen.
  private void handleNext() {
    Optional<SimpleEntry<SingleGoalRefereeState, Map<Color, String>>> state = observerModel.getNextState();
    JScrollPane scrollPane;
    if(state.isPresent()) {
      currentState = Optional.of(state.get().getKey());
      scrollPane = new JScrollPane(new JRefereeStatePanel(state.get().getKey(),
          state.get().getValue()));
    } else {
      scrollPane = new JScrollPane(createWinPanel(observerModel.isGameOver));
    }
    this.getContentPane().remove(0);
    this.getContentPane().add(scrollPane, 0);
    this.getContentPane().revalidate();
    this.getContentPane().repaint();
  }

  //http://www.java2s.com/Tutorials/Java/Swing_How_to/JFileChooser/Prompt_User_for_file_export_location.htm
  // A simple guide to writing to a file from a JFileChooser()
  private void handleToFile() {
    if(!currentState.isPresent()) {
      JOptionPane.showMessageDialog(null, "No state available to save");
      return;
    }
    JFileChooser chooser = new JFileChooser();
    int result = chooser.showSaveDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
      try {
        File file = chooser.getSelectedFile();
        FileWriter writer = new FileWriter(file);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SingleGoalRefereeState.class, new JsonRefereeStateSerializer());
        Gson gson = gsonBuilder.create();
        writer.write(gson.toJson(currentState.get()));
        writer.close();
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(null, "Invalid location");
      }
    }
  }

  //Creates an empty panel with one label to display when there are no "next" states available,
  // If the game is over, it will display "Game is over"
  // If the game has not completed yet, it will display "No states to display"
  private JPanel createWinPanel(boolean gameOver) {
    JLabel label = new JLabel(gameOver ? "Game is over" : "No states to display");
    JPanel newPanel = new JPanel();
    newPanel.add(label);
    return newPanel;
  }
}