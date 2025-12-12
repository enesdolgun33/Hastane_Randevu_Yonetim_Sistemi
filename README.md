# 🏥 MEDISIS - Hastane Randevu Yönetim Sistemi (v2.0 Enterprise)

MEDISIS, JavaFX ve SQLite kullanılarak geliştirilmiş, modern arayüze sahip, kapsamlı bir hastane otomasyon sistemidir. Proje, **Nesne Yönelimli Programlama (OOP)** prensiplerine ve **Yazılım Tasarım Desenlerine (Design Patterns)** tam uyumlu olarak, **MVC (Model-View-Controller)** mimarisiyle geliştirilmiştir.

## 🚀 Özellikler

### 👨‍⚕️ Doktor Modülü
* **Güvenli Giriş:** TC ve şifre ile yetkilendirilmiş giriş.
* **Randevu Yönetimi (Dashboard):** Bekleyen randevuları listeleme, filtreleme (tarih/durum).
* **İşlem Menüsü:** Randevuları "Tamamlandı" veya "Gelmedi" olarak işaretleme (State Deseni).
* **Hasta Detay Görüntüleme:** Randevuya çift tıklayarak hastanın detaylı künyesini (Geçmiş, İletişim, Kronik vb.) görme.
* **Mesai Planlama:** Haftalık çalışma günlerini ve saatlerini (09:00 - 17:00) dinamik olarak ayarlama.
* **Muayene Notu:** Her randevu için özel doktor notu ekleme ve güncelleme.

### 👤 Hasta Modülü
* **Hızlı Kayıt & Giriş:** Yeni üyelik oluşturma ve sisteme giriş.
* **Akıllı Randevu Alma:** Poliklinik ve Doktor seçimine göre dinamik doluluk kontrolü. Sadece doktorun çalıştığı ve boş olduğu saatleri listeler.
* **Randevu Geçmişi:** Aktif ve geçmiş randevuları görüntüleme.
* **İşlemler:** Randevu tarihini güncelleme veya iptal etme.
* **Profil Yönetimi:** Kişisel bilgileri (Tel, Mail, Şifre) güncelleme.

## 🛠️ Kullanılan Teknolojiler ve Araçlar

* **Dil:** Java (JDK 21+)
* **Arayüz (GUI):** JavaFX
* **Veritabanı:** SQLite (Gömülü/Embedded Veritabanı)
* **Build Aracı:** Maven
* **IDE:** IntelliJ IDEA

## 🏗️ Yazılım Mimarisi ve Tasarım Desenleri

Proje, endüstri standartlarına uygun olarak aşağıdaki desenleri kullanır:

| Tasarım Deseni | Kullanım Yeri ve Amacı |
| :--- | :--- |
| **MVC (Model-View-Controller)** | Projenin ana mimarisi. Veri, Arayüz ve İş Mantığı ayrılmıştır. |
| **SINGLETON** | `VeritabaniBaglantisi` ve `Oturum` sınıflarında tekil nesne yönetimi için. |
| **FACTORY METHOD** | `KullaniciFactory` sınıfında Hasta ve Doktor nesnelerinin üretimi için. |
| **OBSERVER** | `RandevuBildirimcisi` ile randevu alındığında ilgili doktora otomatik bildirim (Console log) gitmesi için. |
| **STATE** | Randevu durumlarının (Bekliyor, Tamamlandı, İptal, Gelmedi) `IRandevuDurum` arayüzü ile yönetilmesi için. |
| **DAO (Data Access Object)** | `RandevuDAO` sınıfı ile veritabanı işlemlerinin (SQL) Controller'dan soyutlanması için. |

## 📂 Proje Yapısı

src/main/java/com/example/hastane_randevu_yonetim_sistemi ├── controllers/ # Ekranların iş mantığı (Hello, Hasta, Doktor, Kayıt) ├── database/ # Veritabanı bağlantısı ve DAO sınıfları ├── models/ # Veri nesneleri (Entity) - Abstract Classlar burada ├── patterns/ # Tasarım desenleri (Factory, Observer, State) ├── HastaneRYS_Application.java # Başlatıcı Sınıf └── resources/ # FXML tasarımları ve stil dosyaları


## 📋 Gereksinim Karşılama Tablosu (Ödev Kontrolü)

- [x] **CRUD İşlemleri:** Hasta Kayıt (C), Randevu Listeleme (R), Profil Güncelleme (U), Mesai Silme (D).
- [x] **Singleton Deseni:** Veritabanı ve Oturum yönetiminde mevcut.
- [x] **Factory Deseni:** Kullanıcı üretiminde mevcut.
- [x] **Observer Deseni:** Randevu bildirim sisteminde mevcut.
- [x] **State Deseni:** Randevu durum geçişlerinde mevcut.
- [x] **Ekstra Desen 1 (MVC):** Proje genelinde uygulandı.
- [x] **Ekstra Desen 2 (DAO):** Veritabanı katmanında uygulandı.
- [x] **Abstract Class (En az 2):** `BaseEntity` ve `Kullanici` sınıfları abstract olarak tasarlandı.

## 🚀 Kurulum ve Çalıştırma

1.  Projeyi bilgisayarınıza indirin/klonlayın.
2.  IntelliJ IDEA ile `pom.xml` dosyasını açarak projeyi import edin.
3.  Maven bağımlılıklarının yüklenmesini bekleyin.
4.  `HastaneRYS_Application.java` dosyasını çalıştırın.
5.  *(Veritabanı `hastane.db` dosyası otomatik oluşturulacaktır.)*

---
**Geliştirici:** [Adın Soyadın]
**Ders:** Yazılım Mimarisi ve Tasarımı
