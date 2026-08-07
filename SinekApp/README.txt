SINEK UYGULAMASI - ANDROID STUDIO PROJESI
------------------------------------------

Bu zip dosyası, "Sinek" uygulamasının tüm Android Studio kaynak kodlarını içerir.

Proje Nasıl Çalıştırılır / APK Nasıl Alınır?

1. Zip dosyasını bir klasöre çıkarın.
2. Android Studio'yu açın ve "Open" seçeneği ile çıkardığınız SinekApp klasörünü seçin.
3. Gradle senkronizasyonunun tamamlanmasını bekleyin.
4. Kendi özel sinek görselinizi eklemek isterseniz:
   - Şeffaf arka planlı bir sinek resmi (PNG) hazırlayın.
   - Dosya adını 'sinek.png' yapıp `app/src/main/res/drawable/` içine yapıştırın.
   - FlyService.java dosyasındaki `android.R.drawable.ic_menu_compass` kısmını `R.drawable.sinek` yapın.
5. Menüden: Build > Build Bundle(s) / APK(s) > Build APK(s) seçeneğine tıklayarak APK dosyanızı oluşturun.

Uygulama Özellikleri:
- Sistem üstünde gösterim izni (SYSTEM_ALERT_WINDOW) alır.
- Sinek ekranda rastgele dolaşır, dokununca kaçar.
- İlk 30 saniye boyunca kapatılamaz.
- 30 saniye sonra sağ üst köşede 'X' kapatma butonu belirir.
