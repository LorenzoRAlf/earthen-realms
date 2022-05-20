package game;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.fasterxml.jackson.databind.ObjectMapper;

import JsonData.loginData;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField Usuario;
	private JTextField Contrasena;

	/**
	 * Launch the application.

	/**
	 * Create the frame.
	 */
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(222, 184, 135));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("User:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setForeground(new Color(160, 82, 45));
		lblNewLabel.setBounds(129, 108, 46, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Password:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1.setForeground(new Color(160, 82, 45));
		lblNewLabel_1.setBounds(129, 139, 70, 14);
		contentPane.add(lblNewLabel_1);
		
		Usuario = new JTextField();
		Usuario.setBounds(209, 105, 102, 20);
		contentPane.add(Usuario);
		Usuario.setColumns(10);
		
		Contrasena = new JTextField();
		Contrasena.setBounds(209, 136, 102, 20);
		contentPane.add(Contrasena);
		Contrasena.setColumns(10);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBackground(new Color(160, 82, 45));
		btnNewButton.setForeground(new Color(222, 184, 135));
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
		        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		        mongoLogger.setLevel(Level.SEVERE);
		        
		        String cont;
		        String user;
		        
		        cont = Contrasena.getText();
		        user = Usuario.getText();
		        
		        try {
					URL url = new URL("https://final-project-ams.herokuapp.com/api/login?username=" + user + "&password=" + cont);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestProperty("accept", "application/json");
					InputStream responseStream = connection.getInputStream();
					ObjectMapper mapper = new ObjectMapper();
					loginData log = mapper.readValue(responseStream, loginData.class);
					
					if (log.status.equals("OK")){
						
						MenuSeleccion ms = new MenuSeleccion(log.user.get("_id").textValue());
						ms.setLocationRelativeTo(null);
						
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, log.message);
					}
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(170, 192, 102, 23);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel_2 = new JLabel("EARTHEN REALMS");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setForeground(new Color(160, 82, 45));
		lblNewLabel_2.setBackground(new Color(222, 184, 135));
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblNewLabel_2.setBounds(10, 30, 414, 55);
		contentPane.add(lblNewLabel_2);
	}
}
