// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;


public class DriveTrain extends SubsystemBase {
 
  //Drive Motors
  private final WPI_TalonFX leftFrontMotor = new WPI_TalonFX(DriveConstants.kleftFrontMotor);
  private final WPI_TalonFX leftBackMotor = new WPI_TalonFX(DriveConstants.kleftBackMotor);
  private final WPI_TalonFX rightFrontMotor = new WPI_TalonFX(DriveConstants.krightFrontMotor);
  private final WPI_TalonFX rightBackMotor = new WPI_TalonFX(DriveConstants.krightBackMotor);

  // Drive Class
  private final DifferentialDrive m_Drive = new DifferentialDrive(leftFrontMotor, rightFrontMotor);

  /* Acceleration limiter to compensate for back heavyness
   *
   *As this value moves up the more the robot will be allowed to accelerate */
  SlewRateLimiter filter = new SlewRateLimiter(1.8);  

  double joyThreshold = 0.05; // Default threshold value from XboxController

  public DriveTrain() {

    // Configures the Drive Train Falcon's to default configuration
    leftFrontMotor.configFactoryDefault();
    leftBackMotor.configFactoryDefault();
    rightFrontMotor.configFactoryDefault();
    rightBackMotor.configFactoryDefault();

    // Set the back Falcons to follow the front Falcons
    leftBackMotor.follow(leftFrontMotor);
    rightBackMotor.follow(rightFrontMotor);

    // Invert motor direction if set to true
    rightFrontMotor.setInverted(false);
    rightBackMotor.setInverted(false);
    leftFrontMotor.setInverted(true);
    leftBackMotor.setInverted(true); 

    leftBackMotor.setNeutralMode(NeutralMode.Coast);
    leftBackMotor.setNeutralMode(NeutralMode.Coast);
    rightFrontMotor.setNeutralMode(NeutralMode.Coast);
    rightBackMotor.setNeutralMode(NeutralMode.Coast);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /**
   * Arcade drive inverse kinematics for differential drive platform.
   *
   * @param xSpeed The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
   * @param zRotation robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
   *     positive.*/

  // Drive Type
  public void drive(double xSpeed, double zRotation){
    if(Math.abs(xSpeed) > joyThreshold  || Math.abs(zRotation) > joyThreshold ) {
      m_Drive.arcadeDrive(xSpeed, zRotation);

      m_Drive.arcadeDrive( filter.calculate(xSpeed*1.0), zRotation*-0.75);
    }
    else {
      m_Drive.arcadeDrive(0.0, 0.0);
    }
  }

  public void autoDriveBackwards(){
    leftFrontMotor.set(ControlMode.PercentOutput, 0.25);
    rightFrontMotor.set(ControlMode.PercentOutput, 0.25);
    
  }

  public void StopMotion(){
    leftFrontMotor.set(ControlMode.PercentOutput, 0.0);
    rightBackMotor.set(ControlMode.PercentOutput, 0.0);
  }
}