# Improve-Your-Java-Knowledge v1.0

## Wykorzystywane technologie:
- Spring Boot
- Spring MVC
- Spring Data
- Spring Security
- Thymeleaf
- Bootstrap
- MySQL

## Historia zmian :

### v0.1
- [x] Rejestracja użytkowników z pelną walidacją poprawności danych oraz zapis użytkowników do bazy danych h2 w trybie memory jako rozwiązanie tymczasowe. ( [H2 Maven](https://mvnrepository.com/artifact/com.h2database/h2/1.4.197) )
- [x] Możliwość wypisania listy zarejestrowanych użytkowników. ( ID oraz username)
- [x] Budowa struktury projektu oraz wyodrębnienie powtarzającego się kodu Thymeleaf do fragmentów w celu poprawy jakości kodu.

### v0.2
- [x] Logowanie użytkowników z walidacją poprawności danych. Część treści przeznaczone jest tylko dla zarejestrowanych użytkowników posiadających role "USER" lub "ADMIN".
- [x] Utworzenie bazy danych MySQL oraz 3 tabel ( user, role, user_role ), a następnie podłączenie aplikacji do bazy. Nowi użytkownicy zapisywani są do zewnętrznej bazy danych wraz z informacją o dacie rejestracji oraz aktywności. 
- [x] Poprawki na rzecz czystości kodu - zmianna nazw kilkunastu zmiennych na bardziej odpowiednie oraz wyodrębnienie powtarzającego się kodu to oddzielnych metod. Dodatkowo staram się tworzyć oraz modyfikować kod według zasad SOLID, KISS oraz DRY.
- [x] Wypisywanie listy zarejestrowanych użytkowników umieściłem w czytelnej tabeli, oraz dodałem informację o dacie rejestracji ( LocalDate ), aktywności oraz roli użytkownika.

### v1.0 ( 10.09.2018 - wersja 1.0 w pełni działającej aplikacji )

<details>
<summary>Kliknij aby zobaczyć kod SQL</summary>
  
~~~mysql
CREATE USER 'lukaspar'@'localhost' IDENTIFIED BY '665650';

GRANT ALL PRIVILEGES ON employeeSkillTestsDatabase.* TO 'lukaspar'@'localhost' IDENTIFIED BY '665650';

CREATE TABLE user (
  user_id int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  active int(11) DEFAULT NULL
);

CREATE TABLE role (
  role_id int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  role varchar(255) DEFAULT NULL
);

CREATE TABLE user_role (
  user_id int(11) NOT NULL,
  role_id int(11) NOT NULL,
  PRIMARY KEY (user_id, role_id),
  KEY role_id (role_id),
  FOREIGN KEY (user_id) REFERENCES user (user_id),
  FOREIGN KEY (role_id) REFERENCES role (role_id)
);

INSERT INTO role VALUES (1,'ADMIN');
INSERT INTO role VALUES (2,'USER');
~~~
</details>