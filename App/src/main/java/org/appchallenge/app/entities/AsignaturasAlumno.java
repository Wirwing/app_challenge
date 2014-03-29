/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appchallenge.app.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Xiumeteo
 */
@Entity
@Table(name = "asignaturas_alumno")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AsignaturasAlumno.findAll", query = "SELECT a FROM AsignaturasAlumno a"),
    @NamedQuery(name = "AsignaturasAlumno.findBySituacion", query = "SELECT a FROM AsignaturasAlumno a WHERE a.situacion = :situacion"),
    @NamedQuery(name = "AsignaturasAlumno.findByTipo", query = "SELECT a FROM AsignaturasAlumno a WHERE a.tipo = :tipo"),
    @NamedQuery(name = "AsignaturasAlumno.findByAsignaturasid", query = "SELECT a FROM AsignaturasAlumno a WHERE a.asignaturasAlumnoPK.asignaturasid = :asignaturasid"),
    @NamedQuery(name = "AsignaturasAlumno.findByAlumnoid", query = "SELECT a FROM AsignaturasAlumno a WHERE a.asignaturasAlumnoPK.alumnoid = :alumnoid"),
    @NamedQuery(name = "AsignaturasAlumno.findByPeriodo", query = "SELECT a FROM AsignaturasAlumno a WHERE a.asignaturasAlumnoPK.periodo = :periodo")})
public class AsignaturasAlumno implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AsignaturasAlumnoPK asignaturasAlumnoPK;
    @Column(name = "situacion")
    private Integer situacion;
    @Column(name = "tipo")
    private Integer tipo;
    @JoinColumn(name = "Alumno_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Alumno alumno;
    @JoinColumn(name = "Asignaturas_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Asignaturas asignaturas;

    public AsignaturasAlumno() {
    }

    public AsignaturasAlumno(AsignaturasAlumnoPK asignaturasAlumnoPK) {
        this.asignaturasAlumnoPK = asignaturasAlumnoPK;
    }

    public AsignaturasAlumno(int asignaturasid, int alumnoid, Date periodo) {
        this.asignaturasAlumnoPK = new AsignaturasAlumnoPK(asignaturasid, alumnoid, periodo);
    }

    public AsignaturasAlumnoPK getAsignaturasAlumnoPK() {
        return asignaturasAlumnoPK;
    }

    public void setAsignaturasAlumnoPK(AsignaturasAlumnoPK asignaturasAlumnoPK) {
        this.asignaturasAlumnoPK = asignaturasAlumnoPK;
    }

    public Integer getSituacion() {
        return situacion;
    }

    public void setSituacion(Integer situacion) {
        this.situacion = situacion;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
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
        hash += (asignaturasAlumnoPK != null ? asignaturasAlumnoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsignaturasAlumno)) {
            return false;
        }
        AsignaturasAlumno other = (AsignaturasAlumno) object;
        if ((this.asignaturasAlumnoPK == null && other.asignaturasAlumnoPK != null) || (this.asignaturasAlumnoPK != null && !this.asignaturasAlumnoPK.equals(other.asignaturasAlumnoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.appchallenge.app.entities.AsignaturasAlumno[ asignaturasAlumnoPK=" + asignaturasAlumnoPK + " ]";
    }
    
}
