# OneToMany - Unidirectional
**One-To-Many Mapping**
+ A course can have many reviews
    + Uni -directional

<img src="https://user-images.githubusercontent.com/80107049/188494205-ddfe13fd-2edc-4374-9d7b-f235f1a31544.png" width= 300/>



**Real-World Project Requirement**
+ If we delete a course, also delete the reviews
+ Reviews without a course ... have no meaning

<img src="https://user-images.githubusercontent.com/80107049/188494247-ad049c27-a6f9-46a9-b666-5195df5d74bb.png" width= 300/>


**@OneToMany**

<img src="https://user-images.githubusercontent.com/80107049/188494301-72eb37fb-9355-47a6-9075-344db32da26f.png" width = 300 />



<img src="https://user-images.githubusercontent.com/80107049/188494372-449febdb-fcde-4078-affe-34c03c903c1d.png" width=400 />



**Development Process: One-To-Many**
1. Pre Work - Define database tables
2. Create **Review** class
3. Update **Course** class
4. Create Main App


_Step 1:Define database table_

**table:review**
```POSTGRESQL
CREATE TABLE review
(
    id        BIGSERIAL NOT NULL,
    comment   VARCHAR(256) DEFAULT NULL,
    course_id BIGINT       DEFAULT NULL,
    CONSTRAINT FK_COURSE
        FOREIGN KEY (course_id)
            REFERENCES course (id)
);
```

_Step 2:Create Review class_
```JAVA
@Entity
@Table(name="review")
public class Review{
  
  @Id
  @GeneratedVaue(strategy=GenerationType.IDENTITY)
  @Column(name="id")
  private int id;
  
  @Column(name"comment")
  private String comment;
  
 ... 
  // constructor , getters/setters
}
```

_Step 3:Update Course - reference review_
```JAVA
@Enitiy
@Table(name="course")
public class Course {
  ...
    
  private List<Review> reviews;
  
  // getter / setters
 ... 
}
```

**Add @OneToMany annotation**
```JAVA
@Enitiy
@Table(name="course")
public class Course {
  ...
  @OneToMany
  @joinColumn(name="course_id")
  private List<Review> reviews;
  
  // getter / setters
 ... 
}
```

+ `@joinColumn(name="course_id")` Refers to "course_id" column in "review" table
+ In this, @JoinColumn tells Hibernate
    + Look at the course_id column in the **review** table
    + Use this information to help fond associated reviews for a course


**Add support for Cascading**
```JAVA
@Enitiy
@Table(name="course")
public class Course {
  ...
  @OneToMany(cascade=CascadeType.All)
  @joinColumn(name="course_id")
  private List<Review> reviews;
  
  // getter / setters
 ... 
}
```
+ `cascade=CascadeType.All` Cascade all operations including deletes!

**Add support for Lazy loading**
```JAVA
@Enitiy
@Table(name="course")
public class Course {
  ...
  @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.All)
  @joinColumn(name="course_id")
  private List<Review> reviews;

 ... 
}
```
+ `fetch=FetchType.LAZY` Lazy load the reviews

**Add convenience method for adding review**
```JAVA
@Entity
@Table(name="course")
public class Course {
  ...
  // add convenience methods for adding reviews
  public void add(Review tempReview) {
    
    if (reviews == null) {
      reviews = new ArrayList<>();
    }
    
    reviews.add(tempReview);
  }
  
}
```

_Step 4:Create Main App_
```JAVA
public static void main(String[] args) {
  ...
  // gte the course object
  int theID = 1;
  Course tempCourse = session.get(Course.class, theId);
  
  // print the course 
  System.out.println("tempCourse: " + tempCourse);
  
  // print the associated reviews
  System.out.println("reviews: " + tempCourse.getReviews());
 ... 
}
```
+ `tempCourse.getReviews()` Lazy loading the reviews


**@JoinColum ... where does it find the column?**

**Question**

In the Course class,we have OneToMany relation with reviews with join column course_id.

But in course table we do not have column course_id.

Ideally when we say @JoinColumn a new column should be created in course table ... isn't it?

How does @JoinColum know where to find the join column?

\---


**Answer**

The JoinColumn is actually fairly complex and it goes through a number of advanced steps to find the desired column.

This info below is from the documentation

Source: http\://docs.oracle.com/javaee/7/api/javax/persistence/JoinColumn.html#name--

\---

The table in which it is found depends upon the context.

\- If the join is for a OneToOne or ManyToOne mapping using a foreign key mapping strategy, the foreign key column is in the table of the source entity or embeddable.

\- If the join is for a unidirectional OneToMany mapping using a foreign key mapping strategy, the foreign key is in the table of the target entity.

\- If the join is for a ManyToMany mapping or for a OneToOne or bidirectional ManyToOne/OneToMany mapping using a join table, the foreign key is in a join table.

\- If the join is for an element collection, the foreign key is in a collection table.

\--

So as you can see, it depends on the context.

In our training , we are using @OneToMany uni-directional (course has one-to-many reviews).

As a result, the join column / foreign key column is in the target entity. In this case, the target entity is the Review class. So, you will find the join column "course_id" in the "review" table.
