interface LibraryOperations {
    // Book management
    fun addBook(title: String, author: String, isbn: String, genre: String): Boolean
    fun removeBook(isbn: String): Boolean
    fun findBook(query: String): Book?
    fun getAllBooks(): List<Book>

    // User management
    fun registerUser(name: String, userId: String, email: String, type: UserType): Boolean
    fun findUser(userId: String): User?
    fun getAllUsers(): List<User>?

    // Borrowing operations
    fun borrowBook(userId: String, isbn: String): Boolean
    fun returnBook(userId: String, isbn: String): Boolean
    fun getOverdueBooks(): List<BorrowingRecord>
}