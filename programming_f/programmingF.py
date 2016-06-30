#!/usr/bin/env python3
# -*- coding: utf-8 *-*

import copy
import random
import csv
import math
import sys
import time
import pprint

PLOTTING = False

def screwthis(x):
	print(x)
	sys.exit(1)


class ProgrammingF(object):

	def __init__(self):

		with open('SS16-4201-PA-F.grid.txt', 'r') as gridfile:
			gridreader = csv.reader(gridfile, delimiter=' ', skipinitialspace=True)
			_, _, _, x, _, y = gridreader.next()
			self.x, self.y = int(x), int(y)
			self.points = [list(map(int, grid)) for grid in gridreader]

		self.GRID = [[0 for y in range(self.y)] for x in range(self.x)]

		for (x, y) in self.points:
			self.GRID[x][y] = 1


if __name__ == '__main__':
	f = ProgrammingF()

