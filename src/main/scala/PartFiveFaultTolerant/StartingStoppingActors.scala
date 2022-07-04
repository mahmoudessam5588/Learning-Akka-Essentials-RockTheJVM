package PartFiveFaultTolerant

import PartThreeActors.ChildActors.Parent
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, PoisonPill, Props, Terminated}

object StartStoppingActors extends App {
  val actorSystem = ActorSystem("StartStoppingActors")

  object Parent {
    case class StartChild(name: String)

    case class StopChild(name: String)

    case object Stop
  }

  import Parent.*

  class parent extends Actor with ActorLogging {
    override def receive: Receive = withChildren(Map())

    def withChildren(children: Map[String, ActorRef]): Receive = {
      case StartChild(name) =>
        log.info("Starting Child $name")
        context.become(withChildren(children + (name -> context.actorOf(Props[Child](), name))))
      case StopChild(name) =>
        log.info(s"Stopping Child With The Name $name")
        val childrenInfo = children.get(name)
        childrenInfo.foreach(xChildRef => context.stop(xChildRef))
      /*case Stop =>
        log.info("Stopping Myself")
        context.stop(self)
      case message =>
        log.info(message.toString)*/
    }
  }

  class Child extends Actor with ActorLogging {
    override def receive: Receive = {
      case message => log.info(message.toString)
    }
  }

  class Watcher extends Actor with ActorLogging {

    import Parent.*

    override def receive: Receive = {
      case StartChild(name) =>
        val childRefToWatch = context.actorOf(Props[Child](), name)
        log.info(s"Starting And Watching Child $name")
        context.watch(childRefToWatch)
      case Terminated(ref) =>
        log.info(s"The Reference That I'm Watching Called: $ref has been Stopped ")
    }
  }

  /*val watcher = actorSystem.actorOf(Props[Watcher](), "watcher")
  watcher ! StartChild("watchedChild")
  val watchedChild = actorSystem.actorSelection("/user/watcher/watchedChild")
  Thread.sleep(500)
  watchedChild ! PoisonPill*/
  //other methods to stop
  println("__________________________________")
  val parent = actorSystem.actorOf(Props[Parent]())
  parent ! StartChild("child1")
  val child  = actorSystem.actorSelection("/user/parent/child1")
  child ! "Hey!!"
  parent ! StopChild("child1")
  for (_ <- 1 to 50) child ! "are you still there?"

}
