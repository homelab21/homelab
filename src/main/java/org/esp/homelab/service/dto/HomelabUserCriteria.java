package org.esp.homelab.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link org.esp.homelab.domain.HomelabUser} entity. This class is used
 * in {@link org.esp.homelab.web.rest.HomelabUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /homelab-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HomelabUserCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numCNI;

    private LocalDateFilter dateDeNaissance;

    private StringFilter addressLine1;

    private StringFilter addressLine2;

    private StringFilter city;

    private StringFilter pays;

    private StringFilter phone;

    private LongFilter userId;

    public HomelabUserCriteria() {}

    public HomelabUserCriteria(HomelabUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.numCNI = other.numCNI == null ? null : other.numCNI.copy();
        this.dateDeNaissance = other.dateDeNaissance == null ? null : other.dateDeNaissance.copy();
        this.addressLine1 = other.addressLine1 == null ? null : other.addressLine1.copy();
        this.addressLine2 = other.addressLine2 == null ? null : other.addressLine2.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.pays = other.pays == null ? null : other.pays.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public HomelabUserCriteria copy() {
        return new HomelabUserCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNumCNI() {
        return numCNI;
    }

    public void setNumCNI(StringFilter numCNI) {
        this.numCNI = numCNI;
    }

    public LocalDateFilter getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(LocalDateFilter dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public StringFilter getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(StringFilter addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public StringFilter getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(StringFilter addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getPays() {
        return pays;
    }

    public void setPays(StringFilter pays) {
        this.pays = pays;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HomelabUserCriteria that = (HomelabUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numCNI, that.numCNI) &&
            Objects.equals(dateDeNaissance, that.dateDeNaissance) &&
            Objects.equals(addressLine1, that.addressLine1) &&
            Objects.equals(addressLine2, that.addressLine2) &&
            Objects.equals(city, that.city) &&
            Objects.equals(pays, that.pays) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numCNI, dateDeNaissance, addressLine1, addressLine2, city, pays, phone, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HomelabUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (numCNI != null ? "numCNI=" + numCNI + ", " : "") +
                (dateDeNaissance != null ? "dateDeNaissance=" + dateDeNaissance + ", " : "") +
                (addressLine1 != null ? "addressLine1=" + addressLine1 + ", " : "") +
                (addressLine2 != null ? "addressLine2=" + addressLine2 + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (pays != null ? "pays=" + pays + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
