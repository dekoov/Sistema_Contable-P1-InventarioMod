package com.sistema.comun.negocio;

import com.sistema.comun.modelo.Usuario;
import javax.persistence.*;
import java.util.List;

public class NegocioUsuario {

    private static final String PU = "SistemaContablePU";

    public int insertar(Usuario usuario) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            emf = Persistence.createEntityManagerFactory(PU);
            em  = emf.createEntityManager();
            tx  = em.getTransaction();
            tx.begin();
            em.persist(usuario);
            tx.commit();
            return 1;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
            return -1;
        } finally {
            if (em  != null) em.close();
            if (emf != null) emf.close();
        }
    }

    public int modificar(Usuario usuario) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            emf = Persistence.createEntityManagerFactory(PU);
            em  = emf.createEntityManager();
            tx  = em.getTransaction();
            tx.begin();
            Usuario u = em.find(Usuario.class, usuario.getIdUsuario());
            if (u != null) {
                u.setUsername(usuario.getUsername());
                u.setContrasena(usuario.getContrasena());
                u.setRol(usuario.getRol());
                em.persist(u);
            }
            tx.commit();
            return 1;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
            return -1;
        } finally {
            if (em  != null) em.close();
            if (emf != null) emf.close();
        }
    }

    public int eliminar(Integer idUsuario) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            emf = Persistence.createEntityManagerFactory(PU);
            em  = emf.createEntityManager();
            tx  = em.getTransaction();
            tx.begin();
            Usuario u = em.find(Usuario.class, idUsuario);
            if (u != null) em.remove(u);
            tx.commit();
            return 1;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
            return -1;
        } finally {
            if (em  != null) em.close();
            if (emf != null) emf.close();
        }
    }

    public Usuario login(String username, String contrasena) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory(PU);
            em  = emf.createEntityManager();
            return em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.username = :usr AND u.contrasena = :pwd",
                            Usuario.class)
                    .setParameter("usr", username)
                    .setParameter("pwd", contrasena)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            if (em  != null) em.close();
            if (emf != null) emf.close();
        }
    }

    public List<Usuario> listarTodos() {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory(PU);
            em  = emf.createEntityManager();
            return em.createQuery(
                    "SELECT u FROM Usuario u ORDER BY u.username",
                    Usuario.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (em  != null) em.close();
            if (emf != null) emf.close();
        }
    }

    public Integer obtenerSiguienteId() {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory(PU);
            em  = emf.createEntityManager();
            Number max = (Number) em.createQuery(
                            "SELECT COALESCE(MAX(u.idUsuario), 0) FROM Usuario u")
                    .getSingleResult();
            return max.intValue() + 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        } finally {
            if (em  != null) em.close();
            if (emf != null) emf.close();
        }
    }

    public boolean existeAdministrador() {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory(PU);
            em  = emf.createEntityManager();
            Long count = (Long) em.createQuery(
                            "SELECT COUNT(u) FROM Usuario u WHERE u.rol = 'ADMIN'")
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            return false;
        } finally {
            if (em  != null) em.close();
            if (emf != null) emf.close();
        }
    }
}