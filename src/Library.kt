import java.time.LocalDate

/**
 * Main library class implementing all library operations
 * Fixed architecture with single HashMap per entity type
 */
class Library : LibraryOperations {

    private val booksIsbnToBook: MutableMap<String, Book> = hashMapOf()
    private val booksTitleToBook: MutableMap<String, Book> = hashMapOf()
    private val booksAuthorToBook: MutableMap<String, Book> = hashMapOf()
    private val users: MutableMap<String, User> = hashMapOf()
    private val borrowingHistory: MutableList<BorrowingRecord> = arrayListOf()

    override fun addBook(title: String, author: String, isbn: String, genre: String): Boolean {
        if (booksIsbnToBook.containsKey(isbn)) {
            return false
        }

        val book = Book(isbn, title, author, genre)
        booksIsbnToBook[isbn.lowercase()] = book
        booksAuthorToBook[author.lowercase()] = book
        booksTitleToBook[title.lowercase()] = book

        return true
    }

    override fun removeBook(isbn: String): Boolean {
        val book = booksIsbnToBook[isbn] ?: return false

        if (!book.isAvailable) {
            return false
        }

        booksIsbnToBook.remove(isbn.lowercase())
        booksTitleToBook.remove(book.title.lowercase())
        booksAuthorToBook.remove(book.author.lowercase())
        return true
    }

    override fun findBook(query: String): Book? {
        val preparedQuery = query.lowercase()

        return if (booksIsbnToBook[preparedQuery] != null) {
            booksIsbnToBook[preparedQuery]
        } else if (booksTitleToBook[preparedQuery] != null) {
            booksTitleToBook[preparedQuery]
        } else if (booksAuthorToBook[preparedQuery] != null) {
            booksAuthorToBook[preparedQuery]
        } else
            null
    }

    override fun getAllBooks(): List<Book> = booksIsbnToBook.values.toList()

    override fun registerUser(name: String, userId: String, email: String, type: UserType): Boolean {

        if (users.containsKey(userId)) {
            return false
        }

        val user = when (type) {
            UserType.STUDENT -> Student(userId, name, email)
            UserType.FACULTY -> Faculty(userId, name, email)
            UserType.GUEST -> Guest(userId, name, email)
        }

        users[userId] = user

        return true
    }

    override fun findUser(userId: String): User? = users[userId]

    override fun getAllUsers(): List<User> = users.values.toList()

    override fun borrowBook(userId: String, isbn: String): Boolean {
        val user = users[userId]
        val book = booksIsbnToBook[isbn]

        if (user == null || book == null) return false
        if (!book.isAvailable) return false
        if (!user.canBorrow()) return false

        book.borrow()
        user.addBorrowedBook(isbn)

        val borrowDate = LocalDate.now()
        val record = BorrowingRecord(user, book, borrowDate)
        borrowingHistory.add(record)

        return true
    }

    override fun returnBook(userId: String, isbn: String): Boolean {
        val user = users[userId]
        val book = booksIsbnToBook[isbn]
        val record = borrowingHistory.find { it.user.userId == userId && it.book.isbn == isbn }

        if (user == null || book == null || record == null || book.isAvailable) return false

        user.removeBorrowedBook(isbn)
        book.returnBook()

        return true
    }

    override fun getOverdueBooks(): List<BorrowingRecord> {
        return borrowingHistory.filter { it.isBookOverdue() }
    }
}