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