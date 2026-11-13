package com.mycompany.sigap.controller;

import com.mycompany.sigap.model.Warga;
import com.mycompany.sigap.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class WargaController {

    public void simpan(Warga w) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            s.persist(w);
            tx.commit();
        }
    }

    public List<Warga> getAll() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("from Warga", Warga.class).list();
        }
    }
}