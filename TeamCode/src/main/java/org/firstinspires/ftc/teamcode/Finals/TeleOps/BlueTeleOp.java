package org.firstinspires.ftc.teamcode.Finals.TeleOps;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.ftc.components.LoopTimeComponent;
import dev.nextftc.hardware.driving.DriverControlledCommand;

import org.firstinspires.ftc.teamcode.Finals.Constants;
import org.firstinspires.ftc.teamcode.Finals.Systems.PassiveSystem;
import org.firstinspires.ftc.teamcode.Finals.Systems.ShooterSystem;
import org.firstinspires.ftc.teamcode.Finals.Util.Alliance;

@TeleOp(name = "Blue TeleOp V1", group = "Final")
public class BlueTeleOp extends NextFTCOpMode {

    /* ---------- static ---------- */

    public static Pose startPose = null;

    /* ---------- instance ---------- */

    private DriverControlledCommand drivetrain;
    private Alliance alliance;

    /* ---------- constructor (代替 Kotlin init {}) ---------- */

    public BlueTeleOp() {
        addComponents(
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new LoopTimeComponent(),
                new SubsystemComponent(
                        PassiveSystem.INSTANCE,
                        ShooterSystem.INSTANCE
                ),
                new PedroComponent(Constants::createFollower)
        );
    }

    /* ---------- lifecycle ---------- */

    @Override
    public void onInit() {

        if (startPose != null) {
            PedroComponent.follower().setStartingPose(startPose);
        } else {
            PedroComponent.follower().setStartingPose(
                    new Pose(72.0, 72.0, Math.toRadians(90.0))
            );
        }

        alliance = Alliance.BLUE;
        ShooterSystem.INSTANCE.setAlliance(alliance);
    }

    @Override
    public void onStartButtonPressed() {

        drivetrain = new PedroDriverControlled(
                () -> -Gamepads.gamepad1().leftStickY().get(),
                () -> -Gamepads.gamepad1().leftStickX().get(),
                () -> -Gamepads.gamepad1().rightStickX().get()
        );


        drivetrain.setScalar(1.0);
        drivetrain.schedule();

        /* ---------- Gamepad bindings ---------- */

        Gamepads.gamepad1().rightBumper()
                .whenBecomesTrue(PassiveSystem.pushBallCommand());

        Gamepads.gamepad1().rightTrigger().greaterThan(0.0)
                .or(Gamepads.gamepad1().leftTrigger().greaterThan(0.0))
                .whenTrue(() ->
                        PassiveSystem.intake(
                                Gamepads.gamepad1().rightTrigger().get()
                                        - Gamepads.gamepad1().leftTrigger().get()
                        )
                )
                .whenBecomesFalse(PassiveSystem.stopIntakeCommand());

        Gamepads.gamepad1().cross()
                .toggleOnBecomesTrue()
                .whenBecomesTrue(ShooterSystem.INSTANCE::autoAimOn)
                .whenBecomesFalse(ShooterSystem.INSTANCE::autoAimOff);

        Gamepads.gamepad1().square()
                .toggleOnBecomesTrue()
                .whenBecomesTrue(() -> drivetrain.setScalar(0.2))
                .whenBecomesFalse(() -> drivetrain.setScalar(1));
    }

    @Override
    public void onUpdate() {
        telemetry.addData("Follower", "x = " + PedroComponent.follower().getPose().getX());
        telemetry.addData("Follower", "y = " + PedroComponent.follower().getPose().getY());
        telemetry.addData("Follower", "heading = " + PedroComponent.follower().getPose().getHeading());
        telemetry.update();
    }

    @Override
    public void onStop() {
        startPose = null;
    }
}