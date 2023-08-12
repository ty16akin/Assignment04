/**
 * File:  SecurityRole.java Course materials (23S) CST 8277
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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@SuppressWarnings("unused")

/**
 * Role class used for (JSR-375) Java EE Security authorization/authentication
 */
//TODO SR01 - Make this into JPA entity and add all necessary annotations
@Entity
@Table(name = "security_role")
@NamedQuery(name = SecurityRole.FIND_USER_ROLE, query = "SELECT sr FROM SecurityRole sr where sr.roleName = :param1")
@NamedQuery(name = SecurityRole.FIND_STUDENT_WITH_ROLE, query = "SELECT u FROM SecurityUser u left JOIN FETCH u.student left JOIN FETCH u.roles WHERE u.student.id = :param1")
public class SecurityRole implements Serializable {
    /** Explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    public static final String FIND_USER_ROLE = "SecurityRole.findAll";
    public static final String FIND_STUDENT_WITH_ROLE = "SecurityUser.find";
    
    @Id
    @Column(name = "role_id")
    protected int id;
    
    protected String roleName;
    
    protected Set<SecurityUser> users = new HashSet<SecurityUser>();

    public SecurityRole() {
        super();
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<SecurityUser> getUsers() {
        return users;
    }
    public void setUsers(Set<SecurityUser> users) {
        this.users = users;
    }

    public void addUserToRole(SecurityUser user) {
        getUsers().add(user);
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
        if (obj instanceof SecurityRole otherSecurityRole) {
            // See comment (above) in hashCode():  Compare using only member variables that are
            // truly part of an object's identity
            return Objects.equals(this.getId(), otherSecurityRole.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SecurityRole [id = ").append(id).append(", ");
        if (roleName != null)
            builder.append("roleName = ").append(roleName);
        builder.append("]");
        return builder.toString();
    }
}
