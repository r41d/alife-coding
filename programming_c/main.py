import Gnuplot, Gnuplot.funcutils


ITERATIONS = 1000

x = [1] * (ITERATIONS+1)
y = [2] * (ITERATIONS+1)


def step(i, a,b,c,d,e,f,g,h):
	global x, y
	x[i+1] = x[i] + a*x[i] + b*y[i] + e*x[i]*x[i] + g*x[i]*y[i] # prey
	y[i+1] = y[i] + c*x[i] + d*y[i] + f*y[i]*y[i] + h*x[i]*y[i] # preditor

def plot():
	datX = list(enumerate(x))
	datY = list(enumerate(y))
	graph = Gnuplot.Gnuplot(persist=1)
	graph.reset()				# first reset graph
	graph.title('ALife C')		# set title of graph
	graph.xlabel('time')		# set title of x-axis
	graph.ylabel('population')	# set title of y-axis
	# graph('set style data lines')
	graph('set autoscale')		# autoscale
	graph('set style data lines')
	graph.plot(datX, datY)		# prey
	graph.replot(datY)		# predator
	#graph('set style data linespoints')
	#graph.plot(datX)		# finally plot the data


if __name__ == '__main__':

	x[0] = 1000.0 # prey
	y[0] =  500.0 # predator

	a, b, e, g = 0.03,   0,     0,   -0.0001
	c, d, f, h = 0,     -0.03,  0,    0.00005

	for i in range(ITERATIONS):
		step(i, a,b,c,d,e,f,g,h)

	print(x)
	print(y)

	plot()
