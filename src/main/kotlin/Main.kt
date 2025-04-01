@OptIn(kotlin.ExperimentalStdlibApi::class)
private fun repeatN(n: Int, action: () -> Unit) {
    for (i in 1..<n){
        action()
    }
}

fun main() {
    /*repeatN(n = 5, action = {
    })*/
    var a = 5
    repeatN(n = 5) {
        println("ok")
        a = a + 1
        println(a)
    }
}