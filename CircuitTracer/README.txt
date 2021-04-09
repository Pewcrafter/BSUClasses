****************
* Project 3: CircuitTracer
* CS 221
* Date 12/5/18
* Michael Stoneman
**************** 

OVERVIEW:

 This program takes in a file input depicting a specified grid and finds the shortest paths from point 1
 to point 2 using the specified storage type (stack or queue).

KAWALSKI, ANALYSIS:

For this analysis, assume that the search algorithm takes a specific point on the grid and then tests for open 
positions starting directly above the current point, and proceeding clockwise. Should the position be open, a new
TraceState is created for that position and this repeats until all possible paths have been tested.

1. The choice between using a stack or a queue with the given algorithm above is that a stack operates in a LIFO
manner where a queue acts in a TIFO manner. This will cause the stack to begin its search from the far left of each
point and moving in a counter-clockwise manner, whereas a queue will begin its search just as the algorithm does.

2. The total number of search states is not reliant on the choice of storage type used. Every path will be checked
no matter the storage choice.

3. If we had a perfectly symmetrical board such as the one given in the "Tasks" section, both structures would
have the same memory use. The only time they differ is when one "side" of the grid has less possible paths than the 
other. This would cause the structure associated with that "side" to have more TraceStates at a given time.

4. The big-O runtime of the search algorithm is O(2^n). This is almost exclusively due to the size of the board.
For each row or column we add, we have to test each position within that new row/column through every means of 
getting to that new column. This causes the 2^n effect. 

5. The speed at which a storage structure finds a solution is only dependent on the board layout. Sometimes
the board will favor a right-handed approach benefitting a queue, and other times it will benefit a left-handed
approach and will benefit a stack. Neither way will always be better.

6. Neither storage structures guarantee that the first solution found will be the shortest path, if that were the
case we wouldn't need to test any other paths other than the first!


INCLUDED FILES:

TraceState.java - used by my CircuitBoard.java class to store the paths
Storage.java - used to create and manage the storage type specified
OccupiedPoisitionException.java - thrown when you attempt to make a new TraceState on filled position
InvalidFileFormatException.java - allows me to throw a specific error message to the user when input it incorrect
CircuitTracer.java - Traces a given grid and finds the shortest paths (the main program)
CircuitBoard.java - constructs and manages the board as paths are found

COMPILING AND RUNNING:

 From the directory containing all source files, compile the
 driver class (and all dependencies) with the command:
 $ javac CircuitTracer.java

 Run the compiled class file with the command (without the square brackets, choosing between -q or -s):
 $ java CircuitTracer [-q] or [-s] [-c] [filename]


 Console output will give the results after the program finishes.


PROGRAM DESIGN AND IMPORTANT CONCEPTS:

I followed the given pseudo code for the majority of this project. The idea was that you would create a path for
each open position around the starting position and add that path to your storage. You would then exhaustively
take paths from that storage and generate new paths for each open position around your current one until your
storage was empty. While that was going on, each time the path became "complete" and we had made it from point 1
to point 2, we would test the length of that completed path. Should that completed path be the shortest path we
have found so far, we would clear all previous paths within our "bestpath" list and add the current path in. Should
the current path be tied for the shortest, we would simply add it in to the "bestpath" list.

This program had 4 classes working in unison to aid in the development and effeciency of the path search and 
creation. TraceState.java fulfills the purpose of managing the current path and associating a specific CircuitBoard
to each path. It also handles the completion element of the program, by having a built in test to see if the path
has reached point 2. Storage.java fulfills the need for some type of universal storage. It is written so that
I can use it as either a Stack or a Queue, and it operates correctly both ways. CircuitBoard.java handles the 
creation of the original grid/circuit as specififed by the input file and has methods to allow changes to be made
to the board so that it can depict the current path being followed with "T"'s. It also has predesigned 
encapsulation so that the original board may remain while we edit the copies for each TraceState. The final
class is the main one called CircuitTracer.java. It handles the parsing of the command line, the intialization of
all the previous classes to match the inputs given from the file that was parses, and the actual tracing. It
utilizes all of the previous classes listed in their respective ways to reach a final "bestpaths" list and
output that to the console for you!

TESTING:

To test my program I used all of the given "valid" and "invalid" files through CircuitTracerTester. This tested 
every possible issue with the given file and ensured that my program exited properly and supplied a meaningful
message to the user. 

My program properly handled and issues where:
The input file had an unaccepted character
The input file had too many rows
The input file had too few rows
The input file had too many columns
The input file had too few columns
The input file did not have a starting point
The input file had more than one starting point
The input file did not have an ending point
The input file had more than one ending point
The input file had too few characters in the first row
The input file had too many characters in the first row 

DISCUSSION:
 
Thanks to the pseudo code given in the project outline, this really wasn't too demanding. I will admit that I ran
into many issues where I swapped my "x" and "y" coordinates in the beginning, because I failed to think of my 
"row" as the current row, not where I was at on the given row (x-axis). This led to some early issues, but 
once I finally got that into my head the project went very smoothly. I ran in to another issue wwith my output
failing even though it was correct, but that ended up being an issue with Eclipse rather than my program. 

This project wasn't particularly challenging, but I did struggle a bit on the "x" and "y" coordinate issue. 
That problem really caused me to rethink my naming convention and I believe made my program much easier to follow.
 