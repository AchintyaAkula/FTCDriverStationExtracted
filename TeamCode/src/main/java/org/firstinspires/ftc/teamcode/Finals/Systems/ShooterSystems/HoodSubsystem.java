package org.firstinspires.ftc.teamcode.Finals.Systems.ShooterSystems;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;

public class HoodSubsystem implements Subsystem {
    public static final HoodSubsystem INSTANCE = new HoodSubsystem();
    public static final double defaultHoodPosition = 0.5;

    private HoodSubsystem() {}
    public final ServoEx hoodServo = new ServoEx("hoodServo");

    @Override
    public void initialize() {
        hoodServo.setPosition(defaultHoodPosition);
    }
}
