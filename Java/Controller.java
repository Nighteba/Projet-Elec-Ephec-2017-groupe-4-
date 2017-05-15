//beaucoup de ces lignes de code ont été inspiré du code de Jordan Lagache
package Controller_temperature;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class Controller implements SerialPortEventListener {
	private InputStream in;
	private OutputStream output;
	private BufferedReader input;
	private int temp_actuel = 0;
	private static GUI frame;
	private int temp_max = 30;
	private int count = 0;
	
	
	public Controller(){
		
	}
	//code de Jordan Lagache
	public void connection(String port){
		try{
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(port);
			if(portIdentifier.isCurrentlyOwned()){
				System.out.println("Erreur : le port est actuellement utilisé");
			}else{
				CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
				if(commPort instanceof SerialPort){
					SerialPort serialPort = (SerialPort) commPort;
					serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
					//si tout marche bien
					System.out.println("Vous êtes connecté !");
					
					//utilisation de buffer read
					
					in = serialPort.getInputStream();
					input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
					output = serialPort.getOutputStream();
					serialPort.disableReceiveTimeout();
					serialPort.enableReceiveThreshold(1);
					serialPort.addEventListener(this);
					serialPort.notifyOnDataAvailable(true);
				}else{
					System.out.println("Erreur : seulement les ports séries sont manipulés");
				}
			}
		} catch (NoSuchPortException e){
			System.out.println("Le port " + port + " que vous avez sélectionné n'existe pas ");
		} catch(PortInUseException e){
			System.out.println("Le port " + port + " est déjà utilisé");
		}catch(UnsupportedCommOperationException e){
			System.out.println(" Les commandes au port " + port + " ne sont pas supportées");
		}
		catch(IOException e){
			System.out.println("il n'existe pas de flux d'entrées et de sorties pour le port " + port);
		}catch(TooManyListenersException e){
			e.printStackTrace();
		}
	}
	public void send(String limit){
		if(limit != null){
			try{
				output.write(limit.getBytes());
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	public synchronized void serialEvent(SerialPortEvent oEvent){
		if(oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			String data;
			int temperature;
			try {
				data = input.readLine();
				if(count ++== 0){
					temp_actuel = Integer.parseInt(data);
					temperature = temp_actuel;
				}else{
					temperature = Integer.parseInt(data);
					if(temperature != temp_actuel){
						temp_actuel = temperature;
					}
				}
				frame.alert(temp_actuel);
				frame.setTemp_c(temperature);
			} catch (Exception e){
				System.err.println(e.toString());
			}
		}
	}
	public int getTemp_actuel(){
		return temp_actuel;
	}
	public void setTemp_Actuel(int temp_actuel){
		if(temp_actuel > 0 && temp_actuel < 100){ //entre 0 et 100
			this.temp_actuel = temp_actuel;
		}else{
			System.out.println("Erreur : limite de température dépassée (0° - 100°");
		}
	}
	public int getTemp_max(){
		return temp_max;
	}
	public void setTemp_max(int temp_max){
		if(temp_max > 0 && temp_max < 100){
			this.temp_max = temp_max;
		}else{
			System.out.println("Erreur : limite de température dépassée (0° - 100°");
		}
	}
	public static void main(String[] args) {
		try{
			frame = new GUI("Temperature controller");
			frame.setVisible(true);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}	
