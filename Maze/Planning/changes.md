# Memorandum
TO:           Professor Ben Lerner & Professor Matthias Felleisen

FROM:     David Mitre, Joseph Kim

DATE:       November 9, 2022

SUBJECT: Accomodating Rule Changes

Accommodating blank tiles will be relatively easy, so we give it a rating of 2. We would add a new connector to the Connector.java enum which would be constructed with a new connector character and all of the connective booleans as false. The only method we would update is the switch statement in Connector.next().

Accommodating movable tiles as goals will be relatively hard, so we give it a rating of 4. Anytime a slide is being done we would update the goal coordinates. Additionally, the data definition for a Coordinate would be updated as we need a way for a Coordinate to represent the spare tile, since a goal tile could be slid off the board.

Accommodating having a player sequentially pursue several goals will be somewhat difficult, so we give it a rating of 3. First, we would update the data in RefereePlayerInfo from a boolean to a queue of Coordinates. Then, we would update the checks on whether a player has reached its goal to whether a player has reached all its goals. Finally, we would update how the Referee sets players up. 

