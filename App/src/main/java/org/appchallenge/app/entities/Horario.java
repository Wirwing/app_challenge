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
import javax.persistence.JoinColumns;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Xiumeteo
 */
@Entity
@Table(name = "horario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Horario.findAll", query = "SELECT h FROM Horario h"),
    @NamedQuery(name = "Horario.findByOfertaAsignaturasid", query = "SELECT h FROM Horario h WHERE h.horarioPK.ofertaAsignaturasid = :ofertaAsignaturasid"),
    @NamedQuery(name = "Horario.findByOfertaProfesor", query = "SELECT h FROM Horario h WHERE h.horarioPK.ofertaProfesor = :ofertaProfesor"),
    @NamedQuery(name = "Horario.findByOfertaperiodo", query = "SELECT h FROM Horario h WHERE h.horarioPK.ofertaperiodo = :ofertaperiodo"),
    @NamedQuery(name = "Horario.findByDia", query = "SELECT h FROM Horario h WHERE h.dia = :dia"),
    @NamedQuery(name = "Horario.findByHoraInicial", query = "SELECT h FROM Horario h WHERE h.horaInicial = :horaInicial"),
    @NamedQuery(name = "Horario.findByHoraFinal", query = "SELECT h FROM Horario h WHERE h.horaFinal = :horaFinal")})
public class Horario implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HorarioPK horarioPK;
    @Column(name = "dia")
    private Integer dia;
    @Column(name = "hora_inicial")
    @Temporal(TemporalType.TIME)
    private Date horaInicial;
    @Column(name = "hora_final")
    @Temporal(TemporalType.TIME)
    private Date horaFinal;
    @JoinColumns({
        @JoinColumn(name = "Oferta_Asignaturas_id", referencedColumnName = "Asignaturas_id", insertable = false, updatable = false),
        @JoinColumn(name = "Oferta_Profesor", referencedColumnName = "Profesor", insertable = false, updatable = false),
        @JoinColumn(name = "Oferta_periodo", referencedColumnName = "periodo", insertable = false, updatable = false)})
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Oferta oferta;

    public Horario() {
    }

    public Horario(HorarioPK horarioPK) {
        this.horarioPK = horarioPK;
    }

    public Horario(int ofertaAsignaturasid, int ofertaProfesor, Date ofertaperiodo) {
        this.horarioPK = new HorarioPK(ofertaAsignaturasid, ofertaProfesor, ofertaperiodo);
    }

    public HorarioPK getHorarioPK() {
        return horarioPK;
    }

    public void setHorarioPK(HorarioPK horarioPK) {
        this.horarioPK = horarioPK;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public Date getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(Date horaInicial) {
        this.horaInicial = horaInicial;
    }

    public Date getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(Date horaFinal) {
        this.horaFinal = horaFinal;
    }

    public Oferta getOferta() {
        return oferta;
    }

    public void setOferta(Oferta oferta) {
        this.oferta = oferta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (horarioPK != null ? horarioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Horario)) {
            return false;
        }
        Horario other = (Horario) object;
        if ((this.horarioPK == null && other.horarioPK != null) || (this.horarioPK != null && !this.horarioPK.equals(other.horarioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.appchallenge.app.entities.Horario[ horarioPK=" + horarioPK + " ]";
    }
    
}
