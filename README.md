# 🏥 HRYS - Hastane Randevu Yönetim Sistemi

HRYS, JavaFX ve SQLite kullanılarak geliştirilmiş, modern arayüze sahip, kapsamlı bir hastane otomasyon sistemidir. Proje, **Nesne Yönelimli Programlama (OOP)** prensiplerine ve **Yazılım Tasarım Desenlerine (Design Patterns)** tam uyumlu olarak, **MVC (Model-View-Controller)** mimarisiyle geliştirilmiştir.

## 👥 Proje Ekibi

| Adı Soyadı                                         | Öğrenci No |
| :------------------------------------------------- | :--------- |
| [**Enes Dolgun**](https://github.com/enesdolgun33) | 1230505037 |
| [**Enes Turan**](https://github.com/Enes-Turan)    | 1230505002 |

## 🚀 Özellikler

### 👨‍⚕️ Doktor Modülü



- **Güvenli Giriş:** TC ve şifre ile yetkilendirilmiş giriş.
- **Randevu Yönetimi (Dashboard):** Bekleyen randevuları listeleme, filtreleme (tarih/durum).
- **İşlem Menüsü:** Randevuları "Tamamlandı" veya "Gelmedi" olarak işaretleme (State Deseni).
- **Hasta Detay Görüntüleme:** Randevuya çift tıklayarak hastanın detaylı künyesini (Geçmiş, İletişim, Kronik vb.) görme.
- **Mesai Planlama:** Haftalık çalışma günlerini ve saatlerini (09:00 - 17:00) dinamik olarak ayarlama.
- **Muayene Notu:** Her randevu için özel doktor notu ekleme ve güncelleme.

### 👤 Hasta Modülü

- **Hızlı Kayıt & Giriş:** Yeni üyelik oluşturma ve sisteme giriş.
- **Akıllı Randevu Alma:** Poliklinik ve Doktor seçimine göre dinamik doluluk kontrolü. Sadece doktorun çalıştığı ve boş olduğu saatleri listeler.
- **Randevu Geçmişi:** Aktif ve geçmiş randevuları görüntüleme.
- **İşlemler:** Randevu tarihini güncelleme veya iptal etme.
- **Profil Yönetimi:** Kişisel bilgileri (Tel, Mail, Şifre) güncelleme.

## 🛠️ Kullanılan Teknolojiler ve Araçlar

- **Dil:** Java (JDK 21+)
- **Arayüz (GUI):** JavaFX
- **Veritabanı:** SQLite (Gömülü/Embedded Veritabanı)
- **Build Aracı:** Maven
- **IDE:** IntelliJ IDEA

## 🏗️ Yazılım Mimarisi ve Tasarım Desenleri

Proje, endüstri standartlarına uygun olarak aşağıdaki desenleri kullanır:

| Tasarım Deseni                  | Kullanım Yeri ve Amacı                                                                                     |
| :------------------------------ | :--------------------------------------------------------------------------------------------------------- |
| **MVC (Model-View-Controller)** | Projenin ana mimarisi. Veri, Arayüz ve İş Mantığı ayrılmıştır.                                             |
| **SINGLETON**                   | `VeritabaniBaglantisi` ve `Oturum` sınıflarında tekil nesne yönetimi için.                                 |
| **FACTORY METHOD**              | `KullaniciFactory` sınıfında Hasta ve Doktor nesnelerinin üretimi için.                                    |
| **OBSERVER**                    | `RandevuBildirimcisi` ile randevu alındığında ilgili doktora otomatik bildirim (Console log) gitmesi için. |
| **STATE**                       | Randevu durumlarının (Bekliyor, Tamamlandı, İptal, Gelmedi) `IRandevuDurum` arayüzü ile yönetilmesi için.  |
| **DAO (Data Access Object)**    | `RandevuDAO` sınıfı ile veritabanı işlemlerinin (SQL) Controller'dan soyutlanması için.                    |

## 🚀 Kurulum ve Çalıştırma

1.  Projeyi bilgisayarınıza indirin/klonlayın.
2.  IntelliJ IDEA ile `pom.xml` dosyasını açarak projeyi import edin.
3.  Maven bağımlılıklarının yüklenmesini bekleyin.
4.  `HastaneRYS_Application.java` dosyasını çalıştırın.
5.  _(Veritabanı `hastane.db` dosyası otomatik oluşturulacaktır.)_

---
