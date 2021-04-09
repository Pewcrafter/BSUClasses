# PROJECT 2: Thread-safe Library 

* Author: Michael Stoneman
* Class: CS453

## Overview

pc acts as a thread-safe version of the given linked-list library that offers access and
editing of critical sections without any chance of a deadlock. 

## Building and compiling

To build this code, first navigate into the base folder (P2) and types the following:

$ export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:./list/lib:./lib

After typing this, type:

$ make

The code is now fully compiled and able to be ran

## Running the program

To run the program, first follow the steps outlined in ##Building and compiling. Once
you have a fully compiled program, simply type:

$ ./pc <poolsize> <#items/producer> <#producers> <#consumers> <sleep interval(microsecs)>

## Testing
I tested using test-pc.sh as well as two much larger tests. I then removed the output statements
to see if it changed anything, it did not. 

## My work
Most of my time was spent reading the given man pages. Once I learned how to use 
wait/broadcast and learned the various flags, this project was just a matter of 
impleneting an interface. I didn't struggle much on the locking/unlocking as it made
quite a bit of sense (I hope). I believe I have a useless lock in the "setCapacity" method
It's currently implemented with locks in place as I was worried about multiple threads
trying to change the capacity, but I realize now that the threads don't edit the capacity
anyways, only the original constructor does. I'll fix this issue before I commit.

I spent a lot of time fixing compiler warnings as I wasn't referencing pointers correctly,
but I got those sorted out after i realized it was a simple (tsbList->list) rather than (list)
to actually get the list rather than the entire structure. Honestly, a rookie mistake that 
took me much longer than it should have to figure out, but I got it compiling with no errors
nonetheless. Overall, this project was pretty easy once you figured out how to lock/unlock and
wait!
