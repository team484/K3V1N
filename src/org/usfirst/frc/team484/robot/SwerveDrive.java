package org.usfirst.frc.team484.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.SpeedController;

/**
 * Class designed to allow for manipulating swerve drive on a robot.
 * Requires 4 relative encoders and 8 speed controllers configured on
 * 4 axes of symmetry and calculates the optimal vector for each swerve 
 * wheel and then uses PID to set the rotation of each wheel. This
 * process allows for fluid simultaneous control of x movement,
 * y movement, and rotation of the robot.
 */
public class SwerveDrive {
	/**
	   * The location of a motor on the robot for the purpose of driving
	   */
	  public static class MotorType {

	    /**
	     * The integer value representing this enumeration
	     */
	    public final int value;
	    static final int kFrontLeft_val = 0;
	    static final int kFrontRight_val = 2;
	    static final int kRearLeft_val = 1;
	    static final int kRearRight_val = 3;
	    /**
	     * motortype: front left
	     */
	    public static final MotorType kFrontLeft = new MotorType(kFrontLeft_val);
	    /**
	     * motortype: front right
	     */
	    public static final MotorType kFrontRight = new MotorType(kFrontRight_val);
	    /**
	     * motortype: rear left
	     */
	    public static final MotorType kRearLeft = new MotorType(kRearLeft_val);
	    /**
	     * motortype: rear right
	     */
	    public static final MotorType kRearRight = new MotorType(kRearRight_val);

	    private MotorType(int value) {
	      this.value = value;
	    }
	  }

	private PIDController pidFL;
	private PIDController pidRL;
	private PIDController pidFR;
	private PIDController pidRR;

	private double width = 1.0;
	private double length = 1.0;

	private double rotAngFL;
	private double rotAngRL;
	private double rotAngFR;
	private double rotAngRR;

	private SpeedController transFL;
	private SpeedController transRL;
	private SpeedController transFR;
	private SpeedController transRR;

	private SpeedController rotFL;
	private SpeedController rotRL;
	private SpeedController rotFR;
	private SpeedController rotRR;

	private Encoder encFL;
	private Encoder encRL;
	private Encoder encFR;
	private Encoder encRR;
	
	/**
	 * Constructor for the Swerve Drive class. requires setting values for the PID
	 * loop used to rotate the swerve wheels to set angles, as well as passing through motor controllers for swerve drive
	 * @param kP proportional value for PID loop
	 * @param kI inegral value for PID loop
	 * @param kD derivitive value for PID loop
	 * @param iEncFL front left encoder (must be preset to getDistance in degrees with counter clockwise increasing)
	 * @param iEncRL rear left encoder (must be preset to getDistance in degrees with counter clockwise increasing)
	 * @param iEncFR front right encoder (must be preset to getDistance in degrees with counter clockwise increasing)
	 * @param iEncRR rear right encoder (must be preset to getDistance in degrees with counter clockwise increasing)
	 * @param spFL motor controller used to rotate the front left wheel to a set angle
	 * @param spRL motor controller used to rotate the rear left wheel to a set angle
	 * @param spFR motor controller used to rotate the front right wheel to a set angle
	 * @param spRR motor controller used to rotate the rear right wheel to a set angle
	 * @param iTransFL motor controller used to power front left wheel
	 * @param iTransRL motor controller used to power rear left wheel
	 * @param iTransFR motor controller used to power front right wheel
	 * @param iTransRR motor controller used to power rear right wheel
	 * @param invertWheelRotation set to true if wheels rotate clockwise when set to a positive value
	 */
	public SwerveDrive(double kP, double kI, double kD, Encoder iEncFL, Encoder iEncRL, Encoder iEncFR, Encoder iEncRR, SpeedController spFL, SpeedController spRL, SpeedController spFR, SpeedController spRR, SpeedController iTransFL, SpeedController iTransRL, SpeedController iTransFR, SpeedController iTransRR, boolean invertWheelRotation) {
		transFL = iTransFL;
		transRL = iTransRL;
		transFR = iTransFR;
		transRR = iTransRR;

		rotFL = spFL;
		rotRL = spRL;
		rotFR = spFR;
		rotRR = spRR;

		encFL = iEncFL;
		encRL = iEncRL;
		encFR = iEncFR;
		encRR = iEncRR;

		setWheelbaseDimensions(1.0, 1.0);
		
		if (invertWheelRotation) {
			pidFL = new PIDController(kP, kI, kD, new PIDSource() {
				public double pidGet() {
					return findEncAng(encFL.getDistance());
				}

				@Override
				public void setPIDSourceType(PIDSourceType pidSource) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public PIDSourceType getPIDSourceType() {
					// TODO Auto-generated method stub
					return null;
				}
			}, new PIDOutput() {
				public void pidWrite(double d) {
					rotFL.set(-d);
				}
			});
			pidRL = new PIDController(kP, kI, kD, new PIDSource() {
				public double pidGet() {
					return findEncAng(encRL.getDistance());
				}

				@Override
				public void setPIDSourceType(PIDSourceType pidSource) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public PIDSourceType getPIDSourceType() {
					// TODO Auto-generated method stub
					return null;
				}
			}, new PIDOutput() {
				public void pidWrite(double d) {
					rotRL.set(-d);
				}
			});
			pidFR = new PIDController(kP, kI, kD, new PIDSource() {
				public double pidGet() {
					return findEncAng(encFR.getDistance());
				}

				@Override
				public void setPIDSourceType(PIDSourceType pidSource) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public PIDSourceType getPIDSourceType() {
					// TODO Auto-generated method stub
					return null;
				}
			}, new PIDOutput() {
				public void pidWrite(double d) {
					rotFR.set(-d);
				}
			});
			pidRR = new PIDController(kP, kI, kD, new PIDSource() {
				public double pidGet() {
					return findEncAng(encRR.getDistance());
				}

				@Override
				public void setPIDSourceType(PIDSourceType pidSource) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public PIDSourceType getPIDSourceType() {
					// TODO Auto-generated method stub
					return null;
				}
			}, new PIDOutput() {
				public void pidWrite(double d) {
					rotRR.set(-d);
				}
			});
		} else {
			pidFL = new PIDController(kP, kI, kD, new PIDSource() {
				public double pidGet() {
					return findEncAng(encFL.getDistance());
				}

				@Override
				public void setPIDSourceType(PIDSourceType pidSource) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public PIDSourceType getPIDSourceType() {
					// TODO Auto-generated method stub
					return null;
				}
			}, new PIDOutput() {
				public void pidWrite(double d) {
					rotFL.set(d);
				}
			});
			pidRL = new PIDController(kP, kI, kD, new PIDSource() {
				public double pidGet() {
					return findEncAng(encRL.getDistance());
				}

				@Override
				public void setPIDSourceType(PIDSourceType pidSource) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public PIDSourceType getPIDSourceType() {
					// TODO Auto-generated method stub
					return null;
				}
			}, new PIDOutput() {
				public void pidWrite(double d) {
					rotRL.set(d);
				}
			});
			pidFR = new PIDController(kP, kI, kD, new PIDSource() {
				public double pidGet() {
					return findEncAng(encFR.getDistance());
				}

				@Override
				public void setPIDSourceType(PIDSourceType pidSource) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public PIDSourceType getPIDSourceType() {
					// TODO Auto-generated method stub
					return null;
				}
			}, new PIDOutput() {
				public void pidWrite(double d) {
					rotFR.set(d);
				}
			});
			pidRR = new PIDController(kP, kI, kD, new PIDSource() {
				public double pidGet() {
					return findEncAng(encRR.getDistance());
				}

				@Override
				public void setPIDSourceType(PIDSourceType pidSource) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public PIDSourceType getPIDSourceType() {
					// TODO Auto-generated method stub
					return null;
				}
			}, new PIDOutput() {
				public void pidWrite(double d) {
					rotRR.set(d);
				}
			});
		}
		pidFL.setContinuous();
		pidRL.setContinuous();
		pidFR.setContinuous();
		pidRR.setContinuous();

		pidFL.setInputRange(-180, 180);
		pidRL.setInputRange(-180, 180);
		pidFR.setInputRange(-180, 180);
		pidRR.setInputRange(-180, 180);

		pidFL.setOutputRange(-1, 1);
		pidRL.setOutputRange(-1, 1);
		pidFR.setOutputRange(-1, 1);
		pidRR.setOutputRange(-1, 1);

		pidFL.setSetpoint(0);
		pidRL.setSetpoint(0);
		pidFR.setSetpoint(0);
		pidRR.setSetpoint(0);

		pidFL.enable();
		pidRL.enable();
		pidFR.enable();
		pidRR.enable();
	}

	/**
	 * Used to calculate the optimal angle for wheels for turning
	 * @param baseWidth Distance between left and right wheels
	 * @param baseLength Distance between front and back wheels
	 */
	public void setWheelbaseDimensions(double baseWidth, double baseLength) {
		width = baseWidth;
		length = baseLength;
		rotAngRR = Math.atan(width/length) * 57.2957795;
		rotAngRL = -rotAngRR;
		rotAngFR = 180 - rotAngRR;
		rotAngFL = -rotAngFR;
	}

	/**
	 * Used to change the PID values used for rotating the wheels
	 * @param kP proportional PID value
	 * @param kI integral PID value
	 * @param kD derivitive PID value
	 */
	public void setPID(double kP, double kI, double kD) {
		pidFL.setPID(kP, kI, kD);
		pidRL.setPID(kP, kI, kD);
		pidFR.setPID(kP, kI, kD);
		pidRR.setPID(kP, kI, kD);
	}

	/**
	 * Used to get the angle of one of the encoders
	 * @param motor location of encoder frontLeft, rearLeft, frontRight, rearRight
	 * @return the encoder angle in degrees (-180 to 180)
	 */
	@SuppressWarnings("null")
	public double getEncoderAngle(MotorType motor) {
		switch(motor.value) {
		case 0:
			return findEncAng(encFL.getDistance());
		case 1:
			return findEncAng(encRL.getDistance());
		case 2:
			return findEncAng(encFR.getDistance());
		case 3:
			return findEncAng(encRR.getDistance());
		default:
			return (Double) null;
		}
	}

	/**
	 * Used to get the error value on a wheel's PID loop
	 * @param motor location of wheel frontLeft, rearLeft, frontRight, rearRight
	 * @return the PID error value
	 */
	@SuppressWarnings("null")
	public double getPIDError(MotorType motor) {
		switch(motor.value) {
		case 0:
			return pidFL.getError();
		case 1:
			return pidRL.getError();
		case 2:
			return pidFR.getError();
		case 3:
			return pidRR.getError();
		default:
			return (Double) null;
		}
	}

	/**
	 * Used to get the angles the encoder has gone through since initialized or reset
	 * @param motor location of encoder frontLeft, rearLeft, frontRight, rearRight
	 * @return the encoder angle/distance
	 */
	@SuppressWarnings("null")
	public double getEncoderValue(MotorType motor) {
		switch(motor.value) {
		case 0:
			return encFL.getDistance();
		case 1:
			return encRL.getDistance();
		case 2:
			return encFR.getDistance();
		case 3:
			return encRR.getDistance();
		default:
			return (Double) null;
		}
	}

	/**
	 * Used to get the current set value for a swerve drive motor
	 * @param motor location of motor frontLeft, rearLeft, frontRight, rearRight
	 * @param rotationMotor true for rotation motor false for driving motor
	 * @return returns the set value for the speed controller (-1 to 1)
	 */
	@SuppressWarnings("null")
	public double getMotorValue(MotorType motor, boolean rotationMotor) {
		if (rotationMotor) {
			switch(motor.value) {
			case 0:
				return rotFL.get();
			case 1:
				return rotRL.get();
			case 2:
				return rotFR.get();
			case 3:
				return rotRR.get();
			default:
				return (Double) null;
			}
		} else {
			switch(motor.value) {
			case 0:
				return transFL.get();
			case 1:
				return transRL.get();
			case 2:
				return transFR.get();
			case 3:
				return transRR.get();
			default:
				return (Double) null;
			}	
		}
	}

	private double findEncAng(double angle) {
		while (angle > 180) {
			angle -= 360;
		}
		while (angle <= -180) {
			angle += 360;
		}
		return angle;
	}

	@SuppressWarnings("null")
	/**
	 * Used to get the setpoint for a PID loop on a swerve wheel
	 * @param motor location of wheel frontLeft, rearLeft, frontRight, rearRight
	 * @return the setpoint value for the wheel
	 */
	public double getSetpoint(MotorType motor) {
		switch(motor.value) {
		case 0:
			return pidFL.getSetpoint();
		case 1:
			return pidRL.getSetpoint();
		case 2:
			return pidFR.getSetpoint();
		case 3:
			return pidRR.getSetpoint();
		default:
			return (Double) null;
		}
	}

	private double driveWheel(int wheel, double stickAng, double stickMag, double rot) {
		double rotAng = 0;
		double rotMag = rot;
		double transAng = stickAng;
		double transMag = stickMag;
		double currentWheelAngle = 0.0;
		switch (wheel) {
		case 0:
			rotAng = rotAngFL;
			currentWheelAngle = findEncAng(encFL.getDistance());
			break;
		case 1:
			rotAng = rotAngRL;
			currentWheelAngle = findEncAng(encRL.getDistance());
			break;
		case 2:
			rotAng = rotAngFR;
			currentWheelAngle = findEncAng(encFR.getDistance());
			break;
		case 3:
			rotAng = rotAngRR;
			currentWheelAngle = findEncAng(encRR.getDistance());
			break;
		default:
			break;
		}
		double wheelX = Math.cos(transAng/57.2957795) * transMag + Math.cos(rotAng/57.2957795) * rotMag;
		double wheelY = Math.sin(transAng/57.2957795) * transMag + Math.sin(rotAng/57.2957795) * rotMag;
		double wheelRot = Math.atan2(wheelY, wheelX) * 57.2957795;
		double wheelMag = Math.hypot(wheelX, wheelY);
		if (goToAngle(wheelRot, currentWheelAngle)) {

		} else {
			wheelRot += 180;
			if (wheelRot > 180) {
				wheelRot -= 360;
			}
			wheelMag = -wheelMag;
		}
		if (wheelRot < -180) {
			wheelRot += 360;
		}
		switch(wheel) {
		case 0:
			pidFL.setSetpoint(wheelRot);
			break;
		case 1:
			pidRL.setSetpoint(wheelRot);
			break;
		case 2:
			pidFR.setSetpoint(wheelRot);
			break;
		case 3:
			pidRR.setSetpoint(wheelRot);
			break;
		default:
			break;
		}
		return wheelMag;

	}

	private boolean goToAngle(double wheelRot, double currentWheelAngle) {
		if (Math.abs(wheelRot - currentWheelAngle) < 90) {
			return true;
		}
		if (wheelRot < 0) {
			wheelRot += 360;
		}
		if (currentWheelAngle < 0) {
			currentWheelAngle += 360;
		}
		if (Math.abs(wheelRot - currentWheelAngle) < 90) {
			return true;
		}
		return false;
	}

	/**
	 * Used to update move and rotate values for serve drive. Should be
	 * constantly updated unless values are set to 0
	 * @param stickAngle calculated angle from joystick [stick.getDirectionDegrees()]
	 * @param stickMag calculated magnatude from joystick [stick.getMagnatude()]
	 * @param rot rotation value for the robot
	 * @param angleOffset degrees to offset the stick angle by
	 */
	public void drive(double stickAngle, double stickMag, double rot, double angleOffset) {
		stickAngle = -stickAngle; //start for converting stick angle value to polar coordinate
		stickAngle+= 90;
		if (stickAngle > 180) {
			stickAngle -= 360;
		}
		double fLWheelMag = driveWheel(0,stickAngle - angleOffset,stickMag, rot);
		double rLWheelMag = driveWheel(1,stickAngle - angleOffset,stickMag, rot);
		double fRWheelMag = driveWheel(2,stickAngle - angleOffset,stickMag, rot);
		double rRWheelMag = driveWheel(3,stickAngle - angleOffset,stickMag, rot);
		double divisor = 1;
		if (fLWheelMag >= rLWheelMag && fLWheelMag >= fRWheelMag && fLWheelMag >= rRWheelMag) {
			if (Math.abs(fLWheelMag) > 1) {
				divisor = Math.abs(fLWheelMag);
			}
		} else if (rLWheelMag >= fRWheelMag && rLWheelMag >= rRWheelMag) {
			if (Math.abs(rLWheelMag) > 1) {
				divisor = Math.abs(rLWheelMag);
			}
		} else if (fRWheelMag >= rRWheelMag) {
			if (Math.abs(fRWheelMag) > 1) {
				divisor = Math.abs(fRWheelMag);
			}
		} else {
			if (Math.abs(rRWheelMag) > 1) {
				divisor = Math.abs(rRWheelMag);
			}
		}
		transFL.set(fLWheelMag /= divisor);
		transRL.set(rLWheelMag /= divisor);
		transFR.set(fRWheelMag /= divisor);
		transRR.set(rRWheelMag /= divisor);
	}

	/**
	 * Used to update move and rotate values for serve drive. Should be
	 * constantly updated unless values are set to 0
	 * @param stickAngle calculated angle from joystick [stick.getDirectionDegrees()]
	 * @param stickMag stickMag calculated magnatude from joystick [stick.getMagnatude()]
	 * @param rot rotation value for the robot
	 */
	public void drive(double stickAngle, double stickMag, double rot) {
		drive(stickAngle,stickMag,rot,0.0);
	}

	/**
	 * Frees the speed controllers encoders and PID loops
	 */
	public void free() {
		((PWM) transFL).free();
		((PWM) transRL).free();
		((PWM) transFR).free();
		((PWM) transRR).free();

		((PWM) rotFL).free();
		((PWM) rotRL).free();
		((PWM) rotFR).free();
		((PWM) rotRR).free();

		encFL.free();
		encRL.free();
		encFR.free();
		encRR.free();

		pidFL.disable();
		pidFL.free();
		pidRL.disable();
		pidRL.free();
		pidFR.disable();
		pidFR.free();
		pidRR.disable();
		pidRR.free();
	}

	/**
	 * Resets an encoder's angle to 0
	 * @param motor location of encoder frontLeft, rearLeft, frontRight, rearRight
	 */
	public void resetEncoder(MotorType motor) {
		switch(motor.value) {
		case 0:
			encFL.reset();
		case 1:
			encRL.reset();
		case 2:
			encFR.reset();
		case 3:
			encRR.reset();
		}
	}

	/**
	 * Resets all encoders' angles to 0
	 */
	public void resetEncoder() {
		encFL.reset();
		encRL.reset();
		encFR.reset();
		encRR.reset();
	}

	/**
	 * Sets all motor values to 0
	 */
	public void stopMotors() {
		transFL.set(0);
		transRL.set(0);
		transFR.set(0);
		transRR.set(0);
		rotFL.set(0);
		rotRL.set(0);
		rotFR.set(0);
		rotRR.set(0);
	}
}