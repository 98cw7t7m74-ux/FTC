# FTC AprilTag Alignment Program

This FTC program enables autonomous alignment with AprilTags using a mecanum drive system.

## Hardware Configuration

### Robot Specifications
- **Drive System**: Mecanum wheels
- **Motors**: 4 motors on ports 0-3
- **Camera**: Mounted at 3 inches from the ground
- **Target**: AprilTag at 24 inches (2 feet) from the ground

### Motor Port Configuration
Configure your motors in the FTC Robot Controller app with these names:

| Port | Motor Name    | Position      |
|------|---------------|---------------|
| 0    | front_left    | Front Left    |
| 1    | front_right   | Front Right   |
| 2    | back_left     | Back Left     |
| 3    | back_right    | Back Right    |

### Camera Configuration
- **Device Name**: "Webcam 1"
- **Height**: 3 inches from ground
- **Recommended**: Mount camera facing forward with clear view

## Setup Instructions

### 1. FTC SDK Requirements
This code requires:
- FTC SDK version 8.1 or higher (for AprilTag support)
- Android Studio with FTC plugin
- FTC Robot Controller app installed on Robot Controller phone
- FTC Driver Station app installed on Driver Station phone

### 2. Import This Code
1. Clone this repository into your FTC project
2. Open the project in Android Studio
3. Build the project to verify no errors

### 3. Configure Hardware
1. Open the FTC Robot Controller app
2. Go to "Configure Robot"
3. Create a new configuration or edit existing
4. Add 4 DC Motors:
   - Port 0: `front_left`
   - Port 1: `front_right`
   - Port 2: `back_left`
   - Port 3: `back_right`
5. Add a Webcam: `Webcam 1`
6. Save the configuration

### 4. Verify Motor Directions
The code assumes the following motor configuration:
- Front Left: FORWARD
- Back Left: FORWARD
- Front Right: REVERSE
- Back Right: REVERSE

If your robot moves incorrectly, you may need to adjust motor directions in the code or physically swap motor wires.

## OpModes Included

### 1. AprilTag Alignment (Autonomous)
**File**: `AprilTagAlignmentOpMode.java`

**Purpose**: Automatically detects and aligns with an AprilTag

**How it works**:
- Continuously scans for AprilTags
- Calculates position error (lateral, forward, rotation)
- Uses proportional control to move robot into alignment
- Stops when aligned within thresholds

**Alignment Thresholds**:
- Distance: ±2 inches
- Lateral offset: ±1 inch
- Rotation: ±2 degrees

**To run**:
1. Select "AprilTag Alignment" from Autonomous programs
2. Initialize the OpMode
3. Press START
4. Robot will automatically align with detected AprilTag

### 2. Mecanum TeleOp
**File**: `MecanumTeleOp.java`

**Purpose**: Manual control for testing drive system

**Controls**:
- Left stick Y: Forward/backward
- Left stick X: Strafe left/right
- Right stick X: Rotate
- Right trigger: Slow mode (50% speed)
- Left trigger: Fast mode (150% speed)

**To run**:
1. Select "Mecanum TeleOp" from TeleOp programs
2. Initialize the OpMode
3. Press START
4. Use gamepad to control robot

## How AprilTag Alignment Works

### Detection
The program uses the FTC Vision Portal with AprilTag processor to detect tags in the camera's field of view.

### Alignment Strategy
The robot aligns using three axes of movement:

1. **Forward/Backward**: Adjusts distance to tag
2. **Strafe Left/Right**: Centers on tag laterally
3. **Rotation**: Aligns heading with tag orientation

### Proportional Control
Movement speed is proportional to error:
- Large errors → faster movement
- Small errors → slower movement
- Within threshold → stop

### Stability
The robot must maintain alignment for 5 consecutive frames (250ms) before confirming successful alignment.

## Configuration Parameters

You can adjust these constants in `AprilTagAlignmentOpMode.java`:

```java
// Alignment thresholds (how close to get)
private static final double DISTANCE_THRESHOLD = 2.0; // inches
private static final double ANGLE_THRESHOLD = 2.0; // degrees
private static final double X_OFFSET_THRESHOLD = 1.0; // inches

// Motor power limits
private static final double MAX_DRIVE_POWER = 0.5; // Maximum speed
private static final double MIN_DRIVE_POWER = 0.15; // Minimum to overcome friction
```

## Troubleshooting

### Robot doesn't move
- Check motor configuration names match exactly
- Verify motors are properly connected to ports 0-3
- Check battery is charged

### Robot moves in wrong direction
- Adjust motor directions in `initializeHardware()` method
- Reverse FORWARD/REVERSE for specific motors

### No AprilTag detected
- Ensure camera is connected and named "Webcam 1"
- Check camera has clear view of AprilTag
- Verify sufficient lighting
- Make sure AprilTag is not too far away (recommended < 4 feet)

### Robot oscillates or overshoots
- Reduce MAX_DRIVE_POWER
- Increase alignment thresholds
- Check for mechanical issues (friction, weight distribution)

### Camera not recognized
- Verify camera is configured in Robot Controller app
- Check USB connection
- Restart Robot Controller app

## Testing Procedure

1. **Test Drive System**:
   - Run "Mecanum TeleOp"
   - Verify all directions work correctly
   - Adjust motor directions if needed

2. **Test Camera**:
   - Run "AprilTag Alignment"
   - Check telemetry shows tag detection
   - Verify position values update

3. **Test Alignment**:
   - Place robot 2-3 feet from AprilTag
   - Run "AprilTag Alignment"
   - Observe robot aligning
   - Verify it stops when aligned

## Advanced Configuration

### Using Different AprilTag Sizes
The code works with standard AprilTag sizes. For custom sizes, you may need to adjust the AprilTag processor configuration.

### Multiple Tags
Currently detects and aligns with the first tag seen. To target specific tags, modify the detection logic to filter by tag ID.

### Camera Calibration
For improved accuracy, calibrate your camera using FTC calibration tools and update the AprilTag processor with calibration data.

## Additional Resources

- [FTC Documentation](https://ftc-docs.firstinspires.org/)
- [AprilTag Detection Guide](https://ftc-docs.firstinspires.org/en/latest/apriltag/vision_portal/apriltag_intro/apriltag-intro.html)
- [Mecanum Drive Tutorial](https://gm0.org/en/latest/docs/software/tutorials/mecanum-drive.html)

## License

This code is provided for FTC teams. Feel free to modify and adapt for your robot.

## Support

For issues or questions:
1. Check telemetry output for error messages
2. Review hardware configuration
3. Consult FTC documentation
4. Ask your team mentor or coach
