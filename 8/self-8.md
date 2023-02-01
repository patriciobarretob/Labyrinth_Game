**If you use GitHub permalinks, make sure your link points to the most recent commit before the milestone deadline.**

## Self-Evaluation Form for Milestone 8

Indicate below each bullet which file/unit takes care of each task.

For `Maze/Remote/player`,

- explain how it implements the exact same interface as `Maze/Player/player`
  - https://github.khoury.northeastern.edu/CS4500-F22/clever-armadillos/blob/152b179dc827cba9fd8cdccf23ac54e776dd1cb1/Maze/Common/src/main/java/remote/RemotePlayer.java#L32
- explain how it receives the TCP connection that enables it to communicate with a client
  - https://github.khoury.northeastern.edu/CS4500-F22/clever-armadillos/blob/152b179dc827cba9fd8cdccf23ac54e776dd1cb1/Maze/Common/src/main/java/remote/RemotePlayer.java#L41-L45
  - In order to use this RemotePlayer with a TCP connection, the user would pass the input and output streams of the socket, as seen in Server.java.
- point to unit tests that check whether it writes JSON to a mock output device
  - https://github.khoury.northeastern.edu/CS4500-F22/clever-armadillos/blob/152b179dc827cba9fd8cdccf23ac54e776dd1cb1/Maze/Common/src/test/java/TestRemotePlayer.java#L43-L172
  - We use our own input and output streams to mock the input and output of the remote player, including its output, which we parse, deserialize, and check for equality.

For `Maze/Remote/referee`,

- explain how it implements the same interface as `Maze/Referee/referee`
  - Our remote referee does not implement the same interface as Maze/Referee/referee. Our design decision for this was because we considered it to be more of a listener of the server, rather than a referee that runs the entire game.
- explain how it receives the TCP connection that enables it to communicate with a server
  - https://github.khoury.northeastern.edu/CS4500-F22/clever-armadillos/blob/152b179dc827cba9fd8cdccf23ac54e776dd1cb1/Maze/Common/src/main/java/remote/RemoteReferee.java#L37-L41
  - Similarly to our remote player, it takes in an input and output stream that can be obtained from Sockets. This can be seen in Client.java.
- point to unit tests that check whether it reads JSON from a mock input device
  - https://github.khoury.northeastern.edu/CS4500-F22/clever-armadillos/blob/152b179dc827cba9fd8cdccf23ac54e776dd1cb1/Maze/Common/src/test/java/TestRemoteReferee.java#L48-L175
  - The inputs are initialized in init(), and used as input to the remote referee.

For `Maze/Client/client`, explain what happens when the client is started _before_ the server is up and running:

- does it wait until the server is up (best solution)
  - https://github.khoury.northeastern.edu/CS4500-F22/clever-armadillos/blob/152b179dc827cba9fd8cdccf23ac54e776dd1cb1/Maze/Common/src/test/java/MockClient.java#L40-L42
  - https://github.khoury.northeastern.edu/CS4500-F22/clever-armadillos/blob/152b179dc827cba9fd8cdccf23ac54e776dd1cb1/Maze/Common/src/main/java/remote/Client.java#L39-L41
  - https://github.khoury.northeastern.edu/CS4500-F22/clever-armadillos/blob/152b179dc827cba9fd8cdccf23ac54e776dd1cb1/Maze/Common/src/test/java/TestServer.java#L103-L111
  - The mock clients, which connect to servers similarly to how Client connects to servers, are able to start a remote connection before the server is started without shutting down.
- does it shut down gracefully (acceptable now, but switch to the first option for 9)

For `Maze/Server/server`, explain how the code implements the two waiting periods:

- is it baked in? (unacceptable after Milestone 7)
  - https://github.khoury.northeastern.edu/CS4500-F22/clever-armadillos/blob/152b179dc827cba9fd8cdccf23ac54e776dd1cb1/Maze/Common/src/main/java/remote/Server.java#L57-L72
  - It is baked in; we first get the list of players and the run a game through a referee.
- parameterized by a constant (correct).

The ideal feedback for each of these three points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.

