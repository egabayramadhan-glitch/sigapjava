package com.mycompany.sigap.model;

import jakarta.persistence.*;

@Entity
@Table(name = "agenda")
public class Agenda {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String namaKegiatan, tanggal, lokasi;

    public Agenda() {}
    public Agenda(String namaKegiatan, String tanggal, String lokasi) {
        this.namaKegiatan = namaKegiatan; this.tanggal = tanggal; this.lokasi = lokasi;
    }
    public Long getId() { return id; }
    public String getNamaKegiatan() { return namaKegiatan; }
    public String getTanggal() { return tanggal; }
    public String getLokasi() { return lokasi; }
}