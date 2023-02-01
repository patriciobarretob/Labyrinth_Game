# **TODO:**
1. Make Json deserialization/serialization more strict with primitives -> search for primitives
5. Work on referee.java regarding return types in taking turns, atomicity, and fields. Potentially a full overhaul.  
6. Remove parallel data structures such as InfoMap in RefereePlayerState (colors used as primary key) and PlayerMap inside Referee color to IPlayer.  
7. Removing all mutability from codebase, such as Board and State.  
8. Abstract all other rule related functionality into LabyrinthRules
9. Split LabyrinthRules into interfaces  
10. Enhancing defensive programming such as in class constructors.  
11. Make all methods either composite or atomic.  
12. Turn our JsonRepresentation classes into JsonDeserializers.  
13. Abstract RiemannStrategy & EuclidStrategy tests.  
14. Create class for Rotations, create JsonSerializer/deserializer for it.  

# **Completed:**
1. Abstract any board rule related functionality into the LabyrinthRules class.
    * Commit: dafbd269b29a3d2ee2f6b3e88c465815dfdac781
    * Message: "Tech debt items 1 & 2"
2. Required revision: Allow board size flexibility by accomodating rectangular and larger/smaller sizes.  
    * Commit: dafbd269b29a3d2ee2f6b3e88c465815dfdac781
    * Message: "Tech debt items 1 & 2"

3. Required revision: Enforce home tiles being distinct from each other. 
    * Commit: b1bc56727949da0d9418ebde8ee52aa1ac6e6e87
    * Message: "Tech debt item 3"
    * Commit: 89a2849feb614f66f456a96ed0cd0e86f89cbb1e
    * Message: "Tech debt item 3 part 2"
4. Protecting against unexpected player call results in Referee. 
    * Commit: 0a26008578acbd30f6b4531112df08dfb9211295
    * Message: "Tech debt item 4"
