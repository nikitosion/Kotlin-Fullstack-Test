fun main() {
    val console = LibraryConsole()

    try {
        console.run()
    } catch (e: Exception) {
        println("Critical app error:")
        println("${e.message}")
        println("\nApp will be closed.")
    }
}