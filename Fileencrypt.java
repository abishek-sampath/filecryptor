/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fileencryt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author SAMPATH
 */
public class Fileencryt {

    /**
     * @param args the command line arguments
     */
    File file,file1;
    JLabel Status,enter_string;
    JButton encryptb,exitb,openb,decryptb;
    JTextField keyField;
    JFrame f;
    JFileChooser choose;
    MessageDigest md;
    SecretKeySpec key;
    CipherOutputStream cos;
    Cipher encrypt,decrypt;
    private int read;
    private byte[] buffer = new byte[1024];
    private byte k[];
    private String path;
    
    public Fileencryt() {
        try {
            f = new JFrame("File Encryptor");
            f.setLayout(new BorderLayout());
            f.setSize(300, 400);
            f.setVisible(true);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setResizable(false);
            md = MessageDigest.getInstance("SHA-1");
            choose = new JFileChooser();
            choose.setMultiSelectionEnabled(false);
            choose.setCurrentDirectory(new File("C:\\Users\\SAMPATH\\Desktop\\abi\\"));
            keyField = new JTextField(30);
            JPanel p = new JPanel();
            p.setPreferredSize(new Dimension(50, 50));
            p.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.DARK_GRAY, Color.LIGHT_GRAY));
            Status = new JLabel("Welcome to File Encryptor");
            Container c = new Container();
            file1 = File.createTempFile("temp", null);
            c.setLayout(new GridLayout(6, 1, 0, 10));
            enter_string = new JLabel("Enter Password:");
            openb = new JButton("Open File");
            openb.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                
                    
                        
                        int option = choose.showOpenDialog(new JFrame());
                        if(option == JFileChooser.APPROVE_OPTION) 
                        {
                            file = choose.getSelectedFile();
                            Status.setText("File Selected : "+file.getName());
                        }
                        else
                        {
                            Status.setText("File Selection Canceled");
                        }
                }
            });
            
            encryptb = new JButton("Encrypt and Save File");
            encryptb.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        FileInputStream fis = null; 
                        FileOutputStream fos = null;
                        try 
                            {
                                path = file.getPath();
                                System.out.println(path);
                                Files.copy(file.toPath(), file1.toPath(), StandardCopyOption.REPLACE_EXISTING,StandardCopyOption.COPY_ATTRIBUTES);
                                fis = new FileInputStream(file1);
                                fos = new FileOutputStream(new File(path));
                            }
                            catch (FileNotFoundException ex) {
                                Logger.getLogger(Fileencryt.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            
                        k = keyField.getText().getBytes("UTF-8");
                        k = md.digest(k);
                        k = Arrays.copyOf(k, 16);
                        System.out.println(k.length);
                        key = new SecretKeySpec(k,"AES");
                        encrypt = Cipher.getInstance("AES/ECB/PKCS5Padding");
                        encrypt.init(Cipher.ENCRYPT_MODE, key);
                        cos = new CipherOutputStream(fos, encrypt);
                        while((read = fis.read(buffer)) != -1)
                        {
                            cos.write(buffer, 0, read);
                        }
                        fis.close();
                        cos.flush();
                        cos.close();
                        JOptionPane.showMessageDialog(openb, "File Encrypted!", "inserting giberish....", JOptionPane.INFORMATION_MESSAGE, null);
                    } catch (        NoSuchAlgorithmException | NoSuchPaddingException | IOException | InvalidKeyException ex) {
                        Logger.getLogger(Fileencryt.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });
            exitb = new JButton("Exit");
            exitb.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int temp = JOptionPane.showConfirmDialog(null, "Do you want to exit?", "File  Encryptor", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(temp == JOptionPane.YES_OPTION)
                    {
                    System.exit(0);
                    }
                }
            });
            decryptb = new JButton("Decrypt and save");
            decryptb.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        FileInputStream fis = null;
                        FileOutputStream fos = null;
                        path = file.getPath();
                        Files.copy(file.toPath(), file1.toPath(), StandardCopyOption.REPLACE_EXISTING,StandardCopyOption.COPY_ATTRIBUTES);
                        fis = new FileInputStream(file1);
                        fos = new FileOutputStream(new File(path));
                        
                        k = keyField.getText().getBytes("UTF-8");
                        k = md.digest(k);
                        k = Arrays.copyOf(k, 16);
                        key = new SecretKeySpec(k, "AES");
                        decrypt = Cipher.getInstance("AES/ECB/PKCS5Padding");
                        decrypt.init(Cipher.DECRYPT_MODE, key);
                        cos = new CipherOutputStream(fos, decrypt);
                        while((read = fis.read(buffer)) != -1)
                        {
                            cos.write(buffer, 0, read);
                        }
                        fis.close();
                        cos.flush();
                        cos.close();
                        JOptionPane.showMessageDialog(null, "File Decrypted!", "removing giberish...", JOptionPane.INFORMATION_MESSAGE, null);
                        }
                        catch (IOException ex) {
                            Logger.getLogger(Fileencryt.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(Fileencryt.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoSuchPaddingException ex) {
                        Logger.getLogger(Fileencryt.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvalidKeyException ex) {
                        Logger.getLogger(Fileencryt.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    
                }
            });
            c.add(openb);
            c.add(enter_string);
            c.add(keyField);
            c.add(encryptb);
            c.add(decryptb);
            c.add(exitb);
            f.add(c, BorderLayout.CENTER);
            p.add(Status);
            f.add(p, BorderLayout.SOUTH);
            f.setLocationRelativeTo(null);
            f.revalidate();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Fileencryt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Fileencryt.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    
    
        
    
    

    public static void main(String[] args) {
        Fileencryt e = new Fileencryt();
        
}
}
