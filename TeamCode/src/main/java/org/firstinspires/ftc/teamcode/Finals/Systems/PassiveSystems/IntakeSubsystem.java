package org.firstinspires.ftc.teamcode.Finals.Systems.PassiveSystems;


import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

public class IntakeSubsystem implements Subsystem {
    public static final IntakeSubsystem INSTANCE = new IntakeSubsystem();

    private IntakeSubsystem() {}
    public static final MotorEx intakeMotor = new MotorEx("intakeMotor");

    public void intake(double intakePower) {
        intakeMotor.setPower(intakePower);
    }

    @Override
    public void periodic() {

    }
}
