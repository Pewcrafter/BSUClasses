#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "smash.h"
#include "history.h"
#include <signal.h>
#define MAXLINE 4096

void sighandler(int sig_num)
{
	signal(SIGINT, sighandler);
	printf("\n");
	fputs("$ ", stderr);
}

int main(){
	char bfr[MAXLINE];
	fputs("$ ",stdout);
	init_history();
	signal(SIGINT, sighandler);
	while (fgets(bfr, MAXLINE, stdin) != NULL){
		bfr[strlen(bfr)-1] = '\0';
		executeCommand(bfr);
	}
return 0;
}

