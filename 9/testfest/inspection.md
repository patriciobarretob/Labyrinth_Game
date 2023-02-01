Pair: efficient-whales \
Commit: [d21cebc143060613117e32db89c5138d7dc73797](https://github.khoury.northeastern.edu/CS4500-F22/efficient-whales/tree/d21cebc143060613117e32db89c5138d7dc73797) \
Self-eval: https://github.khoury.northeastern.edu/CS4500-F22/efficient-whales/blob/39ff02efd90789769d8bb21fee57a886fc926b5b/9/self-9.md \
Score: 45/100 \
Grader: Megan Li

Their designated codebase is [clever-armadillos](https://github.khoury.northeastern.edu/CS4500-F22/clever-armadillos)
The codebase they should NOT use is [clever-wolves](https://github.khoury.northeastern.edu/CS4500-F22/clever-wolves)

## Program Inspection

- [14/20] for an accurate and helpful self evaluation. 
  - -6 for misdirect on self-eval asking for unit tests for scoring functions. Self-eval pointed to referee unit tests instead of saying there were no scoring unit tests. The referee unit tests are difficult to understand.

The top-level scoring function must perform the following tasks:

- find the player(s) that has(have) visited the highest number of goals
- compute their distances to the home tile
- pick those with the shortest distance as winners
- subtract the winners from the still-active players to determine the losers


The first three tasks should be separated out as methods/functions:
1. Find player(s) that has(have) visited the highest number of goals.
2. Compute player distances to next goal.
3. Find player(s) with shortest distance to next goal.

- [31/40] Each of these functions should have a good name or a purpose statement.
  - [5/10] Top-level scoring method. No purpose statement and `updateWinners()` isn't a great name since this method doesn't mutate.
  - [10/10] Find players that have visited the highest number of goals - `getPotentialWinnersByNumTreasures()`
  - [10/10] Find players with shortest distance to next goal - `updateWinnersByGoal()` same comment as for `updateWinners()` since this method doesn't update (it just reutrns the potential winners), the method name should not contain `update` 
  - [6/10] for computing player distances to next goal - this is inlined in `updateWinnersByGoal()` but partial credit since explicitly said so in self-eval
- [0/40] These functions should have unit tests
  - [0/20] for unit tests for scoring
  - [0/10] for a unit test that covers no players in the game at the time of scoring OR purpose statements / data interpretations indicate the state comes with at least one active player
  - [0/10] for a unit test that covers scoring a game where multiple players have the same number of goals
  - You do not have unit tests for your scoring functionality. You are indirectly testing scoring through your unit tests for the referee. 
  - Your tests are very difficult to read and understand.

Notes / feedback:
- Make sure to obey the changes to the spec: "If the game-terminating player is one of the players with the highest number of collected treasures, it is the sole winner." 