**If you use GitHub permalinks, make sure your link points to the most recent commit before the milestone deadline.**

## Self-Evaluation Form for Milestone 9

Indicate below each bullet which file/unit takes care of each task.

Getting the new scoring function right is a nicely isolated design
task, ideally suited for an inspection that tells us whether you
(re)learned the basic lessons from Fundamentals I, II, and III. 

This piece of functionality must perform the following four tasks:

- find the player(s) that has(have) visited the highest number of goals
- compute their distances to the home tile
- pick those with the shortest distance as winners
- subtract the winners from the still-active players to determine the losers

The scoring function per se should compose these functions,
with the exception of the last, which can be accomplised with built-ins. 

1. Point to the scoring method plus the three key auxiliaries in your code. 
https://github.khoury.northeastern.edu/CS4500-F22/efficient-whales/blob/d21cebc143060613117e32db89c5138d7dc73797/Maze/Common/src/main/java/referee/MultiGoalReferee.java#L118-L127
https://github.khoury.northeastern.edu/CS4500-F22/efficient-whales/blob/d21cebc143060613117e32db89c5138d7dc73797/Maze/Common/src/main/java/referee/MultiGoalReferee.java#L129-L145

This third method does both the second and third key auxiliary tasks.
https://github.khoury.northeastern.edu/CS4500-F22/efficient-whales/blob/d21cebc143060613117e32db89c5138d7dc73797/Maze/Common/src/main/java/referee/AReferee.java#L373-L391
2. Point to the unit tests of these four functions.
https://github.khoury.northeastern.edu/CS4500-F22/efficient-whales/blob/d21cebc143060613117e32db89c5138d7dc73797/Maze/Common/src/test/java/TestMultiGoalReferee.java#L280-L319

This second unit test tests the remaining three functions.
https://github.khoury.northeastern.edu/CS4500-F22/efficient-whales/blob/d21cebc143060613117e32db89c5138d7dc73797/Maze/Common/src/test/java/TestMultiGoalReferee.java#L321-L360

The ideal feedback for each of these three points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.

