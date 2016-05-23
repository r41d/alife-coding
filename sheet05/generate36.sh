make
for a in `seq 1 3`; do
	for x in `seq 1 2`; do
		./task36 $a $x > task36_a${a}_x${x}
	done
	gnuplot task36_a${a}.gnu
done

