#include "smash.h"
#include "history.h"
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>


int cmdCount = 0;
void executeExternalCommand(char *str){
char fullStr[4096];
strncpy(fullStr, str, 4096);
int realExitStatus = 127;
int exitStatus = 0;
pid_t pid;
int tokCount = 0;
int fileIn=0, fileOut=0;
char *input;
char *output;
char *cmds[1024];
char *token = strtok(str, " \n");
while(token != NULL)
{
	//Finding the potential redirection for stdin/stdout
	char* tokenStr = strstr(token, "<");
	int workAround = 2;
	if(tokenStr != NULL)
	{
		fileIn = 2;
		token += 1;
		input = token;
		tokCount++;
		token = strtok(NULL, " \n");
		workAround = 0;
	}
	if (token != NULL)
	{
		tokenStr = strstr(token, ">");
	}
	if (tokenStr != NULL)
	{
		fileOut = 2;
		token += 1;
		output = token;
		tokCount++;
		token = strtok(NULL, " \n");
	}
	else if (workAround)
	{
		cmds[tokCount] = token;
		tokCount++;
		token = strtok(NULL, " \n");
	}
}
if ((pid = fork()) == 0){
	if (fileIn)
	{
		int fDesc0 = open(input, O_RDONLY);
		dup2(fDesc0, STDIN_FILENO);
		close(fDesc0);
	}
	if(fileOut)
	{
		int fDesc1 = creat(output, 0644);
		dup2(fDesc1, STDOUT_FILENO);
		close(fDesc1);
	}
	cmds[tokCount] = NULL;
	execvp(cmds[0], cmds);
	exit(127);
	}
if ((pid = wait(&exitStatus)) == -1){
	exit(127);
	}
else if (WIFEXITED(exitStatus)){
	realExitStatus = WEXITSTATUS(exitStatus);
	}
add_history(fullStr,realExitStatus);
fprintf(stderr, "PID %d exited, status = %d \n", pid, realExitStatus);
fprintf(stdout, "$ ");
}

void executeCommand(char *str){
char fullStr[4096];
strncpy(fullStr, str, 4096);
char* token = strtok(str, " ");
if (token == NULL){
	fprintf(stdout, "$ ");
	return;
	}
if (strcmp(token, "exit") == 0){
	clear_history();
	exit(0);
	}
if (strcmp(token, "cd") == 0){
	cmdCount++;
	token = strtok(NULL, " ");
	if (chdir(token) == -1){
		fprintf(stderr, "%s: No such file or directory\n$ ", token);
		add_history(fullStr, 1);
		return;
		}
	chdir(token);
	add_history(fullStr, 0);
	fprintf(stdout, "%s\n$ ", token);
	return;
	}
if (strcmp(token, "history") == 0){
	if (cmdCount < 10){
		print_history(1);
		}
	else{
		print_history(cmdCount-9);
	}
	cmdCount++;
	add_history("history", 0);
	fprintf(stdout, "\n$ ");
	return;
	}  
executeExternalCommand(fullStr);
cmdCount++;
return;
}

