package mongo

import com.mongodb.MongoClient
import com.mongodb.client.{MongoCollection, MongoDatabase}
import com.typesafe.config.Config
import org.bson.Document

/** Access Mongo
  *
  * Created by Mukul Dev on 3/21/17.
  */
class Mongo(host: String, port: Int, dbName: String, collectionName: String) {
  private val mongoClient = new MongoClient(host, port)
  private val db = mongoClient.getDatabase(dbName)
  private val collection = db.getCollection(collectionName)
  /** Get Mongo Host Name
    *
    * @return Host Name
    */
  def getHost: String = {
    this.host
  }

  /** Get Mongo Port
    *
    * @return Port
    */
  def getPort: Int = {
    this.port
  }

  /** Get Mongo DB Name
    *
    * @return DB Name
    */
  def getDBName: String = {
    this.dbName
  }

  /** Get Mongo Collection Name
    *
    * @return Collection Name
    */
  def getCollectionName: String = {
    this.collectionName
  }

  /** Get Mongo Client
    *
    * @return Mongo Client
    */
  def getMongoClient: MongoClient = {
    this.mongoClient
  }

  /** Get Mongo Database
    *
    * @return Mongo Database
    */
  def getDB: MongoDatabase = {
    this.db
  }

  /** Get Mongo Collection
    *
    * @return Mongo Collection
    */
  def getCollection: MongoCollection[Document] = {
    this.collection
  }
  def close: Unit ={
    this.mongoClient.close()
  }
}

object Mongo {

  /** Factory Method for getting Mongo object
    *
    * @param host Mongo Host Address
    * @param port Mongo Port
    * @param dbName Mongo Database Name
    * @param collectionName Mongo Collection Name
    * @return
    */
  def apply(host: String,
            port: Int,
            dbName: String,
            collectionName: String): Mongo = {
    new Mongo(host, port, dbName, collectionName)
  }

  /** Factory Method for getting Mongo Object from Type Safe Config Object
    *
    * @param config Type Safe Config object contains information about host, port, DB name and collection name
    * @return Mongo Object
    */
  def apply(config:Config): Mongo = {
    val host = config.getString("host")
    val port = config.getInt("port")
    val dbName = config.getString("dbName")
    val collectionName = config.getString("collectionName")
    apply(host,port,dbName,collectionName)
  }
}