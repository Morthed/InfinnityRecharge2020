package frc.robot;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


class TargetRequest {
  String coords = "";
	private static final String USER_AGENT = "Mozilla/5.0";
	private static final String GET_URL = "http://10.66.52.23:5000";
	public void sendGET() throws IOException {
		URL obj = new URL(GET_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

      // print result
      coords = response.toString();

		} else {
      System.out.println("GET request not worked");
      SmartDashboard.putString("Coords", "F");
		}

	}

}


class BallRequest {

  String coords = "";
	private static final String USER_AGENT = "Mozilla/5.0";
	private static final String GET_URL = "http://10.66.52.23:5000/ball";
	public void sendGET() throws IOException {
		URL obj = new URL(GET_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

      // print result
      coords = response.toString();
     
		} else {
      System.out.println("GET request not worked");
      SmartDashboard.putString("Coords", "F");
		}
	}
}

public class Robot extends TimedRobot {
//motores del chassis
public WPI_TalonSRX md1 = new WPI_TalonSRX(0);
public WPI_VictorSPX md2 = new WPI_VictorSPX(1);
public WPI_VictorSPX md3 = new WPI_VictorSPX(2);
public SpeedControllerGroup md = new SpeedControllerGroup(md1, md2,md3);
public WPI_TalonSRX mi1 = new WPI_TalonSRX(3);
public WPI_VictorSPX mi2 = new  WPI_VictorSPX(4);
public WPI_VictorSPX mi3 = new  WPI_VictorSPX(5);
  public SpeedControllerGroup mi = new SpeedControllerGroup(mi1, mi2,mi3);

//chssis
public DifferentialDrive chasis = new DifferentialDrive(md, mi);

//sensores
//public ADIS gyro1 = new ADXRS450_Gyro();

//Controles
public XboxController cm1 = new XboxController(0);
public XboxController cm2 = new XboxController(1);

//shotter
public WPI_TalonSRX shoot = new WPI_TalonSRX(9);
public WPI_TalonSRX shoot2 = new WPI_TalonSRX(12);
public DifferentialDrive shoot1 = new DifferentialDrive(shoot, shoot2);

//intake
public WPI_VictorSPX intake = new WPI_VictorSPX(10);
//Climb
public WPI_TalonSRX clim = new WPI_TalonSRX(10);


public DoubleSolenoid C = new DoubleSolenoid(0, 1);

//constantes
final double kP = 0.002;
final double kI = 0.0;
final double kD = 0.0;
double setpoint = 90.0;
double errorSum = 0;
double lastTimestamp=0;
double lastError = 0;
double datosensor = 0;
double outputSpeed =0;
double outputSpeed1=0;  
double valora;
double Dir = 0;

String x_str = "";
String y_str = "" ;
int x = 0;
int y = 0;

@Override
  public void robotInit() {
   // gyro1.reset();
  }

 
  @Override
  public void robotPeriodic() {
   SmartDashboard.putNumber("Valores de Giro", x);

    /* valora = gyro1.getAngle();
    if(gyro1.getAngle() >= 360  ){
       gyro1.reset();
 
    }
    if(gyro1.getAngle() <= -360  ){
      gyro1.reset();

   }
    if(gyro1.getAngle() <0){
      valora = gyro1.getAngle() +360;
    }
  

    datosensor = gyro1.getAngle()-setpoint;
    outputSpeed = kP* datosensor;
    
    SmartDashboard.putNumber("Angulo",valora); 
    SmartDashboard.putNumber("outputspeed1",outputSpeed1); 
    System.out.println(Math.round(valora));
    */
  }

  @Override
  public void autonomousInit() {
  }

  
  @Override
  public void autonomousPeriodic() {
    
    BallRequest connection = new BallRequest();     // Create a myCar object
    try {
      connection.sendGET();
      System.out.println(connection.coords);
      x_str = connection.coords.substring(0,3);
      y_str = connection.coords.substring(3,6);
      x = Integer.parseInt(x_str);
      y = Integer.parseInt(y_str);   
      SmartDashboard.putString("Coords", connection.coords);
    } catch (IOException e) {
      System.out.println("F");
      SmartDashboard.putString("Coords", "F");
      //TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    //Seguidor de linea de prepa
    
    if (x > 300&& x <340 ){
      chasis.arcadeDrive(-0.6, 0);
    } else if (x == 0 ){
      chasis.arcadeDrive(0, 0);
    } else if (x > 330){
      chasis.arcadeDrive(0, -0.4);
    }
    else if (x <310){
      chasis.arcadeDrive(0, 0.4);
    }
    
  }
    

  @Override
  public void teleopPeriodic() {
    //accinador de chassis
    Dir=(cm1.getRawAxis(3)-cm1.getRawAxis(2));
    chasis.arcadeDrive(-Dir,cm1.getRawAxis(0));
  
    //
  /*if(cm1.getRawButton(1)==true) {
    if (x < 310 && x >330 ){
      LZ.stopMotor();
    } else if (x < 310 ){
      LZ.set(0.5);
    } else if (x > 330){
      LZ.set(-0.5);
  }
  */
//}
   // velocidad pistones
    if (cm1.getRawButton(1)) { // mecanismo velocidad + //
      C.set(DoubleSolenoid.Value.kForward);
    } else {
      C.set(DoubleSolenoid.Value.kOff);
    }
    if (cm1.getRawButton(4)) { // mecanismo velocidad - //
      C.set(DoubleSolenoid.Value.kForward);
    } else {
      C.set(DoubleSolenoid.Value.kOff);
    }

    // Shooter disparando
    if (cm2.getRawButton(1)) {
      shoot1.arcadeDrive(-1, 0);
    }
    if (cm2.getRawButtonReleased(1)) {
      shoot1.arcadeDrive(0.3, 0.3);
    }
    if (cm2.getRawButtonReleased(2)) {
      clim.set(0.3);
    }
  }
}