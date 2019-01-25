Harry Ross (hgr8)  
DESIGN.md

High-level design goals:
-------------------------------------
- For this project, the goal was to create a functional game of Breakout with
  added features to enhance gameplay.
- As this is the first piece of larger software I've written, a personal goal
  was to implement classes and methods in a sensible way that would be expandable
  and flexible.
 
Adding new features:
-------------------------------------
 - If the new feature does not fall within the scope of the classes already 
  defined, a new class can be created. This object should be instantiated within
  the main game class (Breakout.java) so that its methods and variables can be 
  accessed as the game flow occurs. If this object must be drawn or updated throughout
  the game, try to generalize pre-existing methods to achieve these purposes since
  it is likely that the conditions required for the actions of the new object to occur
  have already been accounted for previously.
  - If a feature is being added to a specific 
  type of object, it can usually be written as a method of that class with new
  instance variables being added as required by the new design choice.
  
Justifying design choices:
-------------------------------------
- If any game concept was a distinct element with unique characteristics, I chose
  to create object classes for them. This meant that Ball, Paddle, Block, 
  Powerup, and Enemy were all created as individual classes. This design allowed 
  for flexibility and modularity in the number of a certain object that
  could exist in the game space at a given point in time. It was important to me
  that the objects I created could be instantiated and modified in the main game class
  at any point with no questions asked. 
- When I was able to, I tried to redefine methods more abstractly to avoid
  duplication for objects with similar behaviors. For example, I generalized my
  original block collision code to be used for the paddle as well, seeing as
  both are rectangles interacting with a ball. 
- If I could start the project from scratch, I would have used more effective abstraction
  to better condense my methods across classes. I did not feel comfortable
  taking large steps in that direction at the time I wrote the bulk of the
  program since it had yet to be focused on in class.
- There were skills I learned in JavaFX as the project progressed that I wish I
  had known when I started, as they would have effected how I structured
  certain aspects of the GUI.