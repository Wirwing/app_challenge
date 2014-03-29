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
public class OfertaPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "Asignaturas_id")
    private int asignaturasid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Profesor")
    private int profesor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    @Temporal(TemporalType.DATE)
    private Date periodo;

    public OfertaPK() {
    }

    public OfertaPK(int asignaturasid, int profesor, Date periodo) {
        this.asignaturasid = asignaturasid;
        this.profesor = profesor;
        this.periodo = periodo;
    }

    public int getAsignaturasid() {
        return asignaturasid;
    }

    public void setAsignaturasid(int asignaturasid) {
        this.asignaturasid = asignaturasid;
    }

    public int getProfesor() {
        return profesor;
    }

    public void setProfesor(int profesor) {
        this.profesor = profesor;
    }

    public Date getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Date periodo) {
        this.periodo = periodo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) asignaturasid;
        hash += (int) profesor;
        hash += (periodo != null ? periodo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OfertaPK)) {
            return false;
        }
        OfertaPK other = (OfertaPK) object;
        if (this.asignaturasid != other.asignaturasid) {
            return false;
        }
        if (this.profesor != other.profesor) {
            return false;
        }
        if ((this.periodo == null && other.periodo != null) || (this.periodo != null && !this.periodo.equals(other.periodo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.appchallenge.app.entities.OfertaPK[ asignaturasid=" + asignaturasid + ", profesor=" + profesor + ", periodo=" + periodo + " ]";
    }
    
}
