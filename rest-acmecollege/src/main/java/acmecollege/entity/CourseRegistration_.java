/**
 * File:  SecurityUser.java Course materials (23S) CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 * 
 * Updated by:  Group 11
 *   040982118, Taiwo, Akinlabi (as from ACSIS)
 *   041043679, Ryan, WAng (as from ACSIS)
 *   041004792, Mohammad,  Alshaikhahmad (as from ACSIS)
 * 
 */
package acmecollege.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-07-20T20:04:45.488-0400")
@StaticMetamodel(CourseRegistration.class)
public class CourseRegistration_ {
	public static volatile SingularAttribute<CourseRegistration, CourseRegistrationPK> id;
	public static volatile SingularAttribute<CourseRegistration, Student> student;
	public static volatile SingularAttribute<CourseRegistration, Course> course;
	public static volatile SingularAttribute<CourseRegistration, Professor> professor;
	public static volatile SingularAttribute<CourseRegistration, Integer> numericGrade;
	public static volatile SingularAttribute<CourseRegistration, String> letterGrade;
}
