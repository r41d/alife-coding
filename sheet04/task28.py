import itertools


#dic = {
#	'R':'ST'
#	'S':'TR'
#	'T':'RS'
#}


def apply(rst, strrr):
	res = ""
	print (rst)

	for _ in range(5):
		strrr = [rst[ord(c)-ord('R')] for c in strrr]
		strrr = ''.join(strrr)
		print(strrr)



start = 'R'

lst = ['RS','ST','TR']

for rst in itertools.permutations(lst, 3):
	#print(i)
	#r, s, t = rst
	apply(rst,start)


####    R -> RS
####    S -> ST
####    T -> TR

