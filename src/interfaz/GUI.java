package interfaz;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;

public class GUI {

	private JFrame frame;
	private JTextField txtEncryptContra;
	private JTextField textDecryptContra;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 300, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnEncrypt = new JButton("ENCRYPT");
		btnEncrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("boton encryot");
				if (txtEncryptContra == null || !txtEncryptContra.equals("") )  {
					System.out.println("Vacio");
					JOptionPane.showMessageDialog(frame, "FORMATO NO VALIDO", "ERROR",JOptionPane.ERROR_MESSAGE);
				}else {
					String contra = txtEncryptContra.getText();
					System.out.println("contra: "+ txtEncryptContra.getText() );
				}
			}
		});
		btnEncrypt.setBackground(SystemColor.activeCaption);
		btnEncrypt.setFont(new Font("Sitka Small", Font.BOLD, 11));
		btnEncrypt.setBounds(159, 120, 89, 23);
		frame.getContentPane().add(btnEncrypt);
		
		JButton btnDecrypt = new JButton("DECRYPT");
		btnDecrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("boton encryot");
				if (txtEncryptContra == null || !txtEncryptContra.equals("") )  {
					System.out.println("Vacio");
					JOptionPane.showMessageDialog(frame, "FORMATO NO VALIDO", "ERROR",JOptionPane.ERROR_MESSAGE);
				}else {
					String contra = txtEncryptContra.getText();
					System.out.println("contra: "+ txtEncryptContra.getText() );
				}
			}
		});
		btnDecrypt.setBackground(SystemColor.activeCaption);
		btnDecrypt.setFont(new Font("Sitka Small", Font.BOLD, 11));
		btnDecrypt.setForeground(Color.BLACK);
		btnDecrypt.setBounds(82, 327, 89, 23);
		frame.getContentPane().add(btnDecrypt);
		
		JLabel lblEncrypt = new JLabel("Encrypt");
		lblEncrypt.setFont(new Font("Sitka Small", Font.BOLD, 15));
		lblEncrypt.setBounds(25, 11, 72, 23);
		frame.getContentPane().add(lblEncrypt);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 156, 264, 8);
		frame.getContentPane().add(separator);
		
		JLabel lblEncryptContra = new JLabel("Contrase\u00F1a");
		lblEncryptContra.setBackground(Color.WHITE);
		lblEncryptContra.setFont(new Font("Sitka Subheading", Font.PLAIN, 12));
		lblEncryptContra.setBounds(25, 60, 100, 23);
		frame.getContentPane().add(lblEncryptContra);
		
		txtEncryptContra = new JTextField();
		txtEncryptContra.addActionListener(null);
		txtEncryptContra.setToolTipText("");
		txtEncryptContra.setBounds(147, 59, 112, 20);
		frame.getContentPane().add(txtEncryptContra);
		txtEncryptContra.setColumns(10);
		
		JButton btnFileEncrypt = new JButton("Subir archivo");
		btnFileEncrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int seleccion = fc.showOpenDialog(frame);
				
				if(seleccion == JFileChooser.APPROVE_OPTION) {
					File fichero = fc.getSelectedFile();
					
				}
			}
		});
		btnFileEncrypt.setBounds(25, 118, 100, 23);
		frame.getContentPane().add(btnFileEncrypt);
		
		JLabel lblDecrypt = new JLabel("DECRYPT");
		lblDecrypt.setFont(new Font("Sitka Small", Font.BOLD, 15));
		lblDecrypt.setBounds(25, 175, 100, 23);
		frame.getContentPane().add(lblDecrypt);
		
		JLabel lblDecryptContra = new JLabel("Contrase\u00F1a");
		lblDecryptContra.setFont(new Font("Sitka Subheading", Font.PLAIN, 12));
		lblDecryptContra.setBackground(Color.WHITE);
		lblDecryptContra.setBounds(25, 212, 100, 23);
		frame.getContentPane().add(lblDecryptContra);
		
		textDecryptContra = new JTextField();
		textDecryptContra.setToolTipText("");
		textDecryptContra.setColumns(10);
		textDecryptContra.setBounds(147, 211, 112, 20);
		frame.getContentPane().add(textDecryptContra);
		
		JButton btnNewButton = new JButton("Subir archivo");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int seleccion = fc.showOpenDialog(frame);
				
				if(seleccion == JFileChooser.APPROVE_OPTION) {
					File fichero = fc.getSelectedFile();
					
				}
			}
		});
		btnNewButton.setBounds(25, 258, 100, 23);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Sha-1");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_1.setBounds(159, 258, 89, 23);
		frame.getContentPane().add(btnNewButton_1);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == txtEncryptContra) {
			System.out.println("contra: "+ txtEncryptContra.getText() );
		}
	}

	
	
	
}
