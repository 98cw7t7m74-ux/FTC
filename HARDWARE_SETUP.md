# Hardware Setup Guide

## Quick Reference

### Motor Configuration (Robot Controller App)

```
Configuration Name: FTC Robot

Motors:
├─ Port 0: front_left (REV HD Hex Motor or equivalent)
├─ Port 1: front_right (REV HD Hex Motor or equivalent)
├─ Port 2: back_left (REV HD Hex Motor or equivalent)
└─ Port 3: back_right (REV HD Hex Motor or equivalent)

Camera:
└─ USB: Webcam 1 (Logitech C920 or equivalent)
```

## Detailed Setup

### Step 1: Motor Installation

1. **Mount Motors**:
   - Install mecanum wheels on all 4 motors
   - Ensure X-pattern: Left wheels with rollers forming an "X" from top view
   - Front left and back right rollers should point to front-right
   - Front right and back left rollers should point to front-left

2. **Wire Motors to Ports**:
   ```
   Control Hub / Expansion Hub:
   Port 0 → Front Left Motor
   Port 1 → Front Right Motor
   Port 2 → Back Left Motor
   Port 3 → Back Right Motor
   ```

3. **Motor Direction Notes**:
   - Right side motors typically need to be reversed
   - This is handled in software (see RobotHardware.java)
   - If robot moves backwards when commanded forward, swap motor directions

### Step 2: Camera Installation

1. **Physical Mounting**:
   - Mount camera 3 inches above the ground
   - Face camera forward
   - Ensure stable mounting (no wobbling)
   - Angle camera slightly upward if needed to see tags

2. **Connection**:
   - Connect USB camera to Control Hub or phone
   - Verify camera is recognized in device manager

3. **Camera Requirements**:
   - Resolution: 640x480 or higher
   - Frame rate: 30 fps minimum
   - Compatible with FTC Vision Portal
   - Recommended: Logitech C920, C270, or Microsoft LifeCam

### Step 3: Robot Controller Configuration

1. **Open FTC Robot Controller App**
2. **Tap "Configure Robot"**
3. **Create New Configuration**:
   - Tap "New"
   - Name it "FTC Robot" (or your team name)

4. **Add Control Hub or Expansion Hub**:
   - Select your hub type
   - Tap on Port 0 → Select "DC Motor" → Name: `front_left`
   - Tap on Port 1 → Select "DC Motor" → Name: `front_right`
   - Tap on Port 2 → Select "DC Motor" → Name: `back_left`
   - Tap on Port 3 → Select "DC Motor" → Name: `back_right`

5. **Add Camera**:
   - Scroll to Webcam section
   - Tap "Add Webcam"
   - Name: `Webcam 1`
   - Select your camera model

6. **Save Configuration**:
   - Tap "Done"
   - Select this configuration as active

### Step 4: Physical Measurements

Verify these measurements for proper AprilTag alignment:

```
Camera Height: 3 inches from ground
├─ Measure from ground to camera lens center
└─ Adjust mounting bracket if needed

AprilTag Height: 24 inches from ground (2 feet)
├─ Measure from ground to AprilTag center
└─ Mount tag on field element or wall
```

### Step 5: Verification

#### Test 1: Motor Test
```java
// In TeleOp, test each motor individually
frontLeftMotor.setPower(0.5);   // Should move front left wheel
frontRightMotor.setPower(0.5);  // Should move front right wheel
backLeftMotor.setPower(0.5);    // Should move back left wheel
backRightMotor.setPower(0.5);   // Should move back right wheel
```

#### Test 2: Direction Test
Run "Mecanum TeleOp" and verify:
- [ ] Forward stick → Robot moves forward
- [ ] Backward stick → Robot moves backward
- [ ] Left stick → Robot strafes left
- [ ] Right stick → Robot strafes right
- [ ] Rotate right → Robot rotates clockwise
- [ ] Rotate left → Robot rotates counter-clockwise

#### Test 3: Camera Test
Run "AprilTag Alignment" and check telemetry:
- [ ] Camera initializes without errors
- [ ] AprilTag is detected when in view
- [ ] Position values (X, Y, Yaw) update in real-time

## Troubleshooting Hardware

### Motors not responding
- Check motor controller connections
- Verify battery is charged (>12V)
- Check circuit breaker is not tripped
- Test motors individually

### Wrong movement direction
- Swap motor wires on the motor itself, OR
- Change motor direction in code:
  ```java
  motor.setDirection(DcMotor.Direction.REVERSE);
  ```

### Robot strafes when trying to go straight
- Check mecanum wheel orientation (X-pattern)
- Verify wheels are not worn unevenly
- Ensure all wheels touch the ground

### Camera not detected
- Try different USB port
- Check camera is powered on
- Restart Robot Controller app
- Update camera drivers (if using phone)

### AprilTag not detected
- Ensure good lighting (not too bright, not too dark)
- Check camera focus (should be set to auto-focus)
- Verify AprilTag is printed clearly
- Test at different distances (start at 2-3 feet)

## Wiring Diagram

```
Control Hub
┌─────────────────────────┐
│                         │
│  [0] front_left        │
│  [1] front_right       │
│  [2] back_left         │
│  [3] back_right        │
│                         │
│  USB: Webcam 1         │
│                         │
│  Power: Battery        │
└─────────────────────────┘
        │
        ├─── Motor 0: Front Left
        ├─── Motor 1: Front Right
        ├─── Motor 2: Back Left
        ├─── Motor 3: Back Right
        └─── USB Camera
```

## Recommended Hardware

### Motors
- REV HD Hex Motor
- goBILDA 5202/5203 Series
- AndyMark NeveRest

### Mecanum Wheels
- 4" or 6" mecanum wheels
- Must be mounted in correct X-pattern

### Camera
- Logitech C920 (best)
- Logitech C270 (budget)
- Microsoft LifeCam HD-3000

### Control System
- REV Control Hub (recommended)
- REV Expansion Hub with Android phone

## Safety Checklist

Before running autonomous:
- [ ] All motors secured and wired correctly
- [ ] Battery charged and secured
- [ ] Camera mounted firmly
- [ ] Robot has clear space to move
- [ ] Emergency stop accessible
- [ ] Team member ready to kill power if needed

## Maintenance

Regular checks:
- Motor wire connections (check for looseness)
- Wheel condition (check for wear on rollers)
- Camera lens (clean if dirty)
- Battery voltage (charge when below 12V)
- Structural integrity (check for loose screws)
