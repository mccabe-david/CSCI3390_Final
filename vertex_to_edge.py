# vertex_to_edge.py
#
# Written by David McCabe
# on 5/5/21
#
# Turns a list of vertices into their corresponding edges

with open("after_scala/log_50_postscala.csv") as f:
    with open("final_matching/log_normal_100_matching.csv", "w") as out:
        power = 2**32
        for line in f:
            l = line.split(",")
            total = int(l[0])
            a, b = (total // power, total % power)
            out.write(str(a) + "," + str(b) + "\n")
