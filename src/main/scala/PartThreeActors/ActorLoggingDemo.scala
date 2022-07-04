package PartThreeActors

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.event.{Logging, LoggingAdapter}

object ActorLogging extends App{
  class SimpleActorWithExplicitLogger extends Actor{
    //1- Explicit Logging
    val logger: LoggingAdapter = Logging(context.system,this)
    override def receive: Receive = {
      /**
       * 1 - Debug
       * 2- Info
       * 3-Warning
       * 4-Error
       */
      case message => logger.info(message.toString) //log it (level2)
    }
  }
  val actorSystem = ActorSystem("LoggingDemo")
  val actor = actorSystem.actorOf(Props[SimpleActorWithExplicitLogger]())
  actor ! "Simple Logging Demo Message"
  //2- Actor Logging
  class ActorWithLogging extends Actor with ActorLogging{
    override def receive: Receive = {
      case (a ,b) => log.info("Two Things: {} and {}", a ,b)
      case msg => log.info(msg.toString)
    }

  }
  val actorLog = actorSystem.actorOf(Props[ActorWithLogging]())
  actorLog ! "Simpler Logging Massage With ActorLogging Trait"
  actorLog ! (45,65)

}
