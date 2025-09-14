/**
 * Console interface for the library management system
 * Fixed with proper input handling and English translations
 */
class LibraryConsole() {
    private val library = Library()

    fun run() {
        while (true) {
            showMenu()
            val choice = getIntInput("Choose section: ", 0..3)

            when (choice) {
                1 -> handleBookManagement()
                2 -> handleUserManagement()
                3 -> handleBorrowing()
                0 -> {
                    println("\nGoodbye! Thanks for using Library Management System!")
                    return
                }
            }
        }
    }

    private fun showMenu() {
        println("\n=== Library Management ===");
        println("1. Book Management");
        println("2. User Management");
        println("3. Borrowing Operations");
        println("0. Exit");
    }

    private fun handleBorrowing() {
        while (true) {
            println("\n=== BORROWING AND RETURN OPERATIONS ===")
            println("1. Borrow book")
            println("2. Return book")
            println("3. View overdue books")
            println("0. Back to main menu")

            val choice = getIntInput("Choose action: ", 0..3)

            when (choice) {
                1 -> borrowBook()
                2 -> returnBook()
                3 -> viewOverdueBooks()
                0 -> return
            }
        }
    }

    private fun borrowBook() {
        println("\nBORROW BOOK")

        val userId = getStringInput("Enter user ID: ")
        val user = library.findUser(userId)

        if (user == null) {
            println("User not found")
            return
        }

        println("User: $user")

        if (!user.canBorrow()) {
            println("User reached borrowing limit (${user.getBorrowedBooks().size}/${user.getMaxBooks()})")
            return
        }

        val isbn = getStringInput("Enter book ISBN: ")
        val book = library.findBook(isbn)

        if (book == null) {
            println("Book not found")
            return
        }

        println("Book: $book")

        if (!book.isAvailable) {
            println("Book is already borrowed")
            return
        }

        if (library.borrowBook(userId, isbn)) {
            println("Book successfully borrowed!")
        } else {
            println("Failed to borrow book")
        }
    }

    private fun returnBook() {
        println("\nRETURN BOOK")

        val userId = getStringInput("Enter user ID: ")
        val user = library.findUser(userId)

        if (user == null) {
            println("User not found")
            return
        }

        val borrowedBooks = user.getBorrowedBooks()
        if (borrowedBooks.isEmpty()) {
            println("User has no borrowed books")
            return
        } else {
            println("User's borrowed books:")
            borrowedBooks.forEachIndexed { index, isbn ->
                val book = library.findBook(isbn)
                println("  ${index + 1}. $book")
            }
        }

        val isbn = getStringInput("Enter book ISBN: ")

        if (library.returnBook(userId, isbn)) {
            println("Book successfully returned!")
        } else {
            println("Failed to return book (check user ID and ISBN)")
        }
    }

    private fun viewOverdueBooks() {
        println("\nOVERDUE BOOKS")
        val overdueBooksList = library.getOverdueBooks()
        if (overdueBooksList.isNotEmpty()) {
            overdueBooksList.forEachIndexed { index, record ->
                println("${index + 1}. $record")
            }
        } else {
            println("No overdue books")
        }
    }


    private fun handleUserManagement() {
        while (true) {
            println("\n=== USER MANAGEMENT ===")
            println("1. Register user")
            println("2. Find user")
            println("3. Show all users")
            println("0. Back to main menu")

            val choice = getIntInput("Choose action: ", 0..3)

            when (choice) {
                1 -> registerUser()
                2 -> findUser()
                3 -> showAllUsers()
                0 -> return
            }
        }
    }

    private fun showAllUsers() {
        println("\nALL USERS")

        val users = library.getAllUsers()
        if (users.isEmpty()) {
            println("No registered users")
            return
        }

        users.sortedBy { it.name }.forEachIndexed { index, user ->
            println("${index + 1}. $user")
        }
    }

    private fun findUser() {
        val userId = getStringInput("Enter user ID: ")
        val user = library.findUser(userId)

        if (user != null) {
            println("\nUser found:")
            println(user)

            val borrowedBooks = user.getBorrowedBooks()
            if (borrowedBooks.isNotEmpty()) {
                println("\nBorrowed books:")
                borrowedBooks.forEach { isbn ->
                    val book = library.findBook(isbn)
                    println("  📖 $book")
                }
            }
        } else {
            println("User with ID '$userId' not found")
        }
    }

    private fun registerUser() {
        println("\nUSER REGISTRATION")
        val name = getValidatedInput(
            "Enter name: ",
            "Name must contain only letters and spaces, minimum 2 characters",
            ValidationType.USER_NAME
        )

        val userId = getValidatedInput(
            "Enter user ID: ",
            "ID must contain only letters and digits, minimum 3 characters",
            ValidationType.USER_ID
        )

        val email = getValidatedInput(
            "Enter email: ",
            "Enter valid email address",
            ValidationType.USER_EMAIL
        )

        println("Select user type:")
        println("1. Student (3 books, 14 days)")
        println("2. Faculty (10 books, 30 days)")
        println("3. Guest (1 book, 7 days)")

        val typeChoice = getIntInput("Enter type number: ", 1..3)
        val userType = when (typeChoice) {
            1 -> UserType.STUDENT
            2 -> UserType.FACULTY
            3 -> UserType.GUEST
            else -> UserType.GUEST
        }

        if (library.registerUser(name, userId, email, userType)) {
            println("User successfully registered!")
            val user = library.findUser(userId)
            println("User: $user")
        } else {
            println("Error: User with this ID already exists")
        }
    }

    private fun handleBookManagement() {
        while (true) {
            println("\n=== BOOK MANAGEMENT ===")
            println("1. Add book")
            println("2. Remove book")
            println("3. Find book by ISBN")
            println("4. Find book by title")
            println("5. Find book by author")
            println("6. Show all books")
            println("0. Back to main menu")

            val choice = getIntInput("Choose action: ", 0..6)

            when (choice) {
                1 -> addBook()
                2 -> removeBook()
                3 -> findBook(BookSearchType.BY_ISBN)
                4 -> findBook(BookSearchType.BY_TITLE)
                5 -> findBook(BookSearchType.BY_AUTHOR)
                6 -> showAllBooks()
                0 -> return
            }
        }
    }

    private fun showAllBooks() {
        println("\nALL BOOKS")

        val books = library.getAllBooks()
        if (books.isEmpty()) {
            println("Library is empty")
            return
        }

        books.sortedBy { it.title }.forEachIndexed { index, book ->
            println("${index + 1}. $book")
        }
    }

    private fun findBook(searchType: BookSearchType) {
        val searchTypeName = when (searchType) {
            BookSearchType.BY_AUTHOR -> "Author"
            BookSearchType.BY_ISBN -> "ISBN"
            BookSearchType.BY_TITLE -> "Title"
        }

        val query = getStringInput("Enter $searchTypeName: ")
        val book = library.findBook(query)

        if (book != null) {
            println("\nBook found:")
            println(book)
        } else {
            println("Book with '$searchTypeName' = '$query' not found")
        }
    }

    private fun removeBook() {
        val isbn = getStringInput("Enter ISBN of book to remove: ")
        val book = library.findBook(isbn)

        if (book == null) {
            println("Book with ISBN '$isbn' not found")
            return
        }

        if (library.removeBook(isbn)) {
            println("Book successfully removed")
        } else {
            println("Failed to remove book (it may be borrowed)")
        }
    }

    private fun addBook() {
        println("\nADD NEW BOOK")

        val title = getValidatedInput(
            "Enter book title: ",
            "Book title cannot be empty",
            ValidationType.BOOK_TITLE
        )

        val author = getValidatedInput(
            "Enter author: ",
            "Author name must be at least 2 characters",
            ValidationType.BOOK_AUTHOR
        )

        val isbn = getValidatedInput(
            "Enter ISBN: ",
            "ISBN must contain at least 10 digits",
            ValidationType.BOOK_ISBN
        )

        val genre = getValidatedInput(
            "Enter genre: ",
            "Genre can contain only letters and spaces",
            ValidationType.BOOK_GENRE
        )

        if (library.addBook(title, author, isbn, genre)) {
            println("Book successfully added!")
            println("\"$title\" by $author [$genre]")
        } else {
            println("Error: Book with this ISBN already exists")
        }
    }

    private fun getValidatedInput(message: String, errorMessage: String, validationType: ValidationType): String {
        while (true) {
            val input = getStringInput(message)

            val validation = when (validationType) {
                ValidationType.BOOK_TITLE -> input.isNotBlank() && input.isNotEmpty()
                ValidationType.BOOK_AUTHOR -> input.isNotBlank() && input.length >= 2
                ValidationType.BOOK_ISBN -> input.isNotBlank() && input.length >= 10

                ValidationType.BOOK_GENRE -> input.isNotBlank() && input.all {
                    it.isLetter() || it.isWhitespace()
                }

                ValidationType.USER_NAME -> input.isNotBlank() && input.length >= 2 && input.all { it.isLetter() || it.isWhitespace() }
                ValidationType.USER_ID -> input.isNotBlank() && input.length >= 3 && input.all { it.isLetterOrDigit() }
                ValidationType.USER_EMAIL -> input.contains("@")
            }
            if (validation) {
                return input.trim()
            } else {
                println(errorMessage)
            }
        }
    }

    private fun getIntInput(message: String, range: IntRange): Int {
        while (true) {
            try {
                println(message)
                val input = readlnOrNull().toString().trim().toInt()
                if (input in range) {
                    return input
                } else {
                    println("Enter number from ${range.first} to ${range.last}")
                }
            } catch (e: NumberFormatException) {
                println("Invalid input. Please enter a number.")
            }
        }
    }

    private fun getStringInput(message: String): String {
        println(message)
        return readlnOrNull()?.trim() ?: ""
    }
}