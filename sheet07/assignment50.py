# -*- coding: utf-8 *-*
# Assignment 50
from numpy import *
import numpy.random as rng
import numpy.linalg as la
import scipy as sp
import scipy.spatial as spatial

angle = lambda u,v: arccos(clip(dot(u,v)/la.norm(u)/la.norm(v), -1.0, 1.0))

N = 100 # dimension
Z = 100 # number of points

X = [rng.rand(N) for _ in range(Z)]

# How long is the average distance between two of these random points X[i] and X[j]?
pairwiseDists = spatial.distance.pdist(X, metric='euclidean')
averageDist = average(pairwiseDists)
print "average distance:", averageDist

# How is the distribution of these distances?
stdDeviation = std(pairwiseDists)
print "standard deviation:", stdDeviation

# What about the angle between two arbitrary lines, each one connecting two random points?
VECS = []
for _ in range(Z):
	idx1, idx2 = map(int, floor(rng.rand(2)*Z))
	while idx2 == idx1: idx2 = int(floor(rng.rand()*Z))
	VECS.append(X[idx1]-X[idx2])

ANGLES = []
for idx in range(len(VECS)):
	for idx2 in range(idx):
		ANGLES.append(angle(VECS[idx], VECS[idx2]))
print "NaN:", len(filter(isnan, ANGLES)) # very rarely a NaN value may occur
ANGLES = [a for a in ANGLES if not isnan(a)] # throw them out
averageAngle = rad2deg(average(ANGLES))
print "average angle in degree:", averageAngle
