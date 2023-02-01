Pair: alecnorrie-joey \
Commit: [a4d94128390e1bb2997bc174030fc9670abb53e0](https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/tree/a4d94128390e1bb2997bc174030fc9670abb53e0) \
Self-eval: https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/6d6d70843a6fdd3321ac1ab4cbc663c174194eaa/2/self-2.md \
Score: 65/80 \
Grader: Eshwari Bhide \

`board: 65/80`

- 30/40 pts: an operation that determines the reachable tiles from some spot
  - [0/10] : is there a data def. for "coordinates" (places to go to)? 
     - No data definition, and if you add data definitions, make sure it has an interpretation.  e.g. is coordinate (0,0) top-left or bottom-left? Is a Coordinate's x, y literal x, y or row, col? etc. See data definition on this page: https://htdp.org/2022-6-7/Book/part_one.html. See instructors if you have more questions.
  - [10/10] : is there a signature/purpose statement? 
     - technically no purpose statement but since you're in a typed language + method name is descriptive enough so you get full points
  - [10/10] : is it clear how the method/function checks for cycles? 
  - [10/10] : is there at least one unit test with a non-empty expected result

- 30/30 pts an operation for sliding a row or column in a direction 
  - [10/10] sig/purp.
    - since you're in a typed language no signature + no purpose statement is fine if your method name is descriptive/clear enough. Since you have helpers like `slideLeft` etc, I'll give it to you, but `slide` in itself is not a sufficient method name on its own with no purpose statement, so make sure to keep this in mind for the future
  - [10/10] the method should check that row/column has even index 
  - [10/10] unit tests: at least one for rows and one for columns

- 5/10 pts an operation for rotating and inserting the spare tile
  - No clarity on what happens to the tile that's pushed out 


`state.md (Ungraded)`
- Player info should also include its color (but you got everything else needed for Player info)
- Need to also store the last action performed so it is not undone by the next player

- So you have mixed in types into your design where you've said lists/queue etc but in the future make sure types are clearly stated for everything. e.g. what is the type of the spare tile/home tile/goal tile (like it might seem unnecessary but still explicitly say that they're instances of your Tile class). and also "a queue of players"–does this mean Player objects that you're going to design in the future? or references to players with IDs? 
- For methods, it would be good if you could use the wish list format. e.g. just add method stubs and a detailed purpose statement so that everything is as clear as possible in terms of the inputs and return types and what the method is doing.
- Ultimately, just be very explicit about everything
