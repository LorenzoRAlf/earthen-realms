package game;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import JsonData.armorData;
import JsonData.damageData;
import JsonData.gameData;
import JsonData.gameSetData;
import JsonData.rivalData;
import JsonData.rivalDataArmor;
import JsonData.weaponsData;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import javax.swing.SwingConstants;

import java.awt.Font;
import javax.swing.JProgressBar;

public class Cliente extends JFrame {

	private JPanel contentPane;
	public Weapon armaRival, armaJugador;
	public Armor armaduraJugador , armaduraRival;
	public int hpJugador, hpRival, ronda = 0, damagetype, tiempoR = 30, danojugador = 0;
	private String idArmaJugador, idArmaduraJugador, gameid;
	public boolean fighting = true, waiting = true;
	private JPanel panel_3;
	private JPanel panel_4;
	private JButton wAttack;
	private JButton nAttack;
	private JButton sAttack;
	private JPanel panel_1;
	private JButton passTurn;
	private JLabel lblNewLabel_2;
	private Timer timer;
	public weaponsData wdata;
	public armorData adata;
	public gameSetData gdata;
	private JProgressBar vidaJugador;
	private JProgressBar vidaRival;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JPanel panel_2;
	private JLabel enemigo = new JLabel(""), jugador = new JLabel("");

	@SuppressWarnings("unlikely-arg-type")
	public Cliente(String nombreArma, String nombreArmadura, String idPlayer, JsonNode weapons, JsonNode armors) {
		//API de buscar partida//
		//Conseguimos el arma y armadura que el usuario ha pedido//
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
			
			for (int x = 0; x < adata.alist.size(); x++) {
				if (nombreArmadura.equals(adata.alist.get(x).get("armorName").textValue())) {
					idArmaduraJugador = adata.alist.get(x).get("_id").textValue();
					armaduraJugador = new Armor(adata.alist.get(x).get("armorName").textValue(), adata.alist.get(x).get("armorType").textValue(), adata.alist.get(x).get("baseSpeed").intValue(), adata.alist.get(x).get("baseDefense").intValue(), adata.alist.get(x).get("_id").textValue());
					
					if (armaduraJugador.getType().equals("Electric")) {
						jugador.setBounds(187, 449, 32, 50);
						jugador.setIcon(new ImageIcon(new URL("https://imgur.com/aI26ZzO.png")));
					} else if (armaduraJugador.getType().equals("Water")) {
						jugador.setBounds(187, 449, 32, 50);
						jugador.setIcon(new ImageIcon(new URL("https://imgur.com/j24bsko.png")));
					} else if (armaduraJugador.getType().equals("Rock")) {
						jugador.setBounds(187, 449, 32, 50);
						jugador.setIcon(new ImageIcon(new URL("https://imgur.com/RABnnWe.png")));
					} else if (armaduraJugador.getType().equals("Light")) {
						jugador.setBounds(187, 449, 32, 50);
						jugador.setIcon(new ImageIcon(new URL("https://imgur.com/KfCeWe3.png")));
					} else if (armaduraJugador.getType().equals("Dark")) {
						jugador.setBounds(187, 449, 32, 50);
						jugador.setIcon(new ImageIcon(new URL("https://imgur.com/TJprbLg.png")));
					} else if (armaduraJugador.getType().equals("Fire")) {
						jugador.setBounds(187, 449, 32, 50);
						jugador.setIcon(new ImageIcon(new URL("https://imgur.com/8W2jZBV.png")));
					} else if (armaduraJugador.getType().equals("Grass")) {
						jugador.setBounds(187, 449, 32, 50);
						jugador.setIcon(new ImageIcon(new URL("https://imgur.com/kzitWk7.png")));
					}
					
				}
			}
			
			for (int x = 0; x < wdata.wlist.size(); x++) {
				if (nombreArma.equals(wdata.wlist.get(x).get("weaponName").textValue())) {
					idArmaJugador = wdata.wlist.get(x).get("_id").textValue();
					armaJugador = new Weapon(wdata.wlist.get(x).get("weaponName").textValue(), wdata.wlist.get(x).get("baseDamage").intValue(), wdata.wlist.get(x).get("damageType").textValue(), wdata.wlist.get(x).get("_id").textValue());
					
					//POST AÑADIR JUGADOR//
					URL ur = new URL ("https://final-project-ams.herokuapp.com/setPlayer?gameId="+ gameid +"&playerId="+ idPlayer +"&weapon="+ idArmaJugador+"&armor="+idArmaduraJugador);
					HttpURLConnection con = (HttpURLConnection)ur.openConnection();
					con.setRequestMethod("POST");
					con.setRequestProperty("Content-Type", "application/json; utf-8");
					con.setRequestProperty("Accept", "application/json");
					
					InputStream responseStream3 = con.getInputStream();
					ObjectMapper mapper3 = new ObjectMapper();
					
					gdata = mapper3.readValue(responseStream3, gameSetData.class);
					
					gameid = gdata.glist.get("_id").textValue();
				}
			}
			
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//Esperamos que se conecte el usuario rival, en caso de que no lo esté//
		while (true) {
			try {
				URL url = new URL("https://final-project-ams.herokuapp.com/getGame?gameId="+ gameid);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestProperty("accept", "application/json");
				InputStream responseStream = connection.getInputStream();
				ObjectMapper mapper = new ObjectMapper();
				
				gameData gdata = mapper.readValue(responseStream, gameData.class);
				
				if (gdata.game.get(0).get("status").textValue().equals("fighting")) {
					URL url2 = new URL("https://final-project-ams.herokuapp.com/rivalWeapon?gameId="+ gameid +"&playerId=" + idPlayer);
					HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
					connection2.setRequestProperty("accept", "application/json");
					InputStream responseStream2 = connection2.getInputStream();
					ObjectMapper mapper2 = new ObjectMapper();
					
					rivalData rwdata = mapper2.readValue(responseStream2, rivalData.class);
					
					for (int x = 0; x < wdata.wlist.size(); x++) {
						if (rwdata.weaponR.equals(wdata.wlist.get(x).get("_id").textValue())) {
							armaRival = new Weapon(wdata.wlist.get(x).get("weaponName").textValue(), wdata.wlist.get(x).get("baseDamage").intValue(), wdata.wlist.get(x).get("damageType").textValue(), wdata.wlist.get(x).get("_id").textValue());
						}
					}
					
					
					
					URL url3 = new URL("https://final-project-ams.herokuapp.com/rivalArmor?gameId="+ gameid +"&playerId=" + idPlayer);
					HttpURLConnection connection3 = (HttpURLConnection) url3.openConnection();
					connection3.setRequestProperty("accept", "application/json");
					InputStream responseStream3 = connection3.getInputStream();
					ObjectMapper mapper3 = new ObjectMapper();
					
					rivalDataArmor radata = mapper3.readValue(responseStream3, rivalDataArmor.class);
					
					for (int x = 0; x < adata.alist.size(); x++) {
						if (radata.armorR.equals(adata.alist.get(x).get("_id").textValue())) {
							armaduraRival = new Armor(adata.alist.get(x).get("armorName").textValue(), adata.alist.get(x).get("armorType").textValue(), adata.alist.get(x).get("baseSpeed").intValue(), adata.alist.get(x).get("baseDefense").intValue(), adata.alist.get(x).get("_id").textValue());
							
							//Ponemos el sprite en pantalla//
							
							if (armaduraRival.getType().equals("Electric")) {
								enemigo.setBounds(696, 448, 32, 50);
								enemigo.setIcon(new ImageIcon(new URL("https://imgur.com/sqxuwAk.png")));
							} else if (armaduraRival.getType().equals("Water")) {
								enemigo.setBounds(696, 448, 32, 50);
								enemigo.setIcon(new ImageIcon(new URL("https://imgur.com/wiDzJIX.png")));
							} else if (armaduraRival.getType().equals("Rock")) {
								enemigo.setBounds(696, 448, 32, 50);
								enemigo.setIcon(new ImageIcon(new URL("https://imgur.com/4R9gGAx.png")));
							} else if (armaduraRival.getType().equals("Light")) {
								enemigo.setBounds(696, 448, 32, 50);
								enemigo.setIcon(new ImageIcon(new URL("https://imgur.com/l2cqYH7.png")));
							} else if (armaduraRival.getType().equals("Dark")) {
								enemigo.setBounds(696, 448, 32, 50);
								enemigo.setIcon(new ImageIcon(new URL("https://imgur.com/qVBPoAF.png")));
							} else if (armaduraRival.getType().equals("Fire")) {
								enemigo.setBounds(696, 448, 32, 50);
								enemigo.setIcon(new ImageIcon(new URL("https://imgur.com/KdtxZzR.png")));
							} else if (armaduraRival.getType().equals("Grass")) {
								enemigo.setBounds(696, 448, 32, 50);
								enemigo.setIcon(new ImageIcon(new URL("https://imgur.com/lZ6gsjk.png")));
							}
						}
					}
					break;
					
					
				}
				
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		//Api de conseguir vida de usuario Rival//
		hpRival = 100;
		
		
		
		//En un futuro, cargariamos estadisticas de armadura seleccionada//
		hpJugador = 100;
		
		//Timer//
		lblNewLabel_2 = new JLabel("30");
		lblNewLabel_2.setBackground(new Color(222, 184, 135));
		lblNewLabel_2.setForeground(new Color(160, 82, 45));
		TimerTask timerTask = new TimerTask() {
	        public void run() {
	        	if (waiting) {
	       		 	lblNewLabel_2.setText("" + tiempoR);
		            tiempoR--;
		            if (tiempoR == -1) {
		           	 	cancel();
		           	 	
		           	 	try {
				           	 URL url2 = new URL ("https://final-project-ams.herokuapp.com/setDamage?playerId="+ idPlayer +"&playerDamage="+ -2 +"&gameId="+ gameid);
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
			           	} catch (MalformedURLException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}

		           	 	JOptionPane.showMessageDialog(null, "Times up! You've lost");
						dispose();
						
						MenuSeleccion ms = new MenuSeleccion(idPlayer);
						ms.setLocationRelativeTo(null);
		            }
		       	 } else {
		       		 //el timer espera a que el juego siga//
		       	 }
	        }
	    };
	    timer = new Timer();
	    timer.scheduleAtFixedRate(timerTask, 0, 1000);
	    
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 635);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(222, 184, 135));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		panel_3 = new JPanel();
		panel_3.setBackground(new Color(222, 184, 135));
		contentPane.add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new GridLayout(0, 2, 0, 0));
		
		lblNewLabel_1 = new JLabel("Player HP");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1.setBackground(new Color(222, 184, 135));
		lblNewLabel_1.setForeground(new Color(160, 82, 45));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		panel_3.add(lblNewLabel_1);
		
		lblNewLabel = new JLabel("Enemy HP");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setBackground(new Color(222, 184, 135));
		lblNewLabel.setForeground(new Color(160, 82, 45));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel_3.add(lblNewLabel);
		
		vidaJugador = new JProgressBar();
		vidaJugador.setBackground(new Color(222, 184, 135));
		panel_3.add(vidaJugador);
		vidaJugador.setValue(hpJugador);
		
		vidaRival = new JProgressBar();
		vidaRival.setBackground(new Color(222, 184, 135));
		vidaRival.setValue(100);
		panel_3.add(vidaRival);
		vidaJugador.setValue(hpRival);
		
		panel_4 = new JPanel();
		panel_4.setBackground(new Color(222, 184, 135));
		contentPane.add(panel_4, BorderLayout.SOUTH);
		panel_4.setLayout(new GridLayout(0, 3, 0, 0));
		
		panel_1 = new JPanel();
		panel_1.setBackground(new Color(222, 184, 135));
		contentPane.add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		passTurn = new JButton("Confirm Turn");
		passTurn.setFont(new Font("Tahoma", Font.PLAIN, 11));
		passTurn.setBackground(new Color(160, 82, 45));
		passTurn.setForeground(new Color(222, 184, 135));
		passTurn.setEnabled(false);
		
		panel_1.add(passTurn);
		
		
		passTurn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				waiting = false;
				wAttack.setEnabled(true);
				nAttack.setEnabled(true);
				sAttack.setEnabled(true);
				passTurn.setEnabled(false);
				tiempoR = 30;
				
				try {
					//set damage que va a recibir, primero hacemos el calculo y luego lo enviamos//
					//calculamos el daño sin la armadura//
					if (damagetype == 1) {
						danojugador = armaJugador.getDamage()/2;
					} else if (damagetype == 2) {
						Random r = new Random();
						int low = 1;
						int high = 100;
						int result = r.nextInt(high-low) + low;
						if (result < 76) {
							danojugador = armaJugador.getDamage();
						} else {
							danojugador = 0;
						}
					} else if (damagetype == 3){
						Random r = new Random();
						int low = 1;
						int high = 100;
						int result = r.nextInt(high-low) + low;
						if (result < 26) {
							danojugador = armaJugador.getDamage()*2;
						} else if (result > 26){
							danojugador = 0;
						}
					}
					
					String tipoa = armaduraRival.getType();
					String tipor = armaJugador.getDamageType();
					
					if (danojugador != 0){
						if (tipoa.equals("Electric")) {
							if (tipor.equals("Rock")) {
								danojugador = danojugador * 2;
								danojugador = (danojugador * 100)/(100+armaduraRival.getDefense());
							} else if (tipor.equals("Water")) {
								danojugador = danojugador / 2;
								danojugador = (danojugador * 100)/(100+armaduraRival.getDefense());
							}
						} else if (tipoa.equals("Water")) {
							if (tipor.equals("Electric")) {
								danojugador = danojugador * 2;
								danojugador = (danojugador * 100)/(100+armaduraRival.getDefense());
							} else if (tipor.equals("Fire")) {
								danojugador = danojugador / 2;
								danojugador = (danojugador * 100)/(100+armaduraRival.getDefense());
							}
						} else if (tipoa.equals("Rock")) {
							if (tipor.equals("Grass")) {
								danojugador = danojugador * 2;
								danojugador = (danojugador * 100)/(100+armaduraRival.getDefense());
							} else if (tipor.equals("Electric")) {
								danojugador = danojugador / 2;
								danojugador = (danojugador * 100)/(100+armaduraRival.getDefense());
							}
						} else if (tipoa.equals("Fire")) {
							if (tipor.equals("Water")) {
								danojugador = danojugador * 2;
								danojugador = (danojugador * 100)/(100+armaduraRival.getDefense());
							} else if (tipor.equals("Grass")) {
								danojugador = danojugador / 2;
								danojugador = (danojugador * 100)/(100+armaduraRival.getDefense());
							} 
						} else if (tipoa.equals("Grass")) {
							if (tipor.equals("Fire")) {
								danojugador = danojugador * 2;
								danojugador = (danojugador * 100)/(100+armaduraRival.getDefense());
							} else if (tipor.equals("Rock")) {
								danojugador = danojugador / 2;
								danojugador = (danojugador * 100)/(100+armaduraRival.getDefense());
							}
						} else if (tipoa.equals("Light")) {
							if (tipor.equals("Dark")) {
								danojugador = danojugador * 3;
								danojugador = (danojugador * 100)/(100+armaduraRival.getDefense());
							} else {
								danojugador = (int) (danojugador * 0.75);
								danojugador = (danojugador * 100)/(100+armaduraRival.getDefense());
							}
						} else if (tipoa.equals("Dark")) {
							if (tipor.equals("Light")) {
								danojugador = danojugador * 3;
								danojugador = (danojugador * 100)/(100+armaduraRival.getDefense());
							} else {
								danojugador = (int) (danojugador * 0.75);
								danojugador = (danojugador * 100)/(100+armaduraRival.getDefense());
							}
						}
					}
					
					
					//Enviamos el daño//
					URL url2 = new URL ("https://final-project-ams.herokuapp.com/setDamage?playerId="+ idPlayer +"&playerDamage="+ danojugador +"&gameId="+ gameid);
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
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				//Pedimos el daño tomado por el enemigo//
				int dano = -1;
				while (dano == -1) {
					try {						
						URL url = new URL("https://final-project-ams.herokuapp.com/damageTaken?playerId=" + idPlayer +"&gameId="+ gameid);
						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						connection.setRequestProperty("accept", "application/json");
						InputStream responseStream = connection.getInputStream();
						ObjectMapper mapper = new ObjectMapper();
						
						damageData gdmgdata = mapper.readValue(responseStream, damageData.class);
						dano = gdmgdata.damageTaken;
						
						if (dano == -2) {
							URL url2 = new URL("https://final-project-ams.herokuapp.com/finishGame?gameId=" + gameid +"&playerId="+ idPlayer);
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
							
							JOptionPane.showMessageDialog(null, "The opponent didnt respond, You've won!");
							dispose();
							timer.cancel();
							
							MenuSeleccion ms = new MenuSeleccion(idPlayer);
							ms.setLocationRelativeTo(null);
						}
						//calculamos el daño que vamos a recibir con la armadura puesta//

						
						if (dano>-1) {
							boolean haciendodano = true;
							while (haciendodano) {
								if (armaduraJugador.getSpeed() > armaduraRival.getSpeed()) {
									if ((hpRival - danojugador)<=0){
										vidaRival.setValue(0);
										
										URL url2 = new URL("https://final-project-ams.herokuapp.com/finishGame?gameId=" + gameid +"&playerId="+ idPlayer);
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
										
										JOptionPane.showMessageDialog(null, "You've won!");
										dispose();
										timer.cancel();
							
										haciendodano = false;
										
										MenuSeleccion ms = new MenuSeleccion(idPlayer);
										ms.setLocationRelativeTo(null);
									} else {
										vidaRival.setValue(hpRival - danojugador);
										hpRival=(hpRival - danojugador);
										
										haciendodano = false;
									}
									
									if ((hpJugador - dano)<= 0) {
										vidaJugador.setValue(0);
										JOptionPane.showMessageDialog(null, "You've lost");
										dispose();
										timer.cancel();
										haciendodano = false;
										
										MenuSeleccion ms = new MenuSeleccion(idPlayer);
										ms.setLocationRelativeTo(null);
									} else {
										vidaJugador.setValue(hpJugador - dano);
										hpJugador = hpJugador - dano; 
										
										haciendodano = false;
									}
								} else {
									if ((hpJugador - dano)<= 0) {
										vidaJugador.setValue(0);
										JOptionPane.showMessageDialog(null, "You've Lost");
										dispose();
										timer.cancel();
										haciendodano = false;
										
										MenuSeleccion ms = new MenuSeleccion(idPlayer);
										ms.setLocationRelativeTo(null);
									} else {
										vidaJugador.setValue(hpJugador - dano);
										hpJugador = hpJugador - dano; 
										
										haciendodano = false;
									}
									
									if ((hpRival - danojugador)<=0){
										vidaRival.setValue(0);
										
										URL url2 = new URL("https://final-project-ams.herokuapp.com/finishGame?gameId=" + gameid +"&playerId="+ idPlayer);
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
										
										JOptionPane.showMessageDialog(null, "You've won!");
										dispose();
										timer.cancel();
										haciendodano = false;
										
										MenuSeleccion ms = new MenuSeleccion(idPlayer);
										ms.setLocationRelativeTo(null);
									} else {
										vidaRival.setValue(hpRival - danojugador);
										hpRival=(hpRival - danojugador);
										
										haciendodano = false;
									}
								}
							}
						}
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				
				
				try {	
					URL url2 = new URL ("https://final-project-ams.herokuapp.com/resetDamage?playerId="+ idPlayer +"&gameId="+ gameid);
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
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				waiting = true;
			}
		});
		
		wAttack = new JButton("Weak Attack");
		wAttack.setBackground(new Color(160, 82, 45));
		wAttack.setForeground(new Color(222, 184, 135));
		sAttack = new JButton("Strong Attack");
		sAttack.setBackground(new Color(160, 82, 45));
		sAttack.setForeground(new Color(222, 184, 135));
		nAttack = new JButton("Normal Attack");
		nAttack.setBackground(new Color(160, 82, 45));
		nAttack.setForeground(new Color(222, 184, 135));
		wAttack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				damagetype = 1;
				wAttack.setEnabled(false);
				nAttack.setEnabled(true);
				sAttack.setEnabled(true);
				passTurn.setEnabled(true);
			}
		});
		nAttack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				damagetype = 2;
				wAttack.setEnabled(true);
				nAttack.setEnabled(false);
				sAttack.setEnabled(true);
				passTurn.setEnabled(true);
			}
		});
		sAttack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				damagetype = 3;
				wAttack.setEnabled(true);
				nAttack.setEnabled(true);
				sAttack.setEnabled(false);
				passTurn.setEnabled(true);
			}
		});
		panel_4.add(wAttack);
		panel_4.add(nAttack);
		panel_4.add(sAttack);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 863, 535);
		panel.add(scrollPane);
		
		panel_2 = new JPanel();
		scrollPane.setViewportView(panel_2);
		panel_2.setLayout(null);
		
		JLabel fondo_batalla = new JLabel("");
		fondo_batalla.setBounds(0, 0, 861, 533);
		try {
			fondo_batalla.setIcon(new ImageIcon(new URL("https://imgur.com/RFsSoud.png")));
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		panel_2.add(fondo_batalla);
		
		fondo_batalla.add(jugador);
		
		fondo_batalla.add(enemigo);
		
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblNewLabel_2, BorderLayout.NORTH);

	}
}