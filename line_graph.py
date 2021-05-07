#
# line_graph.py
# By David McCabe
# Written 5/5/21
#
# Takes as input a .csv file of edges
# Ourputs a .csv file where edges become nodes
def Main(filepath):
    with open("after_python/out.csv", "w") as out:
        with open(filepath) as f:
            ids = dict()
            power = 2**32
            for i, line in enumerate(f):
                l = line.split(",")
                a = int(l[0])
                b = int(l[1])
                Vertex_Id = str(power*a + b)
                if a in ids:
                    for num in ids[a]:
                        out.write(Vertex_Id + "," + num + "\n")
                    ids[a].append(Vertex_Id)
                else:
                    ids[a] = [Vertex_Id]
                if b in ids:
                    for num in ids[b]:
                        out.write(Vertex_Id + "," + num + "\n")
                    ids[b].append(Vertex_Id)
                else:
                    ids[b] = [Vertex_Id]

Main("before_python/soc-pokec-relationships.csv")
            
