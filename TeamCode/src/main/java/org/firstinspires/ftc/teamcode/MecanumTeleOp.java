package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * TeleOp mode for mecanum drive control
 * Use this to test your robot's drive system before running autonomous alignment
 *
 * Controls:
 * - Left stick: Forward/backward and strafe left/right
 * - Right stick X: Rotate left/right
 */
@TeleOp(name = "Mecanum TeleOp", group = "TeleOp")
public class MecanumTeleOp extends LinearOpMode {

    private RobotHardware robot = new RobotHardware();

    @Override
    public void runOpMode() {
        // Initialize hardware
        robot.init(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Controls", "Left stick: move/strafe, Right stick X: rotate");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Get gamepad inputs
            double forward = -gamepad1.left_stick_y;  // Negative because Y stick is reversed
            double strafe = gamepad1.left_stick_x;
            double rotate = gamepad1.right_stick_x;

            // Optional: Add speed control with triggers
            double speedMultiplier = 1.0;
            if (gamepad1.right_trigger > 0.1) {
                speedMultiplier = 0.5; // Slow mode
            } else if (gamepad1.left_trigger > 0.1) {
                speedMultiplier = 1.5; // Fast mode (careful!)
            }

            // Apply speed multiplier
            forward *= speedMultiplier;
            strafe *= speedMultiplier;
            rotate *= speedMultiplier;

            // Drive the robot
            robot.mecanumDrive(forward, strafe, rotate);

            // Display telemetry
            telemetry.addData("Forward", "%.2f", forward);
            telemetry.addData("Strafe", "%.2f", strafe);
            telemetry.addData("Rotate", "%.2f", rotate);
            telemetry.addData("Speed", "%.1fx", speedMultiplier);
            telemetry.update();
        }

        // Stop motors when OpMode ends
        robot.stopAllMotors();
    }
}
