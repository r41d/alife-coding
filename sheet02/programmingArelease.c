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

int main() {
	srand(time(NULL));

	int r = -1;
	while (r < 1 || r > 2) {
		printf("Please enter r (radius): ");
		scanf("%d", &r);
	}
	printf("OK, radius = %d\n", r);

	int n = (2*r+1);
	long wmax = pow(2,pow(2,n)) - 1;
	long w = -1;
	while (w < 1 || w > wmax) {
		printf("Please enter Wolfram number (0-%ld): ", wmax);
		scanf("%ld", &w);
	}
	printf("OK, w = %ld\n", w);
	long wolframBits = pow(2,(2*r+1)); // 8 / 32
	long* wolframP = malloc(wolframBits * sizeof(long));
	for (long i = 0; i < wolframBits; i++) {
		wolframP[i] = (w & (long)pow(2,i)) >> i;
	}

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

	int iter = -1;
	while (iter < 1) {
		printf("How many iterations to show? ");
		scanf("%d", &iter);
	}
	printf("OK, showing %d iterations after initial state\n", iter);

	long* field = malloc(sizeof(long)*FIELDSIZE);
	memset(field, 0, FIELDSIZE);
	field[42] = 1;
	if (shuffle) {
		for(int i=2; i<FIELDSIZE-2; i++) {
			field[i] = rand() % 2;
		}
	}
	long* newField = malloc(sizeof(long)*FIELDSIZE);

	dump(field);
	for (long round=0; round<iter; round++) {
		tick(field, newField, wolframP);
		memcpy(field, newField, sizeof(long)*FIELDSIZE);
		dump(field);
	}

	free(field);
	free(newField);
	free(wolframP);

	return 0;
}
