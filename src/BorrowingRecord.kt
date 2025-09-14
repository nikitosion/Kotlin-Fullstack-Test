import java.time.LocalDate
/**
 * Represents a borrowing transaction record
 */
data class BorrowingRecord (
    val user: User,
    val book: Book,
    val borrowDate: LocalDate,
    var actualReturnDate: LocalDate? = null
) {
    /**
     * Check if the book is overdue
     */
    fun isBookOverdue() : Boolean {
        if (actualReturnDate == null && LocalDate.now() > borrowDate.plusDays(user.getBorrowDays().toLong())) {
            return true
        }

        return false
    }

    override fun toString(): String {
        return "${book.title} → ${user.name} | Borrowed: $borrowDate | Returned: $actualReturnDate"
    }
}
