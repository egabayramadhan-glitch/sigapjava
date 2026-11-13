package com.mycompany.sigap.controller;

import com.mycompany.sigap.model.Agenda;
import com.mycompany.sigap.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AgendaController {

    public void simpan(Agenda a) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            s.persist(a);
            tx.commit();
        }
    }

    public List<Agenda> getAll() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("from Agenda", Agenda.class).list();
        }
    }
}