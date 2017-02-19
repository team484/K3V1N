package org.usfirst.frc.team484.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.VictorSP;

public class RobotIO {
	public CANTalon frontLeftTransMotor;
	public CANTalon frontRightTransMotor;
	public CANTalon rearLeftTransMotor;
	public CANTalon rearRightTransMotor;

	public CANTalon frontLeftRotationalMotor;
	public CANTalon frontRightRotationalMotor;
	public CANTalon rearLeftRotationalMotor;
	public CANTalon rearRightRotationalMotor;

	public CANTalon shooterMotor;
	public VictorSP pickupMotor;

	public VictorSP climbMotorA;
	public VictorSP climbMotorB;
	
	public VictorSP agitateMotor;

	
	public Encoder frontLeftEnc;
	public Encoder frontRightEnc;
	public Encoder rearLeftEnc;
	public Encoder rearRightEnc;
	public Encoder shooterEnc;
	
	//public static NetworkTable visionTable = NetworkTable.getTable("grip");
	public AnalogInput infraredSensor;
	public AnalogGyro topGyro;
	public AnalogGyro bottomGyro;

	public Joystick driveStick;
	public Joystick operatorStick;

	
	
	public RobotIO() {
		frontLeftTransMotor = new CANTalon(RobotMap.frontLeftTransMotor);
		frontRightTransMotor = new CANTalon(RobotMap.frontRightTransMotor);
		rearLeftTransMotor = new CANTalon(RobotMap.rearleftTransMotor);
		rearRightTransMotor = new CANTalon(RobotMap.rearRightTransMotor);	

		frontLeftRotationalMotor = new CANTalon(RobotMap.frontLeftRotationalMotor);
		frontRightRotationalMotor = new CANTalon(RobotMap.frontRightRotationalMotor);
		rearLeftRotationalMotor = new CANTalon(RobotMap.rearleftRotationalMotor);
		rearRightRotationalMotor = new CANTalon(RobotMap.rearRightRotationalMotor);
		
		shooterMotor = new CANTalon(RobotMap.shooterMotor);
		pickupMotor = new VictorSP(RobotMap.pickupMotor);

		climbMotorA = new VictorSP(RobotMap.climbMotorA);
		climbMotorB = new VictorSP(RobotMap.climbMotorB);
		
		agitateMotor = new VictorSP(RobotMap.agitatorMotor);

		frontLeftEnc = new Encoder(RobotMap.frontLeftEncA, RobotMap.frontLeftEncB);
		frontRightEnc = new Encoder(RobotMap.frontRightEncA, RobotMap.frontRightEncB);
		rearLeftEnc = new Encoder(RobotMap.rearleftEncA, RobotMap.rearleftEncB);
		rearRightEnc = new Encoder(RobotMap.rearRightEncA, RobotMap.rearRightEncB);
		shooterEnc = new Encoder(RobotMap.shooterEncA, RobotMap.shooterEncB);

		//public static NetworkTable visionTable = NetworkTable.getTable("grip");
		infraredSensor = new AnalogInput(RobotMap.infraredSensor);
		topGyro = new AnalogGyro(RobotMap.topGyro);
		bottomGyro = new AnalogGyro(RobotMap.bottomGyro);

		driveStick = new Joystick(RobotMap.driveStick);
		operatorStick = new Joystick(RobotMap.operatorStick);
		
		

	}
	
	
}
