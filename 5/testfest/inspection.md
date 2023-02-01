Pair: alecnorrie-joey \
Commit: [3ebecc24a079fc1b8aac67359d43e54d3f1dc6f7](https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/tree/3ebecc24a079fc1b8aac67359d43e54d3f1dc6f7) \
Self-eval: https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/572734b93e2b47b181e5a61ac562e5b5d6ebf9b7/5/self-5.md \
Score: 146/160    
Grader: Varsha Ramesh

20/20: Self-eval

50/50 player.PP: Player class should have the following methods with good names, signatures/types, and purpose statements:

- [10/10pt] name
- [10/10pt] propose board
- [10/10pt] setting up
- [10/10pt] take a turn
- [10/10pt] did I win

26/40 referee.PP must have following functions with good names, signatures/types, and purpose statements:

- [10/10pt] setting up the player with initial information
- [0/10pt] running rounds until the game is over
   - Did not factor it out and while loop is not readable.
- [6/10pt] running a round, which must have functionality for
   - Did not factor out into a different functionality, given 6 points because you have explicitly mentioned it is not there.
- [10/10pt] score the game

`	Every referee call on the player should be protected against illegal behavior and infinite loops/timeouts/exceptions. This should be factored out into a single point of control.`

20/20 Unit tests for running a game:

- [10/10pt] a unit test for the referee function that returns a unique winner
- [10/10pt] a unit test for the scoring function that returns several winners

30/30 interactive-player.md 

Description of gestures that

- [10/10pt] rotates the tile before insertion 
- [10/10pt] selects a row or column to be shifted and in which direction
- [10/10pt] selects the next place for the player's avatar
