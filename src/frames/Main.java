package frames;

import helper.AdzanAnnouncer;
import helper.CMDCaller;
import helper.MonitorHelper;
import helper.PathHelper;
import helper.SocketHelper;

import java.awt.Desktop;
import java.net.InetAddress;
import java.net.URL;
import javax.swing.ImageIcon;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.text.DefaultCaret;
import static javax.swing.text.DefaultCaret.ALWAYS_UPDATE;

/**
 *
 * @author fgroupindonesia
 * @project FGI Parent Remote Client for desktop platform (pc & laptop)
 * @file Main.java
 * @usage main frame GUI for displaying basic information
 *
 */
public class Main extends javax.swing.JFrame {

    MonitorHelper mhp;
    WarningFrame wmf;
    TrayIcon trayIcon;

    private Image createImage(String path, String description) {
        URL imageURL = getClass().getResource(path);

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

    public static final int CONNECTED = 1, DISCONNECTED = 2;

    public void setIconTray(int mode) {
        if (mode == CONNECTED) {
            trayIcon.setImage(imageConnected);
        } else {
            trayIcon.setImage(imageWaiting);
        }
    }

    Image imageWaiting = null;
    Image imageConnected = null;

    boolean hiddenStat = true;
    MenuItem showHideItem = new MenuItem("Show");
    MenuItem timerItem = new MenuItem("Timer");

    private void createTray() {
        if (SystemTray.isSupported()) {
            // get the SystemTray instance
            SystemTray tray = SystemTray.getSystemTray();
            // load an image

            try {
                // URL url = new URL("http://www.digitalphotoartistry.com/rose1.jpg");
                imageWaiting = createImage("/images/waiting.gif", "");
                imageConnected = createImage("/images/connected.gif", "");

            } catch (Exception ex) {
                System.out.println(ex);
            }

            // create a popup menu
            PopupMenu popup = new PopupMenu();

            showHideItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if (hiddenStat) {
                        setVisible(true);
                        setAlwaysOnTop(true);
                        setExtendedState(JFrame.NORMAL);
                        toFront();
                        repaint();
                        showHideItem.setLabel("Hide");
                    } else {
                        setVisible(false);
                        showHideItem.setLabel("Show");
                    }

                    hiddenStat = !hiddenStat;

                }
            });

            MenuItem menuExitItem = new MenuItem("Exit");
            menuExitItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    System.exit(0);
                }
            });

            timerItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if (timerItem.isEnabled()) {
                        openTimerConfig();
                    }
                }
            });

            popup.add(showHideItem);
            popup.add(timerItem);
            popup.add(menuExitItem);

            trayIcon = new TrayIcon(imageWaiting, "FGI Parent Control Client", popup);
            // set the TrayIcon properties

            trayIcon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    //popup.show(rootPane, arg0., WIDTH);
                }
            });
            // ...
            // add the tray image
            try {
                tray.add(trayIcon);

            } catch (AWTException e) {
                System.err.println(e);
            }
        }
    }

    private void openTimerConfig() {
        TimerConfig frame = new TimerConfig(this, true);
        frame.setVisible(true);
    }

    public void applyTimerWork(String action, String time) {
        // executing the timerframe
        int h = Integer.parseInt(time.split(":")[0]);
        int m = Integer.parseInt(time.split(":")[1]);

        TimerCountDownFrame tcframe = new TimerCountDownFrame(h, m, this);
        tcframe.setVisible(true);

        hideTimerMenu(true);
        setFutureAction(action);
    }

    public void applyFutureAction() {
        if (futureAction != null) {
            if (futureAction.equalsIgnoreCase("shutdown")) {
                CMDCaller.shutdown();
            } else {
                CMDCaller.restart();
            }
        }
    }

    String futureAction;

    public void setFutureAction(String s) {
        futureAction = s.toLowerCase();
    }

    public void hideTimerMenu(boolean n) {
        timerItem.setEnabled(!n);
    }

    AdzanAnnouncer adz;

    private void preparePrayersTime() {
        adz = new AdzanAnnouncer(this);
    }

    public void openWebpage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setIcon() {

        // obtaining from the icon programData
        ImageIcon img = new ImageIcon(PathHelper.getLogoPath());
        setIconImage(img.getImage());
    }

    public void blinking(String message) {
        this.setVisible(false);

        if (mhp == null) {
            mhp = new MonitorHelper();
        }

        wmf = new WarningFrame(this);
        wmf.setText(message);

        wmf.centerScreen();

        if (mhp.hasTwoMonitors()) {
            mhp.showOnScreen(1, wmf);
            mhp.setWider(1, wmf);
            System.out.println("Yes this pc has 2 monitors...");
        }

        wmf.setVisible(true);
    }

    public void removeBlinking() {
        if (wmf != null) {
            wmf.dispose();
        }

        // we keep the window hidden still
        //this.setVisible(true);
    }

    /**
     * Creates new form Main
     */
    SocketHelper shp = new SocketHelper();

    public Main() {
        initComponents();
        //jTextArea1.setText(stb.toString());

        // for Adthan purposes
        preparePrayersTime();

        // for settings purposes
        wdSetting = new Settings(this, true);
        wdSetting.dispose();

        shp.setOutputArea(textAreaDescription);
        shp.setMainFrame(this);
        shp.start();

        getMyIP();
        setIcon();
        createTray();

        this.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {
                showHideItem.setLabel("Show");
                setVisible(false);
                hiddenStat = !hiddenStat;
            }

            public void componentShown(ComponentEvent e) {
                /* code run when component shown */
            }
        });

    }

    private void getMyIP() {
        InetAddress IP;
        try {
            IP = InetAddress.getLocalHost();
            labelIpAddress.setText("Current IP Address : " + IP.getHostAddress());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //System.out.println("IP of my system is := " + IP.getHostAddress());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        labelIpAddress = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaDescription = new javax.swing.JTextArea();
        labelRecent = new javax.swing.JLabel();
        buttonClear = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        labelFB = new javax.swing.JLabel();
        labelIG = new javax.swing.JLabel();
        labelTW = new javax.swing.JLabel();
        labelUT = new javax.swing.JLabel();
        labelWA = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        radioModeLocal = new javax.swing.JRadioButton();
        radioModeGlobal = new javax.swing.JRadioButton();
        labelExit = new javax.swing.JLabel();
        labelPrayersTime = new javax.swing.JLabel();
        labelSettings = new javax.swing.JLabel();

        setTitle("FGI Parent Control Client");
        setResizable(false);
        addWindowStateListener(new java.awt.event.WindowStateListener() {
            public void windowStateChanged(java.awt.event.WindowEvent evt) {
                formWindowStateChanged(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        labelIpAddress.setText("- Current IP Address : -");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("FGI Parent Control Client");

        textAreaDescription.setColumns(20);
        textAreaDescription.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        textAreaDescription.setRows(5);
        jScrollPane1.setViewportView(textAreaDescription);

        labelRecent.setText("- Recent Activities :");

        buttonClear.setText("Clear");
        buttonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonClearActionPerformed(evt);
            }
        });

        labelFB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fb.png"))); // NOI18N
        labelFB.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labelFB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelFBMouseClicked(evt);
            }
        });
        jPanel1.add(labelFB);

        labelIG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ig.png"))); // NOI18N
        labelIG.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labelIG.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelIGMouseClicked(evt);
            }
        });
        jPanel1.add(labelIG);

        labelTW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tw.png"))); // NOI18N
        labelTW.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labelTW.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelTWMouseClicked(evt);
            }
        });
        jPanel1.add(labelTW);

        labelUT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/utube.png"))); // NOI18N
        labelUT.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labelUT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelUTMouseClicked(evt);
            }
        });
        jPanel1.add(labelUT);

        labelWA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/whatsapp.png"))); // NOI18N
        labelWA.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labelWA.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelWAMouseClicked(evt);
            }
        });
        jPanel1.add(labelWA);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Mode"));
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        buttonGroup1.add(radioModeLocal);
        radioModeLocal.setSelected(true);
        radioModeLocal.setText("Local");
        jPanel2.add(radioModeLocal);

        buttonGroup1.add(radioModeGlobal);
        radioModeGlobal.setText("Global");
        jPanel2.add(radioModeGlobal);

        labelExit.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelExit.setForeground(new java.awt.Color(51, 0, 204));
        labelExit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelExit.setText("<html><u>Exit</u></html>");
        labelExit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labelExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelExitMouseClicked(evt);
            }
        });

        labelPrayersTime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/moonstar.png"))); // NOI18N
        labelPrayersTime.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labelPrayersTime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelPrayersTimeMouseClicked(evt);
            }
        });

        labelSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/settings.png"))); // NOI18N
        labelSettings.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labelSettings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelSettingsMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelExit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(buttonClear))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(labelIpAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelSettings)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(labelPrayersTime))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labelRecent, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(163, 163, 163)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelIpAddress)
                    .addComponent(labelPrayersTime)
                    .addComponent(labelSettings))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelRecent)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(buttonClear)
                        .addComponent(labelExit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void buttonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonClearActionPerformed
        textAreaDescription.setText("");
    }//GEN-LAST:event_buttonClearActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (shp != null)
            shp.closing();
    }//GEN-LAST:event_formWindowClosing

    private void labelFBMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelFBMouseClicked
        openWebpage("https://facebook.com/fgroupindonesia");
    }//GEN-LAST:event_labelFBMouseClicked

    private void labelIGMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelIGMouseClicked
        openWebpage("https://www.instagram.com/fgroup.indonesia/");
    }//GEN-LAST:event_labelIGMouseClicked

    private void labelTWMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelTWMouseClicked
        openWebpage("https://twitter.com/fgroupindonesia/");
    }//GEN-LAST:event_labelTWMouseClicked

    private void labelUTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelUTMouseClicked
        openWebpage("https://youtube.com/fgroupindonesia/");
    }//GEN-LAST:event_labelUTMouseClicked

    private void labelWAMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelWAMouseClicked
        openWebpage("https://wa.me/6285795569337?text=hello%20admin%21%0ASaya%20mau%20pakai%20FGroupRemote%20Client%20%26%20Android...%0A%2Atolong%20bantu%20saya%2A");
    }//GEN-LAST:event_labelWAMouseClicked

    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged

        // detect when minized
        if (this.getState() == 1) {//this means minimized
            showHideItem.setLabel("Show");
            setVisible(false);
            hiddenStat = !hiddenStat;
        }

    }//GEN-LAST:event_formWindowStateChanged

    private void labelExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelExitMouseClicked

        System.exit(0);

    }//GEN-LAST:event_labelExitMouseClicked

    private void labelPrayersTimeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelPrayersTimeMouseClicked
        // Opening new Frame
        if (adz != null) {
            PrayersTime dialog = new PrayersTime(this, true);
            dialog.setData(adz.getPrayerData(), adz.getMyIP().getCity());

            dialog.setVisible(true);
        }
    }//GEN-LAST:event_labelPrayersTimeMouseClicked
    Settings wdSetting;
    private void labelSettingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelSettingsMouseClicked
        wdSetting = new Settings(this, true);
        wdSetting.setVisible(true);
    }//GEN-LAST:event_labelSettingsMouseClicked

    boolean localMode = true;

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(false);
            }
        });
    }

    public void writeRecentActivity(String pesan) {
        textAreaDescription.append("\n");
        textAreaDescription.append(pesan);

        DefaultCaret caret = (DefaultCaret) textAreaDescription.getCaret();
        caret.setUpdatePolicy(ALWAYS_UPDATE);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonClear;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelExit;
    private javax.swing.JLabel labelFB;
    private javax.swing.JLabel labelIG;
    private javax.swing.JLabel labelIpAddress;
    private javax.swing.JLabel labelPrayersTime;
    private javax.swing.JLabel labelRecent;
    private javax.swing.JLabel labelSettings;
    private javax.swing.JLabel labelTW;
    private javax.swing.JLabel labelUT;
    private javax.swing.JLabel labelWA;
    private javax.swing.JRadioButton radioModeGlobal;
    private javax.swing.JRadioButton radioModeLocal;
    private javax.swing.JTextArea textAreaDescription;
    // End of variables declaration//GEN-END:variables
}
