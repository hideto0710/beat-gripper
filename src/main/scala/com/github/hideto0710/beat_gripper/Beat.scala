package com.github.hideto0710.beat_gripper

import com.amazonaws.services.lambda.runtime.{ Context, RequestHandler }
import com.gargoylesoftware.htmlunit.BrowserVersion
import org.openqa.selenium.UnhandledAlertException
import org.openqa.selenium.htmlunit.HtmlUnitDriver

object Beat extends RequestHandler[BeatRequest, Response] {
  implicit private val driver = new HtmlUnitDriver(BrowserVersion.CHROME)
  private val Url = "https://gripper-user.com/GripperMobile/"

  def main(args: Array[String]): Unit = {
    exec(Attend, "user", "password")
  }

  override def handleRequest(request: BeatRequest, context: Context): Response = {
    val res = BeatRequestStatus.fromId(request.status) match {
      case Attend => exec(Attend, request.user, request.password)
      case Leave => exec(Leave, request.user, request.password)
      case _ => BeatResponse(BadRequest, s"Inputted ${request.status} not supported.")
    }
    if (res.status != Success.code) throw new Exception(s"${res.status}: ${res.detail}")
    Response(Map("detail" -> res.detail), res.status)
  }

  private def exec(status: BeatRequestStatus, user: String, password: String): BeatResponse = {
    if (login(user, password)) {
      println("Page title is: " + driver.getTitle)
      val msg = driver.findElementById("msg").getText
      val gripperStatus = GripperStatus.fromMessage(msg)
      (status, gripperStatus) match {
        case (Attend, NotYet) =>
          clickBtn("btn_s")
          BeatResponse(Success)
        case (Leave, Working) =>
          clickBtn("btn_t")
          BeatResponse(Success)
        case (s, gs) =>
          BeatResponse(BadRequest, s"Try changing to $s, but message is $msg.")
      }
    } else {
      BeatResponse(Unauthorized, s"Could not login.")
    }
  }

  private def login(user: String, pwd: String): Boolean = {
    println("start login.")
    driver.get(Url)
    println("Page title is: " + driver.getTitle)
    val inputUser = driver.findElementById("user")
    inputUser.sendKeys(user)
    val inputPwd = driver.findElementById("pwd")
    inputPwd.sendKeys(pwd)
    inputPwd.submit()
    driver.getCurrentUrl.endsWith("toppage")
  }

  private def clickBtn(btnId: String) = {
    val btn = driver.findElementById(btnId)
    try {
      btn.click()
    } catch {
      case e: UnhandledAlertException =>
        driver.switchTo.alert.accept()
    }
  }
}
