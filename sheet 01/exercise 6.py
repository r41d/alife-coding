#!/usr/bin/env python
# -*- coding: utf-8 *-*
# not intended to be clean code (whole project was done in 20 minutes)
import pygame
from pygame.locals import K_SPACE, K_ESCAPE, KEYUP, KEYDOWN, QUIT
from random import randint, randrange
import os.path as path
from glob import glob

X = 640
Y = 480
FPS = 20

pygame.init()
pygame.mixer.init()

FONT = pygame.font.Font("ufonts.com_segoe-ui-symbol.ttf", 30)

TIMER = pygame.time.Clock()
tick = 0

KEYS = K_SPACE, K_ESCAPE

pygame.display.set_caption('Langtons Ant - AL2016')
DISPLAY = pygame.display.set_mode((X+2, Y+2))

run = True
events = []
slapcnt = 0
timeout = 0

black = (0,0,0)
white = (255,255,255)
grey = (127,127,127)

fieldSize = 40
xx = X / fieldSize
print xx
yy = Y / fieldSize
print yy
field = [[False for y in range(yy)] for x in range(xx)]


def chess(field):
	for x in range(len(field)):
		for y in range(len(field[x])):
			field[x][y] = True if (x+y)%2==0 else False

def tickAnt(field, ant):
	"""scan, turn, flip, move"""
	# SCAN
	curField = field[ant['x']][ant['y']]
	# TURN
	if not curField: # white
		ant['dir'] = (ant['dir']+1) % 4 # turn right
	elif curField: # black
		ant['dir'] = (ant['dir']-1) # turn left
		if ant['dir'] < 0: ant['dir'] = 3
	# FLIP
	field[ant['x']][ant['y']] = not field[ant['x']][ant['y']]
	# MOVE
	if ant['dir'] == 0: # north
		ant['y'] -= 1
	elif ant['dir'] == 1: # east
		ant['x'] += 1
	elif ant['dir'] == 2: # south
		ant['y'] += 1
	elif ant['dir'] == 3: # west
		ant['x'] -= 1

def antStr(dirr):
	if   dirr == 0: return u"↑"
	elif dirr == 1: return u"→"
	elif dirr == 2: return u"↓"
	elif dirr == 3: return u"←"

# init chess board
chess(field) # matrix -> call by reference


ant = {'x':xx/2, 'y':yy/2, 'dir':0}
print ant

# main game loop
while run:

	DOTHETICK = False

	for e in pygame.event.get():
		if e.type == QUIT or (e.type == KEYDOWN and e.key == K_ESCAPE):
			run = False
			continue

		if e.type not in [KEYDOWN, KEYUP] or e.key not in KEYS:
			continue

		if e.type == KEYDOWN:
			events.append(e.key)
		elif e.key in events:
			events.remove(e.key)

		if e.type == KEYDOWN and e.key == K_SPACE:
			DOTHETICK = True


	if DOTHETICK:
		tickAnt(field, ant) # matrix,dict -> call by reference
		print ant

	DISPLAY.fill(grey)
	for i in range(xx):
		x = i * fieldSize
		for j in range(yy):
			y = j * fieldSize
			rct = pygame.Rect(x+2,y+2,fieldSize-2,fieldSize-2)
			#print 'rect', rct, field[i][j]
			pygame.draw.rect(DISPLAY, black if field[i][j] else white, rct)
			if ant['x']==i and ant['y']==j:
				label = FONT.render(antStr(ant['dir']), 1, (255, 0, 0))
				pos = label.get_rect(center=rct.center)
				pos = pos.move(-1,-2)
				DISPLAY.blit(label, pos)

	pygame.display.update()
	TIMER.tick(FPS)
	tick = (tick % (FPS*100)) + 1 # avoid overflow

pygame.quit()

