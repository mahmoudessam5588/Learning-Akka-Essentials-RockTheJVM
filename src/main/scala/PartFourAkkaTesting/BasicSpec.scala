package PartFourAkkaTesting

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.WordSpec

class BAsicSpec extends TestKit(ActorSystem("BasicSpec")) with ImplicitSender
with WordSpecLike{

}
