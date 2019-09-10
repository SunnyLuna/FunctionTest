package com.decard.zj.founctiontest.proxy

class Main {
    fun main(args: Array<String>) {
        val calculateBrain = CalculateBrain()
        val superCalculate = SuperCalculate(calculateBrain)
        superCalculate.calculate()
    }
}