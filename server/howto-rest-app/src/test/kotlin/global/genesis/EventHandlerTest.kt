/**
  * This file tests the event handler APIs. 
  
  * The events: INSERT, MODIFY and DELETE are tested
  * Full documentation on event handler tests may be found here >> https://docs.genesis.global/docs/develop/server-capabilities/core-business-logic-event-handler/#integration-testing

 */

import global.genesis.db.rx.entity.multi.AsyncEntityDb
import global.genesis.testsupport.client.eventhandler.EventClientSync
import global.genesis.testsupport.jupiter.GenesisJunit
import global.genesis.testsupport.jupiter.ScriptFile
import javax.inject.Inject
import kotlin.String
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(GenesisJunit::class)
@ScriptFile("howto-rest-eventhandler.kts")
class EventHandlerTest {
  @Inject
  lateinit var client: EventClientSync

  @Inject
  lateinit var entityDb: AsyncEntityDb

  private val adminUser: String = "admin"
}
