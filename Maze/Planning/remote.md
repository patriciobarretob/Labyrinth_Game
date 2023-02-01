TO: CEOs

FROM: Alec Norrie

CC: Joseph Kim

DATE: November 3rd, 2022

SUBJECT: Distributed Game System

A server holds a single method, runGame(). This method takes in two arguments, a Natural representing the required number of players and a Referee to play the game.

A player stub:
  * Is constructed with the player's name and the method of contacting the player.
  * Implements the Player API
      * For the name() method, return the player's name.
      * For each method besides name(), the method name and parameters will be converted into JSON and sent to the player.
      * The method will then recieve a JSON response, ensure that it is well-formed, and decode it to internal data. 
        * The player stub will leave validity checking to the Referee.
    
### Functionality of runGame()
  * The server accepts potential players over TCP into the game on a first-come first-serve basis.
  * When the desired number of potential players are connected, the server will temporarily stop accepting new connections.
  * When a potential player connects, the server will prompt the player for its name as a String and age as a Natural.
      * If a potential player gives an invalid response or does not respond in time, the server will remove and disconnect the player. Then, it will resume accepting new connections.
      * If a potential player gives a timely, valid response, the server informs the player that it has been added to the game and stores its name and age.
  * Once all of the potential players have responded with valid information, the server will start a game with the given Referee.
      * The server will create player stubs and give them to the Referee, sorted in ascending order by age.
  * Once the Referee finishes running the game, the server will close connections with all of the players and return a List of winning players and cheating players.
