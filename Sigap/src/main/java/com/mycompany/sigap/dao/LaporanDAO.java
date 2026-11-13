package com.mycompany.sigap.dao;

import com.mycompany.sigap.model.Laporan;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class LaporanDAO {

    private SessionFactory sessionFactory;

    // IMPLEMENTASI DI: Setter Injection (SessionFactory disuntikkan Spring)
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Session dikelola Spring (transparent session management)
    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    // Mencari laporan berdasarkan ID, null jika belum ada
    public Laporan cariById(String id) {
        return getSession().find(Laporan.class, id);
    }

    // Mengambil semua laporan
    public List<Laporan> getAll() {
        return getSession()
                .createQuery("FROM Laporan", Laporan.class)
                .getResultList();
    }

    // Menyimpan laporan baru atau memperbarui laporan yang sudah ada
    public void simpanAtauUpdate(Laporan laporan) {
        getSession().merge(laporan);
    }
}