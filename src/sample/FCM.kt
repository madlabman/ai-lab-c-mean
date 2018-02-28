package sample

import java.lang.Math.pow
import java.util.*
import kotlin.collections.ArrayList

class FCM {

    private val epsilon = 0.0
    private val mExp = 2

    private val ptsCount = 0
    private val clsCount = 0

    private val objArray = ArrayList<Point>()
    private val objDegreeMatrix = ArrayList<ArrayList<Double>>()

    private val clusterCentres = ArrayList<Point>()

    /**
     * Calculate relation of object to cluster
     * @var obj - object
     * @var c - cluster index
     */
    private fun calcObjToClusterRel(obj: Point, c: Int): Double {
        var sum = 0.0
        val p = 2 / mExp - 1.0
        for (k in 0 .. clsCount) {
            val r = obj.getLength(clusterCentres[c]) / obj.getLength(clusterCentres[k])
            sum += pow( r, p )
        }

        return 1 / sum
    }

    /**
     * Update degree of objects to clusters
     * @return max difference
     */
    private fun updateObjDegree(): Double {
        var maxDiff = 0.0
        for (i in 0 .. clsCount) {
            for (j in 0 .. ptsCount) {
                val newDegree = calcObjToClusterRel(objArray[j], i)
                val diff = newDegree - objDegreeMatrix[j][i]
                maxDiff = maxOf(maxDiff, diff)
                objDegreeMatrix[j][i] = newDegree
            }
        }

        return maxDiff
    }

    /**
     * Generate random set
     * @var dimensionCount - count of dimensions of points
     */
    private fun generateObjSet(dimensionCount: Int) {
        // Clear
        objArray.clear()
        objDegreeMatrix.clear()
        clusterCentres.clear()
        // Generate
        val random = Random()
        for (i in 0 .. ptsCount) {
            val objDims = ArrayList<Double>()
            for (j in 0 .. dimensionCount) {
                objDims.add(random.nextDouble() * 100)
            }
            objArray.add(Point(objDims))

            val degreeArray = ArrayList<Double>()
            var r = 100.0
            for (k in 0 .. clsCount) {
                val rval = (random.nextDouble() * 100) % (r + 1)
                degreeArray.add(rval / 100.0)

                r -= rval
            }
            objDegreeMatrix.add(degreeArray)
        }
    }



    fun run() {
        generateObjSet(2)
        do {
            // TODO: calc centers
            val maxDiff = updateObjDegree()
        } while (maxDiff > epsilon)
    }
}