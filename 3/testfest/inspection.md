Pair: alecnorrie-joey \
Commit: [63e3068a931dd22f343fcb15866da99b28b347b3](https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/tree/63e3068a931dd22f343fcb15866da99b28b347b3) \
Self-eval: https://github.khoury.northeastern.edu/CS4500-F22/alecnorrie-joey/blob/e01c6afcc50f5b9ef1ed1ea8ae2ff3c8315de028/3/self-3.md \
Score: 48/85 \
Grader: Varsha Ramesh


#### PROGRAMMING

- [20/20] -  Helpful and accurate self-eval 

- [18/45]  - Signatures and purpose statements for the following: 

  - [5/5] rotate the spare tile by some number of degrees
  - [0/5] shift a row/column and insert the spare tile
    - Index deserves an interpretation. Method name is not descriptive enough to not have a purpose statement.
  - [0/5] move the avatar of the currently active player to a designated spot
    - Does not exist.
  - [3/5] check whether the active player's move has returned its avatar home
     - Does not exist but explicitly said so on self-eval.
  - [0/5] kick out the currently active player
     - Your method name is not descriptive enough to have a purpose statement.
 

- Unit tests
  - [10/10] test row and column insertions
    - Tested all 4 directions
  - [0/10] testing player movement when row/column shifts

#### DESIGN

- [10/20]
  - [0/5] player has a `take turn` function/method   
  - [5/5] player's home location
  - [5/5] player's goal assignment
  - [5/5] player's current location
  - [-5] your memo should clarify when/how the player receives different pieces of data
