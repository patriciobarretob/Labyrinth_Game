TO: CEOs

FROM: Joseph Kim

CC: Alec Norrie

DATE: October 13th, 2022

SUBJECT: Player data and functionality

The data that the player needs to know will be a Java class consists of the following components:  
1. The board, which we have implemented as a 2d array of Tiles.  
    * We will need to get the List of Coordinates representing movable positions.
2. The spare tile, which is a Tile.  
3. The player's goal tile, which is a Coordinate.  
4. A list of public player data, containing the current location(Coordinate), avatar(Color), and home tile(Coordinate) of each player.  
5. A queue of players that represents the turn order.
6. The last played move, which is a SimpleEntry of an index and a Direction.
7. Whether or not the game has ended.

A lot of this data is in the State class, but we can't give players all of the State as it has information we don't want players to know.
We plan to make a new data definition, PublicState, that uses the State class to store this data.



The functionality that a player needs to provide is:  

1. IMove makeMove(IMove lastMove)
    * Given the last player move, return the move this player will make.
2. IMove makeMove()
    * Return the move this player will make at the start of the game.

An IMove is one of:
* Pass
* TakeAction

A TakeAction will be a class that consists of:
1. A SimpleEntry of an index and a Direction, indicating a slide.
2. An int, indicating how many time it rotates the old spare tile 90 degrees clockwise.
3. A Coordinate, indicating where to move the avatar.
