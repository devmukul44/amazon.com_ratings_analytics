package mongo

import com.typesafe.config.{Config, ConfigFactory, ConfigRenderOptions}
import org.bson.Document

/** Utility Function for Mongo
  *
  * Created by Mukul Dev on 3/21/17.
  */
object MongoUtility {

  /** Convert Type Safe Config to Mongo Document
    *
    * @param config Type Safe Config which is to be converted to Document
    * @return Mongo Document converted from Type Safe Config
    */
  def convertConfigToDocument(config: Config): Document = {
    val str = convertConfigToJSONString(config)
    Document.parse(str)
  }

  /** Convert Type Safe Config to JSON String
    *
    * @param config Type Safe Config which is to be converted to Config
    * @param formatted String to be formatted or not
    * @return JSON String converted from Type Safe Config
    */
  def convertConfigToJSONString(config: Config,
                                formatted: Boolean = false): String = {
    config
      .root()
      .render(
        ConfigRenderOptions.concise().setFormatted(formatted).setJson(true))
  }

  /** Convert Mongo Document to Type Safe Config
    *
    * @param document Mongo Document which is to be converted to Type Safe Config
    * @return Type Safe Config converted from Document
    */
  def convertDocumentToConfig(document: Document): Config = {
    ConfigFactory.parseMap(document)
  }

  /** Convert JSON String to Type Safe Config
    *
    * @param jsonString JSON String which is to be converted to Type Safe Config
    * @return Type Safe Config converted from JSON String
    */
  def convertJSONStringToConfig(jsonString: String): Config = {
    ConfigFactory.parseString(jsonString)
  }
}
