package org.firstinspires.ftc.teamcode.Finals.Systems.ShooterSystems;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedforward.BasicFeedforwardParameters;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.MotorEx;

public class FlywheelSubsystem implements Subsystem {
    public static final FlywheelSubsystem INSTANCE = new FlywheelSubsystem();

    private FlywheelSubsystem() {}
    public static final MotorEx leftFlyWheelMotor = new MotorEx("leftFlyWheelMotor");
    public static final MotorEx rightFlyWheelMotor = new MotorEx("rightFlyWheelMotor");
    public static final MotorGroup flywheelMotors = new MotorGroup(leftFlyWheelMotor, rightFlyWheelMotor);
    public static PIDCoefficients flywheelPID = new PIDCoefficients(0.01, 0.0, 0.0);
    public static BasicFeedforwardParameters flywheelFF = new BasicFeedforwardParameters(0.00031, 0.0, 0.063);
    public static final ControlSystem flywheelControl = ControlSystem.builder()
            .velPid(flywheelPID)
            .basicFF(flywheelFF)
            .build();

    public boolean flywheelAutoAim = false;

    @Override
    public void periodic() {
        if (flywheelAutoAim) {
            flywheelMotors.setPower(flywheelControl.calculate(flywheelMotors.getState()));
        } else {
            flywheelMotors.setPower(0.35);
        }
    }
}