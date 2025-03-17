# TP2:📂  HospitalAPP

##  💡  Description

Hospitalapp est une application de gestion hospitalière développée avec **Spring Boot**. Cette application permet de gérer les patients, les médecins, les rendez-vous, les services et les traitements au sein d'un hôpital. Elle utilise **Spring Data JPA** pour la gestion de la base de données et **MySQL** comme système de gestion de base de données relationnelle.

## 🚀 Fonctionnalités

- **Gestion des patients** : Ajouter, modifier, supprimer et consulter les informations des patients.
- **Gestion des médecins** : Ajouter, modifier, supprimer et consulter les informations des médecins.
- **Gestion des rendez-vous** : Organiser, modifier, annuler et consulter les rendez-vous entre patients et médecins.
- **Gestion des services** : Ajouter et gérer les services hospitaliers disponibles.

---
## 🛠️ Technologies Utilisées

- **Java** : Langage de programmation principal.
- **Spring Boot** : Framework pour la création de l'application web.
- **Spring Data JPA** : Gestion des accès à la base de données.
- **MySQL** : Système de gestion de base de données relationnelle.
- **Thymeleaf** : Template engine pour la partie front-end.
- **Bootstrap** : Pour le design.
- **Maven** : Gestion des dépendances et de la construction du projet.
- **Git** : Pour le contrôle de version du code source.
....

## 📢 Notes  

- 🚀 Ce projet est en cours de développement. Actuellement, seule la **gestion des patients** est implémentée.  
- 🔧 Les fonctionnalités pour la gestion des **médecins, rendez-vous et consultations** seront ajoutées prochainement.


## 📂 Structure du Projet

├───src
│   ├───main
│   │   ├───java
│   │   │   └───ma
│   │   │       └───enset
│   │   │           └───hospitalapp
│   │   │               │   HospitalappApplication.java
│   │   │               │
│   │   │               ├───entities
│   │   │               │       Patient.java
│   │   │               │
│   │   │               ├───repository
│   │   │               │       PatientRepository.java
│   │   │               │
│   │   │               └───web
│   │   │                       PatientController.java
│   │   │
│   │   └───resources
│   │       │   application.properties
│   │       │
│   │       ├───static
│   │       └───templates
│   │               editPatient.html
│   │               formPatients.html
│   │               patients.html
│   │               template1.html
│   │





## 📸 Exemples de Code

### 🖥️ **Class Patient**
```
package ma.enset.hospitalapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Entity

public class Patient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @NotEmpty
    @Size(min=4,max=40)
    private String nom ;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date dateNaissance;
    private boolean malade ;
    @DecimalMin(("100"))
    private int score ;

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public boolean isMalade() {
        return malade;
    }

    public int getScore() {
        return score;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setMalade(boolean malade) {
        this.malade = malade;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Patient(Long id, String nom, Date dateNaissance, boolean malade, int score) {
        this.id = id;
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        this.malade = malade;
        this.score = score;
    }

    public Patient() {
    }
}

```

### 🖥️ **Class PatientRepository**
```
package ma.enset.hospitalapp.repository;

import ma.enset.hospitalapp.entities.Patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Page<Patient> findByNomContains(String keyword, Pageable pageable);

    @Query("select p from Patient p where p.nom like %:x%")
    Page<Patient> chercher(@Param("x") String keyword, Pageable pageable);
}

```

### 🖥️ **Class PatientController**
```
package ma.enset.hospitalapp.web;

import jakarta.validation.Valid;
import ma.enset.hospitalapp.entities.Patient;
import ma.enset.hospitalapp.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PatientController {
    @Autowired
    private PatientRepository patientRepository;

    @GetMapping({"/index"})
    public String getpatient(Model model,
                             @RequestParam(name ="page",defaultValue = "0")int p,
                             @RequestParam(name = "size",defaultValue = "4")int s,
                             @RequestParam(name = "keyword",defaultValue = "")String kw) {
        Page<Patient> pagepatients;
        // Si le mot-clé est vide, récupérer tous les patients
        if (kw.isEmpty()) {
            pagepatients = patientRepository.findAll(PageRequest.of(p, s));
        } else {
            // Utiliser la méthode chercher pour rechercher par nom
            pagepatients = patientRepository.chercher(kw, PageRequest.of(p, s));
        }
        model.addAttribute("listpatients", pagepatients.getContent());
        model.addAttribute("pages",new int[pagepatients.getTotalPages()]);
        model.addAttribute("currentPage",p);
        model.addAttribute("keyword",kw);
        return "patients";
    }

    @GetMapping("/delete")
    public String eletepatient(@RequestParam Long id,
                                @RequestParam String keyword,
                                @RequestParam int page) {
        patientRepository.deleteById(id);
        return "redirect:/index?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/formPatients")
    public String formPatients(Model model){
        model.addAttribute("patient",new Patient());
        return "formPatients";
    }
    @PostMapping(path = "/save")
    public String save(Model model, @Valid Patient patient, BindingResult bindingResult){
        if(bindingResult.hasErrors()) return "formPatients";
        patientRepository.save(patient);
        return "redirect:/index";
    }

    @GetMapping("/editPatient")
    public String editPatients(Model model,Long id,String keyword,int page){
        Patient patient=patientRepository.findById(id).orElse(null);
        if (patient==null) throw new RuntimeException("patient introvable");
        model.addAttribute("patient", patient);
        model.addAttribute("page",page);
        model.addAttribute("keyword",keyword);

        return "editpatient";
    }


}

```

### 🖥️ **code patients.html**
```
<!doctype html>
<html lang="en" xmlns:th="http://www.w3.org/1999/xhtml"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      layout:decorate="template1"  >

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Clients</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<div layout:fragment="content1">
<div class="container my-3">
    <h1 class="text-center mb-3">Liste des patients</h1>
    <form method="get" th:action="@{/index}">
        <label>Mot-clé :</label>
        <input type="text" name="keyword" th:value="${keyword}">
        <button type="submit" class="btn btn-info">Chercher</button>
    </form>


    <a class="btn btn-primary" th:href="@{/formPatients}">Nouveau patient</a>


    <table class="table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Nom</th>
            <th>Date</th>
            <th>Malade</th>
            <th>Score</th>
            <th>Options</th>
        </tr>
        </thead>
        <tbody class="table-group-divide">
        <tr th:each="p:${listpatients}">
            <td th:text="${p.id}"></td>
            <td th:text="${p.nom}"></td>
            <td th:text="${p.dateNaissance}"></td>
            <td th:text="${p.malade}"></td>
            <td th:text="${p.score}"></td>
            <td style="white-space: nowrap">
                <a class="btn btn-success btn-sm" th:href="@{/editPatient(id=${p.id}, keyword=${keyword}, page=${currentPage})}">Modifier</a>
                <a class="btn btn-danger btn-sm" th:href="@{/delete(id=${p.id}, keyword=${keyword}, page=${currentPage})}" onclick="return confirm('Êtes-vous sûr ?')">Supprimer</a>
            </td>
        </tr>
        </tbody>
    </table>

    <ul class="nav nav-pills">
        <li th:each="page, status : ${pages}">
            <a th:href="@{/index(page=${status.index}, keyword=${keyword})}"
               th:class="${(currentPage == status.index) ? 'btn btn-info ms-1' : 'btn btn-outline-info ms-1'}"
               class="btn btn-outline-info" th:text="${status.index}"></a>
        </li>
    </ul>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</div>
</body>
</html>

```
## 💌 Auteur

👩‍💻 **Rim Aaloi**  
💌 Contact: [GitHub Profile](https://github.com/RimAaloi) | [LinkedIn](https://www.linkedin.com/in/rim-aaloi/)  

---

## 📚 Ressources

- **Spring Documentation** : [https://spring.io/docs](https://spring.io/docs)
- **MySQL Documentation** : [https://dev.mysql.com/doc/](https://dev.mysql.com/doc/)
- **Bootstrap Documentation** : [https://getbootstrap.com/docs](https://getbootstrap.com/docs/)

---
