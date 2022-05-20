package game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import JsonData.armorData;
import JsonData.uarmorData;
import JsonData.uweaponsData;
import JsonData.weaponsData;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SelectWeapon extends JFrame {

	private JPanel contentPane;
	private JLabel label;
	private String armaEscogida, armaduraEscogida;
	public Map<String, ImageIcon> imageMap;
	public ArrayList<String> armas, armaduras;
	private JList listaArmas, listaArmaduras;
	public JsonNode armorsuser, weaponsuser, armors, weapons;
	public weaponsData wdata2;
	public armorData adata2;


	/**
	 * Create the frame.
	 */
	@SuppressWarnings("unchecked")
	public SelectWeapon(String idPlayer) {
		
		try {
			URL url = new URL("https://final-project-ams.herokuapp.com/userWeapons?playerId="+idPlayer);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("accept", "application/json");
			InputStream responseStream = connection.getInputStream();
			ObjectMapper mapper = new ObjectMapper();
			uweaponsData wdata = mapper.readValue(responseStream, uweaponsData.class);
			
			weaponsuser = wdata.wlist;
			
			URL url2 = new URL("https://final-project-ams.herokuapp.com/userArmors?playerId="+idPlayer);
			HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
			connection2.setRequestProperty("accept", "application/json");
			InputStream responseStream2 = connection2.getInputStream();
			ObjectMapper mapper2 = new ObjectMapper();
			uarmorData adata = mapper2.readValue(responseStream2, uarmorData.class);
			
			armorsuser = adata.alist;
			
			URL url3 = new URL("https://final-project-ams.herokuapp.com/weapons");
			HttpURLConnection connection3 = (HttpURLConnection) url3.openConnection();
			connection3.setRequestProperty("accept", "application/json");
			InputStream responseStream3 = connection3.getInputStream();
			ObjectMapper mapper3 = new ObjectMapper();
			
			wdata2 = mapper3.readValue(responseStream3, weaponsData.class);
			
			weapons = wdata2.wlist;
			
			URL url4 = new URL("https://final-project-ams.herokuapp.com/armors");
			HttpURLConnection connection4 = (HttpURLConnection) url4.openConnection();
			connection4.setRequestProperty("accept", "application/json");
			InputStream responseStream4 = connection4.getInputStream();
			ObjectMapper mapper4 = new ObjectMapper();
			
			adata2 = mapper4.readValue(responseStream4, armorData.class);
			
			armors = adata2.alist;
			
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		armas = new ArrayList<String>();
		armaduras = new ArrayList<String>();
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1070, 730);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(222, 184, 135));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		try {
			
			//Hacemos la lista de las armas//
			URL url = new URL("https://final-project-ams.herokuapp.com/weapons");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("accept", "application/json");
			InputStream responseStream = connection.getInputStream();
			ObjectMapper mapper = new ObjectMapper();
			
			weaponsData wdata = mapper.readValue(responseStream, weaponsData.class);
			
			
			
			for (int i = 0; i < weaponsuser.size(); i++) {
				for (int x = 0; x < wdata.wlist.size(); x++) {
					if (weaponsuser.get(i).equals(wdata.wlist.get(x).get("_id"))) {
						armas.add(wdata.wlist.get(x).get("weaponName").textValue());
					}
				}
			}
			
			String[] stringArrayArmas = armas.toArray(new String[0]);
			imageMap = createImageMapArmas(stringArrayArmas);
			listaArmas = new JList(stringArrayArmas);
			listaArmas.setBackground(new Color(222, 184, 135));
			listaArmas.setCellRenderer(new MarioListRenderer());
			contentPane.add(listaArmas);
			
			//Termina la lista de armas//
			
			
			//Hacemos lista armaduras//
			URL url2 = new URL("https://final-project-ams.herokuapp.com/armors");
			HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
			connection2.setRequestProperty("accept", "application/json");
			InputStream responseStream2 = connection2.getInputStream();
			ObjectMapper mapper2 = new ObjectMapper();
			
			armorData adata = mapper2.readValue(responseStream2, armorData.class);
			
			for (int i = 0; i < armorsuser.size(); i++) {
				for (int x = 0; x < adata.alist.size(); x++) {
					if (armorsuser.get(i).equals(adata.alist.get(x).get("_id"))) {
						armaduras.add(adata.alist.get(x).get("armorName").textValue());
					}
				}
			}
			
			String[] stringArrayArmaduras = armaduras.toArray(new String[0]);
			imageMap = createImageMapArmas(stringArrayArmaduras);
			listaArmaduras = new JList(stringArrayArmaduras);
			listaArmaduras.setBackground(new Color(222, 184, 135));
			listaArmaduras.setCellRenderer(new MarioListRenderer());
			contentPane.add(listaArmaduras);
			//Terminamos lista armaduras//
			
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(222, 184, 135));
		contentPane.add(panel);
		
		JButton btnNewButton = new JButton("Start");
		btnNewButton.setBackground(new Color(160, 82, 45));
		btnNewButton.setForeground(new Color(222, 184, 135));
		btnNewButton.setBounds(0, 658, 348, 23);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Cliente start = new Cliente(armaEscogida, armaduraEscogida, idPlayer, weaponsuser, armorsuser);
				dispose();
			}
		});
		panel.setLayout(null);
		panel.add(btnNewButton);
		
		JLabel arma = new JLabel("Select a Weapon");
		arma.setFont(new Font("Tahoma", Font.BOLD, 13));
		arma.setBackground(new Color(222, 184, 135));
		arma.setForeground(new Color(160, 82, 45));
		arma.setHorizontalAlignment(SwingConstants.CENTER);
		arma.setBounds(0, 0, 170, 658);
		panel.add(arma);
		
		JLabel armadura = new JLabel("Select an Armor");
		armadura.setFont(new Font("Tahoma", Font.BOLD, 13));
		armadura.setBackground(new Color(222, 184, 135));
		armadura.setForeground(new Color(160, 82, 45));
		armadura.setHorizontalAlignment(SwingConstants.CENTER);
		armadura.setBounds(168, 0, 180, 658);
		panel.add(armadura);
		
		JLabel ataque = new JLabel("Attack = 0");
		ataque.setFont(new Font("Tahoma", Font.BOLD, 13));
		ataque.setForeground(new Color(160, 82, 45));
		ataque.setHorizontalAlignment(SwingConstants.CENTER);
		ataque.setBounds(10, 370, 148, 14);
		panel.add(ataque);
		
		JLabel defensa = new JLabel("Defense = 0");
		defensa.setFont(new Font("Tahoma", Font.BOLD, 13));
		defensa.setForeground(new Color(160, 82, 45));
		defensa.setHorizontalAlignment(SwingConstants.CENTER);
		defensa.setBounds(180, 370, 158, 14);
		panel.add(defensa);
		
		JLabel tipoArma = new JLabel("Type = Normal");
		tipoArma.setFont(new Font("Tahoma", Font.BOLD, 13));
		tipoArma.setForeground(new Color(160, 82, 45));
		tipoArma.setHorizontalAlignment(SwingConstants.CENTER);
		tipoArma.setBounds(10, 395, 160, 14);
		panel.add(tipoArma);
		
		JLabel tipoArmadura = new JLabel("Type = Normal");
		tipoArmadura.setFont(new Font("Tahoma", Font.BOLD, 13));
		tipoArmadura.setForeground(new Color(160, 82, 45));
		tipoArmadura.setHorizontalAlignment(SwingConstants.CENTER);
		tipoArmadura.setBounds(180, 395, 158, 14);
		panel.add(tipoArmadura);
		
		JLabel velocidadArmadura = new JLabel("Speed = 0");
		velocidadArmadura.setFont(new Font("Tahoma", Font.BOLD, 13));
		velocidadArmadura.setForeground(new Color(160, 82, 45));
		velocidadArmadura.setHorizontalAlignment(SwingConstants.CENTER);
		velocidadArmadura.setBounds(180, 420, 158, 14);
		panel.add(velocidadArmadura);
		
		listaArmas.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
                  arma.setText(listaArmas.getSelectedValue().toString());
                  armaEscogida = listaArmas.getSelectedValue().toString();
                  
                  for (int i = 0; i < weapons.size(); i++) {
                	  if (weapons.get(i).get("weaponName").textValue().equals(listaArmas.getSelectedValue().toString())) {
                		  ataque.setText("Attack = " + weapons.get(i).get("baseDamage").intValue());
                		  tipoArma.setText("Type = " + weapons.get(i).get("damageType").textValue());
                	  }
                  }
                }
            }
        });
		
		listaArmaduras.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
                  armadura.setText(listaArmaduras.getSelectedValue().toString());
                  armaduraEscogida = listaArmaduras.getSelectedValue().toString();
                  
                  for (int i = 0; i < armors.size(); i++) {
                	  if (armors.get(i).get("armorName").textValue().equals(listaArmaduras.getSelectedValue().toString())) {
                		  defensa.setText("Defense = " + armors.get(i).get("baseDefense").intValue());
                		  tipoArmadura.setText("Type = " + armors.get(i).get("armorType").textValue());
                		  velocidadArmadura.setText("Speed = " + armors.get(i).get("baseSpeed").intValue());
                	  }
                  }
                }
            }
        });
	}
	
	private Map<String, ImageIcon> createImageMapArmas(String[] stringArray) {
        Map<String, ImageIcon> map = new HashMap<>();
        try {
            map.put("Salamander Sword", new ImageIcon(new URL("https://imgur.com/yjfU7HB.png")));
            map.put("Sylph Sword", new ImageIcon(new URL("https://imgur.com/fCIgtcb.png")));
            map.put("Blood Sword", new ImageIcon(new URL("https://imgur.com/fX60Wiq.png")));
            map.put("Crappy Sword", new ImageIcon(new URL("https://imgur.com/Twytt1Z.png")));
            map.put("Electric Dagger", new ImageIcon(new URL("https://imgur.com/jPLCqJY.png")));
            map.put("Wooden Hammer", new ImageIcon(new URL("https://imgur.com/EAvE0vC.png")));
            map.put("Aquatic Lance", new ImageIcon(new URL("https://imgur.com/5XbQAV0.png")));
            map.put("Desert Scimitar", new ImageIcon(new URL("https://imgur.com/xlyQtci.png")));
            map.put("Angelic Bow", new ImageIcon(new URL("https://imgur.com/7SHm8M3.png")));
            //Armaduras//
            map.put("Thunder Armor", new ImageIcon(new URL("https://imgur.com/aI26ZzO.png")));
            map.put("Mermaid Armor", new ImageIcon(new URL("https://imgur.com/j24bsko.png")));
            map.put("Gaia Gear", new ImageIcon(new URL("https://imgur.com/RABnnWe.png")));
            map.put("Grand Armor", new ImageIcon(new URL("https://imgur.com/KfCeWe3.png")));
            map.put("Demon's Mail", new ImageIcon(new URL("https://imgur.com/TJprbLg.png")));
            map.put("Ifrit Vest", new ImageIcon(new URL("https://imgur.com/8W2jZBV.png")));
            map.put("Elven Armor", new ImageIcon(new URL("https://imgur.com/kzitWk7.png")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }
	
	public class MarioListRenderer extends DefaultListCellRenderer {

        Font font = new Font("helvitica", Font.BOLD, 24);

        @Override
        public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
            label.setIcon(imageMap.get((String) value));
            label.setHorizontalTextPosition(JLabel.RIGHT);
            label.setFont(font);
            return label;
        }
    }
}
