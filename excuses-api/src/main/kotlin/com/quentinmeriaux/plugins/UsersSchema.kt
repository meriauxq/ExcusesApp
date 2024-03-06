package com.quentinmeriaux.plugins

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class Excuse(val httpCode: Int, val tag: String, val message: String)
class ExcusesService(database: Database) {
    object Excuses : IntIdTable() {
        val httpCode = integer("http_code")
        val tag = varchar("tag", 255)
        val message = varchar("message", 255)
    }

    init {
        // Init the database with the provided data
        val excusesData = listOf(
            Excuse(701, "Inexcusable", "Meh"),
            Excuse(702, "Inexcusable", "Emacs"),
            Excuse(703, "Inexcusable", "Explosion"),
            Excuse(704, "Inexcusable", "Goto Fail"),
            Excuse(705, "Inexcusable", "I wrote the code and missed the necessary validation by an oversight (see 795)"),
            Excuse(706, "Inexcusable", "Delete Your Account"),
            Excuse(707, "Inexcusable", "Can't quit vi"),
            Excuse(710, "Novelty Implementations", "PHP"),
            Excuse(711, "Novelty Implementations", "Convenience Store"),
            Excuse(712, "Novelty Implementations", "NoSQL"),
            Excuse(718, "Novelty Implementations", "I am not a teapot"),
            Excuse(719, "Novelty Implementations", "Haskell"),
            Excuse(720, "Edge Cases", "Unpossible"),
            Excuse(721, "Edge Cases", "Known Unknowns"),
            Excuse(722, "Edge Cases", "Unknown Unknowns"),
            Excuse(723, "Edge Cases", "Tricky"),
            Excuse(724, "Edge Cases", "This line should be unreachable"),
            Excuse(725, "Edge Cases", "It works on my machine"),
            Excuse(726, "Edge Cases", "It's a feature, not a bug"),
            Excuse(727, "Edge Cases", "32 bits is plenty"),
            Excuse(728, "Edge Cases", "It works in my timezone"),
            Excuse(730, "Fucking", "Fucking npm"),
            Excuse(731, "Fucking", "Fucking Rubygems"),
            Excuse(732, "Fucking", "Fucking Unic💩de"),
            Excuse(733, "Fucking", "Fucking Deadlocks"),
            Excuse(734, "Fucking", "Fucking Deferreds"),
            Excuse(736, "Fucking", "Fucking Race Conditions"),
            Excuse(735, "Fucking", "Fucking IE"),
            Excuse(737, "Fucking", "FuckThreadsing"),
            Excuse(738, "Fucking", "Fucking Exactly-once Delivery"),
            Excuse(739, "Fucking", "Fucking Windows"),
            Excuse(738, "Fucking", "Fucking Exactly"),
            Excuse(739, "Fucking", "Fucking McAfee"),
            Excuse(750, "Syntax Errors", "Didn't bother to compile it"),
            Excuse(753, "Syntax Errors", "Syntax Error"),
            Excuse(754, "Syntax Errors", "Too many semi-colons"),
            Excuse(755, "Syntax Errors", "Not enough semi-colons"),
            Excuse(756, "Syntax Errors", "Insufficiently polite"),
            Excuse(757, "Syntax Errors", "Excessively polite"),
            Excuse(759, "Syntax Errors", "Unexpected `T_PAAMAYIM_NEKUDOTAYIM`"),
            Excuse(761, "Substance", "Hungover"),
            Excuse(762, "Substance", "Stoned"),
            Excuse(763, "Substance", "Under-Caffeinated"),
            Excuse(764, "Substance", "Over-Caffeinated"),
            Excuse(765, "Substance", "Railscamp"),
            Excuse(766, "Substance", "Sober"),
            Excuse(767, "Substance", "Drunk"),
            Excuse(768, "Substance", "Accidentally Took Sleeping Pills Instead Of Migraine Pills During Crunch Week"),
            Excuse(771, "Predictable Problems", "Cached for too long"),
            Excuse(772, "Predictable Problems", "Not cached long enough"),
            Excuse(773, "Predictable Problems", "Not cached at all"),
            Excuse(774, "Predictable Problems", "Why was this cached?"),
            Excuse(775, "Predictable Problems", "Out of cash"),
            Excuse(776, "Predictable Problems", "Error on the Exception"),
            Excuse(777, "Predictable Problems", "Coincidence"),
            Excuse(778, "Predictable Problems", "Off By One Error"),
            Excuse(779, "Predictable Problems", "Off By Too Many To Count Error"),
            Excuse(780, "Somebody Else's Problem", "Project owner not responding"),
            Excuse(781, "Somebody Else's Problem", "Operations"),
            Excuse(782, "Somebody Else's Problem", "QA"),
            Excuse(783, "Somebody Else's Problem", "It was a customer request, honestly"),
            Excuse(784, "Somebody Else's Problem", "Management, obviously"),
            Excuse(785, "Somebody Else's Problem", "TPS Cover Sheet not attached"),
            Excuse(786, "Somebody Else's Problem", "Try it now"),
            Excuse(787, "Somebody Else's Problem", "Further Funding Required"),
            Excuse(788, "Somebody Else's Problem", "Designer's final designs weren't"),
            Excuse(789, "Somebody Else's Problem", "Not my department"),
            Excuse(791, "Internet crashed", "The Internet shut down due to copyright restrictions"),
            Excuse(792, "Internet crashed", "Climate change-driven catastrophic weather event"),
            Excuse(793, "Internet crashed", "Zombie Apocalypse"),
            Excuse(794, "Internet crashed", "Someone let PG near a REPL"),
            Excuse(795, "Internet crashed", "#heartbleed (see 705)"),
            Excuse(796, "Internet crashed", "Some DNS fuckery idno"),
            Excuse(797, "Internet crashed", "This is the last page of the Internet. Go back"),
            Excuse(798, "Internet crashed", "I checked the db backups cupboard and the cupboard was bare"),
            Excuse(799, "Internet crashed", "End of the world")
        )

        transaction(database) {
            // Create the table
            SchemaUtils.create(Excuses)

            // Insert all the data
            for (excuse in excusesData) {
                Excuses.insertAndGetId {
                    it[httpCode] = excuse.httpCode
                    it[tag] = excuse.tag
                    it[message] = excuse.message
                }
            }
        }
    }

    /**
     * Utility function to start each query in its own coroutine
     */
    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    /**
     * Utility function to build an [Excuse] from a [ResultRow]
     */
    private fun resultRowToExcuse(row: ResultRow) = Excuse(
        httpCode = row[Excuses.httpCode],
        tag = row[Excuses.tag],
        message = row[Excuses.message],
    )

    /**
     * Insert a new excuse
     */
    suspend fun create(excuse: Excuse): Excuse? = dbQuery {
        val insertStatement = Excuses.insert {
            it[httpCode] = excuse.httpCode
            it[tag] = excuse.tag
            it[message] = excuse.message
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToExcuse)
    }

    /**
     * Get all excuses
     */
    suspend fun readAll(): List<Excuse> {
        return dbQuery {
            Excuses.selectAll()
                .map(::resultRowToExcuse)
        }
    }

    /**
     * Get excuse corresponding to the given [httpCode]
     */
    suspend fun read(httpCode: Int): Excuse? {
        return dbQuery {
            Excuses.select { Excuses.httpCode eq httpCode }
                .map(::resultRowToExcuse)
                .singleOrNull()
        }
    }
}
