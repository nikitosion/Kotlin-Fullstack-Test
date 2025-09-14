
/**
 * Data class representing a book in the library system
 */
data class Book(
    val isbn: String,
    val title: String,
    val author: String,
    val genre: String,
    var isAvailable: Boolean = true
) {

    /**
     * Mark book as borrowed
     */
    fun borrow() {
        isAvailable = false
    }

    /**
     * Mark book as returned
     */
    fun returnBook() {
        isAvailable = true
    }


    override fun toString(): String {
        val status = if (isAvailable) "Available" else "Borrowed"
        return "ISBN:$isbn \"$title\" by $author [$genre] ($status)"
    }
}