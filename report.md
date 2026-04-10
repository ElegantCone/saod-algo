# Алгоритмы

Содержание:
- [1 - Хэширование](#Хэширование)
  - [Perfect hashing](#Perfect-hashing)
  - [Extensible hashing](#Extensible-hashing)
  - [Lsh](#Lsh)

## Хэширование

### Perfect hashing
Алгоритм perfect hashing позволяет создавать хэш-таблицы, которые гарантируют O(1) время доступа к элементам. 
Он использует два уровня хэширования: первый для распределения ключей, а второй - для разрешения коллизий.
Таким в каждой корзине находятся элементы с уникальными значениями хэш-функции.

Построение perfect hash-таблицы требует O(n) времени, где n - количество ключей.
![Рисунок 1](/graphs/lab1/perfectBuild_avgt.png)

Все alloc памяти сделаны только для внутренних структур данных
![](/graphs/lab1/alloc/perfect-build.png)

Больше всего CPU расходует вычисление hash-функции
![](/graphs/lab1/cpu/perfect-build.png)

get выполняется за O(1)
![Рисунок 2](/graphs/lab1/perfectGetExisting_avgt.png)

На больших размерах разброс скорости ответов остается около 16мс
![](/graphs/lab1_fix/l2/perfectGetExisting_avgt.png)
Результат бенчмарков:

|Benchmark                                 |      (size)|Mode     |Cnt    |  Score    |Error| Units  |
|------------------------------------------|------------|---------|-------|-----------|-----|--------|
|PerfectHashingBenchmark.perfectGetExisting|        1000|  avgt   |60     |48,175 ±   |0,363|  ns/op |
|PerfectHashingBenchmark.perfectGetExisting|        2000|  avgt   |60     |52,915 ±   |0,453|  ns/op |
|PerfectHashingBenchmark.perfectGetExisting|        3000|  avgt   |60     |54,685 ±   |0,442|  ns/op |
|PerfectHashingBenchmark.perfectGetExisting|        4000|  avgt   |60     |54,647 ±   |0,600|  ns/op | 
|PerfectHashingBenchmark.perfectGetExisting|        5000|  avgt   |60     |55,326 ±   |0,253|  ns/op |
|PerfectHashingBenchmark.perfectGetExisting|        6000|  avgt   |60     |55,427 ±   |0,244|  ns/op |
|PerfectHashingBenchmark.perfectGetExisting|       10000|  avgt   |60     |56,114 ±   |0,209|  ns/op |
|PerfectHashingBenchmark.perfectGetExisting|       15000|  avgt   |60     |57,838 ±   |0,126|  ns/op |
|PerfectHashingBenchmark.perfectGetExisting|       18000|  avgt   |60     |58,235 ±   |0,105|  ns/op |
|PerfectHashingBenchmark.perfectGetExisting|       21000|  avgt   |60     |58,685 ±   |0,117|  ns/op |
|PerfectHashingBenchmark.perfectGetExisting|       25000|  avgt   |60     |62,085 ±   |0,168|  ns/op |
|PerfectHashingBenchmark.perfectGetExisting|       30000|  avgt   |60     |63,336 ±   |0,157|  ns/op |

Аллокаций нет
![](/graphs/lab1/alloc/perfect-get.png)

Больше всего CPU расходует вычисление hash функции
![](/graphs/lab1/cpu/perfect-get.png)

### Extensible hashing
Алгоритм extensible hashing позволяет динамически расширять хэш-таблицу при добавлении новых элементов. 

Вставка осуществляется за O(1), но иногда сильно проседает - вероятно, связано с фоновыми процессами и неидеальным измерением, поскольку видны сильные отклонения от среднего значения. Также может быть связано с проверкой существования ключа в бакете
![Рисунок 3](/graphs/lab1/extensibleInsert_avgt.png)

Память затрачена на создание файлов
![](/graphs/lab1/alloc/extendible-insert.png)

Больше всего CPU уходит на создание файлов и доступ к ним для записи. Можно улучшить буферной записью
![](/graphs/lab1/cpu/extendible-insert.png)

Получение записи выполняется за O(1)
![Рисунок 4](/graphs/lab1/extensibleGetExisting_avgt.png)

Get при рандомном доступе по ключам аллоцирует новые участки памяти через mmap
![](/graphs/lab1/alloc/extendible-get.png)

CPU уходит на работу с буфером и сравнение ключей
![](/graphs/lab1/cpu/extendible-get.png)

### Lsh
Lsh (Locality-Sensitive Hashing) - это алгоритм для поиска похожих элементов в больших наборах данных. 
Он использует хэш-функции, которые сохраняют локальную структуру данных, что позволяет эффективно находить похожие элементы.

Вставка выполняется за O(1)
![Рисунок 1](/graphs/lab1/lshInsert_avgt.png)

Аллокации осуществляются для формирования "полосок". Можно не "мапить" их в процессе вставки, а просматривать биты только при поиске
![](/graphs/lab1/alloc/lsh-insert.png)

Большую часть времени занимает работа с внутренними коллекциями и вычисление хэш-функции
![](/graphs/lab1/cpu/lsh-insert.png)

Поиск выполняется за O(n)
![Рисунок 2](/graphs/lab1/lshGet_avgt.png)

Аллокация осуществляется для найденных соседей искомой ноды. Можно улучшить поменяв порядок обработки - сразу проходиться по полученным точкам без формирования новой мапы
![](/graphs/lab1/alloc/lsh-get.png)

Процессорное время также тратится на формирование новой hashMap и итератор. Путь улучшения - аналогичный памяти
![](/graphs/lab1/cpu/lsh-get.png)
