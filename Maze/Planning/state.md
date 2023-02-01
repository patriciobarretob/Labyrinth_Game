TO: CEOs

FROM: Alec Norrie

CC: Joseph Kim

DATE: October 7th, 2022

SUBJECT: Game state

Our game state will be a Java class consisting of the following components:  
1. The board, which we have implemented as a 2d array of tiles.  
2. The spare tile.  
3. A list of player data, containing information such as the current location, home tile, and goal tile.  
4. A queue of players that represents the turn order.

The functionality that the game state needs to provide to the referee is as follows:  

1. Removing a player.
2. Executing a turn, including sliding a row or column, inserting and rotating a tile, and moving a player.
3. Determining if the game is over and if there is a winner.
4. Getting information about the game, such as the board, the spare tile, and player data.
