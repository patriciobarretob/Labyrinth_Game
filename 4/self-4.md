**If you use GitHub permalinks, make sure your links points to the most recent commit before the milestone deadline.**

## Self-Evaluation Form for Milestone 4

The milestone asks for a function that performs six identifiable
separate tasks. We are looking for four of them and will overlook that
some of you may have written deep loop nests (which are in all
likelihood difficult to understand for anyone who is to maintain this
code.)

Indicate below each bullet which file/unit takes care of each task:

Note for our code: Our strategies are implemented in "IPlayer", "APlayer", "RiemannPlayer", and "EuclidPlayer". This naming is confusing, but it does not represent player information: that is in "PlayerInfo"

1. the "top-level" function/method, which composes tasks 2 and 3 

https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/d716929a6028e979490b8e006200969364de5536/Maze/Common/src/main/java/components/APlayer.java#L20-L30

2. a method that `generates` the sequence of spots the player may wish to move to

"getAllCandidates" is a (poorly-named) method that generates all the valid coordinates of the given board (from the state).

For Euclid:
https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/d716929a6028e979490b8e006200969364de5536/Maze/Common/src/main/java/components/EuclidPlayer.java#L16-L27

For Riemann:

https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/d716929a6028e979490b8e006200969364de5536/Maze/Common/src/main/java/components/RiemannPlayer.java#L18-L28

3. a method that `searches` rows,  columns, etcetc. 

https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/d716929a6028e979490b8e006200969364de5536/Maze/Common/src/main/java/components/APlayer.java#L32-L67

4. a method that ensure that the location of the avatar _after_ the
   insertion and rotation is a good one and makes the target reachable

https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/d716929a6028e979490b8e006200969364de5536/Maze/Common/src/main/java/components/PublicState.java#L63-L78

ALSO point to

- the data def. for what the strategy returns

https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/d716929a6028e979490b8e006200969364de5536/Maze/Common/src/main/java/components/Move.java#L6-L24

- unit tests for the strategy

https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/d716929a6028e979490b8e006200969364de5536/Maze/Common/src/test/java/TestEuclidPlayer.java#L23

https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/d716929a6028e979490b8e006200969364de5536/Maze/Common/src/test/java/TestRiemannPlayer.java#L23

The ideal feedback for each of these points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality or realized
them differently, say so and explain yourself.


