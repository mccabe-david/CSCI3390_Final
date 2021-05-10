# Large Scale Data Processing: Final Project
# David McCabe
## Results

|           File name           |        Number of edges       |        Size of Matching       |        Total Runtime                     |
| ------------------------------| ---------------------------- | ----------------------------- | ---------------------------------------- |
| com-orkut.ungraph.csv         | 117185083                    | 1323954                       | 4 minutes in Python                      |
| twitter_original_edges.csv    | 63555749                     | 91636                         | 55 seconds in Python                     |
| soc-LiveJournal1.csv          | 42851237                     | 1543228                       | 33 seconds in Python                     |
| soc-pokec-relationships.csv   | 22301964                     | 587601                        | 15 seconds in Python. A 47 minute line   |
|                               |                              |                               | graph creation, and GCP finishes only 7  |
|                               |                              |                               | rounds in 350 minutes with 1 n1 32 CpU-  |
|                               |                              |                               | highMem master + 2 same setup workers    |
| musae_ENGB_edges.csv          | 35324                        | 2887                          | 30s on a default setup in GCP.           |
|                               |                              |                               | 4 second line graph creation.            |
| log_normal_100.csv            | 2671                         | 50                            | 12s on a default setup in GCP.           |
|                               |                              |                               | Near-instant line graph creation.        |

## Description of algorithms
### Smallest Graphs
The first step of my process was turning each list of edges into a line graph where each edge became a node, being connected to another if the pair shared an endpoint. This came at the expense of file size growing exponentially. Next, the line graph was processed using DavidMIS. DavidMIS is what I call the lowest-degree selection algorithm I implemented. Essentially, every vertex computes its degree, then if a vertex is the lowest degree in its neighborhood (breaking ties based on edge orientation), it adds itself to the MIS. In practice, there are more rounds overall than LubyMIS, but the MIS obtained is significantly better. Luby's "Nudged" MIS obtained a matching of only 2370-2440 on the musae_ENGB_edges, but DavidMIS obtained 2887. Furthermore, DavidMIS consistently gave a matching of size 50 for the smallest graph, while the Luby's "nudged" would sometimes output a matching of size 48. The advantage in precision comes at the cost of runtime and memory, however. DavidMIS uses more rounds than Luby's, and both of these approaches require a line graph, which can take a significant amount of time to generate. 
### Third Smallest Graph
This graph was the most frustrating. I successfully created its line graph, but the size of the file was 64.5GB. Gigantic. I was unable to process it locally, so I tried using Google Cloud Platform. I soon learned that the graph was too larged even for GCP when I used as many worker nodes as possible. Next, I tried requesting quota increases allowing me access to more memory and CPUs, as well as N1s optimized for memory. Unfortunately, even this wasn't enough, and I watched my YARN NodeManagers go "Unhealthy" one by one. I did more research, and realized the managers were going unhealthy because they were running out of memory. So, I reconfigured my cluster one more time, using 4 SSDs for each node. Right now, it has been running for 6 hours and has not finished. Sadly, I have submitted the brute force solution for this graph. The analysis is described below. 
### Largest graphs
For the largest graphs, I used brute force. My algorithm iterates through the edges, storing each new vertex it adds to the matching in a set, and it only adds an edge if both of its vertices are not in the set. This algorithm is quick because it doesn't require shuffling, but it is not parallelizable. Additionally, the algorithm is not very memory-intensive as all that is needed is a hash table of vertexIds. The speed and memory benefits come at the cost of precision, however. There is no mechanism for edges to be selected based on the degree of its vertices, which means it is equally likely to select high-degree edges and low-degree edges, even though the latter is preferable. The matching quality is solely dependent on the ordering of the edges in the input file.  
### Strategy for a new test case
If I received a new test case, I would like at the size of the .csv file first. If it was small enough, I'd create a line graph and use DavidMIS to find the edges. Otherwise, I would use brute force. Given more time, I would try to implement Luby's "nudged" algorithm on the original graphs, altered to work on edges instead of nodes.
