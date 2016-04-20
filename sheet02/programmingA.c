#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>

long FIELDSIZE = 84;
long r = 1;

// take a field and rule and return the new field
void tick(long* field, long* newF, const long* rule) {
	memset(newF, 0, FIELDSIZE);
	for(long cell=2; cell<FIELDSIZE-2; cell++) {
		long wolframLookup = 0;
		for(long i=cell-r; i<=cell+r; i++) {
			wolframLookup <<= 1;
			wolframLookup |= field[i];
		}
		newF[cell] = rule[wolframLookup];
	}
	// both outer cells are always dead
	newF[0] = 0;
	newF[1] = 0;
	newF[FIELDSIZE-1] = 0;
	newF[FIELDSIZE-2] = 0;
}

void dump(const long* field) {
	for (long i=0; i < FIELDSIZE; i++) {
		printf("%c", (field[i]==1?'#':'.'));
	}
	printf("\n");
}

int main(int argc, char** argv) {
	srand(time(NULL));
	if (argc < 5) {
		printf("Usage: %s <state-width> <num-states> <radius> <rule> [<initial-state>]\n", argv[0]);
		return -1;
	}

	FIELDSIZE = atoi(argv[1]);
	long n = atoi(argv[2]);
	r = atoi(argv[3]);
	long rule = atoi(argv[4]);
	long wolframBits = pow(2,(2*r+1)); // 8 / 32
	long* wolframP = malloc(wolframBits * sizeof(long));
	for (long i = 0; i < wolframBits; i++) {
		wolframP[i] = (rule & (long)pow(2,i)) >> i;
	}

	long* field = malloc(sizeof(long)*FIELDSIZE);
	memset(field, 0, FIELDSIZE);
	field[42] = 1;
	if (argc > 5 && !strcmp(argv[5], "R")) {
		for(int i=2; i<FIELDSIZE-2; i++) {
			field[i] = rand() % 2;
		}
	}
	long* newField = malloc(sizeof(long)*FIELDSIZE);

	dump(field);
	for (long round=0; round<n; round++) {
		tick(field, newField, wolframP);
		memcpy(field, newField, sizeof(long)*FIELDSIZE);
		dump(field);
	}

	free(field);
	free(newField);
	free(wolframP);

	return 0;
}
