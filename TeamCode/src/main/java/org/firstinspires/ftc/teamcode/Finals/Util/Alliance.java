package org.firstinspires.ftc.teamcode.Finals.Util;

import com.pedropathing.geometry.Pose;

public enum Alliance {

    RED(
            new Pose(135.5, 140.0),
            new Pose(8.9, 8.7, Math.toRadians(180.0)),
            new Pose(47 + 8.9, 8.7, Math.toRadians(180.0)),
            new Pose(141.5 - 47 + 8.7, 141.5 - 8.9, Math.toRadians(90.0))
    ),

    BLUE(
            new Pose(6.0, 140.0),
            new Pose(141.5 - 8.9, 8.7, Math.toRadians(0.0)),
            new Pose(141.5 - 47 - 8.9, 8.7, Math.toRadians(0.0)),
            new Pose(47 - 8.7, 141.5 - 8.9, Math.toRadians(90.0))
    );

    public final Pose goalPose;
    public final Pose resetPose1;
    public final Pose resetPose2;
    public final Pose resetPose3;

    // Kotlin 中每个 enum 里定义的 var
    public Pose lastAutoPose = new Pose();

    Alliance(Pose goalPose, Pose resetPose1, Pose resetPose2, Pose resetPose3) {
        this.goalPose = goalPose;
        this.resetPose1 = resetPose1;
        this.resetPose2 = resetPose2;
        this.resetPose3 = resetPose3;
    }
}
