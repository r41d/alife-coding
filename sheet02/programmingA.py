#!/usr/bin/env python
# -*- coding: utf-8 *-*

from random import randint, randrange
from types import *

ticks = 0

k = 2 # States
r = 1 # Radius

FIELDSIZE = 84
seedPos = 42

S = [0] * (FIELDSIZE-seedPos) + [1] + [0] * (FIELDSIZE-seedPos-1)
R = [0,0] + [randint(0,1) for _ in range(FIELDSIZE-4)] + [0,0]

def tick(field):
	pass

	field[0] = 0
	field[1] = 0
	field[82] = 0
	field[83] = 0


def isint(value):
  try:
    int(value)
    return True
  except:
    return False

def query_input():

	# Parse neighbordhood
	r = ""
	while r not in ['1','2']:
		r = raw_input('Please enter r (radius): ')
	r = int(r)
	n = (2*r+1)

	# Parse wolfram number
	w = ""
	wmax = (2**(2**n)) if r==1 else (2**(2**n))
	while not isint(w) or int(w) < 0 or int(w) < wmax:
		w = raw_input('Please enter Wolfram number: (0-%d) ' % wmax )
	w = int(w)
	w = bin(w)[2:]
	w = ((2**n - len(w)) * "0") + w
	wolfram = [int(x) for x in w]

	f = ""
	while f.upper() not in ['S','R']:
		f = raw_input('Please choose the starting field (S/R): ')
	if f.upper() == 'S':
		field = S
	elif f.upper() == 'R':
		field = R

	return r, wolfram, field


def main():
	r, wolfram, field = query_input()
	# r::int wolfram::[int](8) field::[int](84)




if __name__ == '__main__':
	main()
