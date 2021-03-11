package org.esp.homelab.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A HomelabUser.
 */
@Entity
@Table(name = "homelab_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HomelabUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull
    @Size(min = 13, max = 13)
    @Pattern(regexp = "[0-9]")
    @Column(name = "num_cni", length = 13, nullable = false, unique = true)
    private String numCNI;

    @NotNull
    @Column(name = "date_de_naissance", nullable = false)
    private LocalDate dateDeNaissance;

    @NotNull
    @Column(name = "address_line_1", nullable = false)
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "pays", nullable = false)
    private String pays;

    @NotNull
    @Size(min = 9, max = 13)
    @Column(name = "phone", length = 13, nullable = false, unique = true)
    private String phone;

    @OneToOne(optional = false)
    @NotNull
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumCNI() {
        return numCNI;
    }

    public HomelabUser numCNI(String numCNI) {
        this.numCNI = numCNI;
        return this;
    }

    public void setNumCNI(String numCNI) {
        this.numCNI = numCNI;
    }

    public LocalDate getDateDeNaissance() {
        return dateDeNaissance;
    }

    public HomelabUser dateDeNaissance(LocalDate dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
        return this;
    }

    public void setDateDeNaissance(LocalDate dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public HomelabUser addressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
        return this;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public HomelabUser addressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
        return this;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public HomelabUser city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPays() {
        return pays;
    }

    public HomelabUser pays(String pays) {
        this.pays = pays;
        return this;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getPhone() {
        return phone;
    }

    public HomelabUser phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public HomelabUser user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HomelabUser)) {
            return false;
        }
        return id != null && id.equals(((HomelabUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HomelabUser{" +
            "id=" + getId() +
            ", numCNI='" + getNumCNI() + "'" +
            ", dateDeNaissance='" + getDateDeNaissance() + "'" +
            ", addressLine1='" + getAddressLine1() + "'" +
            ", addressLine2='" + getAddressLine2() + "'" +
            ", city='" + getCity() + "'" +
            ", pays='" + getPays() + "'" +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}
