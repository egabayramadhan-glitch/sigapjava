package com.mycompany.sigap.controller;

import com.mycompany.sigap.model.Laporan;
import com.mycompany.sigap.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class LaporanController {

    public void simpan(Laporan l) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            s.persist(l);
            tx.commit();
        }
    }

    public List<Laporan> getAll() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("from Laporan", Laporan.class).list();
        }
    }

    public void updateStatus(String id, String statusBaru) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            Laporan l = s.get(Laporan.class, id);
            if (l != null) l.setStatus(statusBaru);
            tx.commit();
        }
    }
}