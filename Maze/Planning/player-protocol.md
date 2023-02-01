TO: CEOs

FROM: Alec Norrie

CC: Joseph Kim

DATE: October 20th, 2022

SUBJECT: Referee-Player protocol

1. Pregame
    * Players are accepted.  
        * If a player requests to join a game, the referee will put the player into a queue. 
          This queue will hold all of the players currently waiting to join the game. This allows multiple players to wait to join. 
        * If a player requests to leave a game, the referee will either remove them from the waiting queue or the game.
        * If a referee adds a player who is waiting to join the game, the referee will notify the player that they have been added to the game.
          The referee will add the player to the list of players.
        * If a referee rejects a player who is waiting to join the game, the referee will notify the player that they have been rejected from the game.
        * If a referee removes a player, the referee will notify the player that they have been removed from the game. 
          The referee will remove the player from the list of players.
    * When a referee starts the game, we move on to "Initialize Game"

2. Initialize Game
    * The board is initialized.  
    * The spare tile is initialized.  
    * Players are assigned an avatar (Color), a home tile (Coordinate), and a goal tile (Coordinate). 
    * Players are ordered in a queue by age, from youngest to oldest. This will represent the turn order.
    * The game state is initialized with the board, the spare tile, and the queue of players. Then, we move to "Taking Turns".
    
3. Taking Turns
    * The referee will ask the first player in the queue for a turn. The referee will send the current state of the game, relative to the player. 
    This consists of the public state and the player's goal tile. The referee will wait a predetermined amount of time to recieve a turn.  
        * If the player does not respond in time or gives an invalid move, the referee will remove the player from the game.
        * If the player does respond with a valid move, the referee will enact the turn, cycle the current player to the end of the queue.
    * The referee will determine if the game is over. If it is, we move to "Postgame". Otherwise, continue asking for turns.

4. Postgame
    * The referee scores the game and informs every player of the outcome. 
    * The referee removes all players from the game. 

