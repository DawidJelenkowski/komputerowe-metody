import scala.collection.mutable

object UniCourses {

  type Graph[T] = Map[T, Set[T]]

  // Implementacja DFS
  def findOrderDFS(n: Int, prerequisites: List[(Int, Int)]): List[Int] = {
    val dependencies: Graph[Int] = 
      (0 until n).map(course => (course, Set[Int]())).toMap ++
        prerequisites.foldLeft(Map[Int, Set[Int]]()) {
          case (map, (a, b)) => map + (b -> (map.getOrElse(b, Set()) + a))
        }

    def orderTailrec(
      remainingCourses: Set[Int], 
      stack: List[Int], 
      expanding: Set[Int], 
      expanded: Set[Int], 
      order: List[Int]
    ): List[Int] = {
      if (stack.isEmpty) 
        if (remainingCourses.isEmpty) order
        else orderTailrec(remainingCourses.tail, List(remainingCourses.head), Set(), expanded, order)
      else {
        val course = stack.head
        if (expanded.contains(course))
          orderTailrec(remainingCourses, stack.tail, expanding, expanded, order)
        else if (expanding.contains(course))
          orderTailrec(remainingCourses, stack.tail, expanding - course, expanded + course, course :: order)
        else {
          val coursesAfter = dependencies(course)
          if (coursesAfter.exists(neighborCourse => expanding.contains(neighborCourse)))
            List()
          else
            orderTailrec(remainingCourses, coursesAfter.toList ++ stack, expanding + course, expanded, order)
        }
      }
    }

    orderTailrec(dependencies.keySet, List(), Set(), Set(), List())
  }

  // Implementacja BFS (Kahn's Algorithm)
  def findOrderBFS(n: Int, prerequisites: List[(Int, Int)]): List[Int] = {
    val dependencies: Graph[Int] = (0 until n).map(course => (course, Set[Int]())).toMap ++
      prerequisites.foldLeft(Map[Int, Set[Int]]()) {
        case (map, (a, b)) => map + (b -> (map.getOrElse(b, Set()) + a))
      }
    val inDegree = Array.fill(n)(0)
    prerequisites.foreach { case (a, b) => inDegree(b) += 1 }

    val queue = mutable.Queue[Int]()
    for (i <- 0 until n if inDegree(i) == 0) queue.enqueue(i)

    val order = mutable.ListBuffer[Int]()

    while (queue.nonEmpty) {
      val course = queue.dequeue()
      order += course
      for (neighbor <- dependencies(course)) {
        inDegree(neighbor) -= 1
        if (inDegree(neighbor) == 0) queue.enqueue(neighbor)
      }
    }

    if (order.size == n) order.toList else List()
  }
}


object CompareAlgorithms {

  def measureTime[R](block: => R): Long = {
    val startTime = System.nanoTime()
    block
    val endTime = System.nanoTime()
    endTime - startTime
  }

  def averageTime[R](block: => R, iterations: Int): Double = {
    val times = for (_ <- 1 to iterations) yield measureTime(block)
    times.sum.toDouble / iterations / 1e6 // Konwersja na milisekundy
  }

  def main(args: Array[String]): Unit = {
    val n = 10
    // val prerequisites = List((0, 1), (2, 0), (3, 0), (4, 1), (5, 4))
    val prerequisites = List((0, 1), (2, 0), (3, 0), (4, 1), (5, 4), (6,2), (7,3), (8,6), (9,5))
    val iterations = 1000

    val avgTimeDFS = averageTime(UniCourses.findOrderDFS(n, prerequisites), iterations)
    val avgTimeBFS = averageTime(UniCourses.findOrderBFS(n, prerequisites), iterations)
    val diff = avgTimeDFS - avgTimeBFS

    println(s"Średni czas wykonania DFS: $avgTimeDFS ms")
    println(s"Średni czas wykonania BFS: $avgTimeBFS ms")
    println(s"Średnia różnica w czasie w 10 000 iteracjach między algorytmami: $diff ms")
  }
}