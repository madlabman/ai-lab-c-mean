package sample

import java.lang.Math.pow
import java.util.*
import kotlin.collections.ArrayList

class FCM {

    private val epsilon = 0.000500  // termination criteria
    private val mExp = 3.0          // fuzzyness coefficient

    private val ptsCount = 100      // points count
    private val clsCount = 3        // clusters count
    private val dimCount = 2        // dimensions count

    private val objArray = ArrayList<Point>()
    private val objDegreeMatrix = ArrayList<ArrayList<Double>>()

    private var clusterCentres = ArrayList<Point>()

    /**
     * Calculate relation of object to cluster
     * @var obj - object
     * @var c - cluster index
     */
    private fun calcObjToClusterRel(obj: Point, c: Int): Double {
        var sum = 0.0
        val p = 2 / mExp - 1.0
        for (k in 0 until clsCount) {
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
        for (i in 0 until clsCount) {
            for (j in 0 until ptsCount) {
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
    private fun generateObjSet() {
        // Clear
        objArray.clear()
        objDegreeMatrix.clear()
        clusterCentres.clear()
        // Generate
        val random = Random()
        for (i in 0 until ptsCount) {
            val objDims = ArrayList<Double>()
            for (j in 0 until dimCount) {
                objDims.add(random.nextDouble() * 100)
            }
            objArray.add(Point(objDims))

            val degreeArray = ArrayList<Double>()
            var r = 100.0
            for (k in 0 until clsCount) {
                val rval = (random.nextDouble() * 100) % (r + 1)
                degreeArray.add(rval / 100.0)

                r -= rval
            }
            objDegreeMatrix.add(degreeArray)
        }
    }

    fun calcClusterCentres() {
        clusterCentres = ArrayList(clsCount)

        for (i in 0 until clsCount) {
            val props = ArrayList<Double>()
            for (j in 0 until dimCount) {
                var lsum = 0.0
                var rsum = 0.0
                for (k in 0 until ptsCount) {
                    val elem = pow( objDegreeMatrix[k][i], mExp )
                    lsum += elem.times(objArray[k].props[j])
                    rsum += elem
                }
                props.add(lsum / rsum) // Save state
            }
            clusterCentres.add(Point(props))
        }
    }

    fun run(): ArrayList<ArrayList<Point>> {
        generateObjSet()
        do {
            calcClusterCentres()
            val maxDiff = updateObjDegree()
        } while (maxDiff > epsilon)

        val clusters = ArrayList<ArrayList<Point>>()
        for (k in 0 until clsCount) {
            clusters.add(ArrayList())
        }

        for (i in 0 until ptsCount) {
            val clusterId = objDegreeMatrix[i].indices.maxBy { objDegreeMatrix[i][it] } ?: -1
            clusters[clusterId].add(objArray[i])
        }

        return clusters
    }
}