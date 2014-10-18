package password;

import java.security.*;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author lightcom
 */
public class Password{
    static JFrame frame;
    static JTextField txtId;
    static JTextField txtPwd;
    static JTextField txtSlt;
    static JTextField txtHash;
    static JTextField txtId2;
    static JTextField txtPwd2;
    
    static byte[] main_hash;
    
    public static void addComponentsToPane() {
        //main panel
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        // add borders
        pane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        // label id
        JLabel lblId = new JLabel("Id", JLabel.TRAILING);
        lblId.setAlignmentX(lblId.LEFT_ALIGNMENT);
        lblId.setHorizontalAlignment(SwingConstants.RIGHT);
        pane.add(lblId);
        // editor id
        txtId = new JTextField(15);
        txtId.setAlignmentX(txtPwd.LEFT_ALIGNMENT);
        pane.add(txtId);
        // label password
        JLabel lblPwd = new JLabel("Password", JLabel.TRAILING);
        lblPwd.setAlignmentX(lblPwd.LEFT_ALIGNMENT);
        lblPwd.setHorizontalAlignment(SwingConstants.RIGHT);
        pane.add(lblPwd);
        // editor password
        txtPwd = new JTextField(15);
        txtPwd.setAlignmentX(txtPwd.LEFT_ALIGNMENT);
        pane.add(txtPwd);
        // label salt
        JLabel lblSlt = new JLabel("Salt", JLabel.TRAILING);
        lblSlt.setAlignmentX(lblSlt.LEFT_ALIGNMENT);
        pane.add(lblSlt);
        // editor salt
        txtSlt = new JTextField(15);
        txtSlt.setAlignmentX(txtSlt.LEFT_ALIGNMENT);
        txtSlt.setEditable(false);
        pane.add(txtSlt);
        // label hash
        JLabel lblHash = new JLabel("Hash", JLabel.TRAILING);
        lblHash.setAlignmentX(lblHash.LEFT_ALIGNMENT);
        pane.add(lblHash);
        // editor hash
        txtHash = new JTextField(30);
        txtHash.setAlignmentX(txtHash.LEFT_ALIGNMENT);
        txtHash.setEditable(false);
        pane.add(txtHash);
        // button generate
        JButton btnGen = new JButton("Register");
        btnGen.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGen.addActionListener(new ActionListener() {
            //action of clicking to button
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtId.getText();
                if(id.isEmpty()) return;
                // take the password
                String pass = txtPwd.getText();
                // take system time im mileseconds
                String systime =  String.valueOf(new Date().getTime());
                txtSlt.setText(systime);
                // password+systemtime (ps)
                String ps = pass+systime;
                String hash = "";
                
                byte[] bytesOfMessage;
                try {
                    // ps to bytes
                    bytesOfMessage = ps.getBytes("UTF-8");
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    // ps to hash
                    
                    main_hash = md.digest(bytesOfMessage);
                    // hash to string
                    //hash = new String(thedigest, "UTF-8");
                    hash = toHex(main_hash);
                    
                    BufferedWriter f0 = new BufferedWriter(new FileWriter("./registered.txt",true));
                    String newLine = System.getProperty("line.separator");
                    f0.append(id+":"+systime + ":"+hash+newLine);
                    //f0.write("Reult "+ i +" : "+ ans + newLine);
                    f0.close();
                    
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(Password.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(Password.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Password.class.getName()).log(Level.SEVERE, null, ex);
                }
                txtHash.setText(hash);
            }
        });
        pane.add(btnGen);
        
        // label id
        JLabel lblId2 = new JLabel("Id", JLabel.TRAILING);
        lblId2.setAlignmentX(lblId2.LEFT_ALIGNMENT);
        lblId2.setHorizontalAlignment(SwingConstants.RIGHT);
        pane.add(lblId2);
        // editor id
        txtId2 = new JTextField(15);
        txtId2.setAlignmentX(txtPwd.LEFT_ALIGNMENT);
        pane.add(txtId2);
        // label password
        JLabel lblPwd2 = new JLabel("Password", JLabel.TRAILING);
        lblPwd2.setAlignmentX(lblPwd2.LEFT_ALIGNMENT);
        pane.add(lblPwd2);
        // editor password
        txtPwd2 = new JTextField(15);
        txtPwd2.setAlignmentX(txtPwd2.LEFT_ALIGNMENT);
        pane.add(txtPwd2);
        
        // button "Generate"
        JButton btnCheck = new JButton("Verify");
        btnCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCheck.addActionListener(new ActionListener() {
            //action of clicking to button
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtId2.getText();
                if(id.isEmpty()) return;
                // take a new password
                String pass = txtPwd2.getText();
                byte[] bytesOfMessage;
                try {
                    BufferedReader br = new BufferedReader(new FileReader("./registered.txt"));
                    String line;
                    boolean succes = false;
                    while((line = br.readLine()) != null) {
                         String[] splitted = line.split(":");
                         String tid = splitted[0];
                         String systime = splitted[1];
                         String hash = splitted[2];
                         if(tid.equals(id)){
                            // password+systime
                            String ps = pass+systime;
                            // ps to bytes
                            bytesOfMessage = ps.getBytes("UTF-8");
                            MessageDigest md = MessageDigest.getInstance("MD5");
                            // ps to hash
                            byte[] hash2 = md.digest(bytesOfMessage);
                            //hash = new String(thedigest, "UTF-8");
                            String new_hash = toHex(hash2);
                            if(new_hash.equals(hash)) JOptionPane.showMessageDialog(frame, "Your password is correct.", "Success",JOptionPane.INFORMATION_MESSAGE);
                            else JOptionPane.showMessageDialog(frame, "Password is incorrect.", "Error",JOptionPane.ERROR_MESSAGE);
                            break;
                         }
                    }
                    
                    // check to hashes
//                    if(Arrays.equals(hash2,main_hash)) JOptionPane.showMessageDialog(frame, "Your password is correct.", "Success",JOptionPane.INFORMATION_MESSAGE);
//                    else JOptionPane.showMessageDialog(frame, "Password is incorrect.", "Error",JOptionPane.ERROR_MESSAGE);
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(Password.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(Password.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Password.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Password.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        
        pane.add(btnCheck);
        
        frame.setContentPane(pane);
    }    
    
    public static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        int bb = bytes.length << 1;
        String b1 = Integer.toBinaryString(bytes.length << 1);
        String b2 = "%0" + (bytes.length << 1) + "X";
        String res = String.format("%0" + (bytes.length << 1) + "X", bi);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }
    
    private static void createAndShowGUI() {
        frame = new JFrame("Password");
        frame.setSize(100,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        addComponentsToPane();
 
        frame.pack();
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        createAndShowGUI();
    }
}
