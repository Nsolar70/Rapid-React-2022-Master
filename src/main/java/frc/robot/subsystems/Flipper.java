// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.EncoderConstants;
import frc.robot.Constants.ManipulatorConstants;

public class Flipper extends SubsystemBase {

   //Flipper Motor
   private final WPI_TalonSRX flipperMotor = new WPI_TalonSRX(ManipulatorConstants.kflipperMotor);

   //local setpoint for moving to position by magic motion
   static double iaccum = 0;
   private double setPointSpin;
   private double sensorPosition;
   double posError;
   double dCurrentPosition;
   double positionError;
   double targetPosition;
   int currentPostion;
   int CurrentPosition;
  
   // Delay on button press for flipper (Need to add If-Else  statement to finish)
   public Boolean mblnIsFinished = false;
   
  /** Creates a new Flipper. */
  public Flipper() {

    flipperMotor.configFactoryDefault();

    /* first choose the sensor */
    flipperMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 
    EncoderConstants.kPIDLoopIdx, 
    EncoderConstants.kTimeoutMs);

    flipperMotor.configNeutralDeadband(0.001);

    flipperMotor.setSensorPhase(false);
    flipperMotor.setInverted(false);

    // Reset sensor position
    flipperMotor.setIntegralAccumulator(iaccum, 0, 10);

    /* Set relevant frame periods to be at least as fast as periodic rate */
    //flipperMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, EncoderConstants.kTimeoutMs);
    //flipperMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, EncoderConstants.kTimeoutMs);

    flipperMotor.configAllowableClosedloopError(0, EncoderConstants.kPIDLoopIdx, EncoderConstants.kTimeoutMs);
  
    /* set the peak and nominal outputs */
    flipperMotor.configNominalOutputForward(0, EncoderConstants.kTimeoutMs);
    flipperMotor.configNominalOutputReverse(0, EncoderConstants.kTimeoutMs);
    flipperMotor.configPeakOutputForward(1, EncoderConstants.kTimeoutMs);
    flipperMotor.configPeakOutputReverse(-1, EncoderConstants.kTimeoutMs);
  
    /**
	 * Gains used in Positon Closed Loop, to be adjusted accordingly
     * Gains(kp, ki, kd, kf, izone, peak output);
     */
    //static final Gains kGains = new Gains(0.15, 0.0, 1.0, 0.0, 0, 1.0);
    /* set closed loop gains in slot0 - see documentation */
    flipperMotor.selectProfileSlot(EncoderConstants.kSlotIdx, EncoderConstants.kPIDLoopIdx);
    flipperMotor.config_kF(0, 0.0, EncoderConstants.kTimeoutMs);
    flipperMotor.config_kP(0, 0.35, EncoderConstants.kTimeoutMs);
    flipperMotor.config_kI(0, 0.00205, EncoderConstants.kTimeoutMs); //55
    flipperMotor.config_kD(0, 1.0, EncoderConstants.kTimeoutMs);

    /**
		 * Grab the 360 degree position of the MagEncoder's absolute
		 * position, and intitally set the relative sensor to match.
		 */
		int absolutePosition = flipperMotor.getSensorCollection().getPulseWidthPosition();

		/* Mask out overflows, keep bottom 12 bits */
		absolutePosition &= 0xFFF;
		if (false) { absolutePosition *= -1; }
		if (false) { absolutePosition *= -1; }
		
		/* Set the quadrature (relative) sensor to match absolute */
		flipperMotor.setSelectedSensorPosition(absolutePosition, EncoderConstants.kPIDLoopIdx, EncoderConstants.kTimeoutMs);

    /*
    flipperMotor.configMotionCruiseVelocity(81920, EncoderConstants.kTimeoutMs); //was 81,920
    flipperMotor.configMotionAcceleration(73728, EncoderConstants.kTimeoutMs); //was 73,728 
    /* set acceleration and vcruise velocity - see documentation 
    
    /* zero the sensor */
    flipperMotor.setSelectedSensorPosition(0, EncoderConstants.kPIDLoopIdx, EncoderConstants.kTimeoutMs);
    
    //set Brake Mode for Elevator Motor
    flipperMotor.setNeutralMode(NeutralMode.Brake);
  
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

     //Gets the current sensorpositionErrorposition
     positionError = flipperMotor.getClosedLoopError(EncoderConstants.kPIDLoopIdx);
     
     sensorPosition = flipperMotor.getSelectedSensorPosition();

     posError = sensorPosition - dCurrentPosition;

     //pushes values to the dashboard
     SmartDashboard.putNumber("FlipperSensorPosition", sensorPosition);
     SmartDashboard.putNumber("FlipperOutputPercent", flipperMotor.getMotorOutputPercent());
     SmartDashboard.putNumber("FlipperTargetPosition", targetPosition);
     SmartDashboard.putNumber("FipperNewTarget", CurrentPosition);
     SmartDashboard.putNumber("FlipperPosError", posError);
     
  }

  public void runFlipper() {
   // if ( mblnIsFinished == false){

      // Holds addtional command untill finished
      mblnIsFinished = true;

      // Set position to one rev 
      setPointSpin = 1;

      setPointSpin = this.getValues(setPointSpin); // 4096 counts for each revolution
      targetPosition = setPointSpin;
      dCurrentPosition = sensorPosition + setPointSpin;
     
      /* Motion Magic - 4096 ticks/rev */
      flipperMotor.set(ControlMode.Position, dCurrentPosition);
    //}
  }
    

    /**
   * getValues for commands (Ranges)
   * @param dblValue
   * @return new setpoint for lift position
   */
  private double getValues(double dblValue) {
    
    //Return the setpoint for moving lift
    dblValue = setPointSpin * 4096;
    
    return dblValue;
  }
  
  public boolean CheckForInposition() {
		/* Upper deadband */
		 if (posError >= +15){
       mblnIsFinished = false;
			return true;
		 }
			
		/* Lower deadband */
		if (posError <= -15)
      mblnIsFinished = false;
			return true;
    }
}


