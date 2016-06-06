#!/usr/bin/env python3
# -*- coding: utf-8 *-*

import copy
import random

class Individual(object):
	def __init__(self, genome):
		self.genome = genome
	def calc_fitness(self, f):
		self.fitness = f(self.genome)
	def __str__(self):
		return ("<<<Genome: %s; Fitness: %d>>>" % ([round(x,2) for x in self.genome], self.fitness))

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


if __name__ == '__main__':
	def f(genome):
		# Just some polynomial function for testing
		return sum((((-1)**idx) * (g**idx)) for idx, g in enumerate(genome))
	ev = ProgrammingD(f)
	ev.perform()

