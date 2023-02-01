TO: CEOs

FROM: Joseph Kim

CC: Alec Norrie

DATE: October 25th, 2022

SUBJECT: Interactive Player With GUI

For our interactive player GUI, we plan to implement an MVC architecture.

* For the model, we will extend an IPlayer to create a GUIPlayer, which will store the PublicState, the player's goal Coordinate, and name.
  It will also be what the Referee calls when asking for or sending information to a player.
* For the view, we will display information necessary for players to understand the current state of the game.
* For the controller, we will prompt users for input, depending on the method that requires it.

More specifically, we figured that the view and controller would depend on the state of the game.
  1. We figured we should prompt the user for their nickname that they want to be designated as.
        * There will be a text box for user to write their username in.
        * There will be a submit button for the user to confirm their username.
        * Once the user submits their username, we move on to step 2.
  2. We figured we should prompt the user to propose a board.
        * There will be 11 shapes and all the possible gems for the user to choose at any time as a drop-down list.
        * Using the number of rows and columns provided by the referee, the user will see a row x column grid of squares.
            * A square will display 2 images, showing the currently selected gems, and a character, representing the currently selected connector.
            * A square will have 3 buttons, allowing the user to change either gems or the connector to the currently selected gem or connector in the drop-down list.
        * There will be a button to allow the user to submit their current board.
        * There will be a text box to inform the user if their submitted board is malformed, and if it is, why.
        * Once a user provides a valid board, we move on to step 3.
  3. We figured the user would be shown the initial state of the board.
        * The user would see a row x column grid of squares, similar to the previous step, but this time there would be no buttons
and there would be the home tiles and avatars of all the players.
        * On the top of the screen, the user would be informed what color their avatar is.
        * On the right of the screen, there would various information, such as:
            * Whether the user has reached their goal
            * The coordinates of the user's goal
            * The coordinates of the user's home
            * The spare tile
        * Once the user is prompted for a turn, we move to step 4.
        * If the game ends before the user is prompted for a turn, we move to step 5.
  4. We figured the user would be shown the current state of the game, and input their turn.
        * The game state would be updated according to what the referee sent
        * The user would receive a pop-up, asking for:
            * An index to slide
            * A drop-down menu for the four directions (up, down, left, right)
            * A number of rotations, clockwise
            * A target coordinate
        * If the user closes the pop-up without providing this information, it would pop up again
        * If the user gives an invalid move, it would pop up again, along with the reason why the move is invalid.
        * Once the user provides a valid move, we do one of two:
            * If the user has won the game, we move to step 5
            * Otherwise, we move back to step 3
  5. We figured the user would be shown at the end of the game whether they won or lost
        * The user would see a screen indicating that they either won or lost the game.
