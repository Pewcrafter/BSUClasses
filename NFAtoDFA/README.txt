# Project 2: Nondeterministic Finite Automata

* Author: Joe Gibson, Michael Stoneman
* Class: CS361 Section 002
* Semester: Fall 2020

## Overview

The program configures and constructs a Nondeterministic Finite Automata. 
The program also converts a Nondeterministic Finite Automata into a 
Deterministic Finite Automata if desired. Both can then navigate through
their respective structures.

## Compiling and Using

This section should tell the user how to compile your code.  It is
also appropriate to instruct the user how to use your code. Does your
program require user input? If so, what does your user need to know
about it to use it as quickly as possible?

To compile, ensure that Java is install on your system. One must then run the NFADriver 
while passing in a text file that is formatted as follows:

*names/labels of final states*
*name/label of start state*
*remaining states*
*transitions that are space separated and are represented by 3 symbols of abc where
	a is from state
	b is transition symbol from alphabet
	c is to state*
*every additional line will be treated as a string to be tested on the DFA to test acceptance.*

Every *text* above will exist on its own line. 

## Discussion

The three biggest problems we had were selecting the correct data structure to use to store the NFA,
converting from a DFA to an NFA, and finding the solution to a bug we had that concerned calling
create DFA twice in NFADriver.java

Creating our first implementation of the program was not too difficult as we used many resources from 
our DFA implementation and just adapted them to the NFA. The most time consuming portion was definitely
finding the remedy to small bugs and needed to step through one line at a time in debugging while tracking
our FA's. While time consuming, the debugger in eclipse aided in realizing that we weren't fully resetting 
when creating a new DFA from an NFA. This caused for incomplete DFA's to be made, but that has been remedied. 

The project is very applicable towards our future learning and development as it forced us to handle multiple
data structures and manage the conversion of one FA to another type of FA. This wasn't simple to do, but we
believe we accomplished what we set out to do. 

## Testing

We used the 5 provided tests to ensure our program met the specifications. In addition,
we added warnings and breakpoints throughout while testing to ensure our program was 
correct on every step of running. These were obviously removed prior to submission. 


----------