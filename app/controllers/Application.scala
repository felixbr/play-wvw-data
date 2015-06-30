package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {

  def index = Action {
    Redirect(url = "/api-docs")
  }

  def swagger = Action {
    Ok(views.html.swagger())
  }

}
