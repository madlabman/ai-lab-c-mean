package sample

import java.lang.Math.pow
import kotlin.math.sqrt

data class Point(val props: ArrayList<Double>) {

    /*
     * Return length from on point to another one
     */
    fun getLength(p: Point): Double {
        if (p.props.size != this.props.size)  {
            throw Exception("Dimensions count mismatch!")
        }

        var sum = 0.0
        for (i in 0 until props.size) {
            sum += pow((this.props[i] - p.props[i]), 2.0)
        }

        return sqrt(sum)
    }
}