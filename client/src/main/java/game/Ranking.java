package game;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.fasterxml.jackson.databind.ObjectMapper;

import JsonData.rankingData;
import JsonData.weaponsData;

import java.awt.ScrollPane;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class Ranking extends JFrame {

	private JPanel contentPane;
	private JTable table;
	public Ranking(String idplayer) {
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 312, 517);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(222, 184, 135));
		contentPane.setForeground(new Color(160, 82, 45));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(222, 184, 135));
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JScrollPane rank = new JScrollPane();
		panel.add(rank);
		
		table = new JTable();
		DefaultTableModel model = new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Name", "Points"
			}
		);
		try {
			URL url = new URL("https://final-project-ams.herokuapp.com/usersAndPoints");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("accept", "application/json");
			InputStream responseStream = connection.getInputStream();
			ObjectMapper mapper = new ObjectMapper();
			
			rankingData rdata = mapper.readValue(responseStream, rankingData.class);
			
			for (int i = 0;i < rdata.users.size(); i++) {
				model.addRow(
						new Object[] {
							rdata.users.get(i).get("username").textValue(),
							rdata.users.get(i).get("points").intValue()
						});
			}
			
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		table.setModel(model);
		rank.setViewportView(table);
		JLabel lblNewLabel = new JLabel("GLOBAL RANKING EARTH REALMS");
		lblNewLabel.setForeground(new Color(160, 82, 45));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel, BorderLayout.NORTH);
		
		JButton btnNewButton = new JButton("Return");
		btnNewButton.setBackground(new Color(160, 82, 45));
		btnNewButton.setForeground(new Color(222, 184, 135));
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MenuSeleccion ms = new MenuSeleccion(idplayer);
				ms.setLocationRelativeTo(null);
				
				dispose();
			}
		});
		contentPane.add(btnNewButton, BorderLayout.SOUTH);
		
	}

}
