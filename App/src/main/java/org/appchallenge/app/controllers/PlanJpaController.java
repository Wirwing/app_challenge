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
import org.appchallenge.app.entities.Asignaturas;
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
import org.appchallenge.app.entities.Plan;

/**
 *
 * @author Xiumeteo
 */
public class PlanJpaController implements Serializable {

    public PlanJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Plan plan) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (plan.getAsignaturasCollection() == null) {
            plan.setAsignaturasCollection(new ArrayList<Asignaturas>());
        }
        if (plan.getAlumnoCollection() == null) {
            plan.setAlumnoCollection(new ArrayList<Alumno>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Asignaturas> attachedAsignaturasCollection = new ArrayList<Asignaturas>();
            for (Asignaturas asignaturasCollectionAsignaturasToAttach : plan.getAsignaturasCollection()) {
                asignaturasCollectionAsignaturasToAttach = em.getReference(asignaturasCollectionAsignaturasToAttach.getClass(), asignaturasCollectionAsignaturasToAttach.getId());
                attachedAsignaturasCollection.add(asignaturasCollectionAsignaturasToAttach);
            }
            plan.setAsignaturasCollection(attachedAsignaturasCollection);
            Collection<Alumno> attachedAlumnoCollection = new ArrayList<Alumno>();
            for (Alumno alumnoCollectionAlumnoToAttach : plan.getAlumnoCollection()) {
                alumnoCollectionAlumnoToAttach = em.getReference(alumnoCollectionAlumnoToAttach.getClass(), alumnoCollectionAlumnoToAttach.getAlumnoPK());
                attachedAlumnoCollection.add(alumnoCollectionAlumnoToAttach);
            }
            plan.setAlumnoCollection(attachedAlumnoCollection);
            em.persist(plan);
            for (Asignaturas asignaturasCollectionAsignaturas : plan.getAsignaturasCollection()) {
                asignaturasCollectionAsignaturas.getPlanCollection().add(plan);
                asignaturasCollectionAsignaturas = em.merge(asignaturasCollectionAsignaturas);
            }
            for (Alumno alumnoCollectionAlumno : plan.getAlumnoCollection()) {
                Plan oldPlanOfAlumnoCollectionAlumno = alumnoCollectionAlumno.getPlan();
                alumnoCollectionAlumno.setPlan(plan);
                alumnoCollectionAlumno = em.merge(alumnoCollectionAlumno);
                if (oldPlanOfAlumnoCollectionAlumno != null) {
                    oldPlanOfAlumnoCollectionAlumno.getAlumnoCollection().remove(alumnoCollectionAlumno);
                    oldPlanOfAlumnoCollectionAlumno = em.merge(oldPlanOfAlumnoCollectionAlumno);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPlan(plan.getId()) != null) {
                throw new PreexistingEntityException("Plan " + plan + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Plan plan) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Plan persistentPlan = em.find(Plan.class, plan.getId());
            Collection<Asignaturas> asignaturasCollectionOld = persistentPlan.getAsignaturasCollection();
            Collection<Asignaturas> asignaturasCollectionNew = plan.getAsignaturasCollection();
            Collection<Alumno> alumnoCollectionOld = persistentPlan.getAlumnoCollection();
            Collection<Alumno> alumnoCollectionNew = plan.getAlumnoCollection();
            List<String> illegalOrphanMessages = null;
            for (Alumno alumnoCollectionOldAlumno : alumnoCollectionOld) {
                if (!alumnoCollectionNew.contains(alumnoCollectionOldAlumno)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Alumno " + alumnoCollectionOldAlumno + " since its plan field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Asignaturas> attachedAsignaturasCollectionNew = new ArrayList<Asignaturas>();
            for (Asignaturas asignaturasCollectionNewAsignaturasToAttach : asignaturasCollectionNew) {
                asignaturasCollectionNewAsignaturasToAttach = em.getReference(asignaturasCollectionNewAsignaturasToAttach.getClass(), asignaturasCollectionNewAsignaturasToAttach.getId());
                attachedAsignaturasCollectionNew.add(asignaturasCollectionNewAsignaturasToAttach);
            }
            asignaturasCollectionNew = attachedAsignaturasCollectionNew;
            plan.setAsignaturasCollection(asignaturasCollectionNew);
            Collection<Alumno> attachedAlumnoCollectionNew = new ArrayList<Alumno>();
            for (Alumno alumnoCollectionNewAlumnoToAttach : alumnoCollectionNew) {
                alumnoCollectionNewAlumnoToAttach = em.getReference(alumnoCollectionNewAlumnoToAttach.getClass(), alumnoCollectionNewAlumnoToAttach.getAlumnoPK());
                attachedAlumnoCollectionNew.add(alumnoCollectionNewAlumnoToAttach);
            }
            alumnoCollectionNew = attachedAlumnoCollectionNew;
            plan.setAlumnoCollection(alumnoCollectionNew);
            plan = em.merge(plan);
            for (Asignaturas asignaturasCollectionOldAsignaturas : asignaturasCollectionOld) {
                if (!asignaturasCollectionNew.contains(asignaturasCollectionOldAsignaturas)) {
                    asignaturasCollectionOldAsignaturas.getPlanCollection().remove(plan);
                    asignaturasCollectionOldAsignaturas = em.merge(asignaturasCollectionOldAsignaturas);
                }
            }
            for (Asignaturas asignaturasCollectionNewAsignaturas : asignaturasCollectionNew) {
                if (!asignaturasCollectionOld.contains(asignaturasCollectionNewAsignaturas)) {
                    asignaturasCollectionNewAsignaturas.getPlanCollection().add(plan);
                    asignaturasCollectionNewAsignaturas = em.merge(asignaturasCollectionNewAsignaturas);
                }
            }
            for (Alumno alumnoCollectionNewAlumno : alumnoCollectionNew) {
                if (!alumnoCollectionOld.contains(alumnoCollectionNewAlumno)) {
                    Plan oldPlanOfAlumnoCollectionNewAlumno = alumnoCollectionNewAlumno.getPlan();
                    alumnoCollectionNewAlumno.setPlan(plan);
                    alumnoCollectionNewAlumno = em.merge(alumnoCollectionNewAlumno);
                    if (oldPlanOfAlumnoCollectionNewAlumno != null && !oldPlanOfAlumnoCollectionNewAlumno.equals(plan)) {
                        oldPlanOfAlumnoCollectionNewAlumno.getAlumnoCollection().remove(alumnoCollectionNewAlumno);
                        oldPlanOfAlumnoCollectionNewAlumno = em.merge(oldPlanOfAlumnoCollectionNewAlumno);
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
                Integer id = plan.getId();
                if (findPlan(id) == null) {
                    throw new NonexistentEntityException("The plan with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Plan plan;
            try {
                plan = em.getReference(Plan.class, id);
                plan.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The plan with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Alumno> alumnoCollectionOrphanCheck = plan.getAlumnoCollection();
            for (Alumno alumnoCollectionOrphanCheckAlumno : alumnoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Plan (" + plan + ") cannot be destroyed since the Alumno " + alumnoCollectionOrphanCheckAlumno + " in its alumnoCollection field has a non-nullable plan field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Asignaturas> asignaturasCollection = plan.getAsignaturasCollection();
            for (Asignaturas asignaturasCollectionAsignaturas : asignaturasCollection) {
                asignaturasCollectionAsignaturas.getPlanCollection().remove(plan);
                asignaturasCollectionAsignaturas = em.merge(asignaturasCollectionAsignaturas);
            }
            em.remove(plan);
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

    public List<Plan> findPlanEntities() {
        return findPlanEntities(true, -1, -1);
    }

    public List<Plan> findPlanEntities(int maxResults, int firstResult) {
        return findPlanEntities(false, maxResults, firstResult);
    }

    private List<Plan> findPlanEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Plan.class));
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

    public Plan findPlan(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Plan.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlanCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Plan> rt = cq.from(Plan.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
