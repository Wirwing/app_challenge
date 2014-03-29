/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appchallenge.app.controllers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import org.appchallenge.app.controllers.exceptions.NonexistentEntityException;
import org.appchallenge.app.controllers.exceptions.PreexistingEntityException;
import org.appchallenge.app.controllers.exceptions.RollbackFailureException;
import org.appchallenge.app.entities.Alumno;
import org.appchallenge.app.entities.Asignaturas;
import org.appchallenge.app.entities.AsignaturasAlumno;
import org.appchallenge.app.entities.AsignaturasAlumnoPK;

/**
 *
 * @author Xiumeteo
 */
public class AsignaturasAlumnoJpaController implements Serializable {

    public AsignaturasAlumnoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AsignaturasAlumno asignaturasAlumno) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (asignaturasAlumno.getAsignaturasAlumnoPK() == null) {
            asignaturasAlumno.setAsignaturasAlumnoPK(new AsignaturasAlumnoPK());
        }
        asignaturasAlumno.getAsignaturasAlumnoPK().setAlumnoid(asignaturasAlumno.getAlumno().getAlumnoPK().getId());
        asignaturasAlumno.getAsignaturasAlumnoPK().setAsignaturasid(asignaturasAlumno.getAsignaturas().getId());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Alumno alumno = asignaturasAlumno.getAlumno();
            if (alumno != null) {
                alumno = em.getReference(alumno.getClass(), alumno.getAlumnoPK());
                asignaturasAlumno.setAlumno(alumno);
            }
            Asignaturas asignaturas = asignaturasAlumno.getAsignaturas();
            if (asignaturas != null) {
                asignaturas = em.getReference(asignaturas.getClass(), asignaturas.getId());
                asignaturasAlumno.setAsignaturas(asignaturas);
            }
            em.persist(asignaturasAlumno);
            if (alumno != null) {
                alumno.getAsignaturasAlumnoCollection().add(asignaturasAlumno);
                alumno = em.merge(alumno);
            }
            if (asignaturas != null) {
                asignaturas.getAsignaturasAlumnoCollection().add(asignaturasAlumno);
                asignaturas = em.merge(asignaturas);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAsignaturasAlumno(asignaturasAlumno.getAsignaturasAlumnoPK()) != null) {
                throw new PreexistingEntityException("AsignaturasAlumno " + asignaturasAlumno + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AsignaturasAlumno asignaturasAlumno) throws NonexistentEntityException, RollbackFailureException, Exception {
        asignaturasAlumno.getAsignaturasAlumnoPK().setAlumnoid(asignaturasAlumno.getAlumno().getAlumnoPK().getId());
        asignaturasAlumno.getAsignaturasAlumnoPK().setAsignaturasid(asignaturasAlumno.getAsignaturas().getId());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AsignaturasAlumno persistentAsignaturasAlumno = em.find(AsignaturasAlumno.class, asignaturasAlumno.getAsignaturasAlumnoPK());
            Alumno alumnoOld = persistentAsignaturasAlumno.getAlumno();
            Alumno alumnoNew = asignaturasAlumno.getAlumno();
            Asignaturas asignaturasOld = persistentAsignaturasAlumno.getAsignaturas();
            Asignaturas asignaturasNew = asignaturasAlumno.getAsignaturas();
            if (alumnoNew != null) {
                alumnoNew = em.getReference(alumnoNew.getClass(), alumnoNew.getAlumnoPK());
                asignaturasAlumno.setAlumno(alumnoNew);
            }
            if (asignaturasNew != null) {
                asignaturasNew = em.getReference(asignaturasNew.getClass(), asignaturasNew.getId());
                asignaturasAlumno.setAsignaturas(asignaturasNew);
            }
            asignaturasAlumno = em.merge(asignaturasAlumno);
            if (alumnoOld != null && !alumnoOld.equals(alumnoNew)) {
                alumnoOld.getAsignaturasAlumnoCollection().remove(asignaturasAlumno);
                alumnoOld = em.merge(alumnoOld);
            }
            if (alumnoNew != null && !alumnoNew.equals(alumnoOld)) {
                alumnoNew.getAsignaturasAlumnoCollection().add(asignaturasAlumno);
                alumnoNew = em.merge(alumnoNew);
            }
            if (asignaturasOld != null && !asignaturasOld.equals(asignaturasNew)) {
                asignaturasOld.getAsignaturasAlumnoCollection().remove(asignaturasAlumno);
                asignaturasOld = em.merge(asignaturasOld);
            }
            if (asignaturasNew != null && !asignaturasNew.equals(asignaturasOld)) {
                asignaturasNew.getAsignaturasAlumnoCollection().add(asignaturasAlumno);
                asignaturasNew = em.merge(asignaturasNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                AsignaturasAlumnoPK id = asignaturasAlumno.getAsignaturasAlumnoPK();
                if (findAsignaturasAlumno(id) == null) {
                    throw new NonexistentEntityException("The asignaturasAlumno with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(AsignaturasAlumnoPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AsignaturasAlumno asignaturasAlumno;
            try {
                asignaturasAlumno = em.getReference(AsignaturasAlumno.class, id);
                asignaturasAlumno.getAsignaturasAlumnoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The asignaturasAlumno with id " + id + " no longer exists.", enfe);
            }
            Alumno alumno = asignaturasAlumno.getAlumno();
            if (alumno != null) {
                alumno.getAsignaturasAlumnoCollection().remove(asignaturasAlumno);
                alumno = em.merge(alumno);
            }
            Asignaturas asignaturas = asignaturasAlumno.getAsignaturas();
            if (asignaturas != null) {
                asignaturas.getAsignaturasAlumnoCollection().remove(asignaturasAlumno);
                asignaturas = em.merge(asignaturas);
            }
            em.remove(asignaturasAlumno);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AsignaturasAlumno> findAsignaturasAlumnoEntities() {
        return findAsignaturasAlumnoEntities(true, -1, -1);
    }

    public List<AsignaturasAlumno> findAsignaturasAlumnoEntities(int maxResults, int firstResult) {
        return findAsignaturasAlumnoEntities(false, maxResults, firstResult);
    }

    private List<AsignaturasAlumno> findAsignaturasAlumnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AsignaturasAlumno.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public AsignaturasAlumno findAsignaturasAlumno(AsignaturasAlumnoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AsignaturasAlumno.class, id);
        } finally {
            em.close();
        }
    }

    public int getAsignaturasAlumnoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AsignaturasAlumno> rt = cq.from(AsignaturasAlumno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
