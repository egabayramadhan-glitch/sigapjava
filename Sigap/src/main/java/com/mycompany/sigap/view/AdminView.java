package com.mycompany.sigap.view;

import com.mycompany.sigap.controller.AgendaController;
import com.mycompany.sigap.controller.LaporanController;
import com.mycompany.sigap.controller.WargaController;
import com.mycompany.sigap.model.Agenda;
import com.mycompany.sigap.model.Laporan;
import com.mycompany.sigap.model.Warga;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.function.Consumer;

public class AdminView extends JFrame {
    private final LaporanController laporanCtrl = new LaporanController();
    private final AgendaController agendaCtrl = new AgendaController();
    private final WargaController wargaCtrl = new WargaController();
    private final DefaultTableModel mLap = new DefaultTableModel(
            new String[]{"ID Laporan", "Nama Pelapor", "Judul Pengaduan", "Status"}, 0);
    private final DefaultTableModel mAgenda = new DefaultTableModel(
            new String[]{"Nama Kegiatan", "Tanggal", "Lokasi"}, 0);
    private final DefaultTableModel mWarga = new DefaultTableModel(
            new String[]{"NIK", "Nama Warga", "Alamat"}, 0);

    public AdminView() {
        setTitle("SIGAP - Dashboard Panel Admin");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Header
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(44, 62, 80));
        top.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel lbl = new JLabel("PANEL KONTROL ADMINISTRATOR");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setForeground(Color.WHITE);
        JButton out = new JButton("Logout ke Beranda");
        out.setBackground(new Color(192, 57, 43));
        out.setForeground(Color.WHITE);
        out.addActionListener(e -> { dispose(); new BerandaView().setVisible(true); });
        top.add(lbl, BorderLayout.WEST);
        top.add(out, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        // Tab
        JTabbedPane tab = new JTabbedPane();
        tab.addTab("Kelola Status Laporan Warga", panelLaporan());
        tab.addTab("Kelola Agenda Gotong Royong", panelForm(
                new String[]{"Nama Kegiatan:", "Tanggal:", "Lokasi:"}, "Tambah Agenda Baru", mAgenda,
                v -> agendaCtrl.simpan(new Agenda(v[0], v[1], v[2])), this::muatAgenda));
        tab.addTab("Manajemen Data Warga", panelForm(
                new String[]{"Nomor NIK:", "Nama Warga:", "Alamat:"}, "Simpan Data Warga", mWarga,
                v -> wargaCtrl.simpan(new Warga(v[0], v[1], v[2])), this::muatWarga));
        add(tab, BorderLayout.CENTER);

        muatLaporan();
        muatAgenda();
        muatWarga();
    }

    // Panel laporan: tabel + tombol ubah status
    private JPanel panelLaporan() {
        JTable table = new JTable(mLap);
        table.setRowHeight(25);
        JButton b1 = new JButton("Ubah Status: DIPROSES"), b2 = new JButton("Ubah Status: SELESAI");
        b1.setBackground(new Color(241, 196, 15));
        b2.setBackground(new Color(46, 204, 113));
        b2.setForeground(Color.WHITE);
        b1.addActionListener(e -> ubahStatus(table, "Diproses"));
        b2.addActionListener(e -> ubahStatus(table, "Selesai"));

        JPanel bawah = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bawah.add(b1);
        bawah.add(b2);
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(new EmptyBorder(15, 15, 15, 15));
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        p.add(bawah, BorderLayout.SOUTH);
        return p;
    }

    private void ubahStatus(JTable t, String status) {
        int row = t.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih baris laporan terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        laporanCtrl.updateStatus((String) mLap.getValueAt(row, 0), status);
        muatLaporan();
        JOptionPane.showMessageDialog(this, "Status laporan berhasil diperbarui!");
    }

    // Panel form + tabel yang dipakai bersama oleh Agenda dan Warga.
    // Kolom pertama dan kedua wajib diisi.
    private JPanel panelForm(String[] label, String teksTombol, DefaultTableModel model,
                             Consumer<String[]> simpan, Runnable muat) {
        JPanel form = new JPanel(new GridLayout(label.length + 1, 2, 8, 8));
        JTextField[] f = new JTextField[label.length];
        for (int i = 0; i < label.length; i++) {
            f[i] = new JTextField();
            form.add(new JLabel(label[i]));
            form.add(f[i]);
        }
        JButton btn = new JButton(teksTombol);
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        form.add(new JLabel(""));
        form.add(btn);

        btn.addActionListener(e -> {
            String[] v = new String[f.length];
            for (int i = 0; i < f.length; i++) v[i] = f[i].getText().trim();
            if (v[0].isEmpty() || v[1].isEmpty()) {
                JOptionPane.showMessageDialog(this, label[0] + " dan " + label[1] + " wajib diisi!");
                return;
            }
            try {
                simpan.accept(v);
                muat.run();
                for (JTextField t : f) t.setText("");
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan (mungkin data sudah ada): " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JTable table = new JTable(model);
        table.setRowHeight(25);
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(new EmptyBorder(15, 15, 15, 15));
        p.add(form, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private void muatLaporan() {
        mLap.setRowCount(0);
        for (Laporan l : laporanCtrl.getAll())
            mLap.addRow(new Object[]{l.getId(), l.getNamaPelapor(), l.getJudul(), l.getStatus()});
    }

    private void muatAgenda() {
        mAgenda.setRowCount(0);
        for (Agenda a : agendaCtrl.getAll())
            mAgenda.addRow(new Object[]{a.getNamaKegiatan(), a.getTanggal(), a.getLokasi()});
    }

    private void muatWarga() {
        mWarga.setRowCount(0);
        for (Warga w : wargaCtrl.getAll())
            mWarga.addRow(new Object[]{w.getNik(), w.getNama(), w.getAlamat()});
    }
}