fun main() {

    val person1 = Human("Иванов Иван Иванович", 19, 2, false)
    val person2 = Driver("Максим", 19, 30, true)
    val person3 = Human("Петров Петр Петрович", 22, 3, false)
    val person4 = Human("Сидоров Сидор Сидорович", 25, 4, false)

    val people = listOf(person1, person2, person3, person4)

    val threads = people.map { human ->
        Thread {
            repeat(5) {
                human.moveTo()
                Thread.sleep(500)
            }
        }
    }

    threads.forEach { it.start() }
    threads.forEach { it.join() }
}