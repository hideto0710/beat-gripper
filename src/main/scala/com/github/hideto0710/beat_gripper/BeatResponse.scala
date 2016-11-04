package com.github.hideto0710.beat_gripper

import scala.beans.BeanProperty
import skinny.json4s.JSONStringOps._

sealed abstract class BeatResponseStatus(val code: Int)
case object Success extends BeatResponseStatus(200)
case object BadRequest extends BeatResponseStatus(400)
case object Unauthorized extends BeatResponseStatus(401)
case object InternalServerError extends BeatResponseStatus(500)

case class BeatResponse(status: Int, detail: String)
object BeatResponse {
  def apply(status: BeatResponseStatus): BeatResponse = new BeatResponse(status.code, "")
  def apply(status: BeatResponseStatus, detail: String = ""): BeatResponse = {
    new BeatResponse(status.code, detail)
  }
}

case class Response(
  @BeanProperty body: String,
  @BeanProperty headers: Map[String, String],
  @BeanProperty statusCode: Int
)
object Response {
  val contentJson = "Content-Type" -> "application/json"
  def apply(body: Map[String, Any], statusCode: Int) = {
    new Response(toJSONString(body), Map(contentJson), statusCode)
  }
}
