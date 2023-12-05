This project is run by passing following arguments:

argument 1: Path to the file where all the points in the dataset.<br/>
argument 2: (k) size of the solution.<br/>
argument 3: (n) size of the dataset, typically rows in the dataset.<br/>
argument 4: (d) columns in the dataset<br/>
argument 5: (N) Sample Size.<br/>

This internally uses the Greedy Shrink algorithm assumes a uniform distribution of linear utility funcitons and samples N utility functions from that distribution.

In the output the execution of the file is stored and along with: <br/>
"Iterations Avoided" (for which avg regret ratio is not calculated) as described in paper of section 5.4.<br/>
"Users Avoided" (for which utility functions avoided) using optimized techniques.<br/>

Dataset contains n number of rows, d columns. seperated by line and each point by a tab.


Example input dataset file:

0.94	0.18	0.28<br/>
0.91	0.39	0.13<br/>
0.62	0.24	0.74<br/>
0.15	0.77	0.61<br/>


Example utility functions file:

0.80	0.05	0.15<br/>
0.75	0.20	0.05<br/>
0.40	0.10	0.50<br/>
0.05	0.55	0.40<br/>


for the above input files: usage of the application like:

./run points.txt 2 4 3 10000

./run points.txt 2 4 3 4 utilityFunctions.txt
