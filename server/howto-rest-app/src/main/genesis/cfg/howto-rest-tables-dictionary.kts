/**
  * This file defines the entities (or tables) for the application.  
  * Entities aggregation a selection of the universe of fields defined in 
  * {app-name}-fields-dictionary.kts file into a business entity.  
  *
  * Note: indices defined here control the APIs available to the developer.
  * For example, if an entity requires lookup APIs by one or more of its attributes, 
  * be sure to define either a unique or non-unique index.

  * Full documentation on tables may be found here >> https://docs.genesis.global/docs/develop/server-capabilities/data-model/

 */

tables {
    table("ACCOUNTS", id = 10001) {
        field("ACCOUNT_NUMBER", INT).uniqueIndex().primaryKey()
        field("BALANCE", DOUBLE)
        field("BROKER_ID", INT)
        field("CUSTOMER_ID", INT).uniqueIndex()
        field("BROKER_ID", INT)
        field("OWNER", STRING)
    }

    table("TRADE", id = 10002) {
        field("TRADE_ID").sequence("TR").primaryKey()
        field("QUANTITY", INT)
        field("PRICE", DOUBLE)
        field("SIDE", ENUM("BUY", "SELL"))
        field("INSTRUMENT")
        field("ACCOUNT_NUMBER", INT).uniqueIndex()
    }
}
