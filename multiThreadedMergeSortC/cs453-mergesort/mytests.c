#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/times.h> /* for times system call */
#include <sys/time.h>  /* for gettimeofday system call */
#include <unistd.h>
#include <error.h>
#include <pthread.h>
#include "lab.h"

// function prototypes
int check_if_sorted(int A[], int n);
void generate_random_array(int A[], int n, int seed);
void serial_mergesort(int A[], int p, int r);
void parallel_mergesort(int[],int,int);
double getMilliSeconds(void);


/*
---------------------------------------------------------------------------
clock_t times(struct tms *buffer);
times() fills the structure pointed to by buffer with
time-accounting information.  The structure defined in
<sys/times.h> is as follows:
struct tms {
    clock_t tms_utime;       user time
    clock_t tms_stime;       system time
    clock_t tms_cutime;      user time, children
    clock_t tms_cstime;      system time, children
The time is given in units of 1/CLK_TCK seconds where the
value of CLK_TCK can be determined using the sysconf() function
with the agrgument _SC_CLK_TCK.
---------------------------------------------------------------------------
*/


float report_cpu_time(void)
{
	struct tms buffer;
	float cputime;

	times(&buffer);
	cputime = (buffer.tms_utime)/ (float) sysconf(_SC_CLK_TCK);
	return (cputime);
}


float report_sys_time(void)
{
	struct tms buffer;
	float systime;

	times(&buffer);
	systime = (buffer.tms_stime)/ (float) sysconf(_SC_CLK_TCK);
	return (systime);
}

double getMilliSeconds(void)
{
    struct timeval now;
    gettimeofday(&now, (struct timezone *)0);
    return (double) now.tv_sec*1000.0 + now.tv_usec/1000.0;
}


/*
 * generate_random_array(int A[], int n, int seed):
 *
 * description: Generate random integers in the range [0,RANGE]
 *              and store in A[1..n]
 */

#define RANGE 1000000

void generate_random_array(int A[], int n, int seed)
{
    int i;

	srandom(seed);
    for (i=1; i<=n; i++)
        A[i] = random()%RANGE;
}


/*
 * check_if_sorted(int A[], int n):
 *
 * description: returns TRUE if A[1..n] are sorted in nondecreasing order
 *              otherwise returns FALSE
 */

int check_if_sorted(int A[], int n)
{
	int i=0;

	for (i=1; i<n; i++) {
		if (A[i] > A[i+1]) {
			return FALSE;
		}
	}
	return TRUE;
}




int main(int argc, char **argv) {
	if (argc < 3) { // there must be at least one command-line argument
			fprintf(stderr, "Usage: %s <input size> <# of threads> [<seed>] \n", argv[0]);
			exit(1);
	}

	int n = atoi(argv[1]);
	int seed = 1; //Seed if none is provided
	if (argc == 4)
		seed = atoi(argv[3]);

	int *A = (int *) malloc(sizeof(int) * (n+1)); // n+1 since we are using A[1]..A[n]
	int numThreads = atoi(argv[2]); //grab the number of threads the user would like to use

	// generate random input

	generate_random_array(A,n, seed);

	double start_time;
	double serial_start_time;
	double sorting_time;
	double serial_sorting_time;
	int *A_copy = (int *) malloc(sizeof(int) * (n+1));
  memcpy(A_copy, A, n); //Copy the same array to properly time against different merge sort methods

	// sort the input with parallel sort (and time it)
	start_time = getMilliSeconds();
	parallel_mergesort(A,n,numThreads);
	sorting_time = getMilliSeconds() - start_time;

  // sort the input with serial sort (and time it)
	serial_start_time = getMilliSeconds();
	serial_mergesort(A_copy,1,n);
	serial_sorting_time = getMilliSeconds() - serial_start_time;

	// print results if correctly sorted otherwise cry foul and exit
	if (check_if_sorted(A,n)) {
		printf("Sorting %d elements took %4.2lf seconds using parallel mergesort. \n", n,  sorting_time/1000.0);
	} else {
		printf("%s: sorting failed!!!!\n", argv[0]);
		exit(EXIT_FAILURE);
	}
	if (check_if_sorted(A_copy,n)) {
		printf("Sorting %d elements took %4.2lf seconds using serial mergesort.\n", n,  serial_sorting_time/1000.0);
	} else {
		printf("%s: sorting failed!!!!\n", argv[0]);
		exit(EXIT_FAILURE);
	}
	free(A);
	free(A_copy);

	exit(EXIT_SUCCESS);
}
