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

FONT = pygame.font.Font("ufonts.com_segoe-ui-symbol.ttf", 20)

TIMER = pygame.time.Clock()
tick = 0

KEYS = K_SPACE, K_ESCAPE

pygame.display.set_caption('Langtons Ant - AL2016')
DISPLAY = pygame.display.set_mode((2*X+40+2, Y+2))

run = True
events = []
iterations = 0
timeout = 0

black = (0,0,0)
white = (255,255,255)
grey = (127,127,127)

def whiteField():
	return [[False for y in range(yy)] for x in range(xx)]
def blackField():
	return [[True for y in range(yy)] for x in range(xx)]

fieldSize = 20
xx = X / fieldSize
print xx
yy = Y / fieldSize
print yy

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

# init boards
fieldW = whiteField()
fieldB = blackField()
antW = {'x':xx/2, 'y':yy/2, 'dir':0}
antB = {'x':xx/2, 'y':yy/2, 'dir':0}

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

	if K_SPACE in events:
		DOTHETICK = True

	if DOTHETICK:
		tickAnt(fieldW, antW) # matrix,dict -> call by reference
		tickAnt(fieldB, antB) # matrix,dict -> call by reference
		iterations += 1

	DISPLAY.fill(grey)
	
	# LEFT - white
	for i in range(xx):
		x = i * fieldSize
		for j in range(yy):
			y = j * fieldSize
			rct = pygame.Rect(x+2,y+2,fieldSize-2,fieldSize-2)
			#print 'rect', rct, field[i][j]
			pygame.draw.rect(DISPLAY, black if fieldW[i][j] else white, rct)
			if antW['x']==i and antW['y']==j:
				label = FONT.render(antStr(antW['dir']), 1, (255, 0, 0))
				pos = label.get_rect(center=rct.center)
				pos = pos.move(-1,-2)
				DISPLAY.blit(label, pos)
	
	# RIGHT - black
	for i in range(xx):
		x = X+40+ i * fieldSize
		for j in range(yy):
			y = j * fieldSize
			rct = pygame.Rect(x+2,y+2,fieldSize-2,fieldSize-2)
			pygame.draw.rect(DISPLAY, black if fieldB[i][j] else white, rct)
			if antB['x']==i and antB['y']==j:
				label = FONT.render(antStr(antB['dir']), 1, (255, 0, 0))
				pos = label.get_rect(center=rct.center)
				pos = pos.move(-1,-2)
				DISPLAY.blit(label, pos)

	label = FONT.render(str(iterations), 1, (255, 0, 0))
	pos = label.get_rect(center=(X+20,20))
	DISPLAY.blit(label, pos)

	pygame.display.update()
	TIMER.tick(FPS)
	tick = (tick % (FPS*100)) + 1 # avoid overflow

pygame.quit()

