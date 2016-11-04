package com.github.hideto0710.beat_gripper

import scala.beans.BeanProperty

sealed abstract class BeatResponseStatus(val code: Int)
case object Success extends BeatResponseStatus(200)
case object BadRequest extends BeatResponseStatus(400)
case object Unauthorized extends BeatResponseStatus(401)
case object InternalServerError extends BeatResponseStatus(500)

case class BeatResponse(
  @BeanProperty var status: Int,
  @BeanProperty var detail: String
) {
  def this() = this(status = InternalServerError.code, detail = "")
}

object BeatResponse {
  def apply(status: BeatResponseStatus): BeatResponse = new BeatResponse(status.code, "")
  def apply(status: BeatResponseStatus, detail: String = ""): BeatResponse = {
    new BeatResponse(status.code, detail)
  }
}
