package org.usfirst.frc.team484.robot;

import org.usfirst.frc.team484.robot.commands.Agitate;
import org.usfirst.frc.team484.robot.commands.AutoRotate;
import org.usfirst.frc.team484.robot.commands.DriveWithGyro;
import org.usfirst.frc.team484.robot.commands.JoystickClimb;
import org.usfirst.frc.team484.robot.commands.JoystickDriveAngleVision;
import org.usfirst.frc.team484.robot.commands.PIDShooter;
import org.usfirst.frc.team484.robot.commands.PickupBalls;
import org.usfirst.frc.team484.robot.commands.ResetWheels;
import org.usfirst.frc.team484.robot.commands.ReversePickup;
import org.usfirst.frc.team484.robot.commands.SetRobotFront;
import org.usfirst.frc.team484.robot.commands.ZeroGyro;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;



/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());
	Button resetWheels = new JoystickButton(Robot.io.driveStick, 2);
	Button gyroDrive = new JoystickButton(Robot.io.driveStick, 1);
	Button shooterShoot = new JoystickButton(Robot.io.operatorStick, 1);
	Button pickupButton = new JoystickButton(Robot.io.operatorStick, 2);
	Button pickupButtonTwo = new JoystickButton(Robot.io.operatorStick, 5);
	Button reverseButton = new JoystickButton(Robot.io.operatorStick, 3);
	Button climbButton = new JoystickButton(Robot.io.operatorStick, 6);
	Button rotateButton = new JoystickButton(Robot.io.driveStick, 11);
	Button setFront = new JoystickButton(Robot.io.driveStick, 7);
	Button setRight = new JoystickButton(Robot.io.driveStick, 8);
	Button setLeft = new JoystickButton(Robot.io.driveStick, 10);
	Button setRear = new JoystickButton(Robot.io.driveStick, 9);
	Button driveGear = new JoystickButton(Robot.io.driveStick,3);
	public OI(){
		resetWheels.whileHeld(new ResetWheels());
		gyroDrive.whileHeld(new DriveWithGyro());
		shooterShoot.whileHeld(new PIDShooter());
		shooterShoot.whileHeld(new Agitate());
		pickupButton.whileHeld(new PickupBalls());
		pickupButtonTwo.whileHeld(new PickupBalls());
		reverseButton.whileHeld(new ReversePickup());
		climbButton.whileHeld(new JoystickClimb());
		rotateButton.whileHeld(new AutoRotate(90));
		setRight.whenPressed(new SetRobotFront(SetRobotFront.Side.RIGHT));
		setRear.whenPressed(new SetRobotFront(SetRobotFront.Side.REAR));
		setFront.whenPressed(new SetRobotFront(SetRobotFront.Side.FRONT));
		setLeft.whenPressed(new SetRobotFront(SetRobotFront.Side.LEFT));
		driveGear.whenPressed(new ZeroGyro());
		driveGear.whileHeld(new JoystickDriveAngleVision(270, 0.4));
	}
	
}

