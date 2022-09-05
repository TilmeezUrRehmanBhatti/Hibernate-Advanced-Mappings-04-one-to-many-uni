package com.tilmeez.hibernate.demo;

import com.tilmeez.hibernate.demo.entity.Course;
import com.tilmeez.hibernate.demo.entity.Instructor;
import com.tilmeez.hibernate.demo.entity.InstructorDetail;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class CreateInstructorDemo {

    public static void main(String[] args) {

        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Instructor.class)
                .addAnnotatedClass(InstructorDetail.class)
                .addAnnotatedClass(Course.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {

            // create the object
            Instructor tempInstructor =
                    new Instructor("Chad", "Darby", "darby@gmail.com");

            InstructorDetail tempInstructorDetail =
                    new InstructorDetail("https://www.youtube.com/abcd", "Coding");

            // associate the objects
            tempInstructor.setInstructorDetail(tempInstructorDetail);

            // start a transaction
            session.beginTransaction();

            // save the instruction
            //
            // Note: this will also save the details objects
            // because of CascadeType.All
            //
            System.out.println("Saving Instruction: " + tempInstructor);
            session.save(tempInstructor);

            // commit transaction
            session.getTransaction().commit();

            System.out.println("Done!");

        }finally {
            // add clean uo code
            session.close();

            factory.close();
        }
    }
}
