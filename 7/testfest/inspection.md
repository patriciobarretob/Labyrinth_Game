Pair: clever-armadillos \
Commit: [fe8c7ca2ec510ee7432b3595fd6b621509f1d2d4](https://github.khoury.northeastern.edu/CS4500-F22/clever-armadillos/tree/fe8c7ca2ec510ee7432b3595fd6b621509f1d2d4) \
Self-eval: https://github.khoury.northeastern.edu/CS4500-F22/clever-armadillos/blob/35536ec81dfa0b7c5dc96ad7b49e6b134c95e5db/7/self-7.md \
Score: 197/205 \
Grader: Eshwari Bhide

## TEST FEST

No deductions; `xbad` does not directly use the input spec to form the cheaters output.

## GIT LOG INSPECTION

[70/70]

- [10/10] for git add remote as instructed by the "new repo" instructions of GH Enterprise
- [30/30] checking that a large non-trivial commit is not copied code from other repo.
- [30/30] same as above with a different commit.

## TECH DEBT DOWNPAYMENT 

[80/80]

- [20/20] for an accurate and helpful self evaluation.
- [10/10] descriptive tasks in todo.md (e.g. not "Improve the code").
- [50/50] the git commit message for five completed bullets help the reader understand the change and appreciate the improvement to the code.
  - Technically the git commit messages you have do not fulfill that criteria as they are of the form "Tech debt item <x>" instead of explicitly listing what was done in the change, so it really should be a 0 for this. But I guess anyone could easily look back at your todo list and infer what you're referring to and it didn't seem worth taking off all 50 points (it was pretty much all or nothing).

## REQUIRED REVISION

[32/40]

The require revision calls for
- the relaxation of the constraints on the board size
- a suitability check for the board size vs player number

- [10/10] a unit test that validates the implementation of the relaxation (board is not 7 x 7)
- [10/10] a unit test that validates the implementation of the relaxation (board is not square)
- [6/10] a unit test that validates that a board is suitabile for the given number of players
- [6/10] a unit that that rejects a board as too small for the given number of players
- For the last two tests, acknowledged on self-eval that you do not have it, so 60% credit

## DESIGN

[15/15]

- [5/5] for ranking all three tasks with one of 1, 2, 3, 4, 5
- [5/5] for using complete sentences for each explanation
- [5/5] for delivering acceptable explanations
