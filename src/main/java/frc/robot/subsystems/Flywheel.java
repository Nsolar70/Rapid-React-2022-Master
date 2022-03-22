// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.Constants.ManipulatorConstants;


public class Flywheel extends SubsystemBase {
  
  //Flywheel Motor
  private final WPI_TalonFX flywheelMotor = new WPI_TalonFX(ManipulatorConstants.kflywheelMotor);

  // The network table 
  private static final String TABLE_NAME = "Preferences";
  
  private static final NetworkTable m_table;
  private ShuffleboardTab VariablesTab = Shuffleboard.getTab("Variables");
  private NetworkTableEntry FlywheelInSpeed = VariablesTab.add("FlywheelIn", 1.0).getEntry();
  private NetworkTableEntry FlywheelOutSpeed = VariablesTab.add("Flywheelout", -0.65).getEntry();

  // Checks to see if Flywheel is running
  public Boolean IsFlywheelOn = false;

  // Vision data
  private double limeLightTx;
  private double limeLightTy;
  private double limeLightA2;
  private double distanceToTarget; 
  private double limeFlywheelControl;
  private double angleToGoalDegrees;
  private double angleToGoalRadians;
  private double flywheelSpeed;
  double h1 = 25.5;             // distance from the center of the Limelight lens to the floor    
  double h2 = 101.625;          // distance from the target to the floor
  double a1 = 49.3987;          // how many degrees back is your limelight rotated from perfectly vertical?
  double Yint = 0.580020647;    // calculated from taking data points and graphing them
  double Slope = 0.0025115298;  // calculated from taking data points and graphing them
  boolean targetFound = false;
  boolean IsLimeControlOn = false;


  /** Creates a new Flywheel. */
  public Flywheel() {

   // Configures the Intake Victors's to default configuration
   flywheelMotor.configFactoryDefault();
  
   // Invert motor direction if set to true
   flywheelMotor.setInverted(false);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

   // Estimate distance
   angleToGoalDegrees = a1 + limeLightA2;
   angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180.0);

   distanceToTarget = (h2-h1)/Math.tan(angleToGoalRadians); // = distance away from target (in inches)

   // Set Flywheel Speed for Limelight control (Y = mx+b)
   limeFlywheelControl = ((Slope*distanceToTarget) + Yint) *-1;  // Y = Flywheel Speed  X= distance away from target 
  
  

    SmartDashboard.putBoolean("IsFlyWheelOn?", IsFlywheelOn);
    SmartDashboard.putBoolean("Target Found", targetFound);

    limeLightTx = Robot.m_limeLight.getdegRotationToTarget();
    limeLightTy = Robot.m_limeLight.getdegVerticalToTarget();
    limeLightA2 = Robot.m_limeLight.getdegVerticalToTarget();  
    SmartDashboard.putNumber("Vision X", limeLightTx);
    SmartDashboard.putNumber("Vision Y", limeLightTy);
    targetFound = Robot.m_limeLight.getIsTargetFound();
    SmartDashboard.putNumber("Estimate Distance (d)", distanceToTarget);
    SmartDashboard.putNumber("LimeFlywheelSpeed", limeFlywheelControl );

  }
  public void ballIn() {
    IsFlywheelOn = true;

    flywheelSpeed = FlywheelInSpeed.getDouble(1.0);
    flywheelMotor.set(flywheelSpeed);
  }

  public void ballOut () {
    IsFlywheelOn = true;
    
    flywheelSpeed = FlywheelOutSpeed.getDouble(-0.65);
    flywheelMotor.set(flywheelSpeed);
  }
    
  public void LimeFlywheelSpeed(){
    IsLimeControlOn = true;   
    
    flywheelSpeed = limeFlywheelControl;
    flywheelMotor.set(flywheelSpeed);
  }

  public void StopLimeControl(){
    IsLimeControlOn = false; 
  }

   // Stop Motion
   public void stopMotion() {
    IsFlywheelOn = false;

    flywheelMotor.set(0.0);
  }


  //---------------------------------------------------------------------
  // Estimated Distance
  //---------------------------------------------------------------------

  /*private double EstimatedDistance(double angleToGoalRadians ) {
    return (h2-h1)/Math.tan(angleToGoalRadians); // = distance away from target (in inches)
  }

  // Lime light control for flywheel

  private double LimeFlywheelControl(double distanceToTarget){
    // Y = mx+b 
    return (Slope*distanceToTarget) + Yint;
  } */

  static {
   m_table = NetworkTableInstance.getDefault().getTable(TABLE_NAME);
   m_table.getEntry(".type").setString("RobotPreferences");
  }

 }

