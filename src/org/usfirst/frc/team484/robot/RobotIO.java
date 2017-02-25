package org.usfirst.frc.team484.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

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
	public Relay shooterLED; 
	public Relay pickupLED;

	
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
	
	
	public NetworkTable itab = NetworkTable.getTable(RobotSettings.iTable);

	
	
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
		shooterEnc.setSamplesToAverage(4);
		
		shooterMotor.changeControlMode(TalonControlMode.Voltage);
		shooterMotor.setVoltageRampRate(RobotSettings.shooterWheelsVoltageRampRate);

		//public static NetworkTable visionTable = NetworkTable.getTable("grip");
		infraredSensor = new AnalogInput(RobotMap.infraredSensor);
		topGyro = new AnalogGyro(RobotMap.topGyro);
		bottomGyro = new AnalogGyro(RobotMap.bottomGyro);

		driveStick = new Joystick(RobotMap.driveStick);
		operatorStick = new Joystick(RobotMap.operatorStick);
		shooterLED = new Relay(RobotMap.shooterLED);
		pickupLED = new Relay(RobotMap.pickupLED);
		

	}
	
	
}
