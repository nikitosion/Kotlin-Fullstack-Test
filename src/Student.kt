/**
 * Student user type - limited borrowing privileges
 */
class Student(userId: String, name: String, email: String) : User(userId, name, email) {

    override fun getMaxBooks(): Int = 3

    override fun getBorrowDays(): Int = 14
}