package com.decard.zj.founctiontest.proxy

class SuperCalculate(val calculate: Calculate) : Calculate {
    override fun calculate(): Int {
        return calculate.calculate()
    }
}