// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ManipulatorConstants;

public class Intake extends SubsystemBase {

  // Intake motor
  private final WPI_VictorSPX frontIntakeMotor = new WPI_VictorSPX(ManipulatorConstants.kfrontIntakeMotor);
  private final WPI_VictorSPX backIntakeMotor = new WPI_VictorSPX(ManipulatorConstants.kbackIntakeMotor);



  // The network table 
  private static final String TABLE_NAME = "Preferences";
  
  private static final NetworkTable m_table;
  private ShuffleboardTab IntakeTab = Shuffleboard.getTab("Variables");
  private NetworkTableEntry intakeSpeed = IntakeTab.add("IntakeSpeed", 1.0).getEntry();
  private NetworkTableEntry ejectSpeed = IntakeTab.add("EjectSpeed", 1.0).getEntry();
  private NetworkTableEntry holdSpeed = IntakeTab.add("HoldSpeed", 0.25).getEntry();


  // Creates a new Intake. 
  public Intake() {
   
   // Configures the Intake Victors's to default configuration
   frontIntakeMotor.configFactoryDefault();
   backIntakeMotor.configFactoryDefault();
  
   // Invert motor direction if set to true
   frontIntakeMotor.setInverted(false);
   backIntakeMotor.setInverted(false);

   frontIntakeMotor.setNeutralMode(NeutralMode.Coast);
   backIntakeMotor.setNeutralMode(NeutralMode.Coast);

   

  }

  @Override
  public void periodic() {
   // This method will be called once per scheduler run
  }

  // Run
  public void intakeBall() {
    double InSpeed = intakeSpeed.getDouble(1.0);
    frontIntakeMotor.set(InSpeed);
    backIntakeMotor.set(InSpeed);

}

  public void ejectBall() {
    double OutSpeed = ejectSpeed.getDouble(1.0);
    frontIntakeMotor.set(OutSpeed*-1);
    backIntakeMotor.set(OutSpeed*-1);

}

  public void holdBall() {
    double HoldSpeed = holdSpeed.getDouble(0.25);
    frontIntakeMotor.set(HoldSpeed*-1);
    backIntakeMotor.set(HoldSpeed);


  }

  // Stop Motion
  public void stopMotion() {
    frontIntakeMotor.set(0.0);
    backIntakeMotor.set(0.0);
  }

  static {
   m_table = NetworkTableInstance.getDefault().getTable(TABLE_NAME);
   m_table.getEntry(".type").setString("RobotPreferences");
  }

 }

 
 
