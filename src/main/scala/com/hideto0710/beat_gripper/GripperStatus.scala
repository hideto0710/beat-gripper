package com.hideto0710.beat_gripper

sealed abstract class GripperStatus(val text: String)
case object NotYet extends GripperStatus("未だ出勤されていません")
case object Working extends GripperStatus("から出勤中です")
case object Already extends GripperStatus("に退勤しました")
case object NilGripperStatus extends GripperStatus("")

object GripperStatus {
  def fromMessage(message: String) = {
    message match {
      case text if text.endsWith(NotYet.text) => NotYet
      case text if text.endsWith(Working.text) => Working
      case text if text.endsWith(Already.text) => Already
      case _ => NilGripperStatus
    }
  }
}
