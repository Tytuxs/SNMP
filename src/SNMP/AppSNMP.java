package SNMP;

import org.snmp4j.PDU;
import org.snmp4j.event.ResponseEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;

public class AppSNMP extends JDialog{
    private JPanel panelSNMP;
    private JComboBox comboBoxObjet;
    private JTextField textFieldCommunaute;
    private JComboBox comboBoxIP;
    private JLabel labelResultat;
    private JButton buttonModifier;
    private JTextField textFieldNouvelleValeur;
    private JButton buttonGet;

    DefaultComboBoxModel comboBoxModel_Objet = new DefaultComboBoxModel();
    DefaultComboBoxModel comboBoxModel_IP = new DefaultComboBoxModel();

    public AppSNMP() {
        InitializeComboBox();
        textFieldCommunaute.setText("2326GACO");

        buttonGet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String oid = "";
                    switch (comboBoxObjet.getSelectedItem().toString()) {
                        case "Contact":
                            oid = "1.3.6.1.2.1.1.4.0";
                            break;
                        case "Description":
                            oid = "1.3.6.1.2.1.1.1.0";
                            break;
                        case "Nom":
                            oid = "1.3.6.1.2.1.1.5.0";
                            break;
                    }
                    ResponseEvent paquetReponse = SNMP_utility.SNMP_GET_synchrone(comboBoxIP.getSelectedItem().toString()+"/161",oid,textFieldCommunaute.getText());
                    if (paquetReponse !=null)
                    {
                        PDU pduReponse = paquetReponse.getResponse();
                        if(pduReponse !=null) {
                            System.out.println("Status réponse = " + pduReponse.getErrorStatus());
                            System.out.println("Status réponse = " + pduReponse.getErrorStatusText());
                            Vector vecReponse = pduReponse.getVariableBindings();
                            String affichage = "";
                            for (int i = 0; i < vecReponse.size(); i++) {
                                affichage = affichage + "Elément n°" + i + " : " + vecReponse.elementAt(i) + "\n";
                            }
                            labelResultat.setText(affichage);
                        }
                        else
                            labelResultat.setText("Erreur...");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonModifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String oid = "";
                String nouvelleValeur="";
                String nomObjet = comboBoxObjet.getSelectedItem().toString();
                switch (nomObjet) {
                    case "Contact":
                        oid = "1.3.6.1.2.1.1.4.0";
                        nouvelleValeur = textFieldNouvelleValeur.getText();
                        break;
                    case "Description":
                        labelResultat.setText("Ne peut pas être modifié");
                        break;
                    case "Nom":
                        oid = "1.3.6.1.2.1.1.5.0";
                        nouvelleValeur = textFieldNouvelleValeur.getText();
                        break;
                }

                if(nomObjet.equals("Contact") || nomObjet.equals("Nom")) {
                    try {
                        ResponseEvent paquetReponse = SNMP_utility.SNMP_SET_Synchrone(comboBoxIP.getSelectedItem().toString()+"/161",textFieldCommunaute.getText(),oid,nouvelleValeur);
                        if (paquetReponse !=null)
                        {
                            PDU pduReponse = paquetReponse.getResponse();
                            if(pduReponse !=null) {
                                System.out.println("Status réponse = " + pduReponse.getErrorStatus());
                                System.out.println("Status réponse = " + pduReponse.getErrorStatusText());
                                Vector vecReponse = pduReponse.getVariableBindings();
                                String affichage = "";
                                for (int i = 0; i < vecReponse.size(); i++) {
                                    affichage = affichage + "Elément n°" + i + " : " + vecReponse.elementAt(i) + "\n";
                                }
                                labelResultat.setText(affichage);
                            }
                            else
                                labelResultat.setText("Erreur...");
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        this.setMinimumSize(new Dimension(1400,800));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(panelSNMP);
        this.pack();
    }

    public void InitializeComboBox()
    {
        comboBoxModel_IP.addElement("127.0.0.1");
        comboBoxModel_IP.addElement("192.168.1.17");
        comboBoxModel_Objet.addElement("Contact");
        comboBoxModel_Objet.addElement("Description");
        comboBoxModel_Objet.addElement("Nom");
        this.comboBoxIP.setModel(comboBoxModel_IP);
        this.comboBoxObjet.setModel(comboBoxModel_Objet);
    }
}
