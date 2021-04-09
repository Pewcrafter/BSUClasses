# Project 1: Deterministic Finite Automata

* Author: Joe Gibson and Michael Stoneman
* Class: CS361 Section 002
* Semester: Fall 2020

## Overview

This Java program  models a dterministic finite automaton. 

## Compiling and Using

To compile, use Java to compile DFADriver.java using the command:

$ javac DFADriver.java

Supply a .txt file that contains a properly structured DFA specification as modeled here: 

G D // Final States on line 1
A   // Start States on line 2
B C E F // Normal States on line 3
A1B A2C B1D B2E C1F C2G D1D D2E E1D E2E F1F F2G G1F G2G // Transitions on line 4
121212121 //Sample Input String on all further lines
12221212121 //Sample Input String
12 //Sample Input String
2 //Sample Input String
1212 //Sample Input String

## Discussion

Joe and I managed to get a simple skeleton of the project up and running in less than an hour. We powered through all of the methods and had a fully functioning DFA within about an hour and a half.
We spent the remainder of our time attempting to get the complement function working as intended. We thought we were making errors with a mismatch of objects, but it ended up being a simple ordering 
problem within our code that caused on issue on the edge case of the startState being a final state. Luckily, Professor Sherman found the error pretty quickly and got us pointed in the right direction.
We had a few other minor logical errors within our code, but they were all caught and fixed within about 20 minutes - an hour each. 

Joe and I spent very little time in researching the various uses of Sets/Maps as we both instantly knew to use a linked HashMap and a linked Set to maintain the order in which our states/transitions 
were added. This proved to be the right call as implementing these data structures was straightforward and offered great structure and flow to our program.


## Testing


We tested our project with FA's of varying complexity and edge cases through
the 3 p1tc tests. Tests such as passing in an empty line of states and e values for letters
helped ensure our program what properly handling the cases that were expected of it. 


## Sources used

We studied HashMap from the JavaDocs library as a datamodel for use. 
https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html
----------
