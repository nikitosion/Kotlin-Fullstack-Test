/**
 * Guest user type - minimal borrowing privileges
 */
class Guest(userId: String, name: String, email: String) : User(userId, name, email) {

    override fun getMaxBooks(): Int = 1

    override fun getBorrowDays(): Int = 7
}