# Технический долг. Устранение долга

### Что такое технический долг?
Под техническим долгом понимается осознанное компромиссное решение, когда заказчик и ключевые разработчики четко понимают все преимущества от быстрого, пусть и не идеального технического решения, за которое придется расплатиться позднее. Можно провести параллель между техническим долгом и долгом финансовым. Финансовый долг означает, что вы получаете дополнительные средства сейчас, однако каждый месяц (или каждые пол года) вам придется выплачивать фиксированную процентную ставку, а в конце срока вернуть весь долг кредитору. Аналогичная ситуация происходит и в случае принятия неоптимального технического решения: вы получили готовую систему или новую возможность уже сегодня, однако при добавлении новой возможности вам придется платить «проценты», либо погасить ваш долг путем рефакторинга системы или части системы.

Причины технического долга:
- Давление бизнеса, когда бизнесу требуется выпустить что-то раньше, чем будут сделаны все необходимые изменения, может вылиться в накопление технического долга.
- Отсутствие процессов или понимания, когда бизнес не имеет понятия о технической задолженности и принимает решения без учёта последствий.
- Сильное зацепление компонентов, когда декомпозиция системы выполнена неправильно или недостаточно гибко, чтобы адаптироваться к изменениям бизнес-потребностей.
- Отсутствие тестов — ускоренная разработка и применение быстрых рискованных исправлений («костылей») для исправления ошибок.
- Отсутствие документации, когда код создаётся без необходимой сопроводительной документации. Работа, необходимая для создания вспомогательной документации, — это также долг, который должен быть оплачен.
- Отсутствие взаимодействия между командами, неэффективное управление знаниями в организации. Например, отсутствие наставничества в команде разработчиков.
- Отложенный рефакторинг — чем дольше задерживается рефакторинг, и чем больше написано кода, использующего текущее состояние проекта, тем больше накапливается технический долг, который нужно "оплатить" при следующем рефакторинге.
- Отсутствие опыта, когда разработчики просто не умеют проектировать программные системы или писать качественный код.

### Контроль технического долга на проекте
Подходы к управлению техническим долгом:
- Контроль качества внешними аудиторами, которые будут долго и упорно смотреть на ваш код, а потом сформируют вам огромный отчет по поводу недостатков вашей конфигурации.
- Визуальная проверка кода разработчиками (или code-review). Форма: начальник-стажёр. Стажер задачу решает, приносит код своему наставнику, тот смотрит на этот код и дает своему стажеру некую обратную связь. 
- Разовые автоматизированные отчеты о качестве кода. Например, у вас есть некая система, в которую вы загружаете весь свой код, она вам долго-долго его анализирует, а потом опять-таки выдает огромный отчет. Вы смотрите на этот отчет, но так как у вас перед этой системой нет никаких обязательств, и вы ее запустили именно разово, для того, чтобы увидеть, насколько у вас все плохо, то чаще всего, вы откладываете этот отчет в дальний ящик стола и продолжаете кодировать дальше.
- Непрерывная инспекция. Суть: качество – это общая задача всей команды разработки и результат выполнения этой задачи зависит непосредственно от самих разработчиков, потому что они производят код, за который несут ответственность.

Технический долг как тетрис: выигрыш невозможен. Вы только решаете, насколько быстро проиграть.
Скрытый пропуск в тетрисе представляет собой технический долг.
У любого кода есть технический долг. Это нормально. Вы можете продолжать играть с несколькими пропусками.
Слишком большой технический долг не позволяет за разумное время выпустить новую функцию или исправить ошибку.
Эту проблему не решить добавлением новых разработчиков или, что более драматично, заменой существующих. Это называется техническим долгом: в какой-то момент его придётся выплатить.
Погашение технического долга делает вас конкурентоспособными. Это держит вас в игре.
Слишком много пробелов в тетрисе ведёт к проигрышу.

Проанализировав код проекта по каждому из признаков мы выяснили, что:

| Признак  | Результат анализа  |
| :---| :--- |
| 1. Участки кода, которые переписываются вместе с расширением и масштабированием самого приложения | Участки кода не обнаружены |
| 2. Дублирование кода | Checkstyle подсказывает, что дублирование кода отсутствует. |
| 3. Отсутствие тестов | Код не покрыт тестами. |
| 4. Устаревшие библиотеки и инструменты разработки  |  Используется фреймворк javaFX 8 и Google Maps Static последней версии. |
| 5. Отсутствие стандартов разработки | Код разрабатывается разработчиками, имеющими разные основные умения, поэтому возможны стандарты проектирования, не типичные для Java. |
| 6. Отсутствие технической документации и комментариев |Документация является техическим долгом нашего проекта. |
| 7. Незакоммиченный код и долгоживущие ветки | Присутствуют ветки KlimDev, zhenya, alyohea, master, Archeex. Модель ветвления: [Децентрализованная, но централизованная](https://habr.com/ru/post/106912/). Основная работа проводилась в ветке Archeex, т.к. primary skill разработчика, комитящего в эту ветку - Java. Неиспользованные ветки отсутствуют.|

### План мероприятий по устранению технического долга

- Покрытие кода тестами (13 SP)
- Написание документацией (8 SP)
- Автоматизация тестирования кода (5 SP)

### Оценка плана мероприятий по устранению технического долга
Технический долг составил 26 SP. Акцент стоит на покрытии кода тестами, остальное не сильно влияет на качество продукта ввиду его своеобразности и назначения.

### Выводы и обосновние необходимости устранения долга, коррективы в план разработки проекта

Первоочередными целями на данном этапе является написание тестов и автоматизация тестирования, доработка документации и реализация итогового приложения.