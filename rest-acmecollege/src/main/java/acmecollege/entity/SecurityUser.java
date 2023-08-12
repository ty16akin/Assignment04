/**
 * File:  SecurityUser.java Course materials (23S) CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 * 
 * Updated by:  Group 11
 *   041004792, Mohammed, Alshaikhahmad (as from ACSIS)
 *   041043679, Ryan, Wang (as from ACSIS)
 *   040982118, Taiwo, Akinlabi (as from ACSIS)
 *   
 */
package acmecollege.entity;

import acmecollege.rest.serializer.SecurityRoleSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.security.Principal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static acmecollege.entity.SecurityUser.*;

@SuppressWarnings("unused")

/**
 * User class used for (JSR-375) Java EE Security authorization/authentication
 */

//TODO - Make this into JPA entity and add all the necessary annotations
@Entity
@Table(name = "security_user")
@Access(AccessType.FIELD)
@NamedQueries(value = {@NamedQuery(name = USER_FOR_OWNING_STUDENT_QUERY, query = "SELECT u FROM SecurityUser u left JOIN FETCH u.student left JOIN FETCH u.roles WHERE u.student.id = :param1"),
        @NamedQuery(name = SECURITY_USER_BY_NAME_QUERY, query = "SELECT u FROM SecurityUser u left JOIN FETCH u.student left JOIN FETCH u.roles WHERE u.username = :param1")})

public class SecurityUser implements Serializable, Principal {
    /** Explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    protected int id;
    
    @Basic(optional = false)
    @Column(name = "username", nullable = false)
    protected String username;
    
    @Basic(optional = false)
    @Column(name = "password_hash", nullable = false)
    protected String pwHash;
    
    @OneToOne(optional = true)
    @JoinColumn(name="student_id", referencedColumnName="id")
    protected Student student;
    
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name = "user_has_role", joinColumns = @JoinColumn(referencedColumnName = "user_id", name = "user_id"),
            inverseJoinColumns = @JoinColumn(referencedColumnName = "role_id", name = "role_id"))
    
    protected Set<SecurityRole> roles = new HashSet<SecurityRole>();
    public static final String USER_FOR_OWNING_STUDENT_QUERY = "SecurityUser.userForOwningStudent";
    public static final String SECURITY_USER_BY_NAME_QUERY = "SecurityUser.userByName";

    public SecurityUser() {
        super();
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwHash() {
        return pwHash;
    }
    
    public void setPwHash(String pwHash) {
        this.pwHash = pwHash;
    }

    // TODO SU01 - Setup custom JSON serializer
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = SecurityRoleSerializer.class)
    public Set<SecurityRole> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<SecurityRole> roles) {
        this.roles = roles;
    }

    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }

    // Principal
    @Override
    public String getName() {
        return getUsername();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        // Only include member variables that really contribute to an object's identity
        // i.e. if variables like version/updated/name/etc. change throughout an object's lifecycle,
        // they shouldn't be part of the hashCode calculation
        return prime * result + Objects.hash(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof SecurityUser otherSecurityUser) {
            // See comment (above) in hashCode():  Compare using only member variables that are
            // truly part of an object's identity
            return Objects.equals(this.getId(), otherSecurityUser.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SecurityUser [id = ").append(id).append(", username = ").append(username).append("]");
        return builder.toString();
    }
    
}
