/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appchallenge.app.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Xiumeteo
 */
@Embeddable
public class HorarioPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "Oferta_Asignaturas_id")
    private int ofertaAsignaturasid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Oferta_Profesor")
    private int ofertaProfesor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Oferta_periodo")
    @Temporal(TemporalType.DATE)
    private Date ofertaperiodo;

    public HorarioPK() {
    }

    public HorarioPK(int ofertaAsignaturasid, int ofertaProfesor, Date ofertaperiodo) {
        this.ofertaAsignaturasid = ofertaAsignaturasid;
        this.ofertaProfesor = ofertaProfesor;
        this.ofertaperiodo = ofertaperiodo;
    }

    public int getOfertaAsignaturasid() {
        return ofertaAsignaturasid;
    }

    public void setOfertaAsignaturasid(int ofertaAsignaturasid) {
        this.ofertaAsignaturasid = ofertaAsignaturasid;
    }

    public int getOfertaProfesor() {
        return ofertaProfesor;
    }

    public void setOfertaProfesor(int ofertaProfesor) {
        this.ofertaProfesor = ofertaProfesor;
    }

    public Date getOfertaperiodo() {
        return ofertaperiodo;
    }

    public void setOfertaperiodo(Date ofertaperiodo) {
        this.ofertaperiodo = ofertaperiodo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ofertaAsignaturasid;
        hash += (int) ofertaProfesor;
        hash += (ofertaperiodo != null ? ofertaperiodo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HorarioPK)) {
            return false;
        }
        HorarioPK other = (HorarioPK) object;
        if (this.ofertaAsignaturasid != other.ofertaAsignaturasid) {
            return false;
        }
        if (this.ofertaProfesor != other.ofertaProfesor) {
            return false;
        }
        if ((this.ofertaperiodo == null && other.ofertaperiodo != null) || (this.ofertaperiodo != null && !this.ofertaperiodo.equals(other.ofertaperiodo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.appchallenge.app.entities.HorarioPK[ ofertaAsignaturasid=" + ofertaAsignaturasid + ", ofertaProfesor=" + ofertaProfesor + ", ofertaperiodo=" + ofertaperiodo + " ]";
    }
    
}
