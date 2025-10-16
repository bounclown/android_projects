import kotlin.math.abs

class Human
{
    var name: String = ""
    var surname: String = ""
    var secondName: String = ""
    var age: Int = -1
    var speed: Int = 0
    var x: Int = 0
    var y: Int = 0

    constructor(_name: String, _surname: String, _second: String, _age: Int, _sp: Int) {
        name = _name
        surname = _surname
        secondName = _second
        age = _age
        speed = _sp
        println("We created the Human object with name: $name")
    }

    fun move() {
        println("${name} is moved")
    }

    fun moveTo() {
        var a: Int = 0
        var b: Int = 0
        while (abs(a) + abs(b) != 5) {
            a = (-(speed)..speed).random()
            b = (-(speed)..speed).random()
        }
        println("${name} is moved ${a} on x and ${b} on y ")
        x += a
        y += b
        println("${name} is moved to $x, $y")
    }
}

fun main() {

    val person: Human = Human("Andrey", "Ivanov", "Petrovich", 19, 5)

    person.move()

    for (i in 0..4) {
        person.moveTo()
    }


}

