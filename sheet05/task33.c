#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>
#include <unistd.h>
#include <signal.h>
#include <ncurses.h>

/*
Consider the following 1-dimensional cellular automaton, with a size of S = 101 cells
i = 0, 1, 2, ..., 100, r=1, k = 65536 with a i = 0, ..., 65535, the parameter 0 < α ≤ 1.0
and the totalistic rule:

a i (t + 1) = f loor α ∗ [1.0 ∗ a i−1 (t) + 1.0 ∗ a i (t) + 1.0 ∗ a i+1 (t)]

The left boundary (position 0) is set to the fixed value a 0 = 42000, the right boundary
(position 100) is set to the fixed value a 100 = 420.
In the beginning (t=0) all other cells a 1 (t = 0) to a 99 (t = 0) will be set to 0.
How will the behaviour of this cellular automaton be after a long time for different values
of α? Which class of behaviour will be reached? Describe in your own words how α is
determining the long term behaviour.

*/

#define TICKS_AT_ONCE 1000

const int S = 101; // Field size
const int r = 1; // radius
const int k = 65536; // possible states
double alpha;

static inline void boundary(int* field) {
	// fixed value for both outer cells
	field[0] = 42000;
	field[S-1] = 420;
}

void tick(int* field, int* newF) {
	memset(newF, 0, S);
	for(int x=1; x<S-1; x++) {
		newF[x] = floor(alpha * (field[x-1] + field[x] + field[x+1]));
		newF[x] = fmin(k, newF[x]);
	}
	boundary(newF);
}

void wait(int sleeptime) {
	sleeptime *= 1000; // milliseconds->microseconds 
	sleeptime = (int) sleeptime * 1; // multiplier for decent speed
	usleep(sleeptime);
}

void dump(const int* field) {
	clear();
	for (int x=0; x < S; x++) {
		for (int y=0; y < 64; y++) {
			mvaddch(63-y, x, (field[x]/1024 > y) ? '*' : '.');
		}
	}
	move(64,0);
	for (int x=0; x < S; x++) {
		if (field[x] == k) {
			printw("X ");
		} else {
			printw("%d ", field[x]);
		}
	}
	refresh();
}

void terminate () {
	endwin(); // clean ncurses shutdown
	printf("byebye\n");
	exit(42);
}

int main(int argc, char** argv) {

	if (argc < 2) {
		printf("Usage: %s <alpha>\n", argv[0]);
		return -1;
	}
	
	// Handles for clean ncurses shutdown
	signal(SIGINT,&terminate);
	signal(SIGTERM,&terminate);

	initscr(); // ncurses initialization
	curs_set(0); // invisible cursor

	alpha = strtof(argv[1], NULL);

	int* field = malloc(sizeof(int)*S);
	memset(field, 0, S);
	for (int x=1; x < S-1; x++) {
		field[x] = 0;
		//field[x] = (int) rand() % k;
		//field[x] = k-1;
	}
	boundary(field);

	int* newField = malloc(sizeof(int)*S);

	dump(field);
	wait(1000);

	for (;;) {
		wait(100);
		for (int i=0; i<TICKS_AT_ONCE; i++) {
			tick(field, newField);
			memcpy(field, newField, sizeof(int)*S);
		}
		dump(field);
	}

	endwin();
	free(field);
	free(newField);

	return 0;
}

