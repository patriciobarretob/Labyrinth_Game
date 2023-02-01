Table of Contents
  1. [Overview](#overview)
  2. [Common Components](#common-components)
      1. [Common Component Data Definitions](#common-component-data-definitions)
      2. [Common Component Functionality](#common-component-functionality)
      

## Overview


All of the numbered directories contain the respective Makefiles, tests, and shell script for the corresponding milestone.  
Finger_Exercises is a directory created when we were trying out things (such as improving json) before milestones started.
Maze contains all of the files required for the Labyrinth milestones.
  * Players/ and Referees/ contain hard links to their respective java files, and are currently only used for submission purposes.
  * Planning/ contains all the planning.md files for design tasks.
  * Common/ contains hard links to the first few respective java files, along with
    * A Makefile for compiling the code
    * A pom.xml file for Maven
    * test.py, a python script that compares an expected json output with an actual one.
    * xtest, a shell script that runs all of our unit and "integration" tests (from the top level numbered directories/Tests)
    * src/, which contains the java codebase.

Our codebase contains a test directory, which holds all the unit tests, and a main directory, which holds the resources and the actual code.  
In src/main/java, there is:
  * XMain, which is the java file that contains all the main classes for the milestones
  * components/, which contains the game components, along with a few other things (more about that in the components section)
  * json/, which contains all the json serialization, deserialization, and other classes (more about that in the Json section)
  * players/, which contains players, referees, observers, and strategies (more about that in the players section)

## Common Components
Some prefacing notes:
  1. Throughout our components, we use Color as a "key" to represent the identity of a player. This is our bandage for parallel data structures,
and we keep it because 1. we don't know how to remove parallel data structures and 2. we don't have the time to think about it
  2. I refer to "Pairs" in this README, but in the code they're implemented as SimpleEntries. The reason for this is because we didn't want to import an outside library just to have pairs, but we later realized that this also causes "Pairs" to be mutable. Definitely a tech debt item.

### Common Component Data Definitions
Here are the list of classes in the components/ directory, their data definitions, and interpretation.
Components contains:
  * RefereeState
    * Contains a PublicState and a Map from a Color (PlayerInfo.avatar) to a RefereePlayerInfo
    * Represents the knowledge of a game that a Referee needs to run a Labyrinth game
  * RefereePlayerInfo
    * Contains a boolean of whether the player has reached their goal, and a Coordinate representing their goal.
    * Represents the knowledge of a player that only the Referee should know
  * PublicState
    * Contains:
      * A Board, which is the game board
      * A Tile, which is the spare tile
      * A Queue\<PlayerInfo>, which represents the players and their turns
      * An Optional<Pair<int,Direction>>, which represents the last slide performed (Empty being that there was no last slide)
      * The number of rounds played so far
      * The first _initial_ player of the game, who is used to determine if a round has just ended.
    * Represents the knowledge of a game that all players share
  * PlayerInfo
    * Contains:
      * A Color, representing this player's avatar
      * This player's home, which is a Coordinate
      * This player's current position, which is a Coordinate
      * A boolean, representing whether or not this player has skipped in the current round of the game
    * Represents the knowledge of a player that all players share
  * Coordinate
    * Contains two integers representing the x and y coordinate
    * Represents a coordinate on a Board.
  * Direction
    * Is an enumeration of up, down, left, and right.
    * Represents the four horizontal and vertical directions.
  * Board
    * Contains a rectangular 2D array of Tiles, the board's width, and height.
    * Represents the pieces of a game board.
  * Tile
    * Contains 2 Gems and a Connector.
    * Represents a tile on a game board.
  * Gem
    * Contains a String, representing the Gem name.
    * Represents a gem on a tile.
  * Connector
    * Is an enumeration of the eleven different connectors
    * Represents a the ways a tile can be connected to other tiles.
  * Move
    * Contains:
      * A Pair<int, Direction> representing a slide
      * A number of 90 degree rotations clockwise, as an integer
      * A target Coordinate
    * Represents the three steps of a valid move that a player can make.
  * LabyrinthRules
    * Is a class that contains methods for game rule related judgements. More on this class in Functionality.

### Common Component Functionality
Here is the public functionality of each of the classes in the components/ directory. This will also include any constructors and public fields, as I consider them to be "getter" (hopefully not setter!) functionality. All public fields mentioned should also be in [Common Component Data Definitions](#common-component-data-definitions)

It should be noted that whenever there's a constructor that only takes in one instance of itself as a parameter (e.g. Cookie(Cookie otherCookie)), that indicates that it's a copy constructor. (It also probably indicates that there should be a comment saying that it's a copy constructor.)

  * RefereeState 
    * Constructors
      * RefereeState(Board, Tile, Queue\<PlayerInfo>, Map\<Color, RefereePlayerInfo>)
      * RefereeState(PublicState, Map\<Color, RefereePlayerInfo>)
      * RefereeState(RefereeState)
    * Public Fields
      * final Map\<Color, RefereePlayerInfo> infoMap
        * Read disclaimer 1 in [Common Components](#common-components)
      * final PublicState publicState
    * Public Methods
      * boolean isActivePlayerOnGoalTile()
        * Is the active player currently on their goal tile?
      * void updateActivePlayerHasReachedGoal()
        * EFFECT: Update the RefereePlayerInfo of the active player in infoMap to indicate that they have reached their goal.
      * void kickActivePlayer()
        * EFFECT: Removes the mapping of the active player from infoMap, and removes the active player from publicState
  * RefereePlayerInfo
    * Constructors
      * RefereePlayerInfo(Coordinate goal)
      * RefereePlayerInfo(RefereePlayerInfo)
    * Public Fields
      * nonfinal boolean hasPlayerReachedGoal
      * final Coordinate goal
  * PublicState
    * Constructors
      * PublicState(Board, Tile, Queue\<PlayerInfo>)
      * PublicState(PublicState)
    * Public Fields
      * Tile spare
      * Optional\<Pair\<int, Direction>> lastAction
    * Public Methods
      * void doTurn(Optional\<Move>)
        * EFFECT: Either updates the board, spare tile, and the active player or just the active player.
        * This is a composite method that either updates PlayerInfo.skippedThisRound of the active player if they pass, or runs rotateSpare, shiftAndInsert, and moveActivePlayerTo and updates the last slide if they move.
      * void rotateSpare(int)
        * EFFECT: Rotates the spare tile 90 degrees clockwise by the given number of times.
      * void shiftAndInsert(int, Direction)
        * EFFECT: Updates the board, the spare tile, and potentially many players.
        * This method shifts the board in the given index and direction, updates the spare tile, and moves every player that was on
      * void moveActivePlayerTo
        * EFFECT: Moves the active player to the given target. Throws an IllegalArgumentException if it's not possible to do so.
      * boolean canActivePlayerReach(Coordinate)
        * Can the active player reach the given coordinate?
      * boolean isActivePlayerAtHome()
        * Is the active player at their initial home?
      * void kickActivePlayer()
        * EFFECT: Removes the active player and updates the first initial player if applicable. This automatically makes it the next player's turn, so don't call goToNextActivePlayer after calling this.
      * void goToNextActivePlayer()
        * EFFECT: Updates the active player to be the next player.
      * boolean hasRoundJustEnded()
        * Has the round just ended?
        * This method is intended to be called after a turn to see if the referee needs to do post-round handling.
      * void incrementRound()
        * EFFECT: Increases the round count by 1.
      * void resetSkips()
        * EFFECT: Resets all the skips of every player to say they haven't skipped in the current round.
      * Board getBoard()
      * Tile getSpare()
      * Queue\<PlayerInfo> getPlayers()
      * PlayerInfo getActivePlayer()
      * int getRoundNum()
      * PlayerInfo getFirstPlayer()
  * PlayerInfo
    * Constructors
      * PlayerInfo(Color, Coordinate, Coordinate)
        * First Coordinate is home, second Coordinate is current position
      * PlayerInfo(Color, Coordinate)
        * Constructor that only takes in the player's home
      * PlayerInfo(PlayerInfo)
    * Public Fields
      * final Color avatar
      * final Coordinate home
      * nonfinal Coordinate currentPos
      * nonfinal boolean skippedThisRound
    * Public Methods
      * equals(Object) and hashCode() have been overridden.
  * Coordinate
    * Constructors
      * Coordinate(int, int)
        * The first int is the x coord, and the second int is the y coord. (0, 0) is the top left of the board.
    * Public Fields
      * final int x
      * final int y
    * Public Methods
      * double distanceBetween(Coordinate)
        * Returns the distance between this coordinate and the given one.
      * equals(Object), hashCode, and toString have been overriden.
  * Direction
    * Public Methods
      * Direction getOpposite()
        * Returns the opposite direction of this direction.
  * Board
    * Constructors
      * Board(Tile\[]\[])
      * Board(Board)
    * Public Fields
      * final static int BOARD_DIMENSION = 7
        * Hopefully should be removed soon enough
      * final int width
      * final int height
    * Public Methods
      * Tile\[]\[] getBoardTiles()
      * boolean validSlideRow(int)
        * Is the given row index slidable?
      * boolean validSlideCol(int)
        * Is the given column index slidable?
      * Tile slideAndInsert(int, Direction, Tile)
        * EFFECT: Slides the given index in the given direction, and inserts the given Tile into the empty slot.
        * Returns the tile that got slid off.
      * List\<Coordinate> getReachablePositions(Coordinate)
        * Returns the list of coordinates reachable from the given position, including itself, in row-column order.
      * boolean isPositionReachable(Coordinate, Coordinate)
        * Are the two coordinates reachable from each other?
      * boolean isInBounds(Coordinate)
        * Is the given coordinate in-bounds of this board?
  * Tile
    * Constructors
      * Tile(Gem, Gem, Connector)
    * Public Fields
      * final Gem gem1
      * final Gem gem2
    * Public Methods
      * Tile rotate()
        * Returns this tile after rotating its connector 90 degrees clockwise.
      * Connector getConnector()
  * Gem
    * Constructors
      * Gem(String)
    * Public Fields
      * final String name
      * final static String[] ALL_VALID_GEM_NAMES
  * Connector
    * Constructors
      * Connector(char, boolean, boolean, boolean, boolean)
        * The booleans represent whether this connector is connected up, down, left, and right respectively.
    * Public Fields
      * final char connectorChar
      * final boolean connectsUp
      * final boolean connectsDown
      * final boolean connectsLeft
      * final boolean connectsRight
    * Public Methods
      * static Connector getFromChar(char c)
        * Returns the Connector that corresponds to the given character.
        * Throws an IllegalArgumentException if the character does not correspond to a Connector.
      * Connector getNext()
        * Returns the connector after rotating this connector 90 degrees clockwise.
      * static Connector getRandomConnector(Random)
        * Using the given Random object, returns a random(?) Connector.
  * Move
    * Constructors
      * Move(Pair\<int, Direction>, int, Coordinate)
    * Public Fields
      * final Pair\<int, Direction> slide
      * final int rotations
      * final Coordinate target
    * Public Methods
      * equals(Object), hashCode(), and toString() are overridden.
  * LabyrinthRules
    * Public Methods
      * static List\<Coordinate> getAllImmovableCoordinates(Board)
        * Returns all the coordinates on a board that cannot move.
      * static boolean isLegalBoard(Board)
        * Is the given a legal board?
        * A legal board:
          * Has an odd number of rows and columns
          * Has no tiles with the same pairs of gems
      * static boolean isGameOver(RefereeState)
        * Is the given game over due to a non-player-winning condition?
      * static boolean activePlayerWon(RefereeState)
        * Has the active player in the given game won?
      * static boolean canMove(Move, PublicState)
        * Is the given move valid on the given state?

## Player Components
This section is called Player components because it's referring to every file in the players/ directory, but in reality it consists of various components, such as Strategies, Players, Referees, and Observers. Thus, I will also split up this section into those components.

### Strategy
  * The IStrategy interface requires one method: Optional\<Move> makeMove(PublicState, Target), which takes in a game state and a target position and returns the turn that the strategy wishes to make, with Optional.empty representing a pass.
  * AStrategy abstracts the functionality of RiemannStrategy and EuclidStrategy, and requires them to implement a method that returns a queue of all the candidates in order of priority.

### Players
  * IPlayer is an interface that consists of all the functionality that the Referee needs to play a game with a player.

### Referee
  * Constructed with a list of IPlayers
  * Supports adding Observers, who are sent the state of the game after each turn
  * Can either play a game from start, or from a given state. After playing a game, returns a pair of lists, representing the list of winners and list of cheaters.

