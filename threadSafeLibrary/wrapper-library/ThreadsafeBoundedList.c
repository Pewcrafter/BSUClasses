
/**
*	ThreadsafeBoundedList.c: Implements the methods of ThreadsafeBoundedList.h to wrap a DLL library in a threadsafe manner. I* chose to leave all of the description given in ThreadsafeBoundedList.h to help myself code and offer increased readability
*/
#include <stdio.h>
#include <pthread.h>
#include "ThreadsafeBoundedList.h"

struct tsb_list {
    struct list *list;
    int capacity;
    Boolean stop_requested;
    pthread_mutex_t mutex;
    pthread_cond_t wakeProd;
    pthread_cond_t wakeCons;
};

/**
 * Constructor: Allocates a new listist object and initializes its members. Initialize
 * the mutex and condition variables associated with the bounded list monitor. Set the
 * capacity of the list.
 *
 * @return a pointer to the allocated list.
 */
struct tsb_list * tsb_createList(int (*equals)(const void *, const void *),
                   char * (*toString)(const void *),
                   void (*freeObject)(void *),
				   int capacity){
    struct tsb_list *tsbList = malloc(sizeof(struct tsb_list));
    tsbList->list = createList(equals ,toString, freeObject);
    tsbList->capacity = capacity;
    tsbList->stop_requested=FALSE;
    pthread_mutex_init(&tsbList->mutex,NULL);
    pthread_cond_init(&tsbList->wakeProd,NULL);
    pthread_cond_init(&tsbList->wakeCons,NULL);
    return tsbList;        
	}


/**
 * Frees all elements of the given list and the <code>struct *list</code> itself.
 * Does nothing if list is <code>NULL</code>. Also frees the associated mutex and
 * condition varibles and the wrapper structure.
 *
 * @param list a pointer to a <code>List</code>.
 */
void tsb_freeList(struct tsb_list * tsbList){ /* destructor */
    pthread_mutex_lock(&tsbList->mutex);  //Get the lock
    freeList(tsbList->list);   //Free the list
    pthread_mutex_unlock(&tsbList->mutex);    //Release the lock
    }

/**
 * Returns the size of the given list.
 *
 * No lock needed to get the size of the list
 *
 * @param list a pointer to a <code>List</code>.
 * @return The current size of the list.
 */
int tsb_getSize(struct tsb_list * tsbList){
     return getSize(tsbList->list);
}

/**
 * Returns the maximum capacity of the given list.
 *
 * No lock needed to get the capacity of the list
 *
 * @param list a pointer to a <code>List</code>.
 * @return The macimum capacity of the list.
 */
int tsb_getCapacity(struct tsb_list * tsbList){
    return tsbList->capacity;
}

/**
 * Sets the maximum capacity of the given list.
 *
 * I'm not sure if the lock is needed, but I was worried about a potential race condition where the capacity changes at the wrong time.
 * No issues were found with this lock in place, so I figured it's okay.'
 *
 * @param list a pointer to a <code>List</code>.
 * @param capacity the maximum allowed capacity of the list
 * @return none
 */
void tsb_setCapacity(struct tsb_list * tsbList, int capacity){
    tsbList->capacity = capacity;
}

/**
 * Checks if the list is empty.
 *
 * No lock necessary
 *
 * @param  list a pointer to a <code>List</code>.
 * @return true if the list is empty; false otherwise.
 */
Boolean tsb_isEmpty(struct tsb_list * tsbList){
    if(tsb_getSize(tsbList) == 0){
        return 1;
    }else{
        return 0;
    }
}

/**
 * Checks if the list is full.
 *
 * No lock necessary
 *
 * @param  list a pointer to a <code>List</code>.
 * @return true if the list is full to capacity; false otherwise.
 */
Boolean tsb_isFull(struct tsb_list * tsbList){
    if(getSize(tsbList->list) == tsbList->capacity){
        return 1;
    }else{
        return 0;
    }
}

/**
 * Adds a node to the front of the list. After this method is called,
 * the given node will be the head of the list. (Node must be allocated
 * before it is passed to this function.) If the list and/or node are NULL,
 * the function will do nothing and return.
 *
 * @param list a pointer to a <code>List</code>.
 * @param node a pointer to the node to add.
 */
void tsb_addAtFront(struct tsb_list * tsbList, NodePtr node){
    pthread_mutex_lock(&tsbList->mutex);   //Get lock
    struct node* tempNode = createNode(node);
    addAtFront(tsbList->list, tempNode);
    pthread_cond_broadcast(&tsbList->wakeCons);  //Signal to all consumers
    pthread_mutex_unlock(&tsbList->mutex);    //Release lock
}

/**
 * Adds a node to the rear of the list. After this method is called,
 * the given node will be the tail of the list. (Node must be allocated
 * before it is passed to this function.) If the list and/or node are NULL,
 * the function will do nothing and return.
 *
 * @param list a pointer to a <code>List</code>.
 * @param node a pointer to the node to add.
 */
void tsb_addAtRear(struct tsb_list * tsbList, NodePtr node){
    pthread_mutex_lock(&tsbList->mutex);  //Get the lock
    struct node* tempNode = createNode(node);
    addAtRear(tsbList->list, tempNode);
    pthread_cond_broadcast(&tsbList->wakeCons);  //Signal to all consumers
    pthread_mutex_unlock(&tsbList->mutex);   //Release the lock
}

/**
 * Removes the node from the front of the list (the head node) and returns
 * a pointer to the node that was removed. If the list is NULL or empty,
 * the function will do nothing and return NULL.
 *
 * @param list a pointer to a <code>List</code>.
 * @return a pointer to the node that was removed.
 */
NodePtr tsb_removeFront(struct tsb_list * tsbList){
    struct node* removedNode;
    pthread_mutex_lock(&tsbList->mutex);  //Get the lock
    while(tsb_isEmpty(tsbList)){
	if(!tsbList->stop_requested){
            pthread_cond_wait(&(tsbList->wakeCons), &tsbList->mutex);  //Wait until there's something to remove
	}else{
	    pthread_mutex_unlock(&tsbList->mutex);  //List is empty, nothing to do, release the lock
	    return 0;
	}
    }
    removedNode = removeFront(tsbList->list);
    pthread_cond_broadcast(&tsbList->wakeProd);  // Signal to wake producers
    pthread_mutex_unlock(&tsbList->mutex);	  // Release the lock
    return removedNode;
}

/**
 * Removes the node from the rear of the list (the tail node) and returns
 * a pointer to the node that was removed. If the list is NULL or empty,
 * the function will do nothing and return NULL.
 *
 * @param list a pointer to a <code>List</code>.
 * @return a pointer to the node that was removed.
 */
NodePtr tsb_removeRear(struct tsb_list * tsbList){
    struct node* removedNode;
    pthread_mutex_lock(&tsbList->mutex);  //Get the lock
    while(tsb_isEmpty(tsbList)){
        if(!tsbList->stop_requested){
            pthread_cond_wait(&(tsbList->wakeCons), &tsbList->mutex); //Wait until there's something to remove
	}else{
            pthread_mutex_unlock(&tsbList->mutex);  //List is empty, release the lock
	    return 0;
	}
    }
    removedNode = removeRear(tsbList->list);
    pthread_cond_broadcast(&tsbList->wakeProd);  //Signal to wake producers
    pthread_mutex_unlock(&tsbList->mutex);	  //Release the lock
    return removedNode;
}

/**
 * Removes the node pointed to by the given node pointer from the list and returns
 * the pointer to it. Assumes that the node is a valid node in the list. If the node
 * pointer is NULL, the function will do nothing and return NULL.
 *
 * @param list a pointer to a <code>List</code>.
 * @param node a pointer to the node to remove.
 * @return a pointer to the node that was removed.
 */
NodePtr tsb_removeNode(struct tsb_list * tsbList, NodePtr node){
    struct node* removedNode;
    pthread_mutex_lock(&tsbList->mutex);  //Get the lock
    while(tsb_isEmpty(tsbList)){
	if(!tsbList->stop_requested){
 	    pthread_cond_wait(&(tsbList->wakeCons), &tsbList->mutex);  //Wait until theres something to remove
	}else{
	    pthread_mutex_unlock(&tsbList->mutex);  //List is empty, release the lock
	    return 0;
	}
    }
    removedNode = removeNode(tsbList->list, node);
    pthread_cond_broadcast(&tsbList->wakeProd);   //Signal to wake producers
    pthread_mutex_unlock(&tsbList->mutex);	   //Release the lock
    return removedNode;
}

/**
 * Searches the list for a node with the given key and returns the pointer to the
 * found node.
 *
 * @param list a pointer to a <code>List</code>.
 * @param the object to search for.
 * @return a pointer to the node that was found. Or <code>NULL</code> if a node 
 * with the given key is not found or the list is <code>NULL</code> or empty.
 */
NodePtr tsb_search(struct tsb_list * tsbList, const void *obj){
    struct node* searchNode;
    pthread_mutex_lock(&tsbList->mutex);  //Get the lock
    searchNode = search(tsbList->list,obj);
    pthread_mutex_unlock(&tsbList->mutex);	 //Release the lock
    return searchNode;
}

/**
 * Reverses the order of the given list.
 *
 * No lock needed
 *
 * @param list a pointer to a <code>List</code>.
 */
void tsb_reverseList(struct tsb_list * tsbList){
    reverseList(tsbList->list);
}

/**
 * Prints the list.
 *
 * No lock needed
 *
 * @param list a pointer to a <code>List</code>.
 */
void tsb_printList(struct tsb_list * tsbList){
    printList(tsbList->list);
}

/**
 * Finish up the monitor by broadcasting to all waiting threads
 */
void tsb_finishUp(struct tsb_list * tsbList){
    pthread_mutex_lock(&tsbList->mutex);  //Get the lock
    tsbList->stop_requested = TRUE;   //Request to terminate
    pthread_cond_broadcast(&tsbList->wakeCons);  //Signal to awaken all consumers
    pthread_mutex_unlock(&tsbList->mutex);  //release the lock
}
