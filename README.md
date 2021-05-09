# Large Scale Data Processing: Final Project
# David McCabe
## Results

|           File name           |        Number of edges       |        Size of Matching       |        Total Runtime                     |
| ------------------------------| ---------------------------- | ----------------------------- | ---------------------------------------- |
| com-orkut.ungraph.csv         | 117185083                    | 1323954                       | 4 minutes in Python                      |
| twitter_original_edges.csv    | 63555749                     | 91636                         | 55 seconds in Python                     |
| soc-LiveJournal1.csv          | 42851237                     | 1543228                       | 33 seconds in Python                     |
| soc-pokec-relationships.csv   | 22301964                     | 587601                        | 15 seconds in Python. A 47 minute line   |
|                               |                              |                               | graph creation, and GCP finishes only 5  |
|                               |                              |                               | rounds in 210 minutes with 1 n1 32 CpU-  |
|                               |                              |                               | highMem master + 2 same setup workers    |
| musae_ENGB_edges.csv          | 35324                        | 2887                          | 30s on a default setup in GCP.           |
|                               |                              |                               | 4 second line graph creation.            |
| log_normal_100.csv            | 2671                         | 50                            | 12s on a default setup in GCP.           |
|                               |                              |                               | Near-instant line graph creation.        |

## Description of algorithms
### Smallest Graphs
  * Description(s) of your approach(es) for obtaining the matchings. It is possible to use different approaches for different cases. Please describe each of them as well as your general strategy if you were to receive a new test case.
  * Discussion about the advantages of your algorithm(s). For example, does it guarantee a constraint on the number of shuffling rounds (say `O(log log n)` rounds)? Does it give you an approximation guarantee on the quality of the matching? If your algorithm has such a guarantee, please provide proofs or scholarly references as to why they hold in your report.
### Third Smallest Graph

### Largest graphs

