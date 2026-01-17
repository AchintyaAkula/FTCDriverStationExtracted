package org.firstinspires.ftc.teamcode.Finals.Systems.PassiveSystems;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;

public class ShooterGateSubsystem implements Subsystem {
    public static final ShooterGateSubsystem INSTANCE = new ShooterGateSubsystem();

    private ShooterGateSubsystem() {}
    public static final ServoEx shootGateServo = new ServoEx("shootGateServo", -0.1);
    public static final double gateBlockingPosition = 0.2;
    public static final double gateReleasedPosition = 0.3;

    public void blockGate() {
        shootGateServo.setPosition(gateBlockingPosition);
    }

    public void releaseGate() {
        shootGateServo.setPosition(gateReleasedPosition);
    }

    @Override
    public void initialize() {
        shootGateServo.setPosition(gateBlockingPosition);
    }
}
