import kotlin.math.abs


open class Human
{
    var FIO: String = ""
    var age: Int = -1
    var speed: Int = 0
    var x: Int = 0
    var y: Int = 0
    var isDriver: Boolean = false

    constructor(_fio: String, _age: Int, _sp: Int, _dr: Boolean) {
        FIO = _fio
        age = _age
        speed = _sp
        isDriver = _dr
        when(isDriver) {
            false -> println("We created the Human object with FIO: $FIO")
            true -> println("We created the Driver object with FIO: $FIO")
        }
    }

    open fun moveTo() {
        var a: Int = 0
        var b: Int = 0
        while (abs(a) + abs(b) != speed) {
            a = (-(speed)..speed).random()
            b = (-(speed)..speed).random()
        }
        println("${FIO} is moved ${a} on x and ${b} on y ")
        x += a
        y += b
        println("${FIO} is moved to $x, $y")
    }
}

class Driver(_fio: String, _age: Int, _sp: Int, _dr: Boolean) : Human(_fio, _age, _sp, _dr){

    override fun moveTo(){
        x += speed
        println("${FIO} is moved to $x")
    }

}


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