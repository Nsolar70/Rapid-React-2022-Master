// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climber extends SubsystemBase {

  private final CANSparkMax m_climberMotor = new CANSparkMax(Constants.ManipulatorConstants.kclimberMotor, MotorType.kBrushless);

  private RelativeEncoder m_encoder;

  private SparkMaxPIDController m_pidController;

  private double  kMaxOutput, kMinOutput, maxVel, minVel, maxAcc, allowedErr,maxRPM;
  //private double kP, kI, kD, kIz, kFF;
  private double Highsetpoint = 124; //124
  private double Lowsetpoint = 20; // ?
  private double Zerosetpoint; //0
   // Temporary untill zeroing code is released

  private static final String TABLE_NAME = "Preferences";
  
  private static final NetworkTable m_table;
  private ShuffleboardTab VariablesTab = Shuffleboard.getTab("Variables");
  private NetworkTableEntry Offset = VariablesTab.add("Offset", 0).getEntry(); // Temporary untill zeroing code is released


 
  public Climber() {
    // set factory defaults
    m_climberMotor.restoreFactoryDefaults();

    //Set software limits
    /*m_climberMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
    m_climberMotor.enableSoftLimit(SoftLimitDirection.kForward, false);
    m_climberMotor.setSoftLimit(SoftLimitDirection.kReverse, 0);
    m_climberMotor.setSoftLimit(SoftLimitDirection.kForward, 40); */

    // set default motor mode (Brake/Coast)
    m_climberMotor.setIdleMode(IdleMode.kBrake);


    m_encoder = m_climberMotor.getEncoder();

     // initialze PID controller and encoder objects
     m_pidController = m_climberMotor.getPIDController();
     m_encoder = m_climberMotor.getEncoder();
 
     // PID coefficients
     /*kP = 5e-5; 
     kI = 1e-6;
     kD = 0; 
     kIz = 0; 
     kFF = 0.000156; */
     kMaxOutput = 1; 
     kMinOutput = -1;
     maxRPM = 5700;
 
     // Smart Motion Coefficients
     maxVel = 1600; // rpm
     maxAcc = 600;
 
     // set PID coefficients
     m_pidController.setP(0.00005); // 5e-5
     m_pidController.setI(0.000001); // 1e-6
     m_pidController.setD(0);
     m_pidController.setIZone(0);
     m_pidController.setFF(0.000156);
     m_pidController.setOutputRange(kMinOutput, kMaxOutput);
 
     /**
      * Smart Motion coefficients are set on a SparkMaxPIDController object
      * 
      * - setSmartMotionMaxVelocity() will limit the velocity in RPM of
      * the pid controller in Smart Motion mode
      * - setSmartMotionMinOutputVelocity() will put a lower bound in
      * RPM of the pid controller in Smart Motion mode
      * - setSmartMotionMaxAccel() will limit the acceleration in RPM^2
      * of the pid controller in Smart Motion mode
      * - setSmartMotionAllowedClosedLoopError() will set the max allowed
      * error for the pid controller in Smart Motion mode
      */
     int smartMotionSlot = 0;
     m_pidController.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
     m_pidController.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
     m_pidController.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
     m_pidController.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);

     SmartDashboard.putNumber("Max Output", kMaxOutput);
     SmartDashboard.putNumber("Min Output", kMinOutput);
 
     // display Smart Motion coefficients
     SmartDashboard.putNumber("Max Velocity", maxVel);
     SmartDashboard.putNumber("Min Velocity", minVel);
     SmartDashboard.putNumber("Max Acceleration", maxAcc);
     SmartDashboard.putNumber("Allowed Closed Loop Error", allowedErr);
 
   }
  
  @Override
  public void periodic() {
  
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Encoder Position", m_encoder.getPosition());

    SmartDashboard.putNumber("Encoder Velocity", m_encoder.getVelocity());

    SmartDashboard.putNumber("Output", m_climberMotor.getAppliedOutput());

   SmartDashboard.putNumber("Current", m_climberMotor.getOutputCurrent());


  }

  public void LowPosition(){
    // Calculates the true position by using Offset
                  
    // Set point = Counts wanted + how much the encoder is off of 0 counts when set at hard zero mechanically   
    Lowsetpoint = 20 + (Offset.getDouble(0));

    m_pidController.setReference(Lowsetpoint, CANSparkMax.ControlType.kSmartMotion);


  }
  
  public void HighPosition(){
    // Set point = Counts wanted + how much the encoder is off of 0 counts when set at hard zero mechanically
    Highsetpoint = 124 + Offset.getDouble(0);
  
    m_pidController.setReference(Highsetpoint, CANSparkMax.ControlType.kSmartMotion);

  }

  public void ZeroPosition(){
    // Set point = Counts wanted + how much the encoder is off of 0 counts when set at hard zero mechanically
    Zerosetpoint = 0 + Offset.getDouble(0);

    m_pidController.setReference( Zerosetpoint, CANSparkMax.ControlType.kSmartMotion);
  }

  public void RunUp(){

    m_climberMotor.set(0.1);
  }

  public void RunDown(){
    m_climberMotor.set(-0.1);
  }

  public void StopMotion(){
    m_climberMotor.set(0.0);
  }

  static {
    m_table = NetworkTableInstance.getDefault().getTable(TABLE_NAME);
    m_table.getEntry(".type").setString("RobotPreferences");
   }
}



  
