package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import JsonData.armorData;
import JsonData.coinsData;
import JsonData.damageData;
import JsonData.uarmorData;
import JsonData.uweaponsData;
import JsonData.weaponsData;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import javax.swing.SwingConstants;
import javax.swing.JSplitPane;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Treasury extends JFrame {

	private JPanel contentPane;
	public weaponsData wdata;
	public armorData adata;
	public JLabel armaR, armaduraR;
	public boolean hasit;
	public coinsData coinsd;
	public JsonNode weapons, armors;

	public Treasury(String idPlayer) {
		
		try {
			URL url = new URL("https://final-project-ams.herokuapp.com/userWeapons?playerId="+idPlayer);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("accept", "application/json");
			InputStream responseStream = connection.getInputStream();
			ObjectMapper mapper = new ObjectMapper();
			uweaponsData wdata = mapper.readValue(responseStream, uweaponsData.class);
			
			weapons = wdata.wlist;
			
			URL url2 = new URL("https://final-project-ams.herokuapp.com/userArmors?playerId="+idPlayer);
			HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
			connection2.setRequestProperty("accept", "application/json");
			InputStream responseStream2 = connection2.getInputStream();
			ObjectMapper mapper2 = new ObjectMapper();
			uarmorData adata = mapper2.readValue(responseStream2, uarmorData.class);
			
			armors = adata.alist;
			
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			URL url = new URL("https://final-project-ams.herokuapp.com/weapons");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("accept", "application/json");
			InputStream responseStream = connection.getInputStream();
			ObjectMapper mapper = new ObjectMapper();
			
			wdata = mapper.readValue(responseStream, weaponsData.class);
			
			URL url2 = new URL("https://final-project-ams.herokuapp.com/armors");
			HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
			connection2.setRequestProperty("accept", "application/json");
			InputStream responseStream2 = connection2.getInputStream();
			ObjectMapper mapper2 = new ObjectMapper();
			
			adata = mapper2.readValue(responseStream2, armorData.class);
			
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 681, 302);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(222, 184, 135));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(222, 184, 135));
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("Welcome to the Treasury");
		lblNewLabel.setForeground(new Color(160, 82, 45));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel.add(lblNewLabel);
		
		JButton btnNewButton_2 = new JButton("Return");
		btnNewButton_2.setBackground(new Color(160, 82, 45));
		btnNewButton_2.setForeground(new Color(222, 184, 135));
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MenuSeleccion ms = new MenuSeleccion(idPlayer);
				ms.setLocationRelativeTo(null);
				
				dispose();
			}
		});
		panel.add(btnNewButton_2, BorderLayout.EAST);
		
		try {
			URL url = new URL("https://final-project-ams.herokuapp.com/coins?playerId=" + idPlayer);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("accept", "application/json");
			InputStream responseStream = connection.getInputStream();
			ObjectMapper mapper = new ObjectMapper();
			
			coinsd = mapper.readValue(responseStream, coinsData.class);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		JLabel coins = new JLabel(coinsd.coins + " coins");
		coins.setFont(new Font("Tahoma", Font.BOLD, 11));
		coins.setForeground(new Color(160, 82, 45));
		panel.add(coins, BorderLayout.WEST);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBackground(new Color(222, 184, 135));
		splitPane.setEnabled(false);
		splitPane.setResizeWeight(0.5);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(222, 184, 135));
		splitPane.setLeftComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(222, 184, 135));
		panel_1.add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(null);
		
		armaR = new JLabel("");
		armaR.setHorizontalAlignment(SwingConstants.CENTER);
		armaR.setBounds(0, 11, 324, 72);
		panel_3.add(armaR);
		
		JButton btnArma = new JButton("100 ");
		btnArma.setBackground(new Color(160, 82, 45));
		btnArma.setForeground(new Color(222, 184, 135));
		btnArma.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int monedas =Integer.parseInt(coins.getText().replaceAll(" coins", ""));
				if (monedas < 99) {
					JOptionPane.showMessageDialog(null, "You do not have enough coins");
				} else {
					Random r = new Random();
					int low = 0;
					int high = wdata.wlist.size()-1;
					int result = r.nextInt(high-low) + low;
					
					Weapon arma = new Weapon(wdata.wlist.get(result).get("weaponName").textValue(), wdata.wlist.get(result).get("baseDamage").intValue(), wdata.wlist.get(result).get("damageType").textValue(), wdata.wlist.get(result).get("_id").textValue());
					
					String tipoa = arma.getDamageType();
					try {
						if (tipoa.equals("Electric")) {
							panel_3.remove(armaR);
							armaR = new JLabel("");
							armaR.setHorizontalAlignment(SwingConstants.CENTER);
							armaR.setBounds(0, 11, 324, 72);
							armaR.setIcon(new ImageIcon(new URL("https://imgur.com/jPLCqJY.png")));
							panel_3.add(armaR);
							repaint();
							
							for (int i = 0; i < weapons.size(); i++) {
								if (weapons.get(i).textValue().equals(arma.getId())){
									hasit = true;
								}
							}
							try {
								if (!hasit) {
									URL url2 = new URL ("https://final-project-ams.herokuapp.com/unlockWeapon?playerId="+ idPlayer +"&weapon="+ arma.getId());
									HttpURLConnection con = (HttpURLConnection)url2.openConnection();
									con.setRequestMethod("POST");
									con.setRequestProperty("Content-Type", "application/json; utf-8");
									con.setRequestProperty("Accept", "application/json");
									con.setDoOutput(true);
									try(BufferedReader br = new BufferedReader(
									  new InputStreamReader(con.getInputStream(), "utf-8"))) {
									    StringBuilder response = new StringBuilder();
									    String responseLine = null;
									    while ((responseLine = br.readLine()) != null) {
									        response.append(responseLine.trim());
									    }
									}
								}
								URL url3 = new URL ("https://final-project-ams.herokuapp.com/chargeCoins?playerId="+ idPlayer +"&coins="+ 100);
								HttpURLConnection con3 = (HttpURLConnection)url3.openConnection();
								con3.setRequestMethod("POST");
								con3.setRequestProperty("Content-Type", "application/json; utf-8");
								con3.setRequestProperty("Accept", "application/json");
								con3.setDoOutput(true);
								try(BufferedReader br = new BufferedReader(
								  new InputStreamReader(con3.getInputStream(), "utf-8"))) {
								    StringBuilder response = new StringBuilder();
								    String responseLine = null;
								    while ((responseLine = br.readLine()) != null) {
								        response.append(responseLine.trim());
								    }
								}
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							hasit = false;
						} else if (tipoa.equals("Water")) {
							panel_3.remove(armaR);
							armaR = new JLabel("");
							armaR.setHorizontalAlignment(SwingConstants.CENTER);
							armaR.setBounds(0, 11, 324, 72);
							armaR.setIcon(new ImageIcon(new URL("https://imgur.com/5XbQAV0.png")));
							panel_3.add(armaR);
							repaint();
							
							for (int i = 0; i < weapons.size(); i++) {
								if (weapons.get(i).textValue().equals(arma.getId())){
									hasit = true;
								}
							}
							
							
							try {
								if (!hasit) {
									URL url2 = new URL ("https://final-project-ams.herokuapp.com/unlockWeapon?playerId="+ idPlayer +"&weapon="+ arma.getId());
									HttpURLConnection con = (HttpURLConnection)url2.openConnection();
									con.setRequestMethod("POST");
									con.setRequestProperty("Content-Type", "application/json; utf-8");
									con.setRequestProperty("Accept", "application/json");
									con.setDoOutput(true);
									try(BufferedReader br = new BufferedReader(
									  new InputStreamReader(con.getInputStream(), "utf-8"))) {
									    StringBuilder response = new StringBuilder();
									    String responseLine = null;
									    while ((responseLine = br.readLine()) != null) {
									        response.append(responseLine.trim());
									    }
									}
								}
								URL url3 = new URL ("https://final-project-ams.herokuapp.com/chargeCoins?playerId="+ idPlayer +"&coins="+ 100);
								HttpURLConnection con3 = (HttpURLConnection)url3.openConnection();
								con3.setRequestMethod("POST");
								con3.setRequestProperty("Content-Type", "application/json; utf-8");
								con3.setRequestProperty("Accept", "application/json");
								con3.setDoOutput(true);
								try(BufferedReader br = new BufferedReader(
								  new InputStreamReader(con3.getInputStream(), "utf-8"))) {
								    StringBuilder response = new StringBuilder();
								    String responseLine = null;
								    while ((responseLine = br.readLine()) != null) {
								        response.append(responseLine.trim());
								    }
								}
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							hasit = false;
						} else if (tipoa.equals("Rock")) {
							panel_3.remove(armaR);
							armaR = new JLabel("");
							armaR.setHorizontalAlignment(SwingConstants.CENTER);
							armaR.setBounds(0, 11, 324, 72);
							armaR.setIcon(new ImageIcon(new URL("https://imgur.com/xlyQtci.png")));
							panel_3.add(armaR);
							repaint();
							
							for (int i = 0; i < weapons.size(); i++) {
								if (weapons.get(i).textValue().equals(arma.getId())){
									hasit = true;
								}
							}
							
							try {
								if (!hasit) {
									URL url2 = new URL ("https://final-project-ams.herokuapp.com/unlockWeapon?playerId="+ idPlayer +"&weapon="+ arma.getId());
									HttpURLConnection con = (HttpURLConnection)url2.openConnection();
									con.setRequestMethod("POST");
									con.setRequestProperty("Content-Type", "application/json; utf-8");
									con.setRequestProperty("Accept", "application/json");
									con.setDoOutput(true);
									try(BufferedReader br = new BufferedReader(
									  new InputStreamReader(con.getInputStream(), "utf-8"))) {
									    StringBuilder response = new StringBuilder();
									    String responseLine = null;
									    while ((responseLine = br.readLine()) != null) {
									        response.append(responseLine.trim());
									    }
									}
								}
								URL url3 = new URL ("https://final-project-ams.herokuapp.com/chargeCoins?playerId="+ idPlayer +"&coins="+ 100);
								HttpURLConnection con3 = (HttpURLConnection)url3.openConnection();
								con3.setRequestMethod("POST");
								con3.setRequestProperty("Content-Type", "application/json; utf-8");
								con3.setRequestProperty("Accept", "application/json");
								con3.setDoOutput(true);
								try(BufferedReader br = new BufferedReader(
								  new InputStreamReader(con3.getInputStream(), "utf-8"))) {
								    StringBuilder response = new StringBuilder();
								    String responseLine = null;
								    while ((responseLine = br.readLine()) != null) {
								        response.append(responseLine.trim());
								    }
								}
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							hasit = false;
						} else if (tipoa.equals("Fire")) {
							panel_3.remove(armaR);
							armaR = new JLabel("");
							armaR.setHorizontalAlignment(SwingConstants.CENTER);
							armaR.setBounds(0, 11, 324, 72);
							armaR.setIcon(new ImageIcon(new URL("https://imgur.com/yjfU7HB.png")));
							panel_3.add(armaR);
							repaint();
							
							for (int i = 0; i < weapons.size(); i++) {
								if (weapons.get(i).textValue().equals(arma.getId())){
									hasit = true;
								}
							}
							
							
							try {
								if (!hasit) {
									URL url2 = new URL ("https://final-project-ams.herokuapp.com/unlockWeapon?playerId="+ idPlayer +"&weapon="+ arma.getId());
									HttpURLConnection con = (HttpURLConnection)url2.openConnection();
									con.setRequestMethod("POST");
									con.setRequestProperty("Content-Type", "application/json; utf-8");
									con.setRequestProperty("Accept", "application/json");
									con.setDoOutput(true);
									try(BufferedReader br = new BufferedReader(
									  new InputStreamReader(con.getInputStream(), "utf-8"))) {
									    StringBuilder response = new StringBuilder();
									    String responseLine = null;
									    while ((responseLine = br.readLine()) != null) {
									        response.append(responseLine.trim());
									    }
									}
								}
								
								URL url3 = new URL ("https://final-project-ams.herokuapp.com/chargeCoins?playerId="+ idPlayer +"&coins="+ 100);
								HttpURLConnection con3 = (HttpURLConnection)url3.openConnection();
								con3.setRequestMethod("POST");
								con3.setRequestProperty("Content-Type", "application/json; utf-8");
								con3.setRequestProperty("Accept", "application/json");
								con3.setDoOutput(true);
								try(BufferedReader br = new BufferedReader(
								  new InputStreamReader(con3.getInputStream(), "utf-8"))) {
								    StringBuilder response = new StringBuilder();
								    String responseLine = null;
								    while ((responseLine = br.readLine()) != null) {
								        response.append(responseLine.trim());
								    }
								}
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							hasit = false;
						} else if (tipoa.equals("Grass")) {
							panel_3.remove(armaR);
							armaR = new JLabel("");
							armaR.setHorizontalAlignment(SwingConstants.CENTER);
							armaR.setBounds(0, 11, 324, 72);
							armaR.setIcon(new ImageIcon(new URL("https://imgur.com/EAvE0vC.png")));
							panel_3.add(armaR);
							repaint();
							
							for (int i = 0; i < weapons.size(); i++) {
								if (weapons.get(i).textValue().equals(arma.getId())){
									hasit = true;
								}
							}
							
							try {
								if (!hasit) {
									URL url2 = new URL ("https://final-project-ams.herokuapp.com/unlockWeapon?playerId="+ idPlayer +"&weapon="+ arma.getId());
									HttpURLConnection con = (HttpURLConnection)url2.openConnection();
									con.setRequestMethod("POST");
									con.setRequestProperty("Content-Type", "application/json; utf-8");
									con.setRequestProperty("Accept", "application/json");
									con.setDoOutput(true);
									try(BufferedReader br = new BufferedReader(
									  new InputStreamReader(con.getInputStream(), "utf-8"))) {
									    StringBuilder response = new StringBuilder();
									    String responseLine = null;
									    while ((responseLine = br.readLine()) != null) {
									        response.append(responseLine.trim());
									    }
									}
								}
								
								URL url3 = new URL ("https://final-project-ams.herokuapp.com/chargeCoins?playerId="+ idPlayer +"&coins="+ 100);
								HttpURLConnection con3 = (HttpURLConnection)url3.openConnection();
								con3.setRequestMethod("POST");
								con3.setRequestProperty("Content-Type", "application/json; utf-8");
								con3.setRequestProperty("Accept", "application/json");
								con3.setDoOutput(true);
								try(BufferedReader br = new BufferedReader(
								  new InputStreamReader(con3.getInputStream(), "utf-8"))) {
								    StringBuilder response = new StringBuilder();
								    String responseLine = null;
								    while ((responseLine = br.readLine()) != null) {
								        response.append(responseLine.trim());
								    }
								}
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							hasit = false;
						} else if (tipoa.equals("Light")) {
							panel_3.remove(armaR);
							armaR = new JLabel("");
							armaR.setHorizontalAlignment(SwingConstants.CENTER);
							armaR.setBounds(0, 11, 324, 72);
							armaR.setIcon(new ImageIcon(new URL("https://imgur.com/7SHm8M3.png")));
							panel_3.add(armaR);
							repaint();
							
							for (int i = 0; i < weapons.size(); i++) {
								if (weapons.get(i).textValue().equals(arma.getId())){
									hasit = true;
								}
							}
							
							try {
								if (!hasit) {
									URL url2 = new URL ("https://final-project-ams.herokuapp.com/unlockWeapon?playerId="+ idPlayer +"&weapon="+ arma.getId());
									HttpURLConnection con = (HttpURLConnection)url2.openConnection();
									con.setRequestMethod("POST");
									con.setRequestProperty("Content-Type", "application/json; utf-8");
									con.setRequestProperty("Accept", "application/json");
									con.setDoOutput(true);
									try(BufferedReader br = new BufferedReader(
									  new InputStreamReader(con.getInputStream(), "utf-8"))) {
									    StringBuilder response = new StringBuilder();
									    String responseLine = null;
									    while ((responseLine = br.readLine()) != null) {
									        response.append(responseLine.trim());
									    }
									}
								}
								
								URL url3 = new URL ("https://final-project-ams.herokuapp.com/chargeCoins?playerId="+ idPlayer +"&coins="+ 100);
								HttpURLConnection con3 = (HttpURLConnection)url3.openConnection();
								con3.setRequestMethod("POST");
								con3.setRequestProperty("Content-Type", "application/json; utf-8");
								con3.setRequestProperty("Accept", "application/json");
								con3.setDoOutput(true);
								try(BufferedReader br = new BufferedReader(
								  new InputStreamReader(con3.getInputStream(), "utf-8"))) {
								    StringBuilder response = new StringBuilder();
								    String responseLine = null;
								    while ((responseLine = br.readLine()) != null) {
								        response.append(responseLine.trim());
								    }
								}
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							hasit = false;
						} else if (tipoa.equals("Darkness")) {
							panel_3.remove(armaR);
							armaR = new JLabel("");
							armaR.setHorizontalAlignment(SwingConstants.CENTER);
							armaR.setBounds(0, 11, 324, 72);
							armaR.setIcon(new ImageIcon(new URL("https://imgur.com/fX60Wiq.png")));
							panel_3.add(armaR);
							repaint();
							
							for (int i = 0; i < weapons.size(); i++) {
								if (weapons.get(i).textValue().equals(arma.getId())){
									hasit = true;
								}
							}
							try {
								if (!hasit) {
									URL url2 = new URL ("https://final-project-ams.herokuapp.com/unlockWeapon?playerId="+ idPlayer +"&weapon="+ arma.getId());
									HttpURLConnection con = (HttpURLConnection)url2.openConnection();
									con.setRequestMethod("POST");
									con.setRequestProperty("Content-Type", "application/json; utf-8");
									con.setRequestProperty("Accept", "application/json");
									con.setDoOutput(true);
									try(BufferedReader br = new BufferedReader(
									  new InputStreamReader(con.getInputStream(), "utf-8"))) {
									    StringBuilder response = new StringBuilder();
									    String responseLine = null;
									    while ((responseLine = br.readLine()) != null) {
									        response.append(responseLine.trim());
									    }
									}
								}
								
								URL url3 = new URL ("https://final-project-ams.herokuapp.com/chargeCoins?playerId="+ idPlayer +"&coins="+ 100);
								HttpURLConnection con3 = (HttpURLConnection)url3.openConnection();
								con3.setRequestMethod("POST");
								con3.setRequestProperty("Content-Type", "application/json; utf-8");
								con3.setRequestProperty("Accept", "application/json");
								con3.setDoOutput(true);
								try(BufferedReader br = new BufferedReader(
								  new InputStreamReader(con3.getInputStream(), "utf-8"))) {
								    StringBuilder response = new StringBuilder();
								    String responseLine = null;
								    while ((responseLine = br.readLine()) != null) {
								        response.append(responseLine.trim());
								    }
								}
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							hasit = false;
						} else if (tipoa.equals("Normal")) {
							panel_3.remove(armaR);
							armaR = new JLabel("");
							armaR.setHorizontalAlignment(SwingConstants.CENTER);
							armaR.setBounds(0, 11, 324, 72);
							armaR.setIcon(new ImageIcon(new URL("https://imgur.com/Twytt1Z.png")));
							panel_3.add(armaR);
							repaint();
							
							for (int i = 0; i < weapons.size(); i++) {
								if (weapons.get(i).textValue().equals(arma.getId())){
									hasit = true;
								}
							}
							try {
								if (!hasit) {
									URL url2 = new URL ("https://final-project-ams.herokuapp.com/unlockWeapon?playerId="+ idPlayer +"&weapon="+ arma.getId());
									HttpURLConnection con = (HttpURLConnection)url2.openConnection();
									con.setRequestMethod("POST");
									con.setRequestProperty("Content-Type", "application/json; utf-8");
									con.setRequestProperty("Accept", "application/json");
									con.setDoOutput(true);
									try(BufferedReader br = new BufferedReader(
									  new InputStreamReader(con.getInputStream(), "utf-8"))) {
									    StringBuilder response = new StringBuilder();
									    String responseLine = null;
									    while ((responseLine = br.readLine()) != null) {
									        response.append(responseLine.trim());
									    }
									}
								}
								
								URL url3 = new URL ("https://final-project-ams.herokuapp.com/chargeCoins?playerId="+ idPlayer +"&coins="+ 100);
								HttpURLConnection con3 = (HttpURLConnection)url3.openConnection();
								con3.setRequestMethod("POST");
								con3.setRequestProperty("Content-Type", "application/json; utf-8");
								con3.setRequestProperty("Accept", "application/json");
								con3.setDoOutput(true);
								try(BufferedReader br = new BufferedReader(
								  new InputStreamReader(con3.getInputStream(), "utf-8"))) {
								    StringBuilder response = new StringBuilder();
								    String responseLine = null;
								    while ((responseLine = br.readLine()) != null) {
								        response.append(responseLine.trim());
								    }
								}
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							hasit = false;
						} else if (tipoa.equals("Wind")) {
							panel_3.remove(armaR);
							armaR = new JLabel("");
							armaR.setHorizontalAlignment(SwingConstants.CENTER);
							armaR.setBounds(0, 11, 324, 72);
							armaR.setIcon(new ImageIcon(new URL("https://imgur.com/fCIgtcb.png")));
							panel_3.add(armaR);
							repaint();
							
							for (int i = 0; i < weapons.size(); i++) {
								if (weapons.get(i).textValue().equals(arma.getId())){
									hasit = true;
								}
							}
							try {
								if (!hasit) {
									URL url2 = new URL ("https://final-project-ams.herokuapp.com/unlockWeapon?playerId="+ idPlayer +"&weapon="+ arma.getId());
									HttpURLConnection con = (HttpURLConnection)url2.openConnection();
									con.setRequestMethod("POST");
									con.setRequestProperty("Content-Type", "application/json; utf-8");
									con.setRequestProperty("Accept", "application/json");
									con.setDoOutput(true);
									try(BufferedReader br = new BufferedReader(
									  new InputStreamReader(con.getInputStream(), "utf-8"))) {
									    StringBuilder response = new StringBuilder();
									    String responseLine = null;
									    while ((responseLine = br.readLine()) != null) {
									        response.append(responseLine.trim());
									    }
									}
								}
								
								URL url3 = new URL ("https://final-project-ams.herokuapp.com/chargeCoins?playerId="+ idPlayer +"&coins="+ 100);
								HttpURLConnection con3 = (HttpURLConnection)url3.openConnection();
								con3.setRequestMethod("POST");
								con3.setRequestProperty("Content-Type", "application/json; utf-8");
								con3.setRequestProperty("Accept", "application/json");
								con3.setDoOutput(true);
								try(BufferedReader br = new BufferedReader(
								  new InputStreamReader(con3.getInputStream(), "utf-8"))) {
								    StringBuilder response = new StringBuilder();
								    String responseLine = null;
								    while ((responseLine = br.readLine()) != null) {
								        response.append(responseLine.trim());
								    }
								}

							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					}
					if (!hasit) {
						coins.setText(monedas - 100 + " coins");
						repaint();
					}
					hasit = false;
				}
			}
		});
		btnArma.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel_1.add(btnArma, BorderLayout.SOUTH);
//		if (coinsd.coins < 100) {
//			btnArma.setEnabled(false);
//		}
		
		
		JLabel lblNewLabel_1 = new JLabel("Weapons");
		lblNewLabel_1.setForeground(new Color(160, 82, 45));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblNewLabel_1, BorderLayout.NORTH);
		

		
		JLabel armasChest = new JLabel("");
		armasChest.setHorizontalAlignment(SwingConstants.CENTER);
		armasChest.setBounds(0, 94, 324, 85);
		try {
			armasChest.setIcon(new ImageIcon(new URL("https://imgur.com/1KzZnsJ.png")));
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		panel_3.add(armasChest);
		

		

		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(222, 184, 135));
		splitPane.setRightComponent(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3_1 = new JPanel();
		panel_3_1.setBackground(new Color(222, 184, 135));
		panel_3_1.setLayout(null);
		panel_2.add(panel_3_1, BorderLayout.CENTER);
		
		armaduraR = new JLabel("");
		armaduraR.setHorizontalAlignment(SwingConstants.CENTER);
		armaduraR.setBounds(0, 11, 324, 72);
		panel_3_1.add(armaduraR);
		
		JButton btnArmadura = new JButton("100");
		btnArmadura.setBackground(new Color(160, 82, 45));
		btnArmadura.setForeground(new Color(222, 184, 135));
		btnArmadura.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				int monedas =Integer.parseInt(coins.getText().replaceAll(" coins", ""));
				if (monedas < 99) {
					JOptionPane.showMessageDialog(null, "You do not have enough coins");
				} else {
					Random r = new Random();
					int low = 0;
					int high = adata.alist.size()-1;
					int result = r.nextInt(high-low) + low;
					
					Armor armadura = new Armor(adata.alist.get(result).get("armorName").textValue(), adata.alist.get(result).get("armorType").textValue(), adata.alist.get(result).get("baseSpeed").intValue(), adata.alist.get(result).get("baseDefense").intValue(), adata.alist.get(result).get("_id").textValue());
					
					String tipoa = armadura.getType();
					
					try {
						if (tipoa.equals("Electric")) {
							panel_3_1.remove(armaduraR);
							armaduraR = new JLabel("");
							armaduraR.setHorizontalAlignment(SwingConstants.CENTER);
							armaduraR.setBounds(0, 11, 324, 72);
							armaduraR.setIcon(new ImageIcon(new URL("https://imgur.com/aI26ZzO.png")));
							panel_3_1.add(armaduraR);
							repaint();
							
							for (int i = 0; i < armors.size(); i++) {
								if (armors.get(i).textValue().equals(armadura.getId())){
									hasit = true;
								}
							}
							try {
								if (!hasit) {
									URL url2 = new URL ("https://final-project-ams.herokuapp.com/unlockArmor?playerId="+ idPlayer +"&armor="+ armadura.getId());
									HttpURLConnection con = (HttpURLConnection)url2.openConnection();
									con.setRequestMethod("POST");
									con.setRequestProperty("Content-Type", "application/json; utf-8");
									con.setRequestProperty("Accept", "application/json");
									con.setDoOutput(true);
									try(BufferedReader br = new BufferedReader(
									  new InputStreamReader(con.getInputStream(), "utf-8"))) {
									    StringBuilder response = new StringBuilder();
									    String responseLine = null;
									    while ((responseLine = br.readLine()) != null) {
									        response.append(responseLine.trim());
									    }
									}
								}
								
								URL url3 = new URL ("https://final-project-ams.herokuapp.com/chargeCoins?playerId="+ idPlayer +"&coins="+ 100);
								HttpURLConnection con3 = (HttpURLConnection)url3.openConnection();
								con3.setRequestMethod("POST");
								con3.setRequestProperty("Content-Type", "application/json; utf-8");
								con3.setRequestProperty("Accept", "application/json");
								con3.setDoOutput(true);
								try(BufferedReader br = new BufferedReader(
								  new InputStreamReader(con3.getInputStream(), "utf-8"))) {
								    StringBuilder response = new StringBuilder();
								    String responseLine = null;
								    while ((responseLine = br.readLine()) != null) {
								        response.append(responseLine.trim());
								    }
								}
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							hasit = false;
						} else if (tipoa.equals("Water")) {
							panel_3_1.remove(armaduraR);
							armaduraR = new JLabel("");
							armaduraR.setHorizontalAlignment(SwingConstants.CENTER);
							armaduraR.setBounds(0, 11, 324, 72);
							armaduraR.setIcon(new ImageIcon(new URL("https://imgur.com/j24bsko.png")));
							panel_3_1.add(armaduraR);
							repaint();
							
							for (int i = 0; i < armors.size(); i++) {
								if (armors.get(i).textValue().equals(armadura.getId())){
									hasit = true;
								}
							}
							try {
								if (!hasit) {
									URL url2 = new URL ("https://final-project-ams.herokuapp.com/unlockArmor?playerId="+ idPlayer +"&armor="+ armadura.getId());
									HttpURLConnection con = (HttpURLConnection)url2.openConnection();
									con.setRequestMethod("POST");
									con.setRequestProperty("Content-Type", "application/json; utf-8");
									con.setRequestProperty("Accept", "application/json");
									con.setDoOutput(true);
									try(BufferedReader br = new BufferedReader(
									  new InputStreamReader(con.getInputStream(), "utf-8"))) {
									    StringBuilder response = new StringBuilder();
									    String responseLine = null;
									    while ((responseLine = br.readLine()) != null) {
									        response.append(responseLine.trim());
									    }
									}
								}
								
								URL url3 = new URL ("https://final-project-ams.herokuapp.com/chargeCoins?playerId="+ idPlayer +"&coins="+ 100);
								HttpURLConnection con3 = (HttpURLConnection)url3.openConnection();
								con3.setRequestMethod("POST");
								con3.setRequestProperty("Content-Type", "application/json; utf-8");
								con3.setRequestProperty("Accept", "application/json");
								con3.setDoOutput(true);
								try(BufferedReader br = new BufferedReader(
								  new InputStreamReader(con3.getInputStream(), "utf-8"))) {
								    StringBuilder response = new StringBuilder();
								    String responseLine = null;
								    while ((responseLine = br.readLine()) != null) {
								        response.append(responseLine.trim());
								    }
								}
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							hasit = false;
						} else if (tipoa.equals("Rock")) {
							panel_3_1.remove(armaduraR);
							armaduraR = new JLabel("");
							armaduraR.setHorizontalAlignment(SwingConstants.CENTER);
							armaduraR.setBounds(0, 11, 324, 72);
							armaduraR.setIcon(new ImageIcon(new URL("https://imgur.com/RABnnWe.png")));
							panel_3_1.add(armaduraR);
							repaint();
							
							for (int i = 0; i < armors.size(); i++) {
								if (armors.get(i).textValue().equals(armadura.getId())){
									hasit = true;
								}
							}
							try {
								if (!hasit) {
									URL url2 = new URL ("https://final-project-ams.herokuapp.com/unlockArmor?playerId="+ idPlayer +"&armor="+ armadura.getId());
									HttpURLConnection con = (HttpURLConnection)url2.openConnection();
									con.setRequestMethod("POST");
									con.setRequestProperty("Content-Type", "application/json; utf-8");
									con.setRequestProperty("Accept", "application/json");
									con.setDoOutput(true);
									try(BufferedReader br = new BufferedReader(
									  new InputStreamReader(con.getInputStream(), "utf-8"))) {
									    StringBuilder response = new StringBuilder();
									    String responseLine = null;
									    while ((responseLine = br.readLine()) != null) {
									        response.append(responseLine.trim());
									    }
									}
								}
								
								URL url3 = new URL ("https://final-project-ams.herokuapp.com/chargeCoins?playerId="+ idPlayer +"&coins="+ 100);
								HttpURLConnection con3 = (HttpURLConnection)url3.openConnection();
								con3.setRequestMethod("POST");
								con3.setRequestProperty("Content-Type", "application/json; utf-8");
								con3.setRequestProperty("Accept", "application/json");
								con3.setDoOutput(true);
								try(BufferedReader br = new BufferedReader(
								  new InputStreamReader(con3.getInputStream(), "utf-8"))) {
								    StringBuilder response = new StringBuilder();
								    String responseLine = null;
								    while ((responseLine = br.readLine()) != null) {
								        response.append(responseLine.trim());
								    }
								}
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							hasit = false;
						} else if (tipoa.equals("Light")) { 
							panel_3_1.remove(armaduraR);
							armaduraR = new JLabel("");
							armaduraR.setHorizontalAlignment(SwingConstants.CENTER);
							armaduraR.setBounds(0, 11, 324, 72);
							armaduraR.setIcon(new ImageIcon(new URL("https://imgur.com/KfCeWe3.png")));
							panel_3_1.add(armaduraR);
							repaint();
							
							for (int i = 0; i < armors.size(); i++) {
								if (armors.get(i).textValue().equals(armadura.getId())){
									hasit = true;
								}
							}
							try {
								if (!hasit) {
									URL url2 = new URL ("https://final-project-ams.herokuapp.com/unlockArmor?playerId="+ idPlayer +"&armor="+ armadura.getId());
									HttpURLConnection con = (HttpURLConnection)url2.openConnection();
									con.setRequestMethod("POST");
									con.setRequestProperty("Content-Type", "application/json; utf-8");
									con.setRequestProperty("Accept", "application/json");
									con.setDoOutput(true);
									try(BufferedReader br = new BufferedReader(
									  new InputStreamReader(con.getInputStream(), "utf-8"))) {
									    StringBuilder response = new StringBuilder();
									    String responseLine = null;
									    while ((responseLine = br.readLine()) != null) {
									        response.append(responseLine.trim());
									    }
									}
								}
								
								URL url3 = new URL ("https://final-project-ams.herokuapp.com/chargeCoins?playerId="+ idPlayer +"&coins="+ 100);
								HttpURLConnection con3 = (HttpURLConnection)url3.openConnection();
								con3.setRequestMethod("POST");
								con3.setRequestProperty("Content-Type", "application/json; utf-8");
								con3.setRequestProperty("Accept", "application/json");
								con3.setDoOutput(true);
								try(BufferedReader br = new BufferedReader(
								  new InputStreamReader(con3.getInputStream(), "utf-8"))) {
								    StringBuilder response = new StringBuilder();
								    String responseLine = null;
								    while ((responseLine = br.readLine()) != null) {
								        response.append(responseLine.trim());
								    }
								}
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							hasit = false;
						} else if (tipoa.equals("Dark")) {
							panel_3_1.remove(armaduraR);
							armaduraR = new JLabel("");
							armaduraR.setHorizontalAlignment(SwingConstants.CENTER);
							armaduraR.setBounds(0, 11, 324, 72);
							armaduraR.setIcon(new ImageIcon(new URL("https://imgur.com/TJprbLg.png")));
							panel_3_1.add(armaduraR);
							repaint();
							
							for (int i = 0; i < armors.size(); i++) {
								if (armors.get(i).textValue().equals(armadura.getId())){
									hasit = true;
								}
							}
							try {
								if (!hasit) {
									URL url2 = new URL ("https://final-project-ams.herokuapp.com/unlockArmor?playerId="+ idPlayer +"&armor="+ armadura.getId());
									HttpURLConnection con = (HttpURLConnection)url2.openConnection();
									con.setRequestMethod("POST");
									con.setRequestProperty("Content-Type", "application/json; utf-8");
									con.setRequestProperty("Accept", "application/json");
									con.setDoOutput(true);
									try(BufferedReader br = new BufferedReader(
									  new InputStreamReader(con.getInputStream(), "utf-8"))) {
									    StringBuilder response = new StringBuilder();
									    String responseLine = null;
									    while ((responseLine = br.readLine()) != null) {
									        response.append(responseLine.trim());
									    }
									}
								}
								
								URL url3 = new URL ("https://final-project-ams.herokuapp.com/chargeCoins?playerId="+ idPlayer +"&coins="+ 100);
								HttpURLConnection con3 = (HttpURLConnection)url3.openConnection();
								con3.setRequestMethod("POST");
								con3.setRequestProperty("Content-Type", "application/json; utf-8");
								con3.setRequestProperty("Accept", "application/json");
								con3.setDoOutput(true);
								try(BufferedReader br = new BufferedReader(
								  new InputStreamReader(con3.getInputStream(), "utf-8"))) {
								    StringBuilder response = new StringBuilder();
								    String responseLine = null;
								    while ((responseLine = br.readLine()) != null) {
								        response.append(responseLine.trim());
								    }
								}
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							hasit = false;
						} else if (tipoa.equals("Fire")) {
							panel_3_1.remove(armaduraR);
							armaduraR = new JLabel("");
							armaduraR.setHorizontalAlignment(SwingConstants.CENTER);
							armaduraR.setBounds(0, 11, 324, 72);
							armaduraR.setIcon(new ImageIcon(new URL("https://imgur.com/8W2jZBV.png")));
							panel_3_1.add(armaduraR);
							repaint();
							
							for (int i = 0; i < armors.size(); i++) {
								if (armors.get(i).textValue().equals(armadura.getId())){
									hasit = true;
								}
							}
							try {
								if (!hasit) {
									URL url2 = new URL ("https://final-project-ams.herokuapp.com/unlockArmor?playerId="+ idPlayer +"&armor="+ armadura.getId());
									HttpURLConnection con = (HttpURLConnection)url2.openConnection();
									con.setRequestMethod("POST");
									con.setRequestProperty("Content-Type", "application/json; utf-8");
									con.setRequestProperty("Accept", "application/json");
									con.setDoOutput(true);
									try(BufferedReader br = new BufferedReader(
									  new InputStreamReader(con.getInputStream(), "utf-8"))) {
									    StringBuilder response = new StringBuilder();
									    String responseLine = null;
									    while ((responseLine = br.readLine()) != null) {
									        response.append(responseLine.trim());
									    }
									}
								}
								
								URL url3 = new URL ("https://final-project-ams.herokuapp.com/chargeCoins?playerId="+ idPlayer +"&coins="+ 100);
								HttpURLConnection con3 = (HttpURLConnection)url3.openConnection();
								con3.setRequestMethod("POST");
								con3.setRequestProperty("Content-Type", "application/json; utf-8");
								con3.setRequestProperty("Accept", "application/json");
								con3.setDoOutput(true);
								try(BufferedReader br = new BufferedReader(
								  new InputStreamReader(con3.getInputStream(), "utf-8"))) {
								    StringBuilder response = new StringBuilder();
								    String responseLine = null;
								    while ((responseLine = br.readLine()) != null) {
								        response.append(responseLine.trim());
								    }
								}
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							hasit = false;
						} else if (tipoa.equals("Grass")) {
							panel_3_1.remove(armaduraR);
							armaduraR = new JLabel("");
							armaduraR.setHorizontalAlignment(SwingConstants.CENTER);
							armaduraR.setBounds(0, 11, 324, 72);
							armaduraR.setIcon(new ImageIcon(new URL("https://imgur.com/kzitWk7.png")));
							panel_3_1.add(armaduraR);
							repaint();
							
							for (int i = 0; i < armors.size(); i++) {
								if (armors.get(i).textValue().equals(armadura.getId())){
									hasit = true;
								}
							}
							try {
								if (!hasit) {
									URL url2 = new URL ("https://final-project-ams.herokuapp.com/unlockArmor?playerId="+ idPlayer +"&armor="+ armadura.getId());
									HttpURLConnection con = (HttpURLConnection)url2.openConnection();
									con.setRequestMethod("POST");
									con.setRequestProperty("Content-Type", "application/json; utf-8");
									con.setRequestProperty("Accept", "application/json");
									con.setDoOutput(true);
									try(BufferedReader br = new BufferedReader(
									  new InputStreamReader(con.getInputStream(), "utf-8"))) {
									    StringBuilder response = new StringBuilder();
									    String responseLine = null;
									    while ((responseLine = br.readLine()) != null) {
									        response.append(responseLine.trim());
									    }
									}
								}
								
								URL url3 = new URL ("https://final-project-ams.herokuapp.com/chargeCoins?playerId="+ idPlayer +"&coins="+ 100);
								HttpURLConnection con3 = (HttpURLConnection)url3.openConnection();
								con3.setRequestMethod("POST");
								con3.setRequestProperty("Content-Type", "application/json; utf-8");
								con3.setRequestProperty("Accept", "application/json");
								con3.setDoOutput(true);
								try(BufferedReader br = new BufferedReader(
								  new InputStreamReader(con3.getInputStream(), "utf-8"))) {
								    StringBuilder response = new StringBuilder();
								    String responseLine = null;
								    while ((responseLine = br.readLine()) != null) {
								        response.append(responseLine.trim());
								    }
								}
								
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							
						}
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					}
					if (!hasit) {
						coins.setText(monedas - 100 + " coins");
						repaint();
					}
					hasit = false;
				}
			}
		});
		btnArmadura.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel_2.add(btnArmadura, BorderLayout.SOUTH);
		
		JLabel lblNewLabel_4 = new JLabel("Armors");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_4.setForeground(new Color(160, 82, 45));
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblNewLabel_4, BorderLayout.NORTH);
		
		JLabel armadurasChest = new JLabel("");
		armadurasChest.setHorizontalAlignment(SwingConstants.CENTER);
		armadurasChest.setBounds(0, 94, 324, 85);
		try {
			armadurasChest.setIcon(new ImageIcon(new URL("https://imgur.com/GIymCeT.png")));
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		panel_3_1.add(armadurasChest);
	}
}
