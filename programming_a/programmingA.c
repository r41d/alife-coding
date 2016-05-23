#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>

int FIELDSIZE = 84;
int r = 1;

// take a field and rule and return the new field
void tick(int* field, int* newF, const int* rule) {
	memset(newF, 0, FIELDSIZE);
	for(int cell=2; cell<FIELDSIZE-2; cell++) {
		int wolframLookup = 0;
		for(int i=cell-r; i<=cell+r; i++) {
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

void dump(const int* field) {
	for (int i=0; i < FIELDSIZE; i++) {
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
	int n = atoi(argv[2]);
	r = atoi(argv[3]);
	long wolf = atoi(argv[4]);
	int wolframBits = pow(2,(2*r+1)); // 8 / 32
	int* rule = malloc(wolframBits * sizeof(int));
	for (int i = 0; i < wolframBits; i++) {
		rule[i] = (wolf & (int)pow(2,i)) >> i;
	}

	int* field = malloc(sizeof(int)*FIELDSIZE);
	memset(field, 0, FIELDSIZE);
	field[42] = 1;
	if (argc > 5 && !strcmp(argv[5], "R")) {
		for(int i=2; i<FIELDSIZE-2; i++) {
			field[i] = rand() % 2;
		}
	}
	int* newField = malloc(sizeof(int)*FIELDSIZE);

	dump(field);
	for (int round=0; round<n; round++) {
		tick(field, newField, rule);
		memcpy(field, newField, sizeof(int)*FIELDSIZE);
		dump(field);
	}

	free(field);
	free(newField);
	free(rule);

	return 0;
}
