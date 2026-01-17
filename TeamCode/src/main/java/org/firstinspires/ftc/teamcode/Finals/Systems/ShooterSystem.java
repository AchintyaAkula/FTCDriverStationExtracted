package org.firstinspires.ftc.teamcode.Finals.Systems;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Finals.Systems.ShooterSystems.FlywheelSubsystem;
import org.firstinspires.ftc.teamcode.Finals.Systems.ShooterSystems.HoodSubsystem;
import org.firstinspires.ftc.teamcode.Finals.Systems.ShooterSystems.TurretSystem;
import org.firstinspires.ftc.teamcode.Finals.Util.Alliance;

import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.SubsystemGroup;

import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.ActiveOpMode;

public class ShooterSystem extends SubsystemGroup {
    public static final ShooterSystem INSTANCE = new ShooterSystem();

    private ShooterSystem() {
        super(
                FlywheelSubsystem.INSTANCE,
                HoodSubsystem.INSTANCE,
                TurretSystem.INSTANCE
        );
    }
    private Pose goalPose;
    public boolean fullAutoAim = false;
    public double velocity = 1000;
    public static final double turretTicksPerRev = (38450.0 / 24.0);
    public static final double velCorrection = 0.8;
    public static final double fwVelCorrection = 0.5;
    public static Command autoAimOnCommand(){
        return new InstantCommand(INSTANCE::autoAimOn);
    }

    public static Command autoAimOffCommand(){
        return new InstantCommand(INSTANCE::autoAimOff);
    }

    public void calibrateFlywheel() {
        FlywheelSubsystem.flywheelControl.setGoal(
                new KineticState(0.0, velocity)
        );
    }

    public void calibrateHood(Pose currPose) {
        double distanceFromGoal = currPose.distanceFrom(goalPose);

        if (distanceFromGoal < 63.0) {
            HoodSubsystem.INSTANCE.hoodServo.setPosition(0.0);
        } else if (distanceFromGoal < 105.0) {
            HoodSubsystem.INSTANCE.hoodServo.setPosition(0.65);
        } else {
            HoodSubsystem.INSTANCE.hoodServo.setPosition(1.0);
        }
    }

    public void calibrateTurret(){
        TurretSystem.turretControl.setGoal(
                new KineticState(Math.round(300.2))
        );
    }

    public void autoAimOn() {
        fullAutoAim = true;
        FlywheelSubsystem.INSTANCE.flywheelAutoAim = true;
    }

    public void autoAimOff() {
        fullAutoAim = false;
        FlywheelSubsystem.INSTANCE.flywheelAutoAim = false;
    }

    public void setAlliance(Alliance alliance){
        this.goalPose = alliance.goalPose;
    }

    //setAlliance will be written after
    @Override
    public void periodic(){
        if(fullAutoAim){
            Vector velVector =
                    PedroComponent.follower()
                            .getVelocity()
                            .times(fwVelCorrection);

            Pose predictedPose = new Pose(
                    PedroComponent.follower().getPose().getX()
                            + velVector.getXComponent(),
                    PedroComponent.follower().getPose().getY()
                            + velVector.getYComponent(),
                    PedroComponent.follower().getPose().getHeading()
            );
            calibrateHood(predictedPose);
        }
    }
}