package sample.server.application;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
@Entity 
public class User {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Integer id;

  private String firstName;

  private String lastName;

  @NotBlank(message = "Email is mandatory")
  @Email(message = "Email should be valid")
  private String email;

  @NotBlank(message = "Phone number is mandatory")
  @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
  private String phoneNo;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPhoneNo() {
    return phoneNo;
  }

  public void setPhoneNo(String phoneNo) {
    this.phoneNo = phoneNo;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

}