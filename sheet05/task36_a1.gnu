set terminal pngcairo enhanced font "arial,10" # size 1280,960
set output 'task36_a1.png'
set key inside left top vertical Right noreverse enhanced autotitles box linetype -1 linewidth 1.000

set xlabel "Iterations"
set ylabel "Value"
set yrange [0.1:1]

plot "task36_a1_x1" title 'task36 a_1 x_1' with points linewidth 1, \
     "task36_a1_x2" title 'task36 a_1 x_2' with points linewidth 1
