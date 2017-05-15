//beaucoup de ces lignes de code ont été inspiré de Jordan Lagache
package Controller_temperature;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import temp_handler.Handler;

public class GUI extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextField textField;
	private JComboBox<String> list;
	private JButton btnUnset;
	private JButton btnSet;
	private Controller controller;
	private int temp_c;
	private JLabel lblX;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public GUI(){};

	/**
	 * Create the frame.
	 */
	public GUI(String title) {
		super();
		controller = new Controller();
		GUI frame = new GUI();
		setTitle(title);
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\cedri\\OneDrive\\Images\\Projet\\temperature.png"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.RED);
		setBounds(100, 100, 500, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblComPort = new JLabel("Quel port COM souhaitez-vous utiliser ?");
		GridBagConstraints gbc_lblComPort = new GridBagConstraints();
		gbc_lblComPort.insets = new Insets(0, 0, 5, 5);
		gbc_lblComPort.gridx = 1;
		gbc_lblComPort.gridy = 5;
		contentPane.add(lblComPort, gbc_lblComPort);
		
		String[] selections = { "COM1", "COM2", "COM3", "COM4","COM5", "COM6", "COM7", "COM8","COM9"};
		Border etchedborder = new EtchedBorder(EtchedBorder.RAISED);
		list = new JComboBox<String>(selections);
		list.setBorder(etchedborder);
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.insets = new Insets(0, 0, 5, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 3;
		gbc_list.gridy = 5;
		contentPane.add(list, gbc_list);
		list.addActionListener(this);
		JLabel lblTemperature = new JLabel("Température actuelle");
		GridBagConstraints gbc_lblTemperature = new GridBagConstraints();
		gbc_lblTemperature.insets = new Insets(0, 0, 5, 5);
		gbc_lblTemperature.gridx = 1;
		gbc_lblTemperature.gridy = 6;
		contentPane.add(lblTemperature, gbc_lblTemperature);
		
		lblX = new JLabel(Integer.toString(temp_c));
		GridBagConstraints gbc_lblX = new GridBagConstraints();
		gbc_lblX.insets = new Insets(60, 0, 60, 0);
		gbc_lblX.gridx = 3;
		gbc_lblX.gridy = 6;
		contentPane.add(lblX, gbc_lblX);
		
		JLabel lblTemperatureMax = new JLabel("Température maximum");
		GridBagConstraints gbc_lblTemperatureMax = new GridBagConstraints();
		gbc_lblTemperatureMax.insets = new Insets(0, 0, 5, 5);
		gbc_lblTemperatureMax.gridx = 1;
		gbc_lblTemperatureMax.gridy = 7;
		contentPane.add(lblTemperatureMax, gbc_lblTemperatureMax);
		
		textField = new JTextField(controller.getTemp_max()); //par defaut == 35
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 3;
		gbc_textField.gridy = 7;
		contentPane.add(textField, gbc_textField);
		textField.setColumns(10);
		
		btnSet = new JButton("SET");
		GridBagConstraints gbc_btnSet = new GridBagConstraints();
		gbc_btnSet.insets = new Insets(0, 0, 5, 5);
		gbc_btnSet.gridx = 1;
		gbc_btnSet.gridy = 8;
		contentPane.add(btnSet, gbc_btnSet);
		btnSet.addActionListener(this);
		
		btnUnset = new JButton("RESET");
		GridBagConstraints gbc_btnUnset = new GridBagConstraints();
		gbc_btnUnset.insets = new Insets(0, 0, 5, 5);
		gbc_btnUnset.gridx = 3;
		gbc_btnUnset.gridy = 8;
		contentPane.add(btnUnset, gbc_btnUnset);
		btnUnset.addActionListener(this);
	}
	
	public int getTemp_c(){
		return temp_c;
	}
	
	public void setTemp_c(int temp_c){
		this.temp_c = temp_c;
		lblX.setText(Integer.toString(temp_c));
	}
	
	public void actionPerformed(ActionEvent evt){
		if(evt != null){
			if(evt.getSource() == list){
				System.out.println("list");
				System.out.println(list.getSelectedItem().toString());
				controller.connection(list.getSelectedItem().toString());
				//connection au port
			}
			else if(evt.getSource() == btnSet){
				int temp = Integer.parseInt(textField.getText());
				controller.send(textField.getText() + ";");
				controller.setTemp_max(temp);
				alert(temp);
				//envoi de la valeur au PIC
			}
			else if(evt.getSource() == btnUnset){
				System.out.println("Reset");
				textField.setText("35");
				controller.setTemp_max(35);
				//Remise à 0
			}else{
				System.out.println("Aucun évènement associé");
			}
		}
	}
	public void alert(int temp){
		System.out.println("Température actuelle : " + temp + "     Seuil de température :" + controller.getTemp_max());
		if(temp < controller.getTemp_max()){
			//Si tout est bon
			lblX.setForeground(Color.green);
		}else
			//on passe dans le rouge
			lblX.setForeground(Color.red);
	}
}
