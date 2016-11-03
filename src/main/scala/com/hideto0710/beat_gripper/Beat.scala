package com.hideto0710.beat_gripper

import scala.collection.JavaConverters._
import com.amazonaws.services.lambda.runtime.Context
import com.gargoylesoftware.htmlunit.BrowserVersion
import org.openqa.selenium.UnhandledAlertException
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import scala.annotation.tailrec

sealed abstract class StatusMessage(val text: String)
case object NotYet extends StatusMessage("未だ出勤されていません")
case object Working extends StatusMessage("から出勤中です")
case object Already extends StatusMessage("に退勤しました")

sealed trait Status
case object Attend extends Status
case object Leave extends Status

object Beat {
  implicit private val driver = new HtmlUnitDriver(BrowserVersion.CHROME)
  private val Url = "https://gripper-user.com/GripperMobile/"

  def main(args: Array[String]): Unit = {
    // val arg = "--user user --password password --status 1"
    // val arg = "--user user --password password --status -1"
    exec(Attend, "user", "password")
  }

  def lambda(arg: String, context: Context): java.util.List[String] = {
    val options = nextOption(arg.split(" ").toList)
    val user = options('user)
    val password = options('password)
    val status = options('status).toInt

    if (status == 1) {
      exec(Attend, user, password)
    } else if (status == -1) {
      exec(Leave, user, password)
    } else {
      throw new Exception(s"Inputted $arg not supported.")
    }
    List("success").asJava
  }

  private type OptionMap = Map[Symbol, String]
  private val argsPattern = """--(.+)""".r

  @tailrec
  def nextOption(list: List[String], map: OptionMap = Map()): OptionMap = list match {
    case argsPattern(p) :: value :: tail =>
      nextOption(tail, map ++ Map(Symbol(p) -> value))
    case unknownOption :: tail =>
      nextOption(tail, map)
    case _ => map
  }

  private def exec(status: Status, user: String, password: String) = {
    if (login(user, password)) {
      println("Page title is: " + driver.getTitle)
      val msg = driver.findElementById("msg").getText
      status match {
        case Attend if msg.endsWith(NotYet.text) =>
          changeStatus(status)
        case Leave if msg.endsWith(Working.text) =>
          changeStatus(status)
        case s =>
          throw new Exception(s"Try changing to $s, but message is $msg.")
      }
    } else {
      throw new Exception(s"Could not login.")
    }
  }

  private def login(user: String, pwd: String)(implicit driver: HtmlUnitDriver): Boolean = {
    println("start login.")
    driver.get(Url)
    println("Page title is: " + driver.getTitle)
    // input text
    val inputUser = driver.findElementById("user")
    inputUser.sendKeys(user)
    val inputPwd = driver.findElementById("pwd")
    inputPwd.sendKeys(pwd)
    // execute
    inputPwd.submit()
    // result
    driver.getCurrentUrl.endsWith("toppage")
  }

  private def changeStatus(status: Status) = {
    val btnId = status match {
      case Attend => "btn_s"
      case Leave => "btn_t"
    }
    val btn = driver.findElementById(btnId)
    try {
      btn.click()
    } catch {
      case e: UnhandledAlertException =>
        val alert = driver.switchTo.alert
        alert.accept()
    }
  }
}
