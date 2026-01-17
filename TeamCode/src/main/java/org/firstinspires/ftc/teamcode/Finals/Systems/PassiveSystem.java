package org.firstinspires.ftc.teamcode.Finals.Systems;

import org.firstinspires.ftc.teamcode.Finals.Systems.PassiveSystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Finals.Systems.PassiveSystems.ShooterGateSubsystem;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.conditionals.IfElseCommand;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.SubsystemGroup;

public class PassiveSystem extends SubsystemGroup {
    public static final PassiveSystem INSTANCE = new PassiveSystem();

    private PassiveSystem() {
        super(
                IntakeSubsystem.INSTANCE,
                ShooterGateSubsystem.INSTANCE
        );
    }

    public static Command blockShooterCommand(){
        return new InstantCommand(ShooterGateSubsystem.INSTANCE::blockGate);
    }
    public static Command releaseShooterCommand(){
        return new InstantCommand(ShooterGateSubsystem.INSTANCE::releaseGate);
    }
    public static Command maxIntakeCommand(){
        return new InstantCommand(() -> IntakeSubsystem.INSTANCE.intake(1.0));
    }

    public static Command mediumIntakeCommand(){
        return new InstantCommand(() -> IntakeSubsystem.INSTANCE.intake(0.4));
    }

    public static Command stopIntakeCommand(){
        return new InstantCommand(() -> IntakeSubsystem.INSTANCE.intake(0));
    }

    public static Command pushBallCommand() {
        return new SequentialGroup(
               releaseShooterCommand(),
                new Delay(0.175),
                blockShooterCommand(),
                new Delay(0.1875)
        );
    }

    public static Command positionBallCommand() {
        return new SequentialGroup(
                maxIntakeCommand(),
                new Delay(0.15),
                stopIntakeCommand()
        );
    }

    public static Command tripleShootSequence(){
        return new SequentialGroup(
                positionBallCommand(),
                pushBallCommand(),
                positionBallCommand(),
                pushBallCommand(),
                maxIntakeCommand(),
                new Delay(0.15),
                pushBallCommand(),
                stopIntakeCommand()
        );
    }

    public static Command altTripleShootSequence(){
        return new SequentialGroup(
          releaseShooterCommand(),
          maxIntakeCommand(),
          new Delay(0.75),
          pushBallCommand(),
          stopIntakeCommand()
        );
    }

    public static Command checkTripleShootSequence(){
        return new IfElseCommand(
                () -> ShooterSystem.INSTANCE.fullAutoAim, // 条件判断
                tripleShootSequence());       // True 执行
    }

    public static Command checkAltTripleShootSequence(){
        return new IfElseCommand(
                () -> ShooterSystem.INSTANCE.fullAutoAim, // 条件判断
                altTripleShootSequence());
    }

    public static void intake(double intakePower) {
        IntakeSubsystem.INSTANCE.intake(intakePower);
    }

}
