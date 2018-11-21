package com.wavesplatform.datafeed

import com.wavesplatform.datafeed.settings.WDFSettings
import com.wavesplatform.datafeed.utils._
import play.api.libs.json._
import scalaj.http._
import scala.util.{Try, Success, Failure}


class NodeApiWrapper(settings: WDFSettings) extends Logging {

  val MaxConnTimeout = 60000
  val MaxReadTimeout = 60000
  val RetryDelay = 10000L

  var retryCounter = 0L

  def get(request: String): JsValue =
    if (retryCounter == 0 || (System.currentTimeMillis - retryCounter) > RetryDelay)
      Try {
        //println("settings.nodes.head {" + settings.nodes.head + "}")
        //println("request {" + request + "}")
        //println("https://" + settings.nodes.head + request)
        Json.parse(Http("https://" + settings.nodes.head + request).options(HttpOptions.connTimeout(MaxConnTimeout),
          HttpOptions.readTimeout(MaxReadTimeout)).asString.body)
      } match {
        case Success(s) =>
          retryCounter = 0L
          s
        case Failure(e) =>
          //println("e" + e)
          log.error("Unable to connect to node " + settings.nodes.head + " - Retrying in 10 seconds : " +settings.nodes.head + request )
          retryCounter = System.currentTimeMillis
          JsNull
      } else JsNull

}

object NodeApiWrapper {

  def apply(settings: WDFSettings) = new NodeApiWrapper(settings)

}