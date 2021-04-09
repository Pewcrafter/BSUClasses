#------------------------------------------------------------------------------
#
# Makefile --- Re-usable makefile for building many simple projects
#
# Usage
#	make all	Build the released product
#	make clean	Remove build artifacts
#
# Author(s)
# 01/11/18 Epoch............................................................jrc
#
#------------------------------------------------------------------------------ 
OBJS = history.o commands.o smash.o
CFLAGS = -Wall -std=c99

%.o : %.c
	gcc $(CFLAGS) -c -o $@ $<

all: smash

smashLib.a : $(OBJS)
	ar r $@ $?

smash: smash.o smashLib.a
	gcc -o $@ $^

clean:
	rm -f *.o *.a smash *~
