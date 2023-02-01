**If you use GitHub permalinks, make sure your link points to the most recent commit before the milestone deadline.**

## Self-Evaluation Form for Milestone 5

Indicate below each bullet which file/unit takes care of each task:

The player should support five pieces of functionality: 

- `name`
https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/3ebecc24a079fc1b8aac67359d43e54d3f1dc6f7/Maze/Common/src/main/java/players/PlayerImpl.java#L27-L30
- `propose board` (okay to be `void`)
https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/3ebecc24a079fc1b8aac67359d43e54d3f1dc6f7/Maze/Common/src/main/java/players/PlayerImpl.java#L33-L42
- `setting up`
https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/3ebecc24a079fc1b8aac67359d43e54d3f1dc6f7/Maze/Common/src/main/java/players/PlayerImpl.java#L58-L60
- `take a turn`
https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/3ebecc24a079fc1b8aac67359d43e54d3f1dc6f7/Maze/Common/src/main/java/players/PlayerImpl.java#L63-L65
- `did I win`
https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/3ebecc24a079fc1b8aac67359d43e54d3f1dc6f7/Maze/Common/src/main/java/players/PlayerImpl.java#L70

Provide links. 

The referee functionality should compose at least four functions:

- setting up the player with initial information
https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/3ebecc24a079fc1b8aac67359d43e54d3f1dc6f7/Maze/Common/src/main/java/players/Referee.java#L76-L82
- running rounds until termination
https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/3ebecc24a079fc1b8aac67359d43e54d3f1dc6f7/Maze/Common/src/main/java/players/Referee.java#L168-L196

- running a single round (part of the preceding bullet)
https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/3ebecc24a079fc1b8aac67359d43e54d3f1dc6f7/Maze/Common/src/main/java/players/Referee.java#L168-L196
We did not separate this out into its own functionality. In this same while loop we check if the round has ended but do not separate it.

- scoring the game
https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/3ebecc24a079fc1b8aac67359d43e54d3f1dc6f7/Maze/Common/src/main/java/players/Referee.java#L264-L279

Point to two unit tests for the testing referee:

1. a unit test for the referee function that returns a unique winner
https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/3ebecc24a079fc1b8aac67359d43e54d3f1dc6f7/Maze/Common/src/test/java/TestReferee.java#L41-L76
2. a unit test for the scoring function that returns several winners
https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/3ebecc24a079fc1b8aac67359d43e54d3f1dc6f7/Maze/Common/src/test/java/TestReferee.java#L170-L210

The ideal feedback for each of these points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files -- in the last git-commit from Thursday evening. 

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.

