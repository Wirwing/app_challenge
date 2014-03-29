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
import org.appchallenge.app.entities.Asignaturas;
import org.appchallenge.app.entities.AsignaturasAlumno;
import org.appchallenge.app.entities.Oferta;

/**
 *
 * @author Xiumeteo
 */
public class AsignaturasJpaController implements Serializable {

    public AsignaturasJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Asignaturas asignaturas) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (asignaturas.getPlanCollection() == null) {
            asignaturas.setPlanCollection(new ArrayList<Plan>());
        }
        if (asignaturas.getAsignaturasCollection() == null) {
            asignaturas.setAsignaturasCollection(new ArrayList<Asignaturas>());
        }
        if (asignaturas.getAsignaturasCollection1() == null) {
            asignaturas.setAsignaturasCollection1(new ArrayList<Asignaturas>());
        }
        if (asignaturas.getAsignaturasAlumnoCollection() == null) {
            asignaturas.setAsignaturasAlumnoCollection(new ArrayList<AsignaturasAlumno>());
        }
        if (asignaturas.getOfertaCollection() == null) {
            asignaturas.setOfertaCollection(new ArrayList<Oferta>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Plan> attachedPlanCollection = new ArrayList<Plan>();
            for (Plan planCollectionPlanToAttach : asignaturas.getPlanCollection()) {
                planCollectionPlanToAttach = em.getReference(planCollectionPlanToAttach.getClass(), planCollectionPlanToAttach.getId());
                attachedPlanCollection.add(planCollectionPlanToAttach);
            }
            asignaturas.setPlanCollection(attachedPlanCollection);
            Collection<Asignaturas> attachedAsignaturasCollection = new ArrayList<Asignaturas>();
            for (Asignaturas asignaturasCollectionAsignaturasToAttach : asignaturas.getAsignaturasCollection()) {
                asignaturasCollectionAsignaturasToAttach = em.getReference(asignaturasCollectionAsignaturasToAttach.getClass(), asignaturasCollectionAsignaturasToAttach.getId());
                attachedAsignaturasCollection.add(asignaturasCollectionAsignaturasToAttach);
            }
            asignaturas.setAsignaturasCollection(attachedAsignaturasCollection);
            Collection<Asignaturas> attachedAsignaturasCollection1 = new ArrayList<Asignaturas>();
            for (Asignaturas asignaturasCollection1AsignaturasToAttach : asignaturas.getAsignaturasCollection1()) {
                asignaturasCollection1AsignaturasToAttach = em.getReference(asignaturasCollection1AsignaturasToAttach.getClass(), asignaturasCollection1AsignaturasToAttach.getId());
                attachedAsignaturasCollection1.add(asignaturasCollection1AsignaturasToAttach);
            }
            asignaturas.setAsignaturasCollection1(attachedAsignaturasCollection1);
            Collection<AsignaturasAlumno> attachedAsignaturasAlumnoCollection = new ArrayList<AsignaturasAlumno>();
            for (AsignaturasAlumno asignaturasAlumnoCollectionAsignaturasAlumnoToAttach : asignaturas.getAsignaturasAlumnoCollection()) {
                asignaturasAlumnoCollectionAsignaturasAlumnoToAttach = em.getReference(asignaturasAlumnoCollectionAsignaturasAlumnoToAttach.getClass(), asignaturasAlumnoCollectionAsignaturasAlumnoToAttach.getAsignaturasAlumnoPK());
                attachedAsignaturasAlumnoCollection.add(asignaturasAlumnoCollectionAsignaturasAlumnoToAttach);
            }
            asignaturas.setAsignaturasAlumnoCollection(attachedAsignaturasAlumnoCollection);
            Collection<Oferta> attachedOfertaCollection = new ArrayList<Oferta>();
            for (Oferta ofertaCollectionOfertaToAttach : asignaturas.getOfertaCollection()) {
                ofertaCollectionOfertaToAttach = em.getReference(ofertaCollectionOfertaToAttach.getClass(), ofertaCollectionOfertaToAttach.getOfertaPK());
                attachedOfertaCollection.add(ofertaCollectionOfertaToAttach);
            }
            asignaturas.setOfertaCollection(attachedOfertaCollection);
            em.persist(asignaturas);
            for (Plan planCollectionPlan : asignaturas.getPlanCollection()) {
                planCollectionPlan.getAsignaturasCollection().add(asignaturas);
                planCollectionPlan = em.merge(planCollectionPlan);
            }
            for (Asignaturas asignaturasCollectionAsignaturas : asignaturas.getAsignaturasCollection()) {
                asignaturasCollectionAsignaturas.getAsignaturasCollection().add(asignaturas);
                asignaturasCollectionAsignaturas = em.merge(asignaturasCollectionAsignaturas);
            }
            for (Asignaturas asignaturasCollection1Asignaturas : asignaturas.getAsignaturasCollection1()) {
                asignaturasCollection1Asignaturas.getAsignaturasCollection().add(asignaturas);
                asignaturasCollection1Asignaturas = em.merge(asignaturasCollection1Asignaturas);
            }
            for (AsignaturasAlumno asignaturasAlumnoCollectionAsignaturasAlumno : asignaturas.getAsignaturasAlumnoCollection()) {
                Asignaturas oldAsignaturasOfAsignaturasAlumnoCollectionAsignaturasAlumno = asignaturasAlumnoCollectionAsignaturasAlumno.getAsignaturas();
                asignaturasAlumnoCollectionAsignaturasAlumno.setAsignaturas(asignaturas);
                asignaturasAlumnoCollectionAsignaturasAlumno = em.merge(asignaturasAlumnoCollectionAsignaturasAlumno);
                if (oldAsignaturasOfAsignaturasAlumnoCollectionAsignaturasAlumno != null) {
                    oldAsignaturasOfAsignaturasAlumnoCollectionAsignaturasAlumno.getAsignaturasAlumnoCollection().remove(asignaturasAlumnoCollectionAsignaturasAlumno);
                    oldAsignaturasOfAsignaturasAlumnoCollectionAsignaturasAlumno = em.merge(oldAsignaturasOfAsignaturasAlumnoCollectionAsignaturasAlumno);
                }
            }
            for (Oferta ofertaCollectionOferta : asignaturas.getOfertaCollection()) {
                Asignaturas oldAsignaturasOfOfertaCollectionOferta = ofertaCollectionOferta.getAsignaturas();
                ofertaCollectionOferta.setAsignaturas(asignaturas);
                ofertaCollectionOferta = em.merge(ofertaCollectionOferta);
                if (oldAsignaturasOfOfertaCollectionOferta != null) {
                    oldAsignaturasOfOfertaCollectionOferta.getOfertaCollection().remove(ofertaCollectionOferta);
                    oldAsignaturasOfOfertaCollectionOferta = em.merge(oldAsignaturasOfOfertaCollectionOferta);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAsignaturas(asignaturas.getId()) != null) {
                throw new PreexistingEntityException("Asignaturas " + asignaturas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Asignaturas asignaturas) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Asignaturas persistentAsignaturas = em.find(Asignaturas.class, asignaturas.getId());
            Collection<Plan> planCollectionOld = persistentAsignaturas.getPlanCollection();
            Collection<Plan> planCollectionNew = asignaturas.getPlanCollection();
            Collection<Asignaturas> asignaturasCollectionOld = persistentAsignaturas.getAsignaturasCollection();
            Collection<Asignaturas> asignaturasCollectionNew = asignaturas.getAsignaturasCollection();
            Collection<Asignaturas> asignaturasCollection1Old = persistentAsignaturas.getAsignaturasCollection1();
            Collection<Asignaturas> asignaturasCollection1New = asignaturas.getAsignaturasCollection1();
            Collection<AsignaturasAlumno> asignaturasAlumnoCollectionOld = persistentAsignaturas.getAsignaturasAlumnoCollection();
            Collection<AsignaturasAlumno> asignaturasAlumnoCollectionNew = asignaturas.getAsignaturasAlumnoCollection();
            Collection<Oferta> ofertaCollectionOld = persistentAsignaturas.getOfertaCollection();
            Collection<Oferta> ofertaCollectionNew = asignaturas.getOfertaCollection();
            List<String> illegalOrphanMessages = null;
            for (AsignaturasAlumno asignaturasAlumnoCollectionOldAsignaturasAlumno : asignaturasAlumnoCollectionOld) {
                if (!asignaturasAlumnoCollectionNew.contains(asignaturasAlumnoCollectionOldAsignaturasAlumno)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AsignaturasAlumno " + asignaturasAlumnoCollectionOldAsignaturasAlumno + " since its asignaturas field is not nullable.");
                }
            }
            for (Oferta ofertaCollectionOldOferta : ofertaCollectionOld) {
                if (!ofertaCollectionNew.contains(ofertaCollectionOldOferta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Oferta " + ofertaCollectionOldOferta + " since its asignaturas field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Plan> attachedPlanCollectionNew = new ArrayList<Plan>();
            for (Plan planCollectionNewPlanToAttach : planCollectionNew) {
                planCollectionNewPlanToAttach = em.getReference(planCollectionNewPlanToAttach.getClass(), planCollectionNewPlanToAttach.getId());
                attachedPlanCollectionNew.add(planCollectionNewPlanToAttach);
            }
            planCollectionNew = attachedPlanCollectionNew;
            asignaturas.setPlanCollection(planCollectionNew);
            Collection<Asignaturas> attachedAsignaturasCollectionNew = new ArrayList<Asignaturas>();
            for (Asignaturas asignaturasCollectionNewAsignaturasToAttach : asignaturasCollectionNew) {
                asignaturasCollectionNewAsignaturasToAttach = em.getReference(asignaturasCollectionNewAsignaturasToAttach.getClass(), asignaturasCollectionNewAsignaturasToAttach.getId());
                attachedAsignaturasCollectionNew.add(asignaturasCollectionNewAsignaturasToAttach);
            }
            asignaturasCollectionNew = attachedAsignaturasCollectionNew;
            asignaturas.setAsignaturasCollection(asignaturasCollectionNew);
            Collection<Asignaturas> attachedAsignaturasCollection1New = new ArrayList<Asignaturas>();
            for (Asignaturas asignaturasCollection1NewAsignaturasToAttach : asignaturasCollection1New) {
                asignaturasCollection1NewAsignaturasToAttach = em.getReference(asignaturasCollection1NewAsignaturasToAttach.getClass(), asignaturasCollection1NewAsignaturasToAttach.getId());
                attachedAsignaturasCollection1New.add(asignaturasCollection1NewAsignaturasToAttach);
            }
            asignaturasCollection1New = attachedAsignaturasCollection1New;
            asignaturas.setAsignaturasCollection1(asignaturasCollection1New);
            Collection<AsignaturasAlumno> attachedAsignaturasAlumnoCollectionNew = new ArrayList<AsignaturasAlumno>();
            for (AsignaturasAlumno asignaturasAlumnoCollectionNewAsignaturasAlumnoToAttach : asignaturasAlumnoCollectionNew) {
                asignaturasAlumnoCollectionNewAsignaturasAlumnoToAttach = em.getReference(asignaturasAlumnoCollectionNewAsignaturasAlumnoToAttach.getClass(), asignaturasAlumnoCollectionNewAsignaturasAlumnoToAttach.getAsignaturasAlumnoPK());
                attachedAsignaturasAlumnoCollectionNew.add(asignaturasAlumnoCollectionNewAsignaturasAlumnoToAttach);
            }
            asignaturasAlumnoCollectionNew = attachedAsignaturasAlumnoCollectionNew;
            asignaturas.setAsignaturasAlumnoCollection(asignaturasAlumnoCollectionNew);
            Collection<Oferta> attachedOfertaCollectionNew = new ArrayList<Oferta>();
            for (Oferta ofertaCollectionNewOfertaToAttach : ofertaCollectionNew) {
                ofertaCollectionNewOfertaToAttach = em.getReference(ofertaCollectionNewOfertaToAttach.getClass(), ofertaCollectionNewOfertaToAttach.getOfertaPK());
                attachedOfertaCollectionNew.add(ofertaCollectionNewOfertaToAttach);
            }
            ofertaCollectionNew = attachedOfertaCollectionNew;
            asignaturas.setOfertaCollection(ofertaCollectionNew);
            asignaturas = em.merge(asignaturas);
            for (Plan planCollectionOldPlan : planCollectionOld) {
                if (!planCollectionNew.contains(planCollectionOldPlan)) {
                    planCollectionOldPlan.getAsignaturasCollection().remove(asignaturas);
                    planCollectionOldPlan = em.merge(planCollectionOldPlan);
                }
            }
            for (Plan planCollectionNewPlan : planCollectionNew) {
                if (!planCollectionOld.contains(planCollectionNewPlan)) {
                    planCollectionNewPlan.getAsignaturasCollection().add(asignaturas);
                    planCollectionNewPlan = em.merge(planCollectionNewPlan);
                }
            }
            for (Asignaturas asignaturasCollectionOldAsignaturas : asignaturasCollectionOld) {
                if (!asignaturasCollectionNew.contains(asignaturasCollectionOldAsignaturas)) {
                    asignaturasCollectionOldAsignaturas.getAsignaturasCollection().remove(asignaturas);
                    asignaturasCollectionOldAsignaturas = em.merge(asignaturasCollectionOldAsignaturas);
                }
            }
            for (Asignaturas asignaturasCollectionNewAsignaturas : asignaturasCollectionNew) {
                if (!asignaturasCollectionOld.contains(asignaturasCollectionNewAsignaturas)) {
                    asignaturasCollectionNewAsignaturas.getAsignaturasCollection().add(asignaturas);
                    asignaturasCollectionNewAsignaturas = em.merge(asignaturasCollectionNewAsignaturas);
                }
            }
            for (Asignaturas asignaturasCollection1OldAsignaturas : asignaturasCollection1Old) {
                if (!asignaturasCollection1New.contains(asignaturasCollection1OldAsignaturas)) {
                    asignaturasCollection1OldAsignaturas.getAsignaturasCollection().remove(asignaturas);
                    asignaturasCollection1OldAsignaturas = em.merge(asignaturasCollection1OldAsignaturas);
                }
            }
            for (Asignaturas asignaturasCollection1NewAsignaturas : asignaturasCollection1New) {
                if (!asignaturasCollection1Old.contains(asignaturasCollection1NewAsignaturas)) {
                    asignaturasCollection1NewAsignaturas.getAsignaturasCollection().add(asignaturas);
                    asignaturasCollection1NewAsignaturas = em.merge(asignaturasCollection1NewAsignaturas);
                }
            }
            for (AsignaturasAlumno asignaturasAlumnoCollectionNewAsignaturasAlumno : asignaturasAlumnoCollectionNew) {
                if (!asignaturasAlumnoCollectionOld.contains(asignaturasAlumnoCollectionNewAsignaturasAlumno)) {
                    Asignaturas oldAsignaturasOfAsignaturasAlumnoCollectionNewAsignaturasAlumno = asignaturasAlumnoCollectionNewAsignaturasAlumno.getAsignaturas();
                    asignaturasAlumnoCollectionNewAsignaturasAlumno.setAsignaturas(asignaturas);
                    asignaturasAlumnoCollectionNewAsignaturasAlumno = em.merge(asignaturasAlumnoCollectionNewAsignaturasAlumno);
                    if (oldAsignaturasOfAsignaturasAlumnoCollectionNewAsignaturasAlumno != null && !oldAsignaturasOfAsignaturasAlumnoCollectionNewAsignaturasAlumno.equals(asignaturas)) {
                        oldAsignaturasOfAsignaturasAlumnoCollectionNewAsignaturasAlumno.getAsignaturasAlumnoCollection().remove(asignaturasAlumnoCollectionNewAsignaturasAlumno);
                        oldAsignaturasOfAsignaturasAlumnoCollectionNewAsignaturasAlumno = em.merge(oldAsignaturasOfAsignaturasAlumnoCollectionNewAsignaturasAlumno);
                    }
                }
            }
            for (Oferta ofertaCollectionNewOferta : ofertaCollectionNew) {
                if (!ofertaCollectionOld.contains(ofertaCollectionNewOferta)) {
                    Asignaturas oldAsignaturasOfOfertaCollectionNewOferta = ofertaCollectionNewOferta.getAsignaturas();
                    ofertaCollectionNewOferta.setAsignaturas(asignaturas);
                    ofertaCollectionNewOferta = em.merge(ofertaCollectionNewOferta);
                    if (oldAsignaturasOfOfertaCollectionNewOferta != null && !oldAsignaturasOfOfertaCollectionNewOferta.equals(asignaturas)) {
                        oldAsignaturasOfOfertaCollectionNewOferta.getOfertaCollection().remove(ofertaCollectionNewOferta);
                        oldAsignaturasOfOfertaCollectionNewOferta = em.merge(oldAsignaturasOfOfertaCollectionNewOferta);
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
                Integer id = asignaturas.getId();
                if (findAsignaturas(id) == null) {
                    throw new NonexistentEntityException("The asignaturas with id " + id + " no longer exists.");
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
            Asignaturas asignaturas;
            try {
                asignaturas = em.getReference(Asignaturas.class, id);
                asignaturas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The asignaturas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<AsignaturasAlumno> asignaturasAlumnoCollectionOrphanCheck = asignaturas.getAsignaturasAlumnoCollection();
            for (AsignaturasAlumno asignaturasAlumnoCollectionOrphanCheckAsignaturasAlumno : asignaturasAlumnoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Asignaturas (" + asignaturas + ") cannot be destroyed since the AsignaturasAlumno " + asignaturasAlumnoCollectionOrphanCheckAsignaturasAlumno + " in its asignaturasAlumnoCollection field has a non-nullable asignaturas field.");
            }
            Collection<Oferta> ofertaCollectionOrphanCheck = asignaturas.getOfertaCollection();
            for (Oferta ofertaCollectionOrphanCheckOferta : ofertaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Asignaturas (" + asignaturas + ") cannot be destroyed since the Oferta " + ofertaCollectionOrphanCheckOferta + " in its ofertaCollection field has a non-nullable asignaturas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Plan> planCollection = asignaturas.getPlanCollection();
            for (Plan planCollectionPlan : planCollection) {
                planCollectionPlan.getAsignaturasCollection().remove(asignaturas);
                planCollectionPlan = em.merge(planCollectionPlan);
            }
            Collection<Asignaturas> asignaturasCollection = asignaturas.getAsignaturasCollection();
            for (Asignaturas asignaturasCollectionAsignaturas : asignaturasCollection) {
                asignaturasCollectionAsignaturas.getAsignaturasCollection().remove(asignaturas);
                asignaturasCollectionAsignaturas = em.merge(asignaturasCollectionAsignaturas);
            }
            Collection<Asignaturas> asignaturasCollection1 = asignaturas.getAsignaturasCollection1();
            for (Asignaturas asignaturasCollection1Asignaturas : asignaturasCollection1) {
                asignaturasCollection1Asignaturas.getAsignaturasCollection().remove(asignaturas);
                asignaturasCollection1Asignaturas = em.merge(asignaturasCollection1Asignaturas);
            }
            em.remove(asignaturas);
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

    public List<Asignaturas> findAsignaturasEntities() {
        return findAsignaturasEntities(true, -1, -1);
    }

    public List<Asignaturas> findAsignaturasEntities(int maxResults, int firstResult) {
        return findAsignaturasEntities(false, maxResults, firstResult);
    }

    private List<Asignaturas> findAsignaturasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Asignaturas.class));
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

    public Asignaturas findAsignaturas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Asignaturas.class, id);
        } finally {
            em.close();
        }
    }

    public int getAsignaturasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Asignaturas> rt = cq.from(Asignaturas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
