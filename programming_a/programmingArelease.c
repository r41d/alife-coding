#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>

int FIELDSIZE = 84;
int r = -1;

// take a field and rule and calculate the new field (newF)
void tick(int* field, int* newF, const int* rule) {
	// initialize newField with 0
	memset(newF, 0, FIELDSIZE);
	// iterate over cells (not the 4 outer cells)
	for(int cell=2; cell<FIELDSIZE-2; cell++) {
		// determine the position for lookup in the wolfram table
		int wolframLookup = 0;
		for(int i=cell-r; i<=cell+r; i++) {
			wolframLookup <<= 1;
			wolframLookup |= field[i];
		}
		// assign new living state
		newF[cell] = rule[wolframLookup];
	}
	// both outer cells are always dead
	newF[0] = 0;
	newF[1] = 0;
	newF[FIELDSIZE-1] = 0;
	newF[FIELDSIZE-2] = 0;
}

// just print the field
void dump(const int* field) {
	for (int i=0; i < FIELDSIZE; i++) {
		printf("%c", (field[i]==1?'#':'.'));
	}
	printf("\n");
}

int main() {
	// Initialize random number generator
	srand(time(NULL));

	// Query user for radius
	r = -1;
	while (r < 1 || r > 2) {
		printf("Please enter r (radius): ");
		scanf("%d", &r);
	}
	printf("OK, radius = %d\n", r);

	// Query user for Wolfram number
	int n = (2*r+1);
	long wmax = pow(2,pow(2,n)) - 1;
	long w = -1;
	while (w < 1 || w > wmax) {
		printf("Please enter Wolfram number (0-%ld): ", wmax);
		scanf("%ld", &w);
	}
	printf("OK, w = %ld\n", w);
	// Calculate Wolfram lookup table
	int wolframBits = pow(2,(2*r+1)); // 8 / 32
	int* rule = malloc(wolframBits * sizeof(int));
	for (int i = 0; i < wolframBits; i++) {
		rule[i] = (w & (int)pow(2,i)) >> i;
	}

	// Shall the field be randomized?
	int shuffle = 0;
	char random;
	printf("Random starting field? (y/N): ");
	scanf("%s", &random);
	if (random=='y' || random=='Y') {
		printf("OK, randomizing starting field\n");
		shuffle = 1;
	} else {
		printf("OK, using field[42]=TRUE as starting field\n");
	}

	// Query user for number of iterations to show
	int iter = -1;
	while (iter < 1) {
		printf("How many iterations to show? ");
		scanf("%d", &iter);
	}
	printf("OK, showing %d iterations after initial state\n", iter);

	// Initialize arrays for simulation
	int* field = malloc(sizeof(int)*FIELDSIZE);
	memset(field, 0, FIELDSIZE);
	field[42] = 1;
	if (shuffle) {
		for(int i=2; i<FIELDSIZE-2; i++) {
			field[i] = rand() % 2;
		}
	}
	int* newField = malloc(sizeof(int)*FIELDSIZE);

	// show initial state
	dump(field);
	// loop for the simulation
	for (int round=0; round<iter; round++) {
		tick(field, newField, rule);
		memcpy(field, newField, sizeof(int)*FIELDSIZE);
		dump(field);
	}

	// free all used memory
	free(field);
	free(newField);
	free(rule);

	return 0;
}
