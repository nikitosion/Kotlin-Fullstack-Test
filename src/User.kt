/**
 * Abstract base class for all user types
 */
abstract class User (
    val userId: String,
    val name: String,
    val email: String
) {
    private val _borrowedBooks: MutableList<String> = mutableListOf()

    abstract fun getMaxBooks(): Int
    abstract fun getBorrowDays(): Int

    /**
     * Check if user can borrow more books
     */
    fun canBorrow() : Boolean {
        return _borrowedBooks.size < getMaxBooks()
    }

    /**
     * Add book to user's borrowed list
     */
    fun addBorrowedBook(isbn: String) {
        if (canBorrow()) {
            _borrowedBooks.add(isbn)
        }
    }

    /**
     * Remove book from user's borrowed list
     */
    fun removeBorrowedBook(isbn: String) {
        _borrowedBooks.remove(isbn)
    }

    fun getBorrowedBooks(): List<String> = _borrowedBooks.toList()

    override fun toString(): String {
        val userType = this::class.simpleName
        return "[$userId] $name [$userType] - $email (Books: ${_borrowedBooks.size}/${getMaxBooks()})"
    }
}