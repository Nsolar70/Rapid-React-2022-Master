// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.AutoDriveBackwards;
import frc.robot.commands.ClimbHigh;
import frc.robot.commands.ClimbLow;
import frc.robot.commands.ClimbToZero;
import frc.robot.commands.DriveLimeControl;
import frc.robot.commands.EjectBall;
import frc.robot.commands.Flipper_Intake;
import frc.robot.commands.FlywheelLimeControl;
import frc.robot.commands.HoldBall;
import frc.robot.commands.IntakeBall;
import frc.robot.commands.RunAutoProgram;
import frc.robot.commands.RunClimberDown;
import frc.robot.commands.RunClimberUp;
import frc.robot.commands.RunFlipper;
import frc.robot.commands.RunFlywheel;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Flipper;
import frc.robot.subsystems.Flywheel;
import frc.robot.subsystems.Intake;
import oi.limelightvision.limelight.frc.LimeLight;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */

public class RobotContainer {
  //Subsystems
  private final DriveTrain m_driveTrain = new DriveTrain();
  private final Intake m_intake = new Intake();
  private final Flipper m_flipper = new Flipper();
  private final Flywheel m_flywheel = new Flywheel();
  private final Climber m_climber = new Climber();

 //Commands
  private final IntakeBall m_intakeBall = new IntakeBall(m_intake);
  private final EjectBall m_ejectBall = new EjectBall(m_intake);
  private final HoldBall m_holdBall = new HoldBall(m_intake);
  private final RunFlywheel m_runFlywheel = new RunFlywheel(m_flywheel);
  private final RunFlipper m_runFlipper = new RunFlipper(m_flipper);
  private final ClimbHigh m_climbHigh = new ClimbHigh(m_climber);
  private final ClimbLow m_climbLow = new ClimbLow(m_climber);
  private final ClimbToZero m_climbToZero = new ClimbToZero(m_climber);
  private final RunClimberUp m_runClimberUp = new RunClimberUp(m_climber);
  private final RunClimberDown m_runClimberDown = new RunClimberDown(m_climber);
  private final Flipper_Intake m_Flipper_Intake = new Flipper_Intake(m_flipper, m_intake);
  private final FlywheelLimeControl m_FlywheelLimeControl = new FlywheelLimeControl(m_flywheel);
  

  //Auto Commands 
  private final AutoDriveBackwards m_autoDriveBackwards = new AutoDriveBackwards(m_driveTrain);
  private final RunAutoProgram m_runAutoProgram = new RunAutoProgram(m_flywheel, m_flipper, m_driveTrain);

 //Controllers
  XboxController m_driverController = new XboxController(OIConstants.kdriveJoyStick);
  XboxController m_actuatorController = new XboxController(OIConstants.kactuatorJoyStick);
 

  // LimeLight
  LimeLight m_limeLight = new LimeLight(); 

 
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
   
    m_driveTrain.setDefaultCommand(new RunCommand(
        () -> m_driveTrain.drive(m_driverController.getRightY(), m_driverController.getRightX()),m_driveTrain));

    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */

  private void configureButtonBindings() {

     // Run Intake Ball command when the 'X' button is held
     new JoystickButton(m_actuatorController, Button.kX.value).whileHeld(m_intakeBall);
     
     // Run Eject Ball command when the 'B' button is held
     new JoystickButton(m_actuatorController, Button.kB.value).whileHeld(m_ejectBall);

     //Toggle Hold Ball command when the 'Left Bumper' button is pressed
     new JoystickButton(m_actuatorController, Button.kLeftBumper.value).toggleWhenPressed(m_holdBall);

     //Toggle Shoot Ball command when the 'Y' button is pressed
     new JoystickButton(m_actuatorController, Button.kY.value ).toggleWhenPressed(m_runFlywheel);

     // Run RunFlipper command when the 'A' button is pressed
     new JoystickButton(m_actuatorController, Button.kA.value).whenPressed(m_runFlipper);

     // Run Climb High Command when the 'Y button is pressed
     new JoystickButton(m_driverController, Button.kY.value).toggleWhenPressed(m_climbHigh);

     //Run Climb Low Command when the 'A' Button is pressed
     new JoystickButton(m_driverController, Button.kA.value) .toggleWhenPressed(m_climbToZero);

     //Run Climber up command when the 'Left Bumper' button is held 
     new JoystickButton(m_driverController, Button.kLeftBumper.value).whileHeld(m_runClimberUp);

     //Run Climber down command when the 'Right Bumper' button is held
     new JoystickButton(m_driverController, Button.kRightBumper.value).whileHeld(m_runClimberDown);
     
     // Run Drive Lime Light Control when the 'Back' button is held
     new JoystickButton(m_driverController, Button.kBack.value).whileHeld(new DriveLimeControl(() -> 
                      m_driverController.getRightY(), m_limeLight, m_driveTrain)); 
      
     // Run Flywheel Lime Light Control when the 'Back' is held
     new JoystickButton(m_actuatorController, Button.kBack.value).whileHeld(m_FlywheelLimeControl);


   /*  if(isIntakeOn = true){
      m_actuatorController.setRumble(GenericHID.RumbleType.kLeftRumble, 1.0);
      m_actuatorController.setRumble(GenericHID.RumbleType.kRightRumble, 1.0);
    } else {
      m_actuatorController.setRumble(GenericHID.RumbleType.kLeftRumble, 0.0);
      m_actuatorController.setRumble(GenericHID.RumbleType.kRightRumble, 0.0); 
    } */

     //new POVButton(m_actuatorController, 0).whileActiveContinuous(m_ejectBall);
     //new POVButton(m_actuatorController, 180).whileActiveContinuous(m_intakeBall);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_runAutoProgram;
  }
}
