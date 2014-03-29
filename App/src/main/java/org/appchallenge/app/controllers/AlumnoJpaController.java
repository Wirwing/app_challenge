/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appchallenge.app.controllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.appchallenge.app.entities.Plan;
import org.appchallenge.app.entities.AsignaturasAlumno;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.appchallenge.app.controllers.exceptions.IllegalOrphanException;
import org.appchallenge.app.controllers.exceptions.NonexistentEntityException;
import org.appchallenge.app.controllers.exceptions.PreexistingEntityException;
import org.appchallenge.app.controllers.exceptions.RollbackFailureException;
import org.appchallenge.app.entities.Alumno;
import org.appchallenge.app.entities.AlumnoPK;

/**
 *
 * @author Xiumeteo
 */
public class AlumnoJpaController implements Serializable {

    public AlumnoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Alumno alumno) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (alumno.getAlumnoPK() == null) {
            alumno.setAlumnoPK(new AlumnoPK());
        }
        if (alumno.getAsignaturasAlumnoCollection() == null) {
            alumno.setAsignaturasAlumnoCollection(new ArrayList<AsignaturasAlumno>());
        }
        alumno.getAlumnoPK().setPlanid(alumno.getPlan().getId());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Plan plan = alumno.getPlan();
            if (plan != null) {
                plan = em.getReference(plan.getClass(), plan.getId());
                alumno.setPlan(plan);
            }
            Collection<AsignaturasAlumno> attachedAsignaturasAlumnoCollection = new ArrayList<AsignaturasAlumno>();
            for (AsignaturasAlumno asignaturasAlumnoCollectionAsignaturasAlumnoToAttach : alumno.getAsignaturasAlumnoCollection()) {
                asignaturasAlumnoCollectionAsignaturasAlumnoToAttach = em.getReference(asignaturasAlumnoCollectionAsignaturasAlumnoToAttach.getClass(), asignaturasAlumnoCollectionAsignaturasAlumnoToAttach.getAsignaturasAlumnoPK());
                attachedAsignaturasAlumnoCollection.add(asignaturasAlumnoCollectionAsignaturasAlumnoToAttach);
            }
            alumno.setAsignaturasAlumnoCollection(attachedAsignaturasAlumnoCollection);
            em.persist(alumno);
            if (plan != null) {
                plan.getAlumnoCollection().add(alumno);
                plan = em.merge(plan);
            }
            for (AsignaturasAlumno asignaturasAlumnoCollectionAsignaturasAlumno : alumno.getAsignaturasAlumnoCollection()) {
                Alumno oldAlumnoOfAsignaturasAlumnoCollectionAsignaturasAlumno = asignaturasAlumnoCollectionAsignaturasAlumno.getAlumno();
                asignaturasAlumnoCollectionAsignaturasAlumno.setAlumno(alumno);
                asignaturasAlumnoCollectionAsignaturasAlumno = em.merge(asignaturasAlumnoCollectionAsignaturasAlumno);
                if (oldAlumnoOfAsignaturasAlumnoCollectionAsignaturasAlumno != null) {
                    oldAlumnoOfAsignaturasAlumnoCollectionAsignaturasAlumno.getAsignaturasAlumnoCollection().remove(asignaturasAlumnoCollectionAsignaturasAlumno);
                    oldAlumnoOfAsignaturasAlumnoCollectionAsignaturasAlumno = em.merge(oldAlumnoOfAsignaturasAlumnoCollectionAsignaturasAlumno);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAlumno(alumno.getAlumnoPK()) != null) {
                throw new PreexistingEntityException("Alumno " + alumno + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Alumno alumno) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        alumno.getAlumnoPK().setPlanid(alumno.getPlan().getId());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Alumno persistentAlumno = em.find(Alumno.class, alumno.getAlumnoPK());
            Plan planOld = persistentAlumno.getPlan();
            Plan planNew = alumno.getPlan();
            Collection<AsignaturasAlumno> asignaturasAlumnoCollectionOld = persistentAlumno.getAsignaturasAlumnoCollection();
            Collection<AsignaturasAlumno> asignaturasAlumnoCollectionNew = alumno.getAsignaturasAlumnoCollection();
            List<String> illegalOrphanMessages = null;
            for (AsignaturasAlumno asignaturasAlumnoCollectionOldAsignaturasAlumno : asignaturasAlumnoCollectionOld) {
                if (!asignaturasAlumnoCollectionNew.contains(asignaturasAlumnoCollectionOldAsignaturasAlumno)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AsignaturasAlumno " + asignaturasAlumnoCollectionOldAsignaturasAlumno + " since its alumno field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (planNew != null) {
                planNew = em.getReference(planNew.getClass(), planNew.getId());
                alumno.setPlan(planNew);
            }
            Collection<AsignaturasAlumno> attachedAsignaturasAlumnoCollectionNew = new ArrayList<AsignaturasAlumno>();
            for (AsignaturasAlumno asignaturasAlumnoCollectionNewAsignaturasAlumnoToAttach : asignaturasAlumnoCollectionNew) {
                asignaturasAlumnoCollectionNewAsignaturasAlumnoToAttach = em.getReference(asignaturasAlumnoCollectionNewAsignaturasAlumnoToAttach.getClass(), asignaturasAlumnoCollectionNewAsignaturasAlumnoToAttach.getAsignaturasAlumnoPK());
                attachedAsignaturasAlumnoCollectionNew.add(asignaturasAlumnoCollectionNewAsignaturasAlumnoToAttach);
            }
            asignaturasAlumnoCollectionNew = attachedAsignaturasAlumnoCollectionNew;
            alumno.setAsignaturasAlumnoCollection(asignaturasAlumnoCollectionNew);
            alumno = em.merge(alumno);
            if (planOld != null && !planOld.equals(planNew)) {
                planOld.getAlumnoCollection().remove(alumno);
                planOld = em.merge(planOld);
            }
            if (planNew != null && !planNew.equals(planOld)) {
                planNew.getAlumnoCollection().add(alumno);
                planNew = em.merge(planNew);
            }
            for (AsignaturasAlumno asignaturasAlumnoCollectionNewAsignaturasAlumno : asignaturasAlumnoCollectionNew) {
                if (!asignaturasAlumnoCollectionOld.contains(asignaturasAlumnoCollectionNewAsignaturasAlumno)) {
                    Alumno oldAlumnoOfAsignaturasAlumnoCollectionNewAsignaturasAlumno = asignaturasAlumnoCollectionNewAsignaturasAlumno.getAlumno();
                    asignaturasAlumnoCollectionNewAsignaturasAlumno.setAlumno(alumno);
                    asignaturasAlumnoCollectionNewAsignaturasAlumno = em.merge(asignaturasAlumnoCollectionNewAsignaturasAlumno);
                    if (oldAlumnoOfAsignaturasAlumnoCollectionNewAsignaturasAlumno != null && !oldAlumnoOfAsignaturasAlumnoCollectionNewAsignaturasAlumno.equals(alumno)) {
                        oldAlumnoOfAsignaturasAlumnoCollectionNewAsignaturasAlumno.getAsignaturasAlumnoCollection().remove(asignaturasAlumnoCollectionNewAsignaturasAlumno);
                        oldAlumnoOfAsignaturasAlumnoCollectionNewAsignaturasAlumno = em.merge(oldAlumnoOfAsignaturasAlumnoCollectionNewAsignaturasAlumno);
                    }
                }
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
                AlumnoPK id = alumno.getAlumnoPK();
                if (findAlumno(id) == null) {
                    throw new NonexistentEntityException("The alumno with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(AlumnoPK id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Alumno alumno;
            try {
                alumno = em.getReference(Alumno.class, id);
                alumno.getAlumnoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alumno with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<AsignaturasAlumno> asignaturasAlumnoCollectionOrphanCheck = alumno.getAsignaturasAlumnoCollection();
            for (AsignaturasAlumno asignaturasAlumnoCollectionOrphanCheckAsignaturasAlumno : asignaturasAlumnoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alumno (" + alumno + ") cannot be destroyed since the AsignaturasAlumno " + asignaturasAlumnoCollectionOrphanCheckAsignaturasAlumno + " in its asignaturasAlumnoCollection field has a non-nullable alumno field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Plan plan = alumno.getPlan();
            if (plan != null) {
                plan.getAlumnoCollection().remove(alumno);
                plan = em.merge(plan);
            }
            em.remove(alumno);
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

    public List<Alumno> findAlumnoEntities() {
        return findAlumnoEntities(true, -1, -1);
    }

    public List<Alumno> findAlumnoEntities(int maxResults, int firstResult) {
        return findAlumnoEntities(false, maxResults, firstResult);
    }

    private List<Alumno> findAlumnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Alumno.class));
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

    public Alumno findAlumno(AlumnoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Alumno.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlumnoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Alumno> rt = cq.from(Alumno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
