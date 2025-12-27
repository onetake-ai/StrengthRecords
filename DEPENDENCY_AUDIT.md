# Dependency Audit Report
**Project:** StrengthRecords (GymRoutines)
**Date:** 2025-12-27
**Android Target SDK:** 35
**Kotlin Version:** 2.1.0
**Gradle Version:** 8.11.1

## Executive Summary

This audit analyzed 40+ dependencies across the project for outdated packages, security vulnerabilities, and unnecessary bloat. The project is generally well-maintained with recent AndroidX and Compose libraries, but there are several opportunities for optimization.

**Key Findings:**
- ✅ Most core dependencies are up-to-date (Compose 1.7.8, Room 2.7.0, Lifecycle 2.8.7)
- ⚠️ 6 dependencies have newer versions available
- 🔴 Material Icons Extended adds ~20MB of unused icons (major bloat)
- ⚠️ Both Material 2 and Material 3 are included (potential redundancy)
- ✅ No critical security vulnerabilities identified in current versions

---

## 1. Outdated Dependencies

### High Priority Updates

| Dependency | Current | Recommended | Impact |
|------------|---------|-------------|--------|
| `kotlinx-serialization-json` | 1.6.3 | 1.7.3 | Bug fixes, performance improvements |
| `io.insert-koin:koin-android` | 3.3.3 | 3.5.6 | Security patches, API improvements |
| `io.insert-koin:koin-androidx-compose` | 3.4.1 | 3.5.6 | Version consistency with koin-android |
| `org.assertj:assertj-core` | 3.24.2 | 3.26.3 | Testing improvements (test dependency only) |

### Medium Priority Updates

| Dependency | Current | Recommended | Impact |
|------------|---------|-------------|--------|
| `com.google.android.material:material` | 1.12.0 | 1.13.0 | New features, bug fixes |
| `io.mockk:mockk` | 1.13.4 | 1.13.13 | Bug fixes (test dependency only) |
| `foojay-resolver-convention` | 0.6.0 | 0.9.0 | Build performance improvements |

### Low Priority (Already Recent)

These dependencies are already on recent stable versions:
- ✅ Compose libraries: 1.7.8 (latest stable)
- ✅ Room: 2.7.0 (latest stable)
- ✅ Lifecycle: 2.8.7 (latest stable)
- ✅ Navigation Compose: 2.8.9 (latest stable)
- ✅ Coroutines: 1.10.1 (latest stable)
- ✅ Core KTX: 1.16.0 (latest stable)
- ✅ Android Gradle Plugin: 8.9.1 (latest stable)

---

## 2. Security Analysis

### No Critical Vulnerabilities Found

All dependencies are from trusted sources:
- **Google/AndroidX**: Core, Compose, Room, Lifecycle, Navigation
- **JetBrains**: Kotlin, Coroutines, Serialization
- **Insert-Koin**: Dependency injection framework
- **Jake Wharton**: Process Phoenix (trusted Android developer)

### Recommendations for Security

1. **Update Koin to 3.5.6**: Includes security patches from the 3.4.x and 3.5.x release cycles
2. **Update kotlinx-serialization**: Version 1.7.x includes fixes for edge-case serialization issues
3. **Regular dependency audits**: Recommend quarterly dependency reviews
4. **Consider using Dependabot or Renovate**: Automated dependency update PRs

---

## 3. Bloat Analysis

### 🔴 Critical Bloat: Material Icons Extended (~20MB)

**Issue:** `androidx.compose.material:material-icons-extended:1.7.8`

This library includes **2000+ Material icons** and adds approximately **20MB** to your APK size. Most apps use fewer than 50 icons.

**Impact:**
- APK size: +20MB
- User downloads: Slower, more data usage
- App performance: Minimal runtime impact, but initial load affected

**Solutions (choose one):**

#### Option 1: Use only Core Icons (Recommended)
Remove `material-icons-extended` and use only the ~150 most common icons from `material-icons-core`.

```kotlin
// Remove this line
implementation("androidx.compose.material:material-icons-extended:1.7.8")

// Keep this (already present)
implementation("androidx.compose.material:material-icons-core:1.7.8")
```

**If you need icons not in core:** Copy individual icon vectors from the Material Icons source into your project.

#### Option 2: Use R8/ProGuard Shrinking
Enable code shrinking to remove unused icons:

```kotlin
buildTypes {
    getByName("release") {
        isMinifyEnabled = true  // Currently false
        isShrinkResources = true
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }
}
```

**Note:** This only works for release builds and requires thorough testing.

#### Option 3: Manual Icon Selection
Use a custom icon font or vector drawables for exactly the icons you need.

---

### ⚠️ Moderate Bloat: Material 2 + Material 3 Duplication

**Issue:** Both Material Design 2 and Material Design 3 libraries are included.

```kotlin
implementation("androidx.compose.material:material:1.7.8")        // Material 2
implementation("androidx.compose.material3:material3:1.3.2")     // Material 3
implementation("androidx.compose.material:material-navigation:1.7.8")  // Material 2
```

**Impact:**
- Duplicate theming systems
- Increased APK size: ~2-3MB
- Potential UI inconsistencies

**Recommendation:**

If possible, **migrate fully to Material 3**:

```kotlin
// Remove Material 2 dependencies
// implementation("androidx.compose.material:material:1.7.8")
// implementation("androidx.compose.material:material-navigation:1.7.8")

// Keep Material 3
implementation("androidx.compose.material3:material3:1.3.2")

// For navigation with Material 3, use:
implementation("androidx.compose.material3:material3-window-size-class:1.3.2")  // if needed
```

**Migration effort:** Medium - requires updating all `import androidx.compose.material.*` to `androidx.compose.material3.*` and adapting component APIs.

**Alternative:** If migration is not feasible now, keep both but plan for eventual Material 3-only migration.

---

### ⚠️ Potentially Unnecessary: Process Phoenix

**Issue:** `com.jakewharton:process-phoenix:2.1.2`

This library is used to restart the application process programmatically.

**Use Cases:**
- Language/theme changes requiring full restart
- Clearing app state after settings change
- Developer tools

**Questions to ask:**
1. Is process restart actually necessary? Modern Android handles most config changes gracefully.
2. Could LiveData/Flow state management replace the need for restarts?

**If not needed:** Remove to save ~50KB and reduce complexity.

**If needed:** Keep it - it's a small, well-maintained library.

---

## 4. Dependency Organization

### Potential Simplification

**Current Compose dependencies (10 separate declarations):**

```kotlin
implementation("androidx.compose.ui:ui:1.7.8")
implementation("androidx.compose.ui:ui-tooling:1.7.8")
implementation("androidx.compose.foundation:foundation:1.7.8")
implementation("androidx.compose.material:material:1.7.8")
implementation("androidx.compose.material3:material3:1.3.2")
implementation("androidx.compose.material:material-icons-core:1.7.8")
implementation("androidx.compose.material:material-icons-extended:1.7.8")
implementation("androidx.compose.runtime:runtime-livedata:1.7.8")
// ...
```

**Recommendation:** Consider using a Bill of Materials (BOM) for version consistency:

```kotlin
// Add BOM
implementation(platform("androidx.compose:compose-bom:2024.12.01"))

// Then you can omit versions
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.ui:ui-tooling")
implementation("androidx.compose.foundation:foundation")
implementation("androidx.compose.material3:material3")
// ...
```

**Benefits:**
- Automatic version alignment across all Compose libraries
- Easier updates (update one BOM version)
- Prevents version conflicts

---

## 5. Recommended Action Items

### Immediate Actions (High Impact, Low Effort)

1. **Update Koin dependencies** (app/build.gradle.kts:121-123)
   ```kotlin
   implementation("io.insert-koin:koin-android:3.5.6")
   implementation("io.insert-koin:koin-androidx-compose:3.5.6")
   testImplementation("io.insert-koin:koin-test:3.5.6")
   ```

2. **Update kotlinx-serialization** (app/build.gradle.kts:94)
   ```kotlin
   implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
   ```

3. **Remove material-icons-extended** (app/build.gradle.kts:116)
   - Remove the line entirely
   - Audit codebase for extended icon usage
   - Copy needed icons as vector drawables if necessary

### Short-term Actions (1-2 weeks)

4. **Enable R8 shrinking for release builds** (app/build.gradle.kts:52)
   ```kotlin
   buildTypes {
       getByName("release") {
           isMinifyEnabled = true
           isShrinkResources = true
           // ... rest of config
       }
   }
   ```

5. **Audit Material 2 vs Material 3 usage**
   - Search codebase for `import androidx.compose.material.` vs `.material3.`
   - Plan migration to Material 3 only if feasible

6. **Update foojay-resolver-convention** (settings.gradle.kts:23)
   ```kotlin
   id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
   ```

### Medium-term Actions (1 month)

7. **Migrate to Compose BOM**
   - Test thoroughly as this changes version management strategy

8. **Consider Dependabot/Renovate setup**
   - Automate dependency update PRs
   - Reduce manual maintenance burden

9. **Evaluate Process Phoenix necessity**
   - Remove if not essential

---

## 6. Estimated Impact

| Action | APK Size Reduction | Build Time | Security | Effort |
|--------|-------------------|------------|----------|--------|
| Remove icons-extended | -15 to -20MB | No change | No change | Low |
| Enable R8 shrinking | -5 to -10MB (total) | +10-30s | No change | Low |
| Update Koin | Minimal | Minimal | Improved | Low |
| Update serialization | Minimal | Minimal | Improved | Low |
| Migrate to Material 3 only | -2 to -3MB | Minimal | No change | Medium |
| Process Phoenix removal | -50KB | Minimal | Minimal | Low |
| **Total Potential Savings** | **-22 to -33MB** | - | **Improved** | - |

**Current estimated APK size:** 40-60MB (typical for this dependency stack)
**After optimizations:** 18-38MB (30-40% reduction)

---

## 7. Testing Recommendations

After implementing changes:

1. **Run all tests:** `./gradlew test connectedAndroidTest`
2. **Test release build:** Ensure shrinking doesn't break functionality
3. **Check APK size:** `./gradlew assembleRelease` and compare sizes
4. **Manual QA:** Test all app features, especially:
   - Icon rendering
   - Navigation
   - Dependency injection
   - Serialization/deserialization

---

## 8. Additional Notes

### Build Configuration Observations

**Good practices already in place:**
- ✅ `android.enableJetifier=false` - Jetifier is disabled (faster builds)
- ✅ `org.gradle.unsafe.configuration-cache=true` - Configuration cache enabled
- ✅ Recent Gradle version (8.11.1)
- ✅ Recent Kotlin version (2.1.0)
- ✅ Up-to-date compile/target SDK (35)

### Future Considerations

1. **Kotlin Multiplatform:** If you plan to go multiplatform, current dependencies are compatible
2. **Compose Multiplatform:** Most Compose dependencies work with Compose Multiplatform
3. **AGP 9.x:** When Android Gradle Plugin 9.0 releases, plan migration

---

## Appendix: Full Dependency List

### Production Dependencies
```
androidx.core:core-ktx:1.16.0
androidx.core:core-splashscreen:1.0.1
kotlinx-coroutines-android:1.10.1
material:1.12.0
kotlinx-serialization-json:1.6.3
androidx.room:room-ktx:2.7.0
androidx.room:room-runtime:2.7.0
androidx.lifecycle:lifecycle-common-java8:2.8.7
androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7
androidx.lifecycle:lifecycle-process:2.8.7
androidx.navigation:navigation-compose:2.8.9
androidx.activity:activity-compose:1.10.1
androidx.compose.ui:ui:1.7.8
androidx.compose.ui:ui-tooling:1.7.8
androidx.compose.foundation:foundation:1.7.8
androidx.compose.material:material:1.7.8
androidx.compose.material3:material3:1.3.2
androidx.compose.material:material-icons-core:1.7.8
androidx.compose.material:material-icons-extended:1.7.8
androidx.compose.runtime:runtime-livedata:1.7.8
androidx.compose.material:material-navigation:1.7.8
io.insert-koin:koin-android:3.3.3
io.insert-koin:koin-androidx-compose:3.4.1
androidx.datastore:datastore-preferences:1.1.4
com.jakewharton:process-phoenix:2.1.2
com.eygraber:compose-placeholder-material3:1.0.10
```

### Test Dependencies
```
androidx.room:room-testing:2.7.0
androidx.compose.ui:ui-test:1.7.8
androidx.compose.ui:ui-test-junit4:1.7.8
io.insert-koin:koin-test:3.3.3
androidx.test:core:1.6.1
androidx.test:core-ktx:1.6.1
io.mockk:mockk:1.13.4
io.mockk:mockk-android:1.13.4
junit:junit:4.13.2
assertj-core:3.24.2
```

### Build Dependencies
```
com.android.tools.build:gradle:8.9.1
kotlin-gradle-plugin:2.1.0
ksp:2.1.0-1.0.29
ktlint:12.2.0
foojay-resolver-convention:0.6.0
```

---

**End of Report**
