package com.github.hideto0710.beat_gripper

import com.amazonaws.services.lambda.runtime.{ Context, RequestHandler }
import com.gargoylesoftware.htmlunit.BrowserVersion
import org.openqa.selenium.UnhandledAlertException
import org.openqa.selenium.htmlunit.HtmlUnitDriver

object Beat extends RequestHandler[BeatRequest, BeatResponse] {
  implicit private val driver = new HtmlUnitDriver(BrowserVersion.CHROME)
  private val Url = "https://gripper-user.com/GripperMobile/"

  def main(args: Array[String]): Unit = {
    exec(Attend, "user", "password")
  }

  override def handleRequest(request: BeatRequest, context: Context): BeatResponse = {
    BeatRequestStatus.fromId(request.status) match {
      case Attend => exec(Attend, request.user, request.password)
      case Leave => exec(Leave, request.user, request.password)
      case _ => BeatResponse(BadRequest.code, s"Inputted ${request.status} not supported.")
    }
  }

  private def exec(status: BeatRequestStatus, user: String, password: String): BeatResponse = {
    if (login(user, password)) {
      println("Page title is: " + driver.getTitle)
      val gripperStatus = GripperStatus.fromMessage(driver.findElementById("msg").getText)
      (status, gripperStatus) match {
        case (Attend, NotYet) =>
          clickBtn("btn_s")
          new BeatResponse(Success.code)
        case (Leave, Working) =>
          clickBtn("btn_t")
          new BeatResponse(Success.code)
        case (s, gs) =>
          BeatResponse(BadRequest.code, s"Try changing to $s, but message is ${gs.text}.")
      }
    } else {
      BeatResponse(Unauthorized.code, s"Could not login.")
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
