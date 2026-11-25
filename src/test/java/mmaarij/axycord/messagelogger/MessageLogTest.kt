package mmaarij.axycord.messagelogger

import org.junit.Assert.*
import org.junit.Test

class MessageLogTest {
    @Test fun `editing and deleting retain a stable history snapshot`() {
        val log = MessageLog()
        log.addEdit(1, 10, "first", 100)
        val snapshot = requireNotNull(log.get(1))
        log.addEdit(1, 10, "second", 200)
        log.markDeleted(1, 10, 300)
        assertEquals(listOf("first"), snapshot.edits.map { it.content })
        assertNull(snapshot.deletedAt)
        assertEquals(listOf("first", "second"), requireNotNull(log.get(1)).edits.map { it.content })
        assertEquals(300L, log.get(1)?.deletedAt)
    }

    @Test fun `clear returns only retained deletions and forgets all edits`() {
        val log = MessageLog()
        log.markDeleted(1, 10, 100)
        log.addEdit(2, 20, "old text", 200)
        assertEquals(mapOf(1L to 10L), log.clear())
        assertNull(log.get(1))
        assertNull(log.get(2))
        assertFalse(log.isDeleted(1))
    }

    @Test fun `capacity evicts the oldest message and remove affects only that message`() {
        val log = MessageLog(capacity = 2)
        log.markDeleted(1, 10, 100)
        log.markDeleted(2, 10, 200)
        log.addEdit(3, 10, "third", 300)
        assertNull(log.get(1))
        assertNotNull(log.remove(2))
        assertFalse(log.isDeleted(2))
        assertEquals("third", log.get(3)?.edits?.single()?.content)
    }
}
