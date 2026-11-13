package com.mycompany.sigap;

import com.mycompany.sigap.view.BerandaView;
import javax.swing.SwingUtilities;

public class Sigap {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BerandaView().setVisible(true));
    }
}