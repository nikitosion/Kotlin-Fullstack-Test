/**
 * Faculty user type - extended borrowing privileges
 */
class Faculty(userId: String, name: String, email: String) : User(userId, name, email) {

    override fun getMaxBooks(): Int = 10

    override fun getBorrowDays(): Int = 30
}
