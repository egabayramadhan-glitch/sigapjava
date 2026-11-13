package com.mycompany.sigap.model;

import jakarta.persistence.*;

@Entity
@Table(name = "laporan")
public class Laporan {
    @Id
    private String id;
    private String namaPelapor;
    private String judul;
    @Column(length = 1000)
    private String deskripsi;
    private String status;

    public Laporan() {}  // wajib ada untuk Hibernate

    public Laporan(String id, String namaPelapor, String judul, String deskripsi, String status) {
        this.id = id; this.namaPelapor = namaPelapor; this.judul = judul;
        this.deskripsi = deskripsi; this.status = status;
    }
    public String getId() { return id; }
    public String getNamaPelapor() { return namaPelapor; }
    public String getJudul() { return judul; }
    public String getDeskripsi() { return deskripsi; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}