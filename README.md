<div align="center">

# 🎨 Wallora

### Premium Wallpaper App for Android — free source code, ads already built in.

Clone it → change the pictures → publish it → **earn from AdMob.**

<br>

[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](#)
[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](#)
[![AdMob Ready](https://img.shields.io/badge/AdMob-Ready-EA4335?style=for-the-badge&logo=googleads&logoColor=white)](#)
[![MIT](https://img.shields.io/badge/License-MIT-3B82F6?style=for-the-badge)](LICENSE)
[![Free](https://img.shields.io/badge/Price-FREE-10B981?style=for-the-badge)](#)

<br>

# 🎓 Learn to build apps like this — with AI

### Free **2-hour webinar** · No coding experience needed

[![Join the FREE Webinar](https://img.shields.io/badge/🚀%20JOIN%20FREE%202--HOUR%20WEBINAR-androai.io-10B981?style=for-the-badge)](https://androai.io)

**👉 [androai.io](https://androai.io)**

</div>

---

## 📱 What it looks like

```
┌───────────────────────┐  ┌───────────────────────┐  ┌───────────────────────┐
│  Discover        🔍   │  │  ←              ♥     │  │  Favorites            │
│  ─────────────────    │  │                       │  │  ─────────────────    │
│ [All][Nature][Love]   │  │                       │  │                       │
│                       │  │      FULL SCREEN      │  │   ┌─────┐ ┌─────┐     │
│  ┌─────┐  ┌─────┐     │  │       WALLPAPER       │  │   │  ♥  │ │  ♥  │     │
│  │  ♥  │  │  ♥  │     │  │        PREVIEW        │  │   └─────┘ └─────┘     │
│  └─────┘  └─────┘     │  │                       │  │   ┌─────┐ ┌─────┐     │
│  ┌─────┐  ┌─────┐     │  │                       │  │   │  ♥  │ │  ♥  │     │
│  │  ♥  │  │  ♥  │     │  │  ┌─────┬─────┬─────┐  │  │   └─────┘ └─────┘     │
│  └─────┘  └─────┘     │  │  │ SET │SAVE │SHARE│  │  │                       │
│  ▓▓▓ BANNER AD ▓▓▓    │  │  └─────┴─────┴─────┘  │  │                       │
│  🏠    ♥     ⚙️       │  │                       │  │  🏠    ♥     ⚙️       │
└───────────────────────┘  └───────────────────────┘  └───────────────────────┘
        HOME                      DETAIL                    FAVORITES
```

> 📸 Add real screenshots to `docs/screenshots/` and show them off here.

---

## ✨ Features

|   |   |   |
|:-:|---|---|
| 🖤 | **Premium dark UI** | Glass cards + emerald accent, made for AMOLED |
| 💰 | **AdMob built in** | Banner + interstitial, ready to earn |
| ⚡ | **Zero cost** | No server, no API — images are local |
| ❤️ | **Favorites** | Saved forever on the phone |
| 🔎 | **Search + 6 categories** | Nature · Abstract · Love · Motivation · Premium |
| 📥 | **Set · Save · Share** | One tap each |

---

## 🚀 Run it (2 minutes)

```bash
git clone https://github.com/<you>/wallpaper-android.git
```

**Open in Android Studio → press ▶️ Run.** Done. It works instantly with test ads.

---

## 💰 Earn money (3 steps)

<table>
<tr><td align="center"><h1>1️⃣</h1></td><td>

**Get your AdMob IDs** → sign up free at [admob.google.com](https://admob.google.com), create a **Banner** + an **Interstitial** ad unit.

</td></tr>
<tr><td align="center"><h1>2️⃣</h1></td><td>

**Paste them in** `ads/AdsConfig.kt`

```kotlin
const val APP_ID                  = "ca-app-pub-YOURS~XXXX"
const val HOME_BANNER_AD_UNIT_ID  = "ca-app-pub-YOURS/XXXX"
const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-YOURS/XXXX"
```

</td></tr>
<tr><td align="center"><h1>3️⃣</h1></td><td>

**And in** `res/values/strings.xml`

```xml
<string name="admob_app_id">ca-app-pub-YOURS~XXXX</string>
```

</td></tr>
</table>

### 🎉 That's it — publish and keep 100% of the revenue.

> ⚠️ **Never click your own live ads.** It gets your AdMob account banned. Keep the test IDs while developing.

**Ads show like this:** 📢 Banner on Home · 🎬 Full-screen ad on every **3rd** wallpaper open
*(change `openCount % 3` in `InterstitialAdController.kt` to show more or fewer)*

---

## 🎨 Make it yours

| I want to change… | Go to this file |
|---|---|
| 🖼️ The wallpapers | `res/drawable/wallpaper_1.png` … `wallpaper_12.png` — **just overwrite them!** |
| ✏️ App name & text | `res/values/strings.xml` |
| 🎨 Colors | `res/values/colors.xml` |
| 🏷️ Categories | `Categories` in `model/Wallpaper.kt` |
| 📦 Package name | `applicationId` in `app/build.gradle.kts` |
| 🖼️ App icon | `res/mipmap-*` |

---

## 📂 Inside the code

```
com.demo.wallpaper
│
├── 📱 ui/          Home · Favorites · Settings · Detail
├── 🖼️ model/       Wallpaper + Categories
├── 💾 data/        Wallpaper list + Favorites
├── 🧠 viewmodel/   Search · Filter · Favorites logic
├── 🛠️ util/        Set / Save / Share wallpaper
└── 💰 ads/         Banner + Interstitial
```

Clean **MVVM** · Kotlin · ViewBinding · No complicated setup.

---

## ✅ Before you publish on Play Store

- [ ] 🔑 Put your **real** AdMob IDs in
- [ ] 📦 Change the package name
- [ ] 🖼️ Use **your own images** (the demo ones are placeholders!)
- [ ] 📄 Add a real privacy policy link in `strings.xml`
- [ ] ✍️ Sign your release build

---

<div align="center">

## 🎓 Build your own app with AI — FREE

### 2-hour live webinar · from idea → Play Store

**No coding experience needed.**

[![Reserve Free Seat](https://img.shields.io/badge/🎟️%20RESERVE%20YOUR%20FREE%20SEAT-androai.io-10B981?style=for-the-badge)](https://androai.io)

### 👉 [androai.io](https://androai.io)

<br>

## ⭐ Like it? Star the repo!

**[MIT License](LICENSE)** — free for commercial use. Build it. Brand it. Publish it. Keep the money.

<br>

**Made with 💚 for Android developers who want to ship.**

</div>

## RamWalls v1 branding
This fork is branded as RamWalls, a RAM Digital product. The app uses the RAM Digital dark/blue/violet/gold design palette and the Ram Digital logo as its launcher mark. The AdMob application ID remains Google's test ID for development.
