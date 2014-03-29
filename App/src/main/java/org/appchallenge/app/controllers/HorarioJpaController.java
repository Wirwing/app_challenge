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
import org.appchallenge.app.entities.Oferta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.appchallenge.app.controllers.exceptions.IllegalOrphanException;
import org.appchallenge.app.controllers.exceptions.NonexistentEntityException;
import org.appchallenge.app.controllers.exceptions.PreexistingEntityException;
import org.appchallenge.app.controllers.exceptions.RollbackFailureException;
import org.appchallenge.app.entities.Horario;
import org.appchallenge.app.entities.HorarioPK;

/**
 *
 * @author Xiumeteo
 */
public class HorarioJpaController implements Serializable {

    public HorarioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Horario horario) throws IllegalOrphanException, PreexistingEntityException, RollbackFailureException, Exception {
        if (horario.getHorarioPK() == null) {
            horario.setHorarioPK(new HorarioPK());
        }
        horario.getHorarioPK().setOfertaperiodo(horario.getOferta().getOfertaPK().getPeriodo());
        horario.getHorarioPK().setOfertaAsignaturasid(horario.getOferta().getOfertaPK().getAsignaturasid());
        horario.getHorarioPK().setOfertaProfesor(horario.getOferta().getOfertaPK().getProfesor());
        List<String> illegalOrphanMessages = null;
        Oferta ofertaOrphanCheck = horario.getOferta();
        if (ofertaOrphanCheck != null) {
            Horario oldHorarioOfOferta = ofertaOrphanCheck.getHorario();
            if (oldHorarioOfOferta != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Oferta " + ofertaOrphanCheck + " already has an item of type Horario whose oferta column cannot be null. Please make another selection for the oferta field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Oferta oferta = horario.getOferta();
            if (oferta != null) {
                oferta = em.getReference(oferta.getClass(), oferta.getOfertaPK());
                horario.setOferta(oferta);
            }
            em.persist(horario);
            if (oferta != null) {
                oferta.setHorario(horario);
                oferta = em.merge(oferta);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findHorario(horario.getHorarioPK()) != null) {
                throw new PreexistingEntityException("Horario " + horario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Horario horario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        horario.getHorarioPK().setOfertaperiodo(horario.getOferta().getOfertaPK().getPeriodo());
        horario.getHorarioPK().setOfertaAsignaturasid(horario.getOferta().getOfertaPK().getAsignaturasid());
        horario.getHorarioPK().setOfertaProfesor(horario.getOferta().getOfertaPK().getProfesor());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Horario persistentHorario = em.find(Horario.class, horario.getHorarioPK());
            Oferta ofertaOld = persistentHorario.getOferta();
            Oferta ofertaNew = horario.getOferta();
            List<String> illegalOrphanMessages = null;
            if (ofertaNew != null && !ofertaNew.equals(ofertaOld)) {
                Horario oldHorarioOfOferta = ofertaNew.getHorario();
                if (oldHorarioOfOferta != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Oferta " + ofertaNew + " already has an item of type Horario whose oferta column cannot be null. Please make another selection for the oferta field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (ofertaNew != null) {
                ofertaNew = em.getReference(ofertaNew.getClass(), ofertaNew.getOfertaPK());
                horario.setOferta(ofertaNew);
            }
            horario = em.merge(horario);
            if (ofertaOld != null && !ofertaOld.equals(ofertaNew)) {
                ofertaOld.setHorario(null);
                ofertaOld = em.merge(ofertaOld);
            }
            if (ofertaNew != null && !ofertaNew.equals(ofertaOld)) {
                ofertaNew.setHorario(horario);
                ofertaNew = em.merge(ofertaNew);
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
                HorarioPK id = horario.getHorarioPK();
                if (findHorario(id) == null) {
                    throw new NonexistentEntityException("The horario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(HorarioPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Horario horario;
            try {
                horario = em.getReference(Horario.class, id);
                horario.getHorarioPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The horario with id " + id + " no longer exists.", enfe);
            }
            Oferta oferta = horario.getOferta();
            if (oferta != null) {
                oferta.setHorario(null);
                oferta = em.merge(oferta);
            }
            em.remove(horario);
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

    public List<Horario> findHorarioEntities() {
        return findHorarioEntities(true, -1, -1);
    }

    public List<Horario> findHorarioEntities(int maxResults, int firstResult) {
        return findHorarioEntities(false, maxResults, firstResult);
    }

    private List<Horario> findHorarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Horario.class));
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

    public Horario findHorario(HorarioPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Horario.class, id);
        } finally {
            em.close();
        }
    }

    public int getHorarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Horario> rt = cq.from(Horario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
