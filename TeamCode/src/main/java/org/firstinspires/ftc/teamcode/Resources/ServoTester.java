package org.firstinspires.ftc.teamcode.Resources;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Configurable
@TeleOp
public class ServoTester extends OpMode {

    public Servo testServo;
    public static String configName = "kickerServo";
    public static Double position = 0.5;

    @Override
    public void init() {

    }

    @Override
    public void loop() {
        testServo = hardwareMap.get(Servo.class, configName);
        testServo.setPosition(position);
    }
}
