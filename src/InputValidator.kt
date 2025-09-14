object InputValidator {
    fun isValidName(name: String): Boolean {
        return name.isNotBlank() &&
               name.length >= 2 &&
               name.all { it.isLetter() || it.isWhitespace() }
    }

    fun isValidUserId(userId: String): Boolean {
        return userId.isNotBlank() &&
               userId.length >= 3 &&
               userId.all { it.isLetterOrDigit() }
    }

    fun isValidBookTitle(title: String): Boolean {
        return title.isNotBlank() && title.length >= 1
    }

    fun isValidGenre(genre: String): Boolean {
        return genre.isNotBlank() && genre.all { it.isLetter() || it.isWhitespace() }
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return email.matches(emailRegex)
    }

    fun isValidISBN(isbn: String): Boolean {
        val cleanISBN = isbn.replace("-", "").replace(" ", "")
        return cleanISBN.isNotBlank() &&
               cleanISBN.length >= 10 &&
               cleanISBN.all { it.isDigit() || it == 'X' }
    }
}