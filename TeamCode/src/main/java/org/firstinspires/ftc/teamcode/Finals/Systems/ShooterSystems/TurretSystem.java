package org.firstinspires.ftc.teamcode.Finals.Systems.ShooterSystems;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedforward.BasicFeedforwardParameters;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

public class TurretSystem implements Subsystem {
    public static final TurretSystem INSTANCE = new TurretSystem();

    private TurretSystem() {}
    public static final MotorEx turretMotor = new MotorEx("turretMotor");
    public static PIDCoefficients turretPID = new PIDCoefficients(0.0135, 0.0, 0.0);
    public static BasicFeedforwardParameters turretFF = new BasicFeedforwardParameters(0.0, 0.0, 0.0);
    public static final ControlSystem turretControl = ControlSystem.builder()
            .posPid(turretPID)
            .basicFF(turretFF)
            .build();

    @Override
    public void periodic() {
        turretMotor.setPower(turretControl.calculate(turretMotor.getState()));
    }
}
