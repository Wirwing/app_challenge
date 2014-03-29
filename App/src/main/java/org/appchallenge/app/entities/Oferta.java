/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appchallenge.app.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Xiumeteo
 */
@Entity
@Table(name = "oferta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Oferta.findAll", query = "SELECT o FROM Oferta o"),
    @NamedQuery(name = "Oferta.findByAsignaturasid", query = "SELECT o FROM Oferta o WHERE o.ofertaPK.asignaturasid = :asignaturasid"),
    @NamedQuery(name = "Oferta.findByProfesor", query = "SELECT o FROM Oferta o WHERE o.ofertaPK.profesor = :profesor"),
    @NamedQuery(name = "Oferta.findByPeriodo", query = "SELECT o FROM Oferta o WHERE o.ofertaPK.periodo = :periodo")})
public class Oferta implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OfertaPK ofertaPK;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "oferta", fetch = FetchType.LAZY)
    private Horario horario;
    @JoinColumn(name = "Asignaturas_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Asignaturas asignaturas;

    public Oferta() {
    }

    public Oferta(OfertaPK ofertaPK) {
        this.ofertaPK = ofertaPK;
    }

    public Oferta(int asignaturasid, int profesor, Date periodo) {
        this.ofertaPK = new OfertaPK(asignaturasid, profesor, periodo);
    }

    public OfertaPK getOfertaPK() {
        return ofertaPK;
    }

    public void setOfertaPK(OfertaPK ofertaPK) {
        this.ofertaPK = ofertaPK;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public Asignaturas getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(Asignaturas asignaturas) {
        this.asignaturas = asignaturas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ofertaPK != null ? ofertaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Oferta)) {
            return false;
        }
        Oferta other = (Oferta) object;
        if ((this.ofertaPK == null && other.ofertaPK != null) || (this.ofertaPK != null && !this.ofertaPK.equals(other.ofertaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.appchallenge.app.entities.Oferta[ ofertaPK=" + ofertaPK + " ]";
    }
    
}
