import Gnuplot, Gnuplot.funcutils


ITERATIONS = 10000

x = [0] * (ITERATIONS+1) # prey
y = [0] * (ITERATIONS+1) # predator


def step(i, a,b,c,d,e,f,g,h):
	global x, y
	x[i+1] = x[i] + a*x[i] + b*y[i] + e*x[i]*x[i] + g*x[i]*y[i] # prey
	y[i+1] = y[i] + c*x[i] + d*y[i] + f*y[i]*y[i] + h*x[i]*y[i] # preditor

def plot():
	datX = list(enumerate(x))
	datY = list(enumerate(y))
	graph = Gnuplot.Gnuplot(persist=1)
	graph.reset()				# first reset graph
	graph.title('ALife Programming C') # set title of graph
	graph.xlabel('time')		# set title of x-axis
	graph.ylabel('Population')	# set title of y-axis
	graph('set autoscale')		# autoscale
	graph('set style data lines')
	prey = Gnuplot.PlotItems.Data(datX, with_="lines", title="prey")
	pred = Gnuplot.PlotItems.Data(datY, with_="lines", title="predator")
	graph.plot(prey, pred)

if __name__ == '__main__':

	x[0] = 1000.0 # prey
	y[0] =  500.0 # predator

	a, b, e, g = 0.6, -3, 0.0000001, 0
	c, d, f, h = 0.3, -2, 0.00001  , 0

	for i in range(ITERATIONS):
		step(i, a,b,c,d,e,f,g,h)

	print(x)
	print(y)

	plot()

