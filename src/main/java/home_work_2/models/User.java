package home_work_2.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String name;
    private String email;
    private int age;
    @Column(nullable = false)
    private LocalDateTime created_at;

    public User(){}

    public User(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    @Override
    public boolean equals(Object other){
        if(other == null || other.getClass() != User.class)
            return false;

        if(other == this)
            return true;

        User user = (User) other;
        return user.id.equals(this.id) &&
               user.name.equals(this.name) &&
               user.email.equals(this.email) &&
               user.age == this.age;
    }

    @Override
    public String toString(){
        return String.format("%-4d%-10s%-25s%-5d%s",
                id, name, email, age, created_at.toLocalDate());
    }
}
