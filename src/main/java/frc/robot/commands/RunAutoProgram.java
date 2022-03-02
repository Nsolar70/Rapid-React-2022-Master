// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Flipper;
import frc.robot.subsystems.Flywheel;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class RunAutoProgram extends SequentialCommandGroup {

  /** Creates a new RunAutoProgram. */
  public RunAutoProgram(Flywheel flywheel, Flipper flipper, DriveTrain driveTrain) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new RunFlywheel(flywheel).withTimeout(1.5), 
      parallel(
      new RunFlywheel(flywheel),
      new RunFlipper(flipper)).withTimeout(2.5),
      parallel(
      new RunFlywheel(flywheel),
      new RunFlipper(flipper)).withTimeout(2.5),
      new AutoDriveBackwards(driveTrain).withTimeout(1.0)
      );
    
  }
}
