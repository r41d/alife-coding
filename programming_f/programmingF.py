#!/usr/bin/env python3
# -*- coding: utf-8 *-*

import copy
import random
import csv
import math
import sys
import time
import pprint
import enum

def screwthis(x):
	print(x)
	sys.exit(1)

class Dir(enum.Enum):
	N  = 1
	NE = 2
	E  = 3
	SE = 4
	S  = 5
	SW = 6
	W  = 7
	NW = 8
	def right(d):
		mapping = {N:NE, NE:E, E:SE, SE:S, S:SW, SW:W, W:NW, NW:N}
		return mapping[d]
	def left(d):
		mapping = {N:NW, NE:N, E:NE, SE:E, S:SE, SW:S, W:SW, NW:W}
		return mapping[d]



class Robot(object):

	def __init__(self, x, y, d):
		self.x = x
		self.y = y
		self.d = d # direction


class ProgrammingF(object):

	def __init__(self):

		with open('SS16-4201-PA-F.grid.txt', 'r') as gridfile:
			gridreader = csv.reader(gridfile, delimiter=' ', skipinitialspace=True)
			_, _, _, x, _, y = next(gridreader)
			self.X, self.Y = int(x), int(y)
			self.walls = [list(map(int, grid)) for grid in gridreader]

		self.GRID = [[0 for y in range(self.Y)] for x in range(self.X)]

		for (x, y) in self.walls:
			self.GRID[x][y] = 1

		self.roboot = Robot(48, 17, Dir.S)




if __name__ == '__main__':
	f = ProgrammingF()

