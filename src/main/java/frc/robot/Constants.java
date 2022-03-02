// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */

public final class Constants {

    //Drive Constants
    public static final class DriveConstants {

        //left drive
        public static final int kleftFrontMotor = 1;
        public static final int kleftBackMotor = 2;

        //right drive
        public static final int krightFrontMotor = 3;
        public static final int krightBackMotor = 4;

    }

    public static final class ManipulatorConstants {

        //intake
        public static final int kfrontIntakeMotor = 5;
        public static final int kbackIntakeMotor = 6;

        //flipper
        public static final int kflipperMotor= 11;

        //flywheel
        public static final int kflywheelMotor = 9;

        //climber
        public static final int kclimberMotor = 10;
    }

    //OI Constants
    public static final class OIConstants {

        //Controllers
        public static final int kdriveJoyStick = 0;
        public static final int kactuatorJoyStick = 1;

    }    
    
    public static final class EncoderConstants{

        /**
         * Which PID slot to pull gains from. Starting 2018, you can choose from
         * 0,1,2 or 3. Only the first two (0,1) are visible in web-based
         * configuration.
         */
	    public static final int kSlotIdx = 0;

        /*
        * Talon SRX/ Victor SPX will supported multiple (cascaded) PID loops. For
        * now we just want the primary one.
        */
	    public static final int kPIDLoopIdx = 0;

        /*
        * set to zero to skip waiting for confirmation, set to nonzero to wait and
        * report to DS if action fails.
        */
	    public static final int kTimeoutMs = 30;

    }


}

