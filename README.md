# PlannerokApp
<b><h3> Тестовое техническое задание на должность android разработчика. </h3></b>

Реализованные фичи:
 - авторизация по номеру телефона и коду
 - регистрация
 - просмотр и обновление профиля (в том числе аватарок)
 - состояния загрузки
 - проверка интернет соединения (диалоговое окно с предложением включить сеть, перейдя в активити настроек сети)

Стек технологий:
 - Kotlin
 - Coroutines
 - Android Jetpack Components
 - MVVM
 - Clean Architecture
 - JUnit4
 - DataStore
 - Retrofit

Пример работы приложения:</br>
<a href="https://mega.nz/file/d6ACkD5B#O2P3-DdmAX4A32AO0Z_Wu482wzYRps7pM7OX6CWEx5w">Сценарий когда есть интернет и все норм</a></br>
<a href="https://mega.nz/file/ciRD0aKB#WpihWLwIHALV21tWobv9UVyoZcxDwc4T-YZa-pPHS1Y">Нет интернета</a>

В проекте использованы как сторонние, так и <a href="https://github.com/Alexander1245/BaseMVVM">мои</a> библиотеки. На разработку ушло около 3 дней суммарно. В presentation слое вью моделей присутствует дубляж кода (performAsync) и ивентов, коорые
нужно по хорошему вынести в базовые классы, но горели сроки ;) 
