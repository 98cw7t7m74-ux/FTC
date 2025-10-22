package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

/**
 * FTC Autonomous OpMode: Drive Forward and Align with AprilTag
 *
 * This program:
 * 1. Drives forward for 400 motor rotations
 * 2. Searches for an AprilTag
 * 3. Aligns with the AprilTag (front-to-back and left-to-right)
 *
 * Hardware Configuration:
 * - Mecanum drive motors on ports 0-3
 * - Webcam for AprilTag detection
 */
@Autonomous(name = "Drive & Align Auto", group = "Autonomous")
public class DriveAndAlignAuto extends LinearOpMode {

    // Drive constants
    private static final int TARGET_ROTATIONS = 400;
    private static final double COUNTS_PER_MOTOR_REV = 537.7;  // Example for GoBILDA 312 RPM motor
    private static final double DRIVE_SPEED = 0.6;
    private static final int DRIVE_TARGET_COUNTS = (int)(TARGET_ROTATIONS * COUNTS_PER_MOTOR_REV);

    // Alignment thresholds
    private static final double DISTANCE_THRESHOLD = 2.0; // inches
    private static final double ANGLE_THRESHOLD = 2.0; // degrees
    private static final double X_OFFSET_THRESHOLD = 1.0; // inches

    // Motor power constants for alignment
    private static final double MAX_ALIGN_POWER = 0.5;
    private static final double MIN_ALIGN_POWER = 0.15;

    // Mecanum drive motors (ports 0-3)
    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;

    // Vision components
    private AprilTagProcessor aprilTagProcessor;
    private VisionPortal visionPortal;

    @Override
    public void runOpMode() {
        // Initialize hardware
        initializeHardware();

        // Initialize AprilTag detection
        initializeVision();

        telemetry.addData("Status", "Initialized - Ready to drive and align");
        telemetry.addData("Target Rotations", TARGET_ROTATIONS);
        telemetry.addData("Target Counts", DRIVE_TARGET_COUNTS);
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            // Step 1: Drive forward for 400 rotations
            driveForward();

            // Step 2: Search and align with AprilTag
            alignWithAprilTag();
        }

        // Cleanup
        if (visionPortal != null) {
            visionPortal.close();
        }
    }

    /**
     * Initialize mecanum drive hardware
     */
    private void initializeHardware() {
        // Initialize motors on ports 0-3
        frontLeftMotor = hardwareMap.get(DcMotor.class, "front_left");
        frontRightMotor = hardwareMap.get(DcMotor.class, "front_right");
        backLeftMotor = hardwareMap.get(DcMotor.class, "back_left");
        backRightMotor = hardwareMap.get(DcMotor.class, "back_right");

        // Set motor directions (adjust based on your robot's configuration)
        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);

        // Set zero power behavior
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Reset encoders
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Set motors to run using encoders
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Initialize AprilTag vision processing
     */
    private void initializeVision() {
        // Create AprilTag processor
        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setDrawTagOutline(true)
                .build();

        // Create vision portal
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(aprilTagProcessor)
                .build();
    }

    /**
     * Drive forward for 400 rotations
     */
    private void driveForward() {
        telemetry.addData("Status", "Driving forward...");
        telemetry.update();

        // Set target positions for all motors
        frontLeftMotor.setTargetPosition(DRIVE_TARGET_COUNTS);
        frontRightMotor.setTargetPosition(DRIVE_TARGET_COUNTS);
        backLeftMotor.setTargetPosition(DRIVE_TARGET_COUNTS);
        backRightMotor.setTargetPosition(DRIVE_TARGET_COUNTS);

        // Switch to RUN_TO_POSITION mode
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set motor power
        frontLeftMotor.setPower(DRIVE_SPEED);
        frontRightMotor.setPower(DRIVE_SPEED);
        backLeftMotor.setPower(DRIVE_SPEED);
        backRightMotor.setPower(DRIVE_SPEED);

        // Wait until motors reach target position
        while (opModeIsActive() &&
                (frontLeftMotor.isBusy() || frontRightMotor.isBusy() ||
                 backLeftMotor.isBusy() || backRightMotor.isBusy())) {

            // Display current progress
            telemetry.addData("Status", "Driving Forward");
            telemetry.addData("Front Left", "%d / %d", frontLeftMotor.getCurrentPosition(), DRIVE_TARGET_COUNTS);
            telemetry.addData("Front Right", "%d / %d", frontRightMotor.getCurrentPosition(), DRIVE_TARGET_COUNTS);
            telemetry.addData("Back Left", "%d / %d", backLeftMotor.getCurrentPosition(), DRIVE_TARGET_COUNTS);
            telemetry.addData("Back Right", "%d / %d", backRightMotor.getCurrentPosition(), DRIVE_TARGET_COUNTS);
            telemetry.update();
        }

        // Stop all motors
        stopMotors();

        // Switch back to RUN_USING_ENCODER mode for alignment phase
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Status", "Forward drive complete!");
        telemetry.update();
        sleep(500); // Brief pause before starting alignment
    }

    /**
     * Search for and align with AprilTag
     */
    private void alignWithAprilTag() {
        telemetry.addData("Status", "Searching for AprilTag...");
        telemetry.update();

        boolean aligned = false;
        int consecutiveAlignedFrames = 0;
        final int REQUIRED_ALIGNED_FRAMES = 5;

        while (opModeIsActive() && !aligned) {
            List<AprilTagDetection> detections = aprilTagProcessor.getDetections();

            if (detections.isEmpty()) {
                telemetry.addData("Status", "No AprilTag detected");
                telemetry.addData("Action", "Searching...");
                stopMotors();
            } else {
                // Use the first detected tag
                AprilTagDetection detection = detections.get(0);

                // Get tag position relative to camera
                double x = detection.ftcPose.x; // Lateral offset (strafe left/right)
                double y = detection.ftcPose.y; // Forward/back distance
                double yaw = detection.ftcPose.yaw; // Rotation angle
                double range = detection.ftcPose.range; // Direct distance

                // Calculate alignment errors
                double lateralError = x;
                double forwardError = y;
                double rotationError = yaw;

                // Display telemetry
                telemetry.addData("Tag ID", detection.id);
                telemetry.addData("Range", "%.1f inches", range);
                telemetry.addData("X (Lateral)", "%.1f inches", x);
                telemetry.addData("Y (Forward)", "%.1f inches", y);
                telemetry.addData("Yaw", "%.1f degrees", yaw);
                telemetry.addLine();

                // Check if aligned
                boolean isLateralAligned = Math.abs(lateralError) < X_OFFSET_THRESHOLD;
                boolean isForwardAligned = Math.abs(forwardError) < DISTANCE_THRESHOLD;
                boolean isRotationAligned = Math.abs(rotationError) < ANGLE_THRESHOLD;

                telemetry.addData("Lateral Aligned", isLateralAligned ? "YES" : "NO");
                telemetry.addData("Forward Aligned", isForwardAligned ? "YES" : "NO");
                telemetry.addData("Rotation Aligned", isRotationAligned ? "YES" : "NO");

                if (isLateralAligned && isForwardAligned && isRotationAligned) {
                    consecutiveAlignedFrames++;
                    telemetry.addData("Status", "ALIGNED (%d/%d)", consecutiveAlignedFrames, REQUIRED_ALIGNED_FRAMES);
                    stopMotors();

                    if (consecutiveAlignedFrames >= REQUIRED_ALIGNED_FRAMES) {
                        aligned = true;
                        telemetry.addData("Status", "ALIGNMENT COMPLETE!");
                    }
                } else {
                    consecutiveAlignedFrames = 0;

                    // Calculate motor powers for alignment
                    double strafePower = calculatePower(lateralError, X_OFFSET_THRESHOLD);
                    double forwardPower = calculatePower(forwardError, DISTANCE_THRESHOLD);
                    double rotatePower = calculatePower(rotationError, ANGLE_THRESHOLD);

                    // Apply mecanum drive control
                    mecanumDrive(forwardPower, -strafePower, -rotatePower);

                    telemetry.addData("Status", "Aligning...");
                    telemetry.addData("Forward Power", "%.2f", forwardPower);
                    telemetry.addData("Strafe Power", "%.2f", strafePower);
                    telemetry.addData("Rotate Power", "%.2f", rotatePower);
                }
            }

            telemetry.update();
            sleep(50); // Small delay for stability
        }

        // Stop motors when aligned
        stopMotors();
        telemetry.addData("Final Status", "Alignment Complete!");
        telemetry.update();
    }

    /**
     * Calculate proportional motor power based on error
     */
    private double calculatePower(double error, double threshold) {
        double power = (error / 12.0) * MAX_ALIGN_POWER; // Scale based on 12 inches

        // Clamp power
        power = Math.max(-MAX_ALIGN_POWER, Math.min(MAX_ALIGN_POWER, power));

        // Apply minimum power threshold to overcome static friction
        if (Math.abs(power) > 0 && Math.abs(power) < MIN_ALIGN_POWER) {
            power = Math.signum(power) * MIN_ALIGN_POWER;
        }

        return power;
    }

    /**
     * Mecanum drive control
     * @param forward Forward/backward power (-1 to 1)
     * @param strafe Left/right strafe power (-1 to 1)
     * @param rotate Rotation power (-1 to 1)
     */
    private void mecanumDrive(double forward, double strafe, double rotate) {
        // Calculate wheel powers using mecanum drive kinematics
        double frontLeftPower = forward + strafe + rotate;
        double frontRightPower = forward - strafe - rotate;
        double backLeftPower = forward - strafe + rotate;
        double backRightPower = forward + strafe - rotate;

        // Normalize powers if any exceed 1.0
        double maxPower = Math.max(
            Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower)),
            Math.max(Math.abs(backLeftPower), Math.abs(backRightPower))
        );

        if (maxPower > 1.0) {
            frontLeftPower /= maxPower;
            frontRightPower /= maxPower;
            backLeftPower /= maxPower;
            backRightPower /= maxPower;
        }

        // Set motor powers
        frontLeftMotor.setPower(frontLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backLeftMotor.setPower(backLeftPower);
        backRightMotor.setPower(backRightPower);
    }

    /**
     * Stop all drive motors
     */
    private void stopMotors() {
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);
    }
}
