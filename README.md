# Komputerowe metody optymalizacji i wspomagania decyzji

## Typ algorytmu

Algorytm sortowania topologicznego przy użyciu przeszukiwania w głąb (DFS - Depth-First Search).

W tym projekcie algorytm rozwiązuje problem ustalania kolejności wykonywania kursów na podstawie ich zależności.

### Sortowanie topologiczne

Sortowanie topologiczne skierowanego grafu acyklicznego – liniowe uporządkowanie wierzchołków, w którym jeśli istnieje krawędź skierowana prowadząca od wierzchołka x do y , to x znajdzie się przed wierzchołkiem y.

### Przeszukiwanie w głąb (DFS - Depth-First Search)

Algorytm przeszukiwania grafu, który eksploruje jak najdalej wzdłuż każdej gałęzi przed powrotem (backtracking).


## Problem

Jest n kursów oznaczonych od 0 do n-1 oraz lista zależności między kursami, gdzie każda zależność jest parą (a, b), co oznacza, że kurs a musi być ukończony przed kursem b. 

Zadaniem jest ustalenie kolejności, w jakiej kursy mogą być ukończone, tak aby wszystkie zależności były spełnione. Jeśli nie jest możliwe ukończenie wszystkich kursów (np. z powodu cyklu w zależnościach), algorytm powinien zwrócić pustą listę.

### Zależności

Na przykładzie jest 6 kursów.

```md
0 -> [2, 3]
1 -> [0, 4]
2 -> []
3 -> []
4 -> [5]
5 -> []
```

### Wizualizacja krok po kroku

- Start: Zaczynamy od kursu 1.
- Kurs 1: Ma zależności od kursów 0 i 4.
- Kurs 0: Ma zależności od kursów 2 i 3.
- Kurs 4: Ma zależność od kursu 5.
- Kursy 2, 3, 5: Nie mają żadnych zależnych kursów.

### Wizualizacja

```md
    1
   / \
  0   4
 / \   \
2   3   5
```

## Opis kodu

### Wejście
- n: liczba kursów.
- prerequisites: lista par (a, b), gdzie kurs a musi być ukończony przed kursem b.

### Wyjście
Lista kursów w kolejności, w jakiej mogą być ukończone, spełniając wszystkie zależności. Jeśli nie jest możliwe ukończenie wszystkich kursów, zwróć pustą listę.

### Reprezentacja grafu
Graf jest reprezentowany jako mapa, gdzie klucze to kursy, a wartości to zbiory kursów, które są bezpośrednio zależne od klucza.

### Inicjalizacja
Tworzenie mapy zależności na podstawie listy prerequisites.

### DFS z rekurencją
Użycie rekurencyjnej funkcji orderTailrec do przeszukiwania grafu w głąb. Funkcja śledzi odwiedzone wierzchołki (expanding), wierzchołki, które zostały w pełni przetworzone (expanded), oraz buduje listę uporządkowanych kursów (order).

### Wykrywanie cykli:
Jeśli podczas przeszukiwania algorytm napotka wierzchołek, który jest już w trakcie eksploracji, oznacza to, że graf zawiera cykl, co uniemożliwia sortowanie topologiczne. W takim przypadku algorytm zwraca pustą listę.

### Zwracanie wyniku
Jeśli wszystkie kursy mogą być ukończone, algorytm zwraca listę kursów w kolejności topologicznej.

## Kod pisany w scala

```scala
object UniversityCourses {

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
    println(findOrder(2, List((0, 1)))) // Expected output: List(1, 0)
    println(findOrder(3, List((0, 1), (1, 2), (2, 0)))) // Expected output: List() (cycle detected)
    println(findOrder(6, List((0, 1), (2, 0), (3, 0), (4, 1), (5, 4)))) // Expected output: List(1, 4, 5, 0, 3, 2)
  }
}
```
## Przykłady zastosowania algorytmu

### Planowanie zadań i zarządzanie projektami
Sortowanie topologiczne pozwala ustalić kolejność wykonywania zadań w projekcie, tak aby zadania zależne były realizowane po zadaniach, od których zależą. Dzięki temu można zoptymalizować harmonogram i uniknąć blokowania się zadań

### Wykrywanie cykli w grafie skierowanym
Jeśli sortowanie topologiczne nie jest możliwe dla danego grafu, oznacza to, że graf zawiera cykl. Próba wykonania sortowania topologicznego z użyciem DFS może więc służyć do wykrywania cykli w grafach skierowanych

### Kompilacja programów i zarządzanie zależnościami
Sortowanie topologiczne jest używane przez systemy budowania oprogramowania (np. make) do ustalenia kolejności kompilacji modułów z uwzględnieniem zależności między nimi. Pozwala to uniknąć problemów z brakującymi zależnościami

### Szeregowanie kursów na uczelni
Na podstawie zależności między kursami (wymagane zaliczenie jednych przed innymi) można za pomocą sortowania topologicznego ustalić prawidłową kolejność realizacji kursów przez studentów

### Wykrywanie zakleszczeń w systemach operacyjnych
Sortowanie topologiczne grafu zależności między procesami ubiegającymi się o zasoby pozwala wykryć potencjalne zakleszczenia w systemie operacyjnym

### Analiza ścieżki krytycznej w zarządzaniu projektami
Sortowanie topologiczne umożliwia wyznaczenie ścieżki krytycznej projektu, czyli najdłuższej sekwencji zależnych zadań, która determinuje minimalny czas realizacji całego projektu
