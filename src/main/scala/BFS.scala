import scala.collection.mutable

object UniCoursesBFS {

  type Graph[T] = Map[T, Set[T]]

  def findOrder(n: Int, prerequisites: List[(Int, Int)]): List[Int] = {
    // Inicjalizacja grafu i stopni wejściowych
    val dependencies: Graph[Int] = 
      (0 until n).map(course => (course, Set[Int]())).toMap ++ 
        prerequisites.foldLeft(Map[Int, Set[Int]]()) {
          case (map, (a, b)) => map + (b -> (map.getOrElse(b, Set()) + a))
      }

    val inDegree = Array.fill(n)(0)
    prerequisites.foreach { case (a, b) => inDegree(a) += 1 }

    // Kolejka do przetwarzania wierzchołków o zerowym stopniu wejściowym
    val queue = mutable.Queue[Int]()
    for (i <- 0 until n if inDegree(i) == 0) queue.enqueue(i)

    // Lista wynikowa
    val order = mutable.ListBuffer[Int]()

    // Przetwarzanie wierzchołków
    while (queue.nonEmpty) {
      val course = queue.dequeue()
      order += course
      for (neighbor <- dependencies(course)) {
        inDegree(neighbor) -= 1
        if (inDegree(neighbor) == 0) queue.enqueue(neighbor)
      }
    }

    // Sprawdzenie cyklu
    if (order.size == n) order.toList else List()
  }

  def main(args: Array[String]): Unit = {
    val startTime = System.nanoTime()
    println(findOrder(6, List((0, 1), (2, 0), (3, 0), (4, 1), (5, 4)))) // List(1, 0, 4, 2, 3, 5)
    val endTime = System.nanoTime()
    val duration = (endTime - startTime) / 1e6 // Konwersja na milisekundy
    println(s"Czas wykonania: $duration ms")
  }
}