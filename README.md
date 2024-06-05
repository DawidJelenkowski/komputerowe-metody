# Komputerowe metody optymalizacji i wspomagania decyzji

## Problem

Jest n kursów oznaczonych od 0 do n-1 oraz lista zależności między kursami, gdzie każda zależność jest parą (a, b), co oznacza, że kurs A musi być ukończony przed kursem B. 

Zadaniem jest ustalenie kolejności, w jakiej kursy mogą być ukończone, tak aby wszystkie zależności były spełnione. Jeśli nie jest możliwe ukończenie wszystkich kursów (np. z powodu cyklu w zależnościach), algorytm powinien zwrócić pustą listę.

W tym projekcie algorytmy rozwiązują problem ustalania kolejności wykonywania kursów na podstawie ich zależności.

## Zależności instancja 1

Na przykładzie jest graf z 6-oma kursami.

```md
0 -> [2, 3]
1 -> [0, 4]
2 -> []
3 -> []
4 -> [5]
5 -> []
```

#### Wizualizacja

- Start - Zaczynamy od kursu 1.
- Kurs 1 - Ma zależności od kursów 0 i 4.
- Kurs 0 - Ma zależności od kursów 2 i 3.
- Kurs 4 - Ma zależność od kursu 5.
- Kursy 2, 3, 5 - Nie mają żadnych zależnych kursów.

```md
    1
   / \
  0   4
 / \   \
2   3   5
```

## DFS (DFS - Depth-First Search)
### Typ algorytmu

Algorytm sortowania topologicznego przy użyciu przeszukiwania w głąb (DFS - Depth-First Search).


- Złożoność czasowa O(n + m)
- Złożoność przestrzenna O(n + m)

Całkowita złożoność przestrzenna algorytmu wynosi O(n + m), gdzie n to liczba kursów, a m to liczba zależności.

#### Sortowanie topologiczne

Sortowanie topologiczne skierowanego grafu acyklicznego – liniowe uporządkowanie wierzchołków, w którym jeśli istnieje krawędź skierowana prowadząca od wierzchołka x do y , to x znajdzie się przed wierzchołkiem y.

#### Przeszukiwanie w głąb (DFS - Depth-First Search)

Algorytm przeszukiwania grafu, który eksploruje jak najdalej wzdłuż każdej gałęzi przed powrotem (backtracking).

### Opis kodu

#### Wejście
- n: liczba kursów.
- prerequisites: lista par (a, b), gdzie kurs a musi być ukończony przed kursem b.

#### Wyjście
Lista kursów w kolejności, w jakiej mogą być ukończone, spełniając wszystkie zależności. Jeśli nie jest możliwe ukończenie wszystkich kursów, zwróć pustą listę.

#### Reprezentacja grafu
Graf jest reprezentowany jako mapa, gdzie klucze to kursy, a wartości to zbiory kursów, które są bezpośrednio zależne od klucza.

#### Inicjalizacja
Tworzenie mapy zależności na podstawie listy prerequisites.

#### DFS z rekurencją
Użycie rekurencyjnej funkcji orderTailrec do przeszukiwania grafu w głąb. Funkcja śledzi odwiedzone wierzchołki (expanding), wierzchołki, które zostały w pełni przetworzone (expanded), oraz buduje listę uporządkowanych kursów (order).

#### Wykrywanie cykli:
Jeśli podczas przeszukiwania algorytm napotka wierzchołek, który jest już w trakcie eksploracji, oznacza to, że graf zawiera cykl, co uniemożliwia sortowanie topologiczne. W takim przypadku algorytm zwraca pustą listę.

#### Zwracanie wyniku
Jeśli wszystkie kursy mogą być ukończone, algorytm zwraca listę kursów w kolejności topologicznej.


### Przykłady zastosowania algorytmu DFS

#### Planowanie zadań i zarządzanie projektami
Sortowanie topologiczne pozwala ustalić kolejność wykonywania zadań w projekcie, tak aby zadania zależne były realizowane po zadaniach, od których zależą. Dzięki temu można zoptymalizować harmonogram i uniknąć blokowania się zadań

#### Wykrywanie cykli w grafie skierowanym
Jeśli sortowanie topologiczne nie jest możliwe dla danego grafu, oznacza to, że graf zawiera cykl. Próba wykonania sortowania topologicznego z użyciem DFS może więc służyć do wykrywania cykli w grafach skierowanych

#### Kompilacja programów i zarządzanie zależnościami
Sortowanie topologiczne jest używane przez systemy budowania oprogramowania (np. make) do ustalenia kolejności kompilacji modułów z uwzględnieniem zależności między nimi. Pozwala to uniknąć problemów z brakującymi zależnościami

#### Szeregowanie kursów na uczelni
Na podstawie zależności między kursami (wymagane zaliczenie jednych przed innymi) można za pomocą sortowania topologicznego ustalić prawidłową kolejność realizacji kursów przez studentów

#### Wykrywanie zakleszczeń w systemach operacyjnych
Sortowanie topologiczne grafu zależności między procesami ubiegającymi się o zasoby pozwala wykryć potencjalne zakleszczenia w systemie operacyjnym

#### Analiza ścieżki krytycznej w zarządzaniu projektami
Sortowanie topologiczne umożliwia wyznaczenie ścieżki krytycznej projektu, czyli najdłuższej sekwencji zależnych zadań, która determinuje minimalny czas realizacji całego projektu

## BFS - Breadth-First Search (przeszukiwanie wszerz)

### Typ algorytmu

BFS to algorytm używany do przeszukiwania lub przechodzenia przez grafy i drzewa. BFS eksploruje graf poziomami, zaczynając od wierzchołka początkowego i odwiedzając wszystkie jego sąsiednie wierzchołki przed przejściem do wierzchołków na następnym poziomie.

- Złożoność czasowa O(n + m)
- Złożoność przestrzenna O(n + m)

Całkowita złożoność czasowa i przestrzenna algorytmu wynosi O(n + m), gdzie n to liczba kursów, a m to liczba zależności.

### Opis kodu

#### Inicjalizacja
Dodaj wierzchołek początkowy do kolejki i oznacz go jako odwiedzony.

#### Przetwarzanie wierzchołków
  Dopóki kolejka nie jest pusta
  - Usuń wierzchołek z kolejki.
  - Odwiedź wszystkich sąsiadów tego wierzchołka, którzy nie zostali jeszcze odwiedzeni.
  - Dodaj tych sąsiadów do kolejki i oznacz ich jako odwiedzonych.

#### Kontynuacja
Powtarzaj proces, aż wszystkie wierzchołki zostaną odwiedzone lub kolejka będzie pusta.

#### Wizualizacja

```md
    1
   / \
  0   4
 / \   \
2   3   5
```

- Start od 1.
- Odwiedź 1, dodaj 0 i 4 do kolejki.
- Odwiedź 0, dodaj 2 i 3 do kolejki.
- Odwiedź 4, dodaj 5 do kolejki.
- Odwiedź 2 (brak nowych sąsiadów).
- Odwiedź 3 (brak nowych sąsiadów).
- Odwiedź 5 (brak nowych sąsiadów).

Kolejność odwiedzania wierzchołków: 1 -> 0 -> 4 -> 2 -> 3 -> 5

### Przykłady zastosowania algorytmu BFS

#### Znajdowanie najkrótszej ścieżki
Znajdowanie najkrótszej ścieżki w grafie nieskierowanym lub skierowanym, gdzie wszystkie krawędzie mają tę samą wagę. BFS gwarantuje znalezienie najkrótszej ścieżki od wierzchołka początkowego do każdego innego wierzchołka.

#### Przeszukiwanie grafów
Przeszukiwanie wszystkich wierzchołków w grafie lub drzewie. Jest to podstawowa operacja w wielu algorytmach grafowych.

#### Sprawdzanie spójności grafu
Sprawdzanie czy graf jest spójny, czyli czy istnieje ścieżka między dowolnymi dwoma wierzchołkami.

#### Rozwiązywanie problemów logicznych
Rozwiązywanie łamigłówek i problemów logicznych, gdzie celem jest znalezienie rozwiązania w minimalnej liczbie kroków.

#### Generowanie drzew minimalnych
Generowanie drzew minimalnych w grafach nieskierowanych, co jest przydatne w optymalizacji sieci.

#### Wykrywanie cyklów
Wykrywanie cyklów w grafach skierowanych i nieskierowanych.

#### Algorytmy przepływu w sieciach
BFS jest używany w algorytmach przepływu w sieciach, takich jak algorytm Forda-Fulkersona do znajdowania maksymalnego przepływu w sieci.

#### Algorytmy klasteryzacji
BFS może być używany do klasteryzacji wierzchołków w grafach, co jest przydatne w analizie danych i uczeniu maszynowym.

## Instancja 2

Tym razem jest graf z 10-oma kursami.

```md
0 -> [2, 3]
1 -> [0, 4]
2 -> [6]
3 -> [7]
4 -> [5]
5 -> [9]
6 -> [2]
7 -> [0]
8 -> [0]
9 -> [0]
```

#### Wizualizacja instancji 2 krok po kroku

- Start - Zaczynamy od kursu 1.
- Kurs 1 - Ma zależności od kursów 0 i 4.
- Kurs 0 - Ma zależności od kursów 2 i 3.
- Kurs 4 - Ma zależność od kursu 5.
- Kurs 2 - Ma zależność od kursu 6.
- Kurs 3 - Ma zależność od kursu 7.
- Kurs 5 - Ma zależność od kursu 9.
- Kurs 6 - Ma zależność od kursu 8.
- Kursy 8, 7, 9 - Nie mają żadnych zależnych kursów.

```md
    1
   / \
  0   4
 / \ / \
2   3   5
|   |   |
6   7   9
|
8
```
## Wnioski

### Iteracja 1
Średni czas wykonania dla n równego 6 DFS: 0.093951836 ms
Średni czas wykonania dla n równego 6 BFS: 0.02536306 ms
Średnia różnica w czasie w 10 000 iteracjach między algorytmami: 0.06858877599999999 ms

### Iteracja 2
Średni czas wykonania dla n równego 10 DFS: 0.102176766 ms
Średni czas wykonania dla n równego 10 BFS: 0.035778946000000006 ms
Średnia różnica w czasie w 10 000 iteracjach między algorytmami: 0.06639782 ms

Przy zwiększeniu n o 4 dla algorytmu DFS czas wzrósł o 0.087544111431734021674680205292 ms
Przy zwiększeniu n o 4 dla algorytmu BFS czas wzrósł o 0.410671504148159015513112376819 ms
Natomiast średnia różnica w czasie w 10 000 iteracjach między algorytmami zmalała o 0.031943360528842070595247322804 ms
