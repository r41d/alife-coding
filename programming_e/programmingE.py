#!/usr/bin/env python3
# -*- coding: utf-8 *-*

import copy
import random
import csv
import math
import sys
import time

PLOTTING = False
PlotData = []

def screwthis(x):
	print(x)
	sys.exit(1)

class Individual(object):
	def __init__(self, genome):
		self.genome = genome
	def calc_fitness(self, f):
		self.fitness = f(self.genome)
	def __str__(self):
		return ("<<<Genome: %s; Fitness: %d>>>" % (self.genome, self.fitness))

class EvolutionaryAlgorithm(object):

	def __init__(self):
		self.INIT = True
		self.RUN = True
		self.ITERATIONS = 0

	def Initialization(self):
		raise NotImplementedError

	def ParentSelection(self):
		raise NotImplementedError

	def Inheritance(self):
		raise NotImplementedError

	def Mutation(self):
		raise NotImplementedError

	def FitnessEvaluation(self):
		raise NotImplementedError

	def ExternalSelection(self):
		raise NotImplementedError

	def Finish(self):
		raise NotImplementedError

	def perform(self):
		while self.RUN:
			if not self.INIT: self.ParentSelection()
			if not self.INIT: self.Inheritance()
			if not self.INIT: self.Mutation()
			if self.INIT: self.Initialization()
			self.INIT = False
			self.FitnessEvaluation()
			self.ExternalSelection()
			self.Finish() # mayb set RUN to False
			self.ITERATIONS += 1


class ProgrammingD(EvolutionaryAlgorithm):
	# The Evolutionary Algo. shall find the maximum of a function f(g).
	# Implement a mini population of P = 2 individuals, each
	# with a genome g that is a vector of real valued L components.
	def __init__(self, *args):
		super().__init__()
		self.P = 2 # population size
		self.L = 10 # dimension of a genome vector
		self.EPS = 0.1 # Epsilon -> maximum change to genome values
		# Increasing this value leads to finding solutions faster.
		# Decreasing this value leads to potentional better candidates (more fine-grained)
		self.args = args

	def Initialization(self):
		# Initialize both genomes with random values
		self.f = self.args[0]
		self.Population = []
		for _ in range(self.P):
			thenewguy = Individual([random.random()*5 for _ in range(self.L)])
			self.Population.append(thenewguy)
		self.NewPopulation = []

	def ParentSelection(self):
		# Can be omitted, so this does nothing
		pass

	def Inheritance(self):
		# The inheritance step is just copying of the surviving
		# genome to be the newly created genome.
		self.NewPopulation = copy.deepcopy(self.Population)

	def Mutation(self):
		# The mutation step is only performed for the newly created genome.
		# Implement the mutation step by just adding a small random
		# vector r to the genome, each of the L components of r shall
		# be either equally distributed between −ε and +ε or
		# normally distributed with adjustable bounds.
		for newbie in self.NewPopulation: # should just be executed once in our case
			# Wiggle a little on the values
			newbie.genome = [x+random.uniform(-self.EPS,self.EPS) for x in newbie.genome]

	def FitnessEvaluation(self):
		# Fitness evaluation is just calculating the fitness function
		# f(g) for all P genomes of the population, and providing
		# the results for the external selection process.
		self.Population = self.Population + self.NewPopulation
		for individual in self.Population:
			individual.calc_fitness(self.f)

	def ExternalSelection(self):
		# The external selection steps keeps only the best one
		# of the two individuals, and discards the other one.
		sortPop = sorted(self.Population, key=lambda p: p.fitness, reverse=True)
		print("Iteration %3d: Current candidates: %s and %s" % (self.ITERATIONS, str(sortPop[0]), str(sortPop[1])))
		self.Population = [sortPop[0]]

	def Finish(self):
		# Implement a reasonable stopping criterion
		indi = self.Population[0]
		# if we are at this point, indi is the only individual at the moment
		if indi.fitness >= 1e6:
			self.RUN = False
			print("Found candicate with fitness over 1 million after %d iterations!" % self.ITERATIONS)


class ProgrammingE(EvolutionaryAlgorithm):

	# Write a C, C++, Java or Python Programm, that implements an evolutionary
	# algorithm to maximize the length of a route going twice through a given
	# set of N points (cities) in 2-dimensions.
	# Starting, and ending point are OPEN TO BE DETERMINED by the algorithm;
	# each point must be visited EXACTLY TWICE.

	# It is completely your choice, which variant of evolutionary algorithm to take.

	def __init__(self, P, µ):
		super().__init__()
		self.P = P # maximum population size
		self.µ = µ
		self.λ = P-µ
		self.ITER_STOP = 1000
		# the N points X_n = (x_1, x_2)_n shall be read in from the text-file Positions PA-E.txt
		# Allow a maximum of up to N = 150 points.
		with open('Positions_PA-E.txt', 'r') as posfile:
			posreader = csv.reader(posfile, delimiter='\t', skipinitialspace=True)
			self.X = [list(map(int, pos[1:3])) for pos in posreader][1:] # only store coordinates and skip first line
		self.X = self.X[:150] # truncate after 150 positions
		self.N = len(self.X) # determine number of positions
		print("X: %s" % str(self.X))
		print("N: %d" % self.N)
		time.sleep(1.5)

	def pathlen(self, genome):
		# genome is just a list of len(X)*2 pairs
		# just calculate the length of the whole path
		dist = 0
		for idx in range(len(genome)-1):
			dist += math.hypot((genome[idx][0]-genome[idx+1][0]), (genome[idx][1]-genome[idx+1][1]))
		return dist

	def Initialization(self):
		# gen. init. pop. P
		self.Population = []
		for _ in range(self.P):
			path = self.X + self.X # visit all points exactly twice
			random.shuffle(path) # randomly shuffle the path
			indi = Individual(path) # new individual, TADA
			self.Population.append(indi)
		self.NewPopulation = []
		self.LastBest = None
		self.LastBestIter = 0

	def ParentSelection(self):
		# during this whole phase we have pop. size µ
		# Simple approach: All λ offspring is just a copy of the current best individual
		self.BestIndividual = copy.deepcopy(self.Population[0])
		# Better approach: Use the top
		self.BestIndividuals = copy.deepcopy(self.Population[:self.λ]) # maximum of λ entries

	def Inheritance(self):
		# starting with µ, generate λ, so that we have pop. size P afterwards
		self.NewPopulation = []
		# I don't know how inheritance from more that 1 parent should work,
		# as we need to be sure to always have every point in out path exactly twice.
		# Taking parts of paths from different parents would often completely destroy this property.
		self.NewPopulation.append(copy.deepcopy(self.BestIndividual)) # take current best in every case
		#for _ in range(self.λ-1):
			# Simple approach: All λ offspring is just a copy of the current best individual
			#self.NewPopulation.append(copy.deepcopy(self.BestIndividual))
		# Better approach: Sample from the top
		BestIndividuals = self.BestIndividuals
		while len(BestIndividuals) < self.λ: # multiply list to avoid ValueError from sample()
			BestIndividuals.extend(self.BestIndividuals)
		self.NewPopulation.extend(random.sample(BestIndividuals, self.λ-1))

	def Mutation(self):
		# during this whole phase we have pop. size P
		# A simple approch seems to be to just switch 2 points randomly.
		for indi in self.NewPopulation:
			i = random.randrange(self.N*2)
			j = random.randrange(self.N*2)
			# simply swap two points :)
			indi.genome[i], indi.genome[j] = indi.genome[j], indi.genome[i]

	def FitnessEvaluation(self):
		# during this whole phase we have pop. size P
		self.Population = self.Population + self.NewPopulation
		self.NewPopulation = []
		for individual in self.Population:
			individual.calc_fitness(self.pathlen)

	def ExternalSelection(self):
		# starting with P, discard λ, keep µ, new pop. size = µ
		sortPop = sorted(self.Population, key=lambda p: p.fitness, reverse=True) # sort population by fitness (=pathlen)
		self.Population = sortPop[:self.µ] # only keep µ individuals

	def Finish(self):
		# The program has to output the fitness of the best individual, the mean
		# fitness and the least fitness of the parents in every generation in a
		# Gnuplot readable format.
		# Depict and draw the development of these three values into a graph.
		# Hand it in together with the other solutions.
		if self.LastBest != None:
			PlotData.append(self.LastBest.fitness)
		if not self.LastBest or self.LastBest.fitness < self.Population[0].fitness:
			self.LastBest = self.Population[0]
			self.LastBestIter = self.ITERATIONS
			print("Iteration", self.ITERATIONS, "Current best pathlen:", self.LastBest.fitness)
		if self.ITERATIONS - self.LastBestIter > self.ITER_STOP:
			print("Didn't find a longer path for the last", self.ITER_STOP, "steps...")
			print("Here is my best solution after", self.LastBestIter, "steps with length", self.LastBest.fitness)
			print(self.LastBest)
			self.RUN = False


if __name__ == '__main__':
	E = True
	if not E: # D
		def f(genome):
			# Just some polynomial function for testing
			return sum((((-1)**idx) * (g**idx)) for idx, g in enumerate(genome))
		ev = ProgrammingD(f)
		ev.perform()
	else: # E
		DEBUGGING = False
		# The parameters P, μ and λ, are to be set by the user at runtime
		# P = no. of individuals to have after INIT or INHERITANCE and during MUTATION and FITNESS EVAL
		# µ = no. of individuals that survive the EXTERNAL SELECTION phase (the new parents for PAR. SEL. and INHERITANCE)
		# λ = no. if individuals that are DISCARDED during EXT. SEL. and generated in INHERITANCE phase (offspring)
		if DEBUGGING:
			P = 100
			µ = 10
		else:
			try:
				P = int(input("P? "))
			except ValueError:
				screwthis("WTF?")
			if P <= 1:
				screwthis("P>1 please")

			try:
				µ = int(input("µ? "))
			except ValueError:
				screwthis("WTF?")
			if µ <= 0:
				screwthis("µ>0 please")
			if P-µ <= 0:
				screwthis("P>µ please")
			print("λ=", P-µ)
		ev = ProgrammingE(P, µ)
		ev.perform()

		if PLOTTING:
			import matplotlib.pyplot as plt
			plt.title('Performance Graph for Development of best fitness within the polulation')
			plt.xlabel('Iterations')
			plt.ylabel('Maximal fitness')
			plt.plot(PlotData)
			plt.show()

