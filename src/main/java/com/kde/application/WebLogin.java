package com.kde.application;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.kde.gui.QueryWindow;
import com.kde.ontologies.OntCreator;

/**
 * Create ontologies
 *
 * @author  Baolei, Rajesh, Sourojit
 */
public class WebLogin extends JFrame implements ActionListener {
	JLabel l1, l2, l3;
	JTextField tf1;
	JButton btn1;
	JPasswordField p1;

	WebLogin() {
		final JFrame frame = new JFrame("Group C SPARQL Login form");
		l1 = new JLabel("Group C SPARQL Login form");
		l1.setForeground(Color.WHITE);
		l1.setFont(new Font("Serif", Font.BOLD, 25));

		l2 = new JLabel("Username");
		l2.setForeground(Color.WHITE);
		l2.setFont(new Font("Serif", Font.BOLD, 20));

		l3 = new JLabel("Password");
		l3.setForeground(Color.WHITE);
		l3.setFont(new Font("Serif", Font.BOLD, 20));

		tf1 = new JTextField();
		p1 = new JPasswordField();
		btn1 = new JButton("Login");

		l1.setBounds(100, 30, 400, 30);
		l2.setBounds(80, 70, 200, 30);
		l3.setBounds(80, 110, 200, 30);
		tf1.setBounds(300, 70, 200, 30);
		p1.setBounds(300, 110, 200, 30);
		btn1.setBounds(150, 160, 100, 30);
		try {
			frame.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("background.png")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
		frame.pack();
		frame.setVisible(true);
		frame.add(l1);
		frame.add(l2);
		frame.add(tf1);
		frame.add(l3);
		frame.add(p1);
		frame.add(btn1);

		frame.setSize(700, 350);
		frame.setLayout(null);
		frame.setVisible(true);
		
		btn1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String uname = tf1.getText();
				String pass = p1.getText();
				if (uname.equals("groupc") && pass.equals("1")) {
					try {
						OntCreator.createOntology();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					QueryWindow window = new QueryWindow();
			        window.launch();
				} else {
					JOptionPane.showMessageDialog(frame, "Incorrect username or password"); 
				}
			}
		});
	}

	


	public static void main(String[] args) {
		new WebLogin();
	}

	public static void WebLogin() {
		// TODO Auto-generated method stub
		new WebLogin();
	}




	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
