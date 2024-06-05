object UniCoursesDFS {

  // Define a type alias for a graph where nodes are of type Int and edges are represented as sets of Int
  type Graph[T] = Map[T, Set[T]]

  // Function to find the topological order of courses given the number of courses and their prerequisites
  def findOrder(n: Int, prerequisites: List[(Int, Int)]): List[Int] = {
    /* 
      n = number of courses (count from 0 to n-1)
      prerequisites = List of pairs (prerequisite, dependent)
    */

    // Create a map to represent the dependencies of each course
    // (course -> set of courses that depend on it)
    // e.g. { 0 -> [2, 3], 1 -> [0, 4], 2 -> [], 3 -> [], 4 -> [5], 5 -> []}
    val dependencies: Graph[Int] = 
      (0 until n).map(course => (course, Set[Int]())).toMap ++
        prerequisites.foldLeft(Map[Int, Set[Int]]()) {
          case (map, (a, b)) => map + (b -> (map.getOrElse(b, Set()) + a))
        }

    /* 
      Explanation of dependencies:
      - Course 2 and 3 depend on 0
      - Course 0 and 4 depend on 1
      - No course depends on 2
      - No course depends on 3
      - Course 5 depends on 4
      - No course depends on 5
      
      Example:
      { 0 -> [2, 3], 1 -> [0, 4], 2 -> [], 3 -> [], 4 -> [5], 5 -> []}

      Execution Trace:
      This trace shows the step-by-step evaluation of the `orderTailrec` function.
      Each line represents a call to `orderTailrec` with the current state of its parameters.

      orderTailrec([0 1 2 3 4 5], [], [], [], []) =
      orderTailrec([1 2 3 4 5], [0], [], [] ,[]) =
      orderTailrec([1 2 3 4 5], [2 3 0], [0], [], []) =
      orderTailrec([1 2 3 4 5], [3 0], [0], [2], [2]) =
      orderTailrec([1 2 3 4 5], [0], [0], [2 3], [3 2]) =
      orderTailrec([1 2 3 4 5], [], [], [0 2 3], [0 3 2]) =
      orderTailrec([2 3 4 5], [1], [], [0 2 3], [0 3 2]) =
      orderTailrec([2 3 4 5], [0 4 1], [1], [0 2 3], [0 3 2]) =
      orderTailrec([2 3 4 5], [4 1], [1], [0 2 3], [0 3 2]) =
      orderTailrec([2 3 4 5], [5 4 1], [4 1], [0 2 3], [0 3 2]) =
      orderTailrec([2 3 4 5], [4 1], [4 1], [0 2 3 5], [5 0 3 2]) =
      orderTailrec([2 3 4 5], [1], [1], [0 2 3 4 5], [4 5 0 3 2]) =
      orderTailrec([2 3 4 5], [], [], [0 1 2 3 4 5], [1 4 5 0 3 2]) =
      orderTailrec([3 4 5], [2], [], [0 1 2 3 4 5], [1 4 5 0 3 2]) =
      orderTailrec([3 4 5], [], [], [0 1 2 3 4 5], [1 4 5 0 3 2]) =
      orderTailrec([4 5], [3], [], [0 1 2 3 4 5], [1 4 5 0 3 2]) =
      orderTailrec([4 5], [], [], [0 1 2 3 4 5], [1 4 5 0 3 2]) =
      orderTailrec([5], [4], [], [0 1 2 3 4 5], [1 4 5 0 3 2]) =
      orderTailrec([5], [], [], [0 1 2 3 4 5], [1 4 5 0 3 2]) =
      orderTailrec([], [5], [], [0 1 2 3 4 5], [1 4 5 0 3 2]) =
      orderTailrec([], [], [], [0 1 2 3 4 5], [1 4 5 0 3 2]) =
      [1 4 5 0 3 2]
      
      Complexity: O(N) time, O(N) space
    */

    // Helper function to perform topological sorting using tail recursion
    def orderTailrec(
      remainingCourses: Set[Int], // Set of courses that are yet to be processed
      stack: List[Int], // Stack to keep track of the current path in Deepth-First Search (DFS)
      expanding: Set[Int], // Set of courses currently being expanded (visited in DFS)
      expanded: Set[Int], // Set of courses that have been fully processed
      order: List[Int] // Accumulator for the final topological order
    ): List[Int] = {
      if (stack.isEmpty)
        if (remainingCourses.isEmpty) order // If no more courses to process, return the order
        else orderTailrec(remainingCourses.tail, List(remainingCourses.head), Set(), expanded, order)
      else {
        val course = stack.head
        if (expanded.contains(course))
          // If the course is already in the final order, skip it
          orderTailrec(remainingCourses, stack.tail, expanding, expanded, order)
        else if (expanding.contains(course))
          // If the course is currently being expanded, add it to the final order
          orderTailrec(remainingCourses, stack.tail, expanding - course, expanded + course, course :: order)
        else {
          // Expansion phase: process the current course and its dependencies
          val coursesAfter = dependencies(course) // Get the courses that depend on the current course
          if (coursesAfter.exists(neighborCourse => expanding.contains(neighborCourse)))
            List() // If there's a cycle, return an empty list
          else
            orderTailrec(remainingCourses, coursesAfter.toList ++ stack, expanding + course, expanded, order)
        }
      }
    }

    // Start the topological sorting with all courses and an empty stack
    orderTailrec(dependencies.keySet, List(), Set(), Set(), List())
  }

  // Main function to test the findOrder function with different inputs
  def main(args: Array[String]): Unit = {
    val startTime = System.nanoTime()
    println(findOrder(6, List((0, 1), (2, 0), (3, 0), (4, 1), (5, 4)))) // List(1, 4, 5, 0, 3, 2)
    val endTime = System.nanoTime()
    val duration = (endTime - startTime) / 1e6 // Konwersja na milisekundy
    println(s"Czas wykonania: $duration ms")
  }
}