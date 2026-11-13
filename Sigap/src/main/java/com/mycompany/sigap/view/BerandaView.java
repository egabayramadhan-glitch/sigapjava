package com.mycompany.sigap.view;

import com.mycompany.sigap.controller.AgendaController;
import com.mycompany.sigap.controller.LaporanController;
import com.mycompany.sigap.model.Agenda;
import com.mycompany.sigap.model.Laporan;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BerandaView extends JFrame {
    private final LaporanController laporanCtrl = new LaporanController();
    private final AgendaController agendaCtrl = new AgendaController();
    private final DefaultTableModel mTrack = new DefaultTableModel(
            new String[]{"ID Laporan", "Nama Pelapor", "Judul Pengaduan", "Status"}, 0);
    private final DefaultTableModel mAgenda = new DefaultTableModel(
            new String[]{"Nama Kegiatan", "Tanggal", "Lokasi"}, 0);
    private final JTextField txtID = new JTextField(), txtNama = new JTextField(20), txtJudul = new JTextField(20);
    private final JTextArea txtDesc = new JTextArea(4, 20);

    public BerandaView() {
        setTitle("SIGAP - Sistem Informasi Tanggap Warga");
        setSize(850, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 128, 185));
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel title = new JLabel("SIGAP - Beranda Warga");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        JButton btnAdmin = new JButton("Login Admin");
        btnAdmin.setBackground(new Color(231, 76, 60));
        btnAdmin.setForeground(Color.WHITE);
        btnAdmin.addActionListener(e -> loginAdmin());
        header.add(title, BorderLayout.WEST);
        header.add(btnAdmin, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Tab
        JTabbedPane tab = new JTabbedPane();
        tab.addTab("E-Lapor (Buat Pengaduan)", panelLapor());
        tab.addTab("Tracking Status Laporan", panelTabel("Status Laporan Warga:", mTrack));
        tab.addTab("Agenda Gotong Royong", panelTabel("Agenda Kegiatan Warga:", mAgenda));
        tab.addChangeListener(e -> muatData());
        add(tab, BorderLayout.CENTER);
        muatData();
    }

    private String idBaru() { return "LPR-" + (System.currentTimeMillis() % 1000000); }

    private JPanel panelLapor() {
        txtID.setText(idBaru());
        txtID.setEditable(false);
        txtDesc.setLineWrap(true);
        JButton btn = new JButton("Kirim Pengaduan Sekarang");
        btn.setBackground(new Color(46, 204, 113));
        btn.setForeground(Color.WHITE);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(new EmptyBorder(20, 30, 20, 30));
        Object[][] baris = {
            {"ID Laporan (Otomatis):", txtID}, {"Nama Lengkap Pelapor:", txtNama},
            {"Judul Pengaduan:", txtJudul}, {"Deskripsi / Keterangan:", new JScrollPane(txtDesc)}, {"", btn}};
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;
        for (int i = 0; i < baris.length; i++) {
            g.gridy = i;
            g.gridx = 0; g.weightx = 0; p.add(new JLabel((String) baris[i][0]), g);
            g.gridx = 1; g.weightx = 1; p.add((Component) baris[i][1], g);
        }

        btn.addActionListener(e -> {
            if (txtNama.getText().isBlank() || txtJudul.getText().isBlank() || txtDesc.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Semua kolom wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                laporanCtrl.simpan(new Laporan(txtID.getText(), txtNama.getText().trim(),
                        txtJudul.getText().trim(), txtDesc.getText().trim(), "Menunggu"));
                JOptionPane.showMessageDialog(this, "Laporan terkirim!\nSimpan ID Laporan Anda: " + txtID.getText());
                txtID.setText(idBaru());
                txtNama.setText(""); txtJudul.setText(""); txtDesc.setText("");
                muatData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        return p;
    }

    private JPanel panelTabel(String judul, DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(25);
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        p.add(new JLabel(judul), BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private void muatData() {
        try {
            mTrack.setRowCount(0);
            for (Laporan l : laporanCtrl.getAll())
                mTrack.addRow(new Object[]{l.getId(), l.getNamaPelapor(), l.getJudul(), l.getStatus()});
            mAgenda.setRowCount(0);
            for (Agenda a : agendaCtrl.getAll())
                mAgenda.addRow(new Object[]{a.getNamaKegiatan(), a.getTanggal(), a.getLokasi()});
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loginAdmin() {
        JTextField user = new JTextField("admin");
        JPasswordField pass = new JPasswordField();
        int r = JOptionPane.showConfirmDialog(this, new Object[]{"Username Admin:", user, "Password Admin:", pass},
                "Autentikasi Admin SIGAP", JOptionPane.OK_CANCEL_OPTION);
        if (r != JOptionPane.OK_OPTION) return;
        if (user.getText().trim().equals("admin") && new String(pass.getPassword()).equals("admin123")) {
            dispose();
            new AdminView().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Username atau password salah! (admin / admin123)", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}