package project_3

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.graphx._
import org.apache.spark.storage.StorageLevel
import org.apache.log4j.{Level, Logger}

object main{
  val rootLogger = Logger.getRootLogger()
  rootLogger.setLevel(Level.ERROR)

  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
  Logger.getLogger("org.spark-project").setLevel(Level.WARN)

  def DavidMIS(g_in: Graph[Int, Int]): Graph[Int, Int] = {
    var remaining_vertices = g_in.numVertices
    var g = g_in

    while (remaining_vertices >= 1) {

      // (Id (degree, current_label))
      val degree_rdd = g.aggregateMessages[(Int, Int)]( 
      triplet => {
        if (triplet.srcAttr == 0)
        {
          if (triplet.dstAttr == 0)
          {
            triplet.sendToDst((1, 0))
            triplet.sendToSrc((1, 0))
          }
          else {
            triplet.sendToSrc((0, 0))
            triplet.sendToDst((-1, triplet.dstAttr))
          }
        }
        else if (triplet.dstAttr == 0){
          triplet.sendToDst((0, 0))
          triplet.sendToSrc((-1, triplet.srcAttr))
        }
        else 
        {
          triplet.sendToSrc((-1, triplet.srcAttr))
          triplet.sendToDst((-1, triplet.dstAttr))
        }
    }, ((a, b) => {
      if (a._1 >= 0) {
        (a._1 + b._1, 0)
      }
      else 
      {
        a
      }
    }) )
    // (vertexId, (degree, randbit, current_label))
    val randbit_rdd = degree_rdd.map(x => {
      if (x._2._1 < 0) 
      {
        (x._1, (x._2._1, 0, x._2._2))
      }
      else 
      {
        (x._1, (x._2._1, 1, x._2._2))
      }
    })
    val g_new = Graph(randbit_rdd, g.edges)
    // val g_finished only has activated vertices
      val updates_rdd = g_new.aggregateMessages[Int](
        triplet => {
          // both already labeled
          if (triplet.srcAttr._3 != 0 && triplet.dstAttr._3 != 0)
          {
            if (triplet.srcAttr._3 == 1)
            {
              triplet.sendToSrc(0)
            }
            if (triplet.dstAttr._3 == 1)
            {
              triplet.sendToDst(0)
            }
          }
          // just source labeled
          else if (triplet.srcAttr._3 != 0)
          {
            if (triplet.srcAttr._3 == 1)
            {
              triplet.sendToSrc(0)
            }
            triplet.sendToDst(0)
          }
          // Just destination labeled
          else if (triplet.dstAttr._3 != 0)
          {
            if (triplet.dstAttr._3 == 1)
            {
              triplet.sendToDst(0)
            }
            triplet.sendToSrc(0)
          }
          // Both unlabeled
          else
          {
            if (triplet.srcAttr._1 >= triplet.dstAttr._1)
            {
              triplet.sendToSrc(1)
              triplet.sendToDst(0)
            }
            else
            {
              triplet.sendToSrc(0)
              triplet.sendToDst(1)
            }
          }
      }, (a, b) => {
        if (a == 0)
        {
          b
        }
        else 
        {
          1
        }
      }).map(x => {
        if (x._2 == 0)
        {
          (x._1, 1)
        }
        else 
        {
          (x._1, 0)
        }
        } )
      val g_almost = Graph(updates_rdd, g.edges)
      val remove_neighbors = g_almost.aggregateMessages[Int](
      (triplet => {
        if (triplet.srcAttr == 1)
        {
          triplet.sendToDst(-1)
          triplet.sendToSrc(1)            
        }
        else if (triplet.dstAttr == 1)
        {
          triplet.sendToSrc(-1)
          triplet.sendToDst(1)
        }
        else
        {
          triplet.sendToSrc(0)
          triplet.sendToDst(0)
        }
      }), ((a, b) => {
        if (a != 0)
        {
          a
        }
        else 
        {
          b
        }
          }) )
    g = Graph(remove_neighbors, g.edges)

    remaining_vertices = g.vertices.map(x => {
    if (x._2 == 0)
    {
      1
    }
    else 
    {
      0
    }
    }).collect.sum
    println(remaining_vertices)
  }
    return g
  }

  def verifyMIS(g_in: Graph[Int, Int]): Boolean = {

    // Checks for vertices not next to an active one
    val rdd1 = g_in.aggregateMessages[Int]( 
      triplet => {
        if (triplet.srcAttr != -1 || triplet.dstAttr != -1) {
          triplet.sendToDst(1)
          triplet.sendToSrc(1)
        }
    }, ((a, b) => 1) )
    val bordered = rdd1.map(x => x._2).collect.sum

    // Checks for activate vertices next to one another
    val collisions_rdd = g_in.triplets.map(x => {
      if (x.srcAttr == 1 && x.dstAttr == 1)
      {
        1
      }
      else 
      {
        0
      }
    })

    if (collisions_rdd.reduce(_+_) > 0 || bordered.toLong != g_in.numVertices){
      return false
    }
    return true
  }


  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("project_3")
    val sc = new SparkContext(conf)
    val spark = SparkSession.builder.config(conf).getOrCreate()
/* You can either use sc or spark */

    if(args.length == 0) {
      println("Usage: project_3 option = {compute, verify}")
      sys.exit(1)
    }
    if(args(0)=="compute") {
      if(args.length != 3) {
        println("Usage: project_3 compute graph_path output_path")
        sys.exit(1)
      }
      val startTimeMillis = System.currentTimeMillis()
      val edges = sc.textFile(args(1)).map(line => {val x = line.split(","); Edge(x(0).toLong, x(1).toLong , 1)} ).filter({case Edge(a,b,c) => a!=b})
      val g = Graph.fromEdges[Int, Int](edges, 0, edgeStorageLevel = StorageLevel.MEMORY_AND_DISK, vertexStorageLevel = StorageLevel.MEMORY_AND_DISK)
      val g2 = DavidMIS(g)
      val endTimeMillis = System.currentTimeMillis()
      val durationSeconds = (endTimeMillis - startTimeMillis) / 1000
      println("==================================")
      println("Luby's algorithm completed in " + durationSeconds + "s.")
      println("==================================")
      val ans = verifyMIS(g2)
      if(ans)
        println("Yes")
      else
        println("No")
      val verts = g2.vertices.filter { case (id, attr) => attr == 1 }
      println(verts.count)
      val g2df = spark.createDataFrame(verts)
      g2df.coalesce(1).write.format("csv").mode("overwrite").save(args(2))
    }
    else if(args(0)=="verify") {
      if(args.length != 3) {
        println("Usage: project_3 verify graph_path MIS_path")
        sys.exit(1)
      }

      val edges = sc.textFile(args(1)).map(line => {val x = line.split(","); Edge(x(0).toLong, x(1).toLong , 1)} ).filter({case Edge(a,b,c) => a!=b})
      val vertices = sc.textFile(args(2)).map(line => {val x = line.split(","); (x(0).toLong, x(1).toInt) })
      val g = Graph[Int, Int](vertices, edges, edgeStorageLevel = StorageLevel.MEMORY_AND_DISK, vertexStorageLevel = StorageLevel.MEMORY_AND_DISK)

      val ans = verifyMIS(g)
      if(ans)
        println("Yes")
      else
        println("No")
    }
    else
    {
        println("Usage: project_3 option = {compute, verify}")
        sys.exit(1)
    }
  }
}
