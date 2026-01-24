package org.firstinspires.ftc.teamcode

import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.nextftc.control.ControlSystem
import dev.nextftc.control.KineticState
import dev.nextftc.core.components.BindingsComponent
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.extensions.pedro.PedroComponent.Companion.follower
import dev.nextftc.extensions.pedro.PedroDriverControlled
import dev.nextftc.ftc.Gamepads
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import dev.nextftc.ftc.components.LoopTimeComponent
import dev.nextftc.hardware.controllable.MotorGroup
import dev.nextftc.hardware.impl.MotorEx
import dev.nextftc.hardware.impl.ServoEx

@TeleOp()
class teleop: NextFTCOpMode() {
    init {
        addComponents(
            BindingsComponent,
            BulkReadComponent,
            LoopTimeComponent(),
            // Change the Constants to wtvr constants it is here
            PedroComponent(Constants::createFollower)
        )
    }

    val goalPose: Pose = Pose(144.0,144.0)
    val fwMotor1: MotorEx = MotorEx("")
    val fwMotor2: MotorEx = MotorEx("")
    val fwMotors = MotorGroup(fwMotor1,fwMotor2)
    // Put in pid vals
    val fwControl = ControlSystem.builder()
        .velPid(0.0,0.0,0.0)
        .basicFF(0.0,0.0,0.0)
        .build()
    var fwTarget = 0.0
    var turretTarget = 0.0
    var autoAim: Boolean = true
    var staticAim = false
    val turretMotor: MotorEx = MotorEx("")
    // Put in PID vals
    val turretControl = ControlSystem.builder()
        .posPid(0.0,0.0,0.0)
        .build()
    val intakeMotor: MotorEx = MotorEx("")
    // use da blocker wherevr u need it
    val blocker: ServoEx = ServoEx("")
    val blocker_open_pos: Double = 0.0
    val blocker_block_pos: Double = 0.2
    override fun onStartButtonPressed() {
        // Update here to be start pose
        follower.setStartingPose(Pose())
        Gamepads.gamepad1.rightTrigger.greaterThan(0.0).or(Gamepads.gamepad1.leftTrigger.greaterThan(0.0))
            .whenTrue { intakeMotor.power = -Gamepads.gamepad1.rightTrigger.get() + Gamepads.gamepad1.leftTrigger.get() }
        Gamepads.gamepad1.rightBumper
            .whenBecomesTrue {
                blocker.position = blocker_open_pos
                intakeMotor.power = 1.0
            }
            .whenBecomesFalse {
                blocker.position = blocker_block_pos
                intakeMotor.power = 0.0
            }
        Gamepads.gamepad1.x
            .whenBecomesTrue { autoAim = !autoAim }
        Gamepads.gamepad1.y
            .whenBecomesTrue {
                autoAim = false
                staticAim = true
            }
        val drivetrain = PedroDriverControlled(
            -Gamepads.gamepad1.leftStickY,
            -Gamepads.gamepad1.leftStickX,
            -Gamepads.gamepad1.rightStickX
        )
        drivetrain.schedule()
        Gamepads.gamepad1.dpadUp
            // Update this here to be reset pose
            .whenBecomesTrue { follower.pose = Pose() }
    }

    override fun onUpdate() {
        // Update this with gear ratio, angle, custom target, wtvr u want
        val currPose = follower.pose
        val targetAng = normalizeAngle(Math.toDegrees(Math.atan2(goalPose.y - currPose.y,goalPose.y - currPose.x) - currPose.heading))
        val distance = currPose.distanceFrom(goalPose)
        turretTarget = (358092.0/289.0) * targetAng
        if (autoAim) {
            // flywheel target, update with any custom sht u want
            fwTarget = 0.0
            fwControl.goal = KineticState(0.0,fwTarget)
            turretControl.goal = KineticState(turretTarget)
            fwMotors.power = fwControl.calculate(fwMotors.state)
        } else if (staticAim) {
            fwControl.goal = KineticState(0.0,fwTarget)
            turretControl.goal = KineticState(turretTarget)
            fwMotors.power = fwControl.calculate(fwMotors.state)
        }
        turretMotor.power = turretControl.calculate(turretMotor.state)
    }
    fun normalizeAngle(ang: Double): Double {
        var a = ang
        if (a < 0.0) {
            a += 360.0
        }
        if (a > 180.0) {
            a -= 360.0
        }
        return a
    }
}
