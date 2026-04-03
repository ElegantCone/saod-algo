# Алгоритмы

Содержание:
- [1 - Хэширование](#Хэширование)
  - [Perfect hashing](#Perfect-hashing)
  - [Extensible hashing](#Extensible-hashing)
  - [Lsh](#Lsh)
- [2 - Деревья (BSP)](#Деревья)

## Хэширование
| Benchmark                                    | (size) | Mode | Cnt | Score        | Error     | Units |
|----------------------------------------------|--------|------|-----|--------------|-----------|-------|
| ExtendibleBenchmark.extensibleGetExisting    | 100    | avgt | 15  | 36,424 ±     | 5,034     | ns/op |
| ExtendibleBenchmark.extensibleGetExisting    | 300    | avgt | 15  | 30,593 ±     | 0,174     | ns/op |
| ExtendibleBenchmark.extensibleGetExisting    | 500    | avgt | 15  | 40,830 ±     | 1,326     | ns/op |
| ExtendibleBenchmark.extensibleGetSameEntry   | 100    | avgt | 15  | 3,335 ±      | 0,009     | ns/op |
| ExtendibleBenchmark.extensibleGetSameEntry   | 300    | avgt | 15  | 3,336 ±      | 0,013     | ns/op |
| ExtendibleBenchmark.extensibleGetSameEntry   | 500    | avgt | 15  | 3,338 ±      | 0,015     | ns/op |
| ExtendibleBenchmark.extensibleInsert         | 100    | avgt | 15  | 73,499 ±     | 12,170    | ns/op |
| ExtendibleBenchmark.extensibleInsert         | 300    | avgt | 15  | 89,388 ±     | 11,458    | ns/op |
| ExtendibleBenchmark.extensibleInsert         | 500    | avgt | 15  | 143,499 ±    | 71,761    | ns/op |
| ExtendibleBenchmark.extensibleRemoveExisting | 100    | avgt | 15  | 3,987 ±      | 0,008     | ns/op |
| ExtendibleBenchmark.extensibleRemoveExisting | 300    | avgt | 15  | 3,987 ±      | 0,006     | ns/op |
| ExtendibleBenchmark.extensibleRemoveExisting | 500    | avgt | 15  | 3,987 ±      | 0,006     | ns/op |
| ExtendibleBenchmark.extensibleUpdateExisting | 100    | avgt | 15  | 33,767 ±     | 0,393     | ns/op |
| ExtendibleBenchmark.extensibleUpdateExisting | 300    | avgt | 15  | 30,282 ±     | 0,329     | ns/op |
| ExtendibleBenchmark.extensibleUpdateExisting | 500    | avgt | 15  | 39,987 ±     | 0,245     | ns/op |
| LshBenchmark.lshGet                          | 100    | avgt | 15  | 3287,375 ±   | 369,662   | ns/op |
| LshBenchmark.lshGet                          | 300    | avgt | 15  | 9551,059 ±   | 947,326   | ns/op |
| LshBenchmark.lshGet                          | 500    | avgt | 15  | 16005,822 ±  | 1398,963  | ns/op |
| LshBenchmark.lshGet                          | 700    | avgt | 15  | 26813,984 ±  | 6536,652  | ns/op |
| LshBenchmark.lshGet                          | 1000   | avgt | 15  | 48990,191 ±  | 13581,072 | ns/op |
| LshBenchmark.lshGet                          | 1300   | avgt | 15  | 45589,544 ±  | 5224,504  | ns/op |
| LshBenchmark.lshGet                          | 1600   | avgt | 15  | 58118,431 ±  | 6232,838  | ns/op |
| LshBenchmark.lshGet                          | 1900   | avgt | 15  | 65885,180 ±  | 6699,664  | ns/op |
| LshBenchmark.lshGet                          | 2100   | avgt | 15  | 74571,098 ±  | 8425,457  | ns/op |
| LshBenchmark.lshGet                          | 2500   | avgt | 15  | 89278,625 ±  | 8561,513  | ns/op |
| LshBenchmark.lshGet                          | 2800   | avgt | 15  | 105686,958 ± | 9199,953  | ns/op |
| LshBenchmark.lshGet                          | 3000   | avgt | 15  | 117560,959 ± | 21045,994 | ns/op |
| LshBenchmark.lshGetExact                     | 100    | avgt | 15  | 3325,206 ±   | 242,935   | ns/op |
| LshBenchmark.lshGetExact                     | 300    | avgt | 15  | 10817,392 ±  | 1543,169  | ns/op |
| LshBenchmark.lshGetExact                     | 500    | avgt | 15  | 17465,019 ±  | 1498,615  | ns/op |
| LshBenchmark.lshGetExact                     | 700    | avgt | 15  | 25030,819 ±  | 3346,248  | ns/op |
| LshBenchmark.lshGetExact                     | 1000   | avgt | 15  | 37194,326 ±  | 3032,356  | ns/op |
| LshBenchmark.lshGetExact                     | 1300   | avgt | 15  | 47305,921 ±  | 5169,456  | ns/op |
| LshBenchmark.lshGetExact                     | 1600   | avgt | 15  | 61113,778 ±  | 6373,928  | ns/op |
| LshBenchmark.lshGetExact                     | 1900   | avgt | 15  | 75682,887 ±  | 6878,161  | ns/op |
| LshBenchmark.lshGetExact                     | 2100   | avgt | 15  | 77056,987 ±  | 7032,904  | ns/op |
| LshBenchmark.lshGetExact                     | 2500   | avgt | 15  | 90885,287 ±  | 11111,691 | ns/op |
| LshBenchmark.lshGetExact                     | 2800   | avgt | 15  | 102985,485 ± | 11149,793 | ns/op |
| LshBenchmark.lshGetExact                     | 3000   | avgt | 15  | 112484,384 ± | 12806,635 | ns/op |
| LshBenchmark.lshInsert                       | 100    | avgt | 15  | 272,439 ±    | 7,267     | ns/op |
| LshBenchmark.lshInsert                       | 300    | avgt | 15  | 260,644 ±    | 12,958    | ns/op |
| LshBenchmark.lshInsert                       | 500    | avgt | 15  | 253,487 ±    | 16,170    | ns/op |
| LshBenchmark.lshInsert                       | 700    | avgt | 15  | 248,903 ±    | 9,617     | ns/op |
| LshBenchmark.lshInsert                       | 1000   | avgt | 15  | 240,514 ±    | 10,031    | ns/op |
| LshBenchmark.lshInsert                       | 1300   | avgt | 15  | 241,732 ±    | 13,090    | ns/op |
| LshBenchmark.lshInsert                       | 1600   | avgt | 15  | 268,864 ±    | 59,236    | ns/op |
| LshBenchmark.lshInsert                       | 1900   | avgt | 15  | 263,953 ±    | 38,201    | ns/op |
| LshBenchmark.lshInsert                       | 2100   | avgt | 15  | 252,690 ±    | 27,732    | ns/op |
| LshBenchmark.lshInsert                       | 2500   | avgt | 15  | 275,806 ±    | 37,070    | ns/op |
| LshBenchmark.lshInsert                       | 2800   | avgt | 15  | 253,605 ±    | 23,144    | ns/op |
| LshBenchmark.lshInsert                       | 3000   | avgt | 15  | 309,115 ±    | 102,057   | ns/op |
| PerfectHashingBenchmark.perfectBuild         | 100    | avgt | 15  | 14555,844 ±  | 222,789   | ns/op |
| PerfectHashingBenchmark.perfectBuild         | 300    | avgt | 15  | 40940,721 ±  | 512,157   | ns/op |
| PerfectHashingBenchmark.perfectBuild         | 500    | avgt | 15  | 73755,504 ±  | 2459,420  | ns/op |
| PerfectHashingBenchmark.perfectBuild         | 700    | avgt | 15  | 97508,410 ±  | 3459,116  | ns/op |
| PerfectHashingBenchmark.perfectBuild         | 1000   | avgt | 15  | 151317,930 ± | 3537,495  | ns/op |
| PerfectHashingBenchmark.perfectBuild         | 1300   | avgt | 15  | 190397,645 ± | 9801,509  | ns/op |
| PerfectHashingBenchmark.perfectBuild         | 1600   | avgt | 15  | 261066,048 ± | 9626,431  | ns/op |
| PerfectHashingBenchmark.perfectBuild         | 1900   | avgt | 15  | 303729,291 ± | 4404,647  | ns/op |
| PerfectHashingBenchmark.perfectBuild         | 2100   | avgt | 15  | 312536,985 ± | 5160,291  | ns/op |
| PerfectHashingBenchmark.perfectBuild         | 2500   | avgt | 15  | 375597,246 ± | 19578,841 | ns/op |
| PerfectHashingBenchmark.perfectBuild         | 2800   | avgt | 15  | 445266,993 ± | 26795,866 | ns/op |
| PerfectHashingBenchmark.perfectBuild         | 3000   | avgt | 15  | 478603,165 ± | 30885,094 | ns/op |
| PerfectHashingBenchmark.perfectGetExisting   | 100    | avgt | 15  | 47,285 ±     | 1,623     | ns/op |
| PerfectHashingBenchmark.perfectGetExisting   | 300    | avgt | 15  | 49,208 ±     | 1,406     | ns/op |
| PerfectHashingBenchmark.perfectGetExisting   | 500    | avgt | 15  | 51,379 ±     | 1,925     | ns/op |
| PerfectHashingBenchmark.perfectGetExisting   | 700    | avgt | 15  | 52,635 ±     | 1,576     | ns/op |
| PerfectHashingBenchmark.perfectGetExisting   | 1000   | avgt | 15  | 52,810 ±     | 0,966     | ns/op |
| PerfectHashingBenchmark.perfectGetExisting   | 1300   | avgt | 15  | 54,549 ±     | 1,179     | ns/op |
| PerfectHashingBenchmark.perfectGetExisting   | 1600   | avgt | 15  | 54,992 ±     | 1,366     | ns/op |
| PerfectHashingBenchmark.perfectGetExisting   | 1900   | avgt | 15  | 55,770 ±     | 1,357     | ns/op |
| PerfectHashingBenchmark.perfectGetExisting   | 2100   | avgt | 15  | 56,152 ±     | 1,914     | ns/op |
| PerfectHashingBenchmark.perfectGetExisting   | 2500   | avgt | 15  | 55,784 ±     | 1,461     | ns/op |
| PerfectHashingBenchmark.perfectGetExisting   | 2800   | avgt | 15  | 54,785 ±     | 0,634     | ns/op |
| PerfectHashingBenchmark.perfectGetExisting   | 3000   | avgt | 15  | 55,401 ±     | 0,790     | ns/op |


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

Аллокаций нет
![](/graphs/lab1/alloc/perfect-get.png)

Больше всего CPU расходует вычисление hash функции
![](/graphs/lab1/cpu/perfect-get.png)

### Extensible hashing
Алгоритм extensible hashing позволяет динамически расширять хэш-таблицу при добавлении новых элементов. 

Вставка осуществляется за O(1)
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