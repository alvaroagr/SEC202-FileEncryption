package interfaz;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.FileEncrypterDecrypter;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;



public class GUI {

	public final static String SALT = "1234";
	public final static int ITERATIONS = 10000;
	public final static int KEY_LENGTH = 128;
	
	private JFrame frame;
	private JPasswordField txtEncryptContra;
	private JPasswordField textDecryptContra;
	private FileEncrypterDecrypter modelo;
	File inEnc;
	File inDec;
	File inHash;

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
		modelo= new FileEncrypterDecrypter();
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
				if (txtEncryptContra == null || String.valueOf(txtEncryptContra.getPassword()).equals("") )  {
					JOptionPane.showMessageDialog(frame, "FORMATO NO VALIDO", "ERROR",JOptionPane.ERROR_MESSAGE);
				}else {
					char[] contra = txtEncryptContra.getPassword();
					if(inEnc!= null) {						
						try {
							modelo.encrypt(modelo.PBKDF2(contra, SALT.getBytes(), ITERATIONS, KEY_LENGTH), inEnc);
							JOptionPane.showMessageDialog(frame, "Se ha encriptado el archivo: " +inEnc.getCanonicalPath(), "Encriptar",JOptionPane.INFORMATION_MESSAGE);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
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
				if (textDecryptContra == null || String.valueOf(textDecryptContra.getPassword()).equals("") )  {
					JOptionPane.showMessageDialog(frame, "FORMATO NO VALIDO", "ERROR",JOptionPane.ERROR_MESSAGE);
				}else {
					char[] contra = textDecryptContra.getPassword();
					if(inDec!= null) {						
						try {
							if(modelo.decrypt(modelo.PBKDF2(contra, SALT.getBytes(), ITERATIONS, KEY_LENGTH), inDec, new File(inDec.getParent()+"/"+"decrypt.docx"), inHash)== true) {
								JOptionPane.showMessageDialog(frame, "El hash del cifrado concide con el decifrado", "Hash Valido",JOptionPane.INFORMATION_MESSAGE);
							}else {
								JOptionPane.showMessageDialog(frame, "El hash del cifrado no concide con el decifrado", "Hash Invalido",JOptionPane.ERROR_MESSAGE);
							}
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(frame, "La contraseña no concide", "ERROR",JOptionPane.ERROR_MESSAGE);
						}
					}	
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
		
		txtEncryptContra = new JPasswordField();
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
					 inEnc = fc.getSelectedFile();
					 System.out.println(inEnc);
				}
			}
		});
		btnFileEncrypt.setBounds(25, 118, 117, 23);
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
		
		textDecryptContra = new JPasswordField();
		textDecryptContra.setToolTipText("");
		textDecryptContra.setColumns(10);
		textDecryptContra.setBounds(147, 211, 112, 20);
		frame.getContentPane().add(textDecryptContra);
		
		JButton btnFileDecrypt = new JButton("Subir archivo");
		btnFileDecrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int seleccion = fc.showOpenDialog(frame);
				
				
				if(seleccion == JFileChooser.APPROVE_OPTION) {
					 inDec = fc.getSelectedFile();
					 System.out.println(inDec);
				}else {
					JOptionPane.showMessageDialog(frame, "FORMATO NO VALIDO", "ERROR",JOptionPane.ERROR_MESSAGE);					
				}
			}
		});
		btnFileDecrypt.setBounds(25, 258, 124, 23);
		frame.getContentPane().add(btnFileDecrypt);
		
		JButton btnShaButton = new JButton("Subir Hash");
		btnShaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int seleccion = fc.showOpenDialog(frame);
				
				if(seleccion == JFileChooser.APPROVE_OPTION) {
					inHash = fc.getSelectedFile();
				}else {
					JOptionPane.showMessageDialog(frame, "FORMATO NO VALIDO", "ERROR",JOptionPane.ERROR_MESSAGE);					
				}
			}
		});
		btnShaButton.setBounds(159, 258, 89, 23);
		frame.getContentPane().add(btnShaButton);
	}
	


	
	
	
}
