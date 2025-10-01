class Driver(_fio: String, _age: Int, _sp: Int, _dr: Boolean) : Human(_fio, _age, _sp, _dr){

    override fun moveTo(){
        x += speed
        println("${FIO} is moved to $x")
    }

}