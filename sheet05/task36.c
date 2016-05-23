#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>
#include <unistd.h>
#include <signal.h>

/*
Please determine the long-term behaviour of the following iterated function:
x[i+1] = a * x[i] * (1 - x[i])
for a = 3.3,
for a = 3.51,
and for a = 3.75
for the two starting conditions
x[i=0] = 0.228734167 and
x̂[i=0] = 0.228734168.
Which class (Wolfram classification) of behaviour is reached?
The development of the difference between the values x[i] and x̂[i] is as well interesting, and helpful to understand the behavior.
*/

#define S 10000 // Iterieren bis S

const long double a1 = 3.3;
const long double a2 = 3.51;
const long double a3 = 3.75;

const long double x1 = 0.228734167;
const long double x2 = 0.228734168;


void wait(int sleeptime) { // sleeptine in milliseconds
	usleep((int) (sleeptime * 1000)); // usleep takes microseconds
}

int main(int argc, char** argv) {

	if (argc < 3) {
		printf("Usage: %s <a> <x>\n", argv[0]);
		exit(1);
	}
	long double a;
	switch (atoi(argv[1])) {
		case 1: a = a1; break;
		case 2: a = a2; break;
		case 3: a = a3; break;
		default: exit(2);
	}
	long double startX;
	switch (atoi(argv[2])) {
		case 1: startX = x1; break;
		case 2: startX = x2; break;
		default: exit(3);
	}

	long double x = startX;

	wait(1000); // sekunde warten

	for (long long int s = 0 ; s <= S ; s++) {
		//if (s > S*0.9)
		//	printf("%10lld: %.50Lf\n", s, x);
		printf("%lld %.70Lf\n", s, x);
		x = a * x * (1 - x);
		//wait(10);
	}

	return 0;
}

