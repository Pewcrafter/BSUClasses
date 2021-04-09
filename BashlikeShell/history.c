#define _SVID_SOURCE
#include "history.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

static struct Cmd history[MAXHISTORY];
int count = 0;

void init_history(void){
	}

void add_history(char *cmd, int exitStatus){
if (count <10){
	history[count].cmd = strdup(cmd);
	history[count].exitStatus = exitStatus;
	count++;
	}
else {
	int z;
	for (z=0; z<9; z++){
		strcpy(history[z].cmd, history[z+1].cmd);
		history[z].exitStatus = history[z+1].exitStatus;
		}
	strcpy(history[9].cmd, cmd);
	history[9].exitStatus = exitStatus;
	}
}

void clear_history(void){
int z;
for (z=0;z<10;z++){
	free(history[z].cmd);
	}
}

void print_history(int firstSequenceNumber){
int z;
if (history[0].cmd != NULL){
	fprintf(stdout, "%d	[%d]	%s", firstSequenceNumber, history[0].exitStatus, history[0].cmd);
	for(z=1; z<10; ++z){
		if (history[z].cmd != NULL){
			int output = firstSequenceNumber+z;
			fprintf(stdout, "\n%d	[%d]	%s", output, history[z].exitStatus, history[z].cmd);
			}
		}
	}
}
	
