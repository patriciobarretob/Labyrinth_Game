Pair: clever-armadillos \
Commit: [244fc38afed72c27d06db9c415defd94ff11e0e9](https://github.khoury.northeastern.edu/CS4500-F22/clever-armadillos/tree/244fc38afed72c27d06db9c415defd94ff11e0e9) \
Self-eval: https://github.khoury.northeastern.edu/CS4500-F22/clever-armadillos/blob/244fc38afed72c27d06db9c415defd94ff11e0e9/8/self-8.md \
Score: 100/110 \
Grader: Connie Tang

#### TEST FEST

No deductions; `xbad2` does not directly use the input spec to form the cheaters output.

#### PROGRAM INSPECTION 

[100/110]

- [20/20] for an accurate and helpful self evaluation

- [20/20] `Maze/Remote/player` must implement the same interface as `Maze/Player/player`, that is:
  - [5/5] accepting `setup` calls, turn them into JSON, get result in JSON, return when done
  - [5/5] accepting `take-turn` calls, turn them into JSON, receive CHOICE in JSON, return as value
  - [5/5] accepting `win` calls, turn them into JSON, get result in JSON, return when done
  - [5/5] These methods must not do more (or less) than exactly that
- [10/10] Constructor must receive handles for sending/receiving over TCP
- [10/10] Unit tests for `Maze/Remote/player`

- [10/10] `Maze/Remote/referee` must implement the same "context" as `Maze/Player/referee`
  - All or nothing:
  - making `setup` calls
  - making `take-turn` calls
  - making `win` calls
- [10/10] Receive handles for sending/receiving over TCP
- [10/10] Unit tests for `Maze/Remote/referee`
  
Client-Server

- [10/10] If someone starts the client before the server is up, the client must wait or shut down gracefully
- [0/10] The two waiting periods in the server is not hardwired
  - pointed to a different part in the self-eval, this was asking about the two waiting periods (which you have in `serverSetup`)
  - How would you modify your code if the spec changes to 10 waiting periods?
