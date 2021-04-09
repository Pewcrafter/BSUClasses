/**
 * Name: Michael Stoneman
 * Email: michaelstoneman@u.boisestate.edu
 * Github Username: Pewcrafter
 */
#include "lab.h"
#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <pthread.h>

//function prototypes
void serial_mergesort(int A[], int p, int r);
void parallel_mergesort(int[],int,int);
void merge(int A[], int p, int q, int r);
void insertion_sort(int A[], int p, int r);
void* serial_mergesort_helper(void*);
struct thread_data* init_thread_data(int[], int, int);


struct thread_data { //Structure to pass data to pthread calls
	int* A;
	int p;
	int r;
};


/*
 * insertion_sort(int A[], int p, int r):
 *
 * description: Sort the section of the array A[p..r].
 */
void insertion_sort(int A[], int p, int r)
{
	int j;

	for (j=p+1; j<=r; j++) {
		int key = A[j];
		int i = j-1;
		while ((i > p-1) && (A[i] > key)) {
			A[i+1] = A[i];
			i--;
		}
		A[i+1] = key;
	}
}


/*
 * serial_mergesort(int A[], int p, int r):
 *
 * description: Sort the section of the array A[p..r].
 */
void serial_mergesort(int A[], int p, int r)
{
	if (r-p+1 <= INSERTION_SORT_THRESHOLD)  {
			insertion_sort(A,p,r);
	} else {
		int q =  r + (p - r)/2;
		serial_mergesort(A, p, q);
		serial_mergesort(A, q+1, r);
		merge(A, p, q, r);
	}
}


/*
 * merge(int A[], int p, int q, int r):
 *
 * description: Merge two sorted sequences A[p..q] and A[q+1..r]
 *              and place merged output back in array A. Uses extra
 *              space proportional to A[p..r].
 */
void merge(int A[], int p, int q, int r)
{
	int *B = (int *) malloc(sizeof(int) * (r-p+1));

	int i = p;
	int j = q+1;
	int k = 0;
	int l;

	// as long as both lists have unexamined elements
	// this loop keeps executing.
	while ((i <= q) && (j <= r)) {
		if (A[i] < A[j]) {
			B[k] = A[i];
			i++;
		} else {
			B[k] = A[j];
			j++;
		}
		k++;
	}

	// now only at most one list has unprocessed elements.

	if (i <= q) {
		// copy remaining elements from the first list
		for (l=i; l<=q; l++) {
			B[k] = A[l];
			k++;
		}
	} else {
		// copy remaining elements from the second list
		for (l=j; l<=r; l++) {
			B[k] = A[l];
			k++;
		}
	}

	// copy merged output from array B back to array A
	k=0;
	for (l=p; l<=r; l++) {
		A[l] = B[k];
		k++;
	}

	free(B);
}

/* Can't pass the correct arguments to serial mergesort with pthread, so i made this helper to do it for me
 *
 */
 void* serial_mergesort_helper(void* thread_info)
 {
	//Get the data for this thread
 	 int* A = ((struct thread_data*)thread_info)->A;
	 int p = ((struct thread_data*)thread_info)->p;
	 int r = ((struct thread_data*)thread_info)->r;
	 //call serial mergesort
	 serial_mergesort(A, p, r);
   
   
   return NULL; //required to get rid of a warning
 }
 
 /* initializes the thread_data structure to pass into pthread
 */
 struct thread_data* init_thread_data(int A[], int p, int r)
 {
    struct thread_data* thread_info = (struct thread_data*)malloc(sizeof(struct thread_data));
    thread_info->A = A;
    thread_info->p = p;
    thread_info->r = r;
    return thread_info;
 }

/* parallel_mergesort(int A[], int r, int numThread)
 *
 * description: uses numThread threads to perform mergesort by splitting the original array into numThread parts
 * and calling serial mergesort on each of them. You could also take a recursive approach, but I found this to be
 * very easy!
 */

void parallel_mergesort(int A[], int r, int numThread)
{
	int numEle = r/numThread; //Number of elements per thread
  int i;
  int startIndex;
  int endIndex;
  int q;
  pthread_t* threads = (pthread_t*)malloc(sizeof(pthread_t)*numThread); //Array of our threads we will create
	struct thread_data** args = (struct thread_data**)malloc(sizeof(struct thread_data*)* numThread); 
 
	for (i=0; i < numThread; i++)
	{
		startIndex = i*numEle; //Where do we start based on which thread we are on
		endIndex = startIndex + numEle -1; //where to end based on size and where we began this thread

		if(endIndex > r || (endIndex < r && i == numThread -1)) //the last thread may have one less or one more element
			endIndex = r;

		args[i] = init_thread_data(A, startIndex, endIndex);
		pthread_create(&threads[i],NULL, serial_mergesort_helper ,(void*)args[i]); //make a new thread, calling serialmergesort
	}

	for(i=0; i<numThread; i++)
	{
		pthread_join(threads[i],NULL); //join all of our threads
	}

	for(i=0; i<numThread; i++)
	{
		if (i < numThread - 1)
		{
			endIndex = args[i+1]->r;
			q = args[i]->r;

			merge(A,1,q,endIndex); //merge all our threads!
		}
		
		free(args[i]);
	}
	free(args);
	free(threads);
}