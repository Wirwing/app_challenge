/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appchallenge.app.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Xiumeteo
 */
@Entity
@Table(name = "asignaturas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Asignaturas.findAll", query = "SELECT a FROM Asignaturas a"),
    @NamedQuery(name = "Asignaturas.findById", query = "SELECT a FROM Asignaturas a WHERE a.id = :id"),
    @NamedQuery(name = "Asignaturas.findByCreditos", query = "SELECT a FROM Asignaturas a WHERE a.creditos = :creditos")})
public class Asignaturas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "creditos")
    private Integer creditos;
    @JoinTable(name = "asignaturas_has_plan", joinColumns = {
        @JoinColumn(name = "Asignaturas_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "Plan_id", referencedColumnName = "id")})
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Plan> planCollection;
    @JoinTable(name = "dependencias", joinColumns = {
        @JoinColumn(name = "asignatura_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "requisito_id", referencedColumnName = "id")})
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Asignaturas> asignaturasCollection;
    @ManyToMany(mappedBy = "asignaturasCollection", fetch = FetchType.LAZY)
    private Collection<Asignaturas> asignaturasCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "asignaturas", fetch = FetchType.LAZY)
    private Collection<AsignaturasAlumno> asignaturasAlumnoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "asignaturas", fetch = FetchType.LAZY)
    private Collection<Oferta> ofertaCollection;

    public Asignaturas() {
    }

    public Asignaturas(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCreditos() {
        return creditos;
    }

    public void setCreditos(Integer creditos) {
        this.creditos = creditos;
    }

    @XmlTransient
    public Collection<Plan> getPlanCollection() {
        return planCollection;
    }

    public void setPlanCollection(Collection<Plan> planCollection) {
        this.planCollection = planCollection;
    }

    @XmlTransient
    public Collection<Asignaturas> getAsignaturasCollection() {
        return asignaturasCollection;
    }

    public void setAsignaturasCollection(Collection<Asignaturas> asignaturasCollection) {
        this.asignaturasCollection = asignaturasCollection;
    }

    @XmlTransient
    public Collection<Asignaturas> getAsignaturasCollection1() {
        return asignaturasCollection1;
    }

    public void setAsignaturasCollection1(Collection<Asignaturas> asignaturasCollection1) {
        this.asignaturasCollection1 = asignaturasCollection1;
    }

    @XmlTransient
    public Collection<AsignaturasAlumno> getAsignaturasAlumnoCollection() {
        return asignaturasAlumnoCollection;
    }

    public void setAsignaturasAlumnoCollection(Collection<AsignaturasAlumno> asignaturasAlumnoCollection) {
        this.asignaturasAlumnoCollection = asignaturasAlumnoCollection;
    }

    @XmlTransient
    public Collection<Oferta> getOfertaCollection() {
        return ofertaCollection;
    }

    public void setOfertaCollection(Collection<Oferta> ofertaCollection) {
        this.ofertaCollection = ofertaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Asignaturas)) {
            return false;
        }
        Asignaturas other = (Asignaturas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.appchallenge.app.entities.Asignaturas[ id=" + id + " ]";
    }
    
}
