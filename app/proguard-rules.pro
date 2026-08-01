# Retrofit
-keepattributes Signature
-keepattributes Exceptions
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# Gson
-keepclassmembers class com.dramafy.app.data.model.** { *; }
-keep class com.dramafy.app.data.model.** { *; }

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Media3
-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**
