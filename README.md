# Improve-Your-Knowledge v1.0

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
- [x] Podgląd listy zarejestrowanych użytkowników. ( ID oraz username)
- [x] Budowa struktury projektu (MVC) oraz wyodrębnienie powtarzającego się kodu Thymeleaf do fragmentów w celu poprawy jakości kodu.

### v0.2
- [x] Logowanie użytkowników z walidacją poprawności danych. Część treści dostępna jest tylko dla zalogowanych użytkowników posiadających role "USER".
- [x] Utworzenie bazy danych MySQL oraz 3 tabel ( user, role, user_role ), a następnie podłączenie aplikacji do bazy. Nowi użytkownicy zapisywani są do zewnętrznej bazy danych wraz z informacją o dacie rejestracji oraz aktywności. 
- [x] Refactor kodu - zmianna nazw kilkunastu zmiennych na bardziej odpowiednie oraz wyodrębnienie powtarzającego się kodu do oddzielnych metod. Dodatkowo staram się tworzyć oraz modyfikować kod według zasad SOLID, KISS oraz DRY.
- [x] Wypisywanie listy zarejestrowanych użytkowników umieściłem w czytelnej tabeli, oraz dodałem informację o dacie rejestracji ( LocalDate ) aktywności oraz roli użytkownika.

### v1.0
( W pełni działająca podstawowa wersja aplikacji )
- [x] Profil użytkownika - wyświetla podstawowe dane takie jak: nazwa zalogowanego użytkownika, data rejestracji, liczba punktów, pozycja w rankingu. Dodatkowo z pozycji profilu użytkownika istnieje możliwość przejścia do poszczególnych testów.
- [x] Możliwość usunięcia konta użytkownika w profilu po poprawnej weryfikacji hasła.
- [x] Stworzono Quizy - główną funkcjonalność aplikacji. Użytkownik ma możliwość wyboru testów z kilku różnych kategorii. Pytania wraz z odpowiedziami oraz liczbą punktów zdobytą przez każdego użytkownika w danym teście przechowywana jest w plikach tekstowych. Aplikacja korzysta z nich przy pomocy ```BufferedReader``` oraz ```BufferedWriter```. W celu optymalizacji, nowy użytkownik po zarejestrowaniu nie jest zapisywany do pliku z quizami. Następuje to dopiero w momencie kiedy użytkownik odwiedzi test pierwszy raz.
- [x] Quizy wbrew pierwotnemu założeniu wyświetlają się po 1 pytaniu - po wciśnięciu przycisku 'next' wyświetla się kolejne pytanie.
- [x] Nowy sposób weryfikacji czy użytkownik jest zalogowany - zajmuje się tym thymeleaf przy pomocy ```sec:authorize``` ( Nowa zależność w pom.xml)
- [x] Ranking - wyświetla 10 użytkowników posiadających największą sumę punktów ze wszystkich testów.
- [x] Tabele w aplikacji zostały zmodernizowane przy pomocy klas bootstrapa.
- [x] Zakładka kontakt.
- [x] Wygląd testów został zmodernizowany przy pomocy - [LINK](https://bootsnipp.com/snippets/Zk2Pz)


## Galeria aplikacji 
Poniższe zdjęcia prezentują częściowo funkcjonalność aplikacji.
<details>
<summary>Kliknij aby wyświetlić zdjęcia</summary>

### Strona główna
![](/project-execution-images/1.png)
### Rejestracja
![](/project-execution-images/2.png)
### Nieudana rejestracja
![](/project-execution-images/3.png)
### Pomyślna rejestracja
![](/project-execution-images/4.png)
### Logowanie
![](/project-execution-images/5.png)
### Ranking
![](/project-execution-images/6.png)
### Lista zarejestrowanych użytkowników
![](/project-execution-images/7.png)
### Profil użytkownika
![](/project-execution-images/8.png)
### Kategorie testów
![](/project-execution-images/9.png)
### Przykład testu
![](/project-execution-images/10.png)
### Wynik testu
![](/project-execution-images/11.png)

</details>

## Składnia SQL :
<details>
<summary>Kliknij aby zobaczyć kod SQL</summary>
  
~~~mysql
CREATE DATABASE ImproveYourKnowledge

CREATE USER 'lukaspar'@'localhost' IDENTIFIED BY 'moje_haslo';

GRANT ALL PRIVILEGES ON employeeSkillTestsDatabase.* TO 'lukaspar'@'localhost'

ALTER USER 'lukaspar'@'localhost' IDENTIFIED WITH mysql_native_password BY 'moje_haslo';

'CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `active` int(11) DEFAULT NULL,
  `date_of_registration` date NOT NULL,
  `score` int(11) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci'

'CREATE TABLE `role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci'

'CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `user_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `user_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci'

INSERT INTO role VALUES (1,'ADMIN');
INSERT INTO role VALUES (2,'USER');
~~~
</details>

## Plany na dalszy rozwój :
- [ ] Osiągnięcia.
- [ ] Rola Admina wraz z możliwością blokowania użytkowników.
- [ ] Czas na wykonanie każdego quizu.
- [ ] Utworzenie dokumentacji kodu.
