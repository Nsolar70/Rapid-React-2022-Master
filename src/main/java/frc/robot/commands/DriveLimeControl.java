// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;
import oi.limelightvision.limelight.frc.LimeLight;

public class DriveLimeControl extends CommandBase {
  private final DriveTrain m_driveTrain;
  private final DoubleSupplier m_speed;
  private final LimeLight m_LimeLight;
  /** Creates a new DriveLimeControl. */
  public DriveLimeControl(DoubleSupplier speed, LimeLight limeLight, DriveTrain driveTrain) {
    m_speed = speed;
    m_LimeLight = limeLight;
    m_driveTrain = driveTrain;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_driveTrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_LimeLight.setPipeline(1);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double kp = 0.05;

    double xSpeed = m_speed.getAsDouble();
    double zRotation = m_LimeLight.getdegRotationToTarget()*kp;
    m_driveTrain.drive(xSpeed, zRotation); 
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_LimeLight.setPipeline(0);
    m_driveTrain.StopMotion();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
