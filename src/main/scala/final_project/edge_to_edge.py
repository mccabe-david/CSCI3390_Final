#
# edge_to_edge.py
#
# Written by David McCabe
# on 5/8/21
#
#
# Uses Brute Force to compute a maximal edge matching 
# Not optimal, but line graph takes up too much memory for graphs
# with over 4 million edges

with open("before_python/com-orkut.ungraph.csv") as f:
    with open("final_matching/com-orkut.ungraph_matching.csv", "w") as out:
        seen = set()
        for i, line in enumerate(f):
            l = line.split(",")
            a, b = int(l[0]), int(l[1])
            if a in seen or b in seen:
                continue
            seen.add(a)
            seen.add(b)
            out.write(line)
            
        
