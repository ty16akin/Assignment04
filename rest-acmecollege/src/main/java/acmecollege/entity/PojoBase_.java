package acmecollege.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-08-10T21:41:03.813-0400")
@StaticMetamodel(PojoBase.class)
public class PojoBase_ {
	public static volatile SingularAttribute<PojoBase, Integer> id;
	public static volatile SingularAttribute<PojoBase, Integer> version;
	public static volatile SingularAttribute<PojoBase, LocalDateTime> created;
	public static volatile SingularAttribute<PojoBase, LocalDateTime> updated;
}
