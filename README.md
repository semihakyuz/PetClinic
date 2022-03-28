# PetClinic

- HTML, CSS, Javascript
- Thymeleaf
- JPA/Hibernate
- Spring Boot
- Spring Security
- MySQL

teknolojileri kullanılarak oluşturulmuş, veteriner klinik yönetim uygulaması.

### Kurulum

##### MySQL ortamı hazırlanır.
```
spring.datasource.url=jdbc:mysql://localhost:3306/petclinicdb
spring.datasource.username=root
spring.datasource.password=root123
```

##### Projenin ana dizininde aşağıdaki komut çalıştırılır.
```
mvn clean install
```
##### Build işleminden sonra projenin /target dizininde aşağıdaki kod çalıştırılır.
```
java -jar petclinic-1.0-SNAPSHOT.jar
```

##### Proje ön tanımlı olarak localhost:8080 adresinde yayınlanır.
### Sistem hakkında
- Yetkilendirme
  - Sistem rol tabanlı yetkilendirme ile çalışır.
  - Üye kaydı admin rolündeki kullanıcı tarafından yapılır ve sisteme sadece klinik personeli girebilir. Kullanıcılar kendi kullanıcı adlarını ve parolalarını değiştirebilirken, admin rolündeki kullanıcı tüm kullanıcıların bilgilerini değiştirebilir ve üyeliğini silebilir.
  - user rolündeki kullanıcılar hasta kaydı ve muayenesi tamamlanan hayvanların çıkış işlemlerini yapabilir. Sistem içerisindeki muayene durumuna,muayene geçmişine,hayvan sahibi bilgilerine ve hayvan bilgilerine ulaşabilir.
  - Hayvan sahiplerinin ve hayvanların bilgilerinde düzenleme,silme işlemlerini sadece admin rolündeki kullanıcı yapabilir.
- Genel Kullanım
  - Sistem çalıştığında Kullanıcı adı : admin , parola : 123 hesap kaydı oluşturulur. Sistemden oluşturulan kayıtlar user rolünde kayıt edilir.
  - Çıkış işlemi yapılmış kayıtlar sadece aynı gün içerisindeki işlemleri gösterir. Tüm kayıtlara tablo üzerindeki ilgili linkten ulaşılabilir.
