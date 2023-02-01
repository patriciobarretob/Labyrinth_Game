## Self-Evaluation Form for Milestone 2

Indicate below each bullet which file/unit takes care of each task:

1. point to the functinality for determining reachable tiles 

   - a data representation for "reachable tiles" 
      - A Set of Coordinates
   - its signature and purpose statement
   https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/a4d94128390e1bb2997bc174030fc9670abb53e0/Maze/Common/Board.java#L133
   - its "cycle detection" coding (accumulator)
   https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/a4d94128390e1bb2997bc174030fc9670abb53e0/Maze/Common/Board.java#L142-L153
   - its unit test(s) 
   https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/a4d94128390e1bb2997bc174030fc9670abb53e0/Maze/Tests/TestBoard.java#L189-L278

2. point to the functinality for shifting a row or column 

   - its signature and purpose statement
   https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/a4d94128390e1bb2997bc174030fc9670abb53e0/Maze/Common/Board.java#L63
   - unit tests for rows and columns
   https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/a4d94128390e1bb2997bc174030fc9670abb53e0/Maze/Tests/TestBoard.java#L103-L154
   
3. point to the functinality for inserting a tile into the open space

   - its signature and purpose statement
   https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/a4d94128390e1bb2997bc174030fc9670abb53e0/Maze/Common/Board.java#L123
   - unit tests for rows and columns
   https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/a4d94128390e1bb2997bc174030fc9670abb53e0/Maze/Tests/TestBoard.java#L156-L187

If you combined pieces of functionality or separated them, explain.

If you think the name of a method/function is _totally obvious_,
there is no need for a purpose statement. 

The ideal feedback for each of these points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.

