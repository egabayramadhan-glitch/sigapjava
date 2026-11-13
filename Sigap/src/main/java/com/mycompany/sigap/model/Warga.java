package com.mycompany.sigap.model;

import jakarta.persistence.*;

@Entity
@Table(name = "warga")
public class Warga {
    @Id
    private String nik;
    private String nama, alamat;

    public Warga() {}
    public Warga(String nik, String nama, String alamat) {
        this.nik = nik; this.nama = nama; this.alamat = alamat;
    }
    public String getNik() { return nik; }
    public String getNama() { return nama; }
    public String getAlamat() { return alamat; }
}