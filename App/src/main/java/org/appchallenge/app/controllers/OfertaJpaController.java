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
import org.appchallenge.app.entities.Horario;
import org.appchallenge.app.entities.Asignaturas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.appchallenge.app.controllers.exceptions.IllegalOrphanException;
import org.appchallenge.app.controllers.exceptions.NonexistentEntityException;
import org.appchallenge.app.controllers.exceptions.PreexistingEntityException;
import org.appchallenge.app.controllers.exceptions.RollbackFailureException;
import org.appchallenge.app.entities.Oferta;
import org.appchallenge.app.entities.OfertaPK;

/**
 *
 * @author Xiumeteo
 */
public class OfertaJpaController implements Serializable {

    public OfertaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Oferta oferta) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (oferta.getOfertaPK() == null) {
            oferta.setOfertaPK(new OfertaPK());
        }
        oferta.getOfertaPK().setAsignaturasid(oferta.getAsignaturas().getId());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Horario horario = oferta.getHorario();
            if (horario != null) {
                horario = em.getReference(horario.getClass(), horario.getHorarioPK());
                oferta.setHorario(horario);
            }
            Asignaturas asignaturas = oferta.getAsignaturas();
            if (asignaturas != null) {
                asignaturas = em.getReference(asignaturas.getClass(), asignaturas.getId());
                oferta.setAsignaturas(asignaturas);
            }
            em.persist(oferta);
            if (horario != null) {
                Oferta oldOfertaOfHorario = horario.getOferta();
                if (oldOfertaOfHorario != null) {
                    oldOfertaOfHorario.setHorario(null);
                    oldOfertaOfHorario = em.merge(oldOfertaOfHorario);
                }
                horario.setOferta(oferta);
                horario = em.merge(horario);
            }
            if (asignaturas != null) {
                asignaturas.getOfertaCollection().add(oferta);
                asignaturas = em.merge(asignaturas);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findOferta(oferta.getOfertaPK()) != null) {
                throw new PreexistingEntityException("Oferta " + oferta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Oferta oferta) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        oferta.getOfertaPK().setAsignaturasid(oferta.getAsignaturas().getId());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Oferta persistentOferta = em.find(Oferta.class, oferta.getOfertaPK());
            Horario horarioOld = persistentOferta.getHorario();
            Horario horarioNew = oferta.getHorario();
            Asignaturas asignaturasOld = persistentOferta.getAsignaturas();
            Asignaturas asignaturasNew = oferta.getAsignaturas();
            List<String> illegalOrphanMessages = null;
            if (horarioOld != null && !horarioOld.equals(horarioNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Horario " + horarioOld + " since its oferta field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (horarioNew != null) {
                horarioNew = em.getReference(horarioNew.getClass(), horarioNew.getHorarioPK());
                oferta.setHorario(horarioNew);
            }
            if (asignaturasNew != null) {
                asignaturasNew = em.getReference(asignaturasNew.getClass(), asignaturasNew.getId());
                oferta.setAsignaturas(asignaturasNew);
            }
            oferta = em.merge(oferta);
            if (horarioNew != null && !horarioNew.equals(horarioOld)) {
                Oferta oldOfertaOfHorario = horarioNew.getOferta();
                if (oldOfertaOfHorario != null) {
                    oldOfertaOfHorario.setHorario(null);
                    oldOfertaOfHorario = em.merge(oldOfertaOfHorario);
                }
                horarioNew.setOferta(oferta);
                horarioNew = em.merge(horarioNew);
            }
            if (asignaturasOld != null && !asignaturasOld.equals(asignaturasNew)) {
                asignaturasOld.getOfertaCollection().remove(oferta);
                asignaturasOld = em.merge(asignaturasOld);
            }
            if (asignaturasNew != null && !asignaturasNew.equals(asignaturasOld)) {
                asignaturasNew.getOfertaCollection().add(oferta);
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
                OfertaPK id = oferta.getOfertaPK();
                if (findOferta(id) == null) {
                    throw new NonexistentEntityException("The oferta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(OfertaPK id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Oferta oferta;
            try {
                oferta = em.getReference(Oferta.class, id);
                oferta.getOfertaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The oferta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Horario horarioOrphanCheck = oferta.getHorario();
            if (horarioOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Oferta (" + oferta + ") cannot be destroyed since the Horario " + horarioOrphanCheck + " in its horario field has a non-nullable oferta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Asignaturas asignaturas = oferta.getAsignaturas();
            if (asignaturas != null) {
                asignaturas.getOfertaCollection().remove(oferta);
                asignaturas = em.merge(asignaturas);
            }
            em.remove(oferta);
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

    public List<Oferta> findOfertaEntities() {
        return findOfertaEntities(true, -1, -1);
    }

    public List<Oferta> findOfertaEntities(int maxResults, int firstResult) {
        return findOfertaEntities(false, maxResults, firstResult);
    }

    private List<Oferta> findOfertaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Oferta.class));
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

    public Oferta findOferta(OfertaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Oferta.class, id);
        } finally {
            em.close();
        }
    }

    public int getOfertaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Oferta> rt = cq.from(Oferta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
