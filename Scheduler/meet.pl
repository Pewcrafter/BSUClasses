%Credit to Bryant for assistance with some syntax and basic ideas.

%#!/bin/gprolog --consult-file

:- include('data.pl').
:- include('uniq.pl').


lte(time(H1, _),  time(H2, _))  :- H1 < H2.

lte(time(H, M1), time(H, M2)) :- M1 =< M2.

% 3 predicates needed
% potential meeting time within meeting time entirely

intersection(slot(PMstart,PMend),slot(Mstart,Mend)) :- 
free(_,slot(PMstart, PMend)), free(_,slot(Mstart,Mend)),
lte(PMstart,Mstart), lte(Mend,PMend),
Mstart \==Mend.

% potential meeting time before meeting time

intersection(slot(PMstart,PMend),slot(Mstart,PMend)) :- 
free(_,slot(PMstart, PMend)), free(_,slot(Mstart,Mend)),
lte(PMstart,Mstart), lte(Mstart, PMend), lte(PMend,Mend),
Mstart \==PMend.

% potential meeting time after meeting time

intersection(slot(PMstart,PMend),slot(PMstart,Mend)) :- 
free(_,slot(PMstart, PMend)), free(_,slot(Mstart,Mend)),
lte(Mstart,PMstart), lte(PMstart, Mend), lte(Mend,PMend),
PMstart \==Mend.

meetone(Person, freeSlot) :-
free(Person, newSlot), 
intersection(newSlot, freeSlot).

meetall([Head|Tail], Slot) :- 
meetone(Head, Slot), meetall(Tail, Slot).
meetall([], _).

meet(Slot) :- people(P), meetall(P, Slot).

people([ann,bob,carla]).

main :- findall(Slot, meet(Slot), Slots),
        uniq(Slots, Uniq),
        write(Uniq), nl, halt.

:- initialization(main).
