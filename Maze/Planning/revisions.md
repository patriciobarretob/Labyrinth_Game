# **CHANGES:**
1. Changed activePlayerWon in LabyrinthRules to activePlayerEndedGame
2. Abstracted out Referee functionality same for single
    and multiple goal games as AReferee
3. Abstracted RefereePlayer and RefereeState into abstract 
    versions and single goal versions.
4. Renamed old Referee to SingleGoalReferee
5. Created new referee called MultiGoalReferee, the referee
    that will handle assigning multiple goals.