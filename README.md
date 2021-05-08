# Large Scale Data Processing: Final Project
# David McCabe
## Results

|           File name           |        Number of edges       |        Size of Matching       |        Total Runtime                     |
| ------------------------------| ---------------------------- | ----------------------------- | ---------------------------------------- |
| com-orkut.ungraph.csv         | 117185083                    | 1323954                       | 4 minutes in python                      |
| twitter_original_edges.csv    | 63555749                     | 91636                         | 55 seconds in python                     |
| soc-LiveJournal1.csv          | 42851237                     | 1543228                       | 33 seconds in python                     |
| soc-pokec-relationships.csv   | 22301964                     | 1                             | 12 Hours in GCP with an N2 8cpu master,  |
|                               |                              |                               | with 5 N1 8cpu workers, and local 4cpu   |
|                               |                              |                               | 47 minute line graph creation            |
| musae_ENGB_edges.csv          | 35324                        | 2887                          | 30s on a default setup in GCP.           |
|                               |                              |                               | 4 second line graph creation.            |
| log_normal_100.csv            | 2671                         | 50                            | 12s on a default setup in GCP.           |
|                               |                              |                               | Near-instant line graph creation.        |

## Description of algorithms
### Smallest 3 graphs
  * Description(s) of your approach(es) for obtaining the matchings. It is possible to use different approaches for different cases. Please describe each of them as well as your general strategy if you were to receive a new test case.
  * Discussion about the advantages of your algorithm(s). For example, does it guarantee a constraint on the number of shuffling rounds (say `O(log log n)` rounds)? Does it give you an approximation guarantee on the quality of the matching? If your algorithm has such a guarantee, please provide proofs or scholarly references as to why they hold in your report.
### Largest 3 graphs

