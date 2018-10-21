# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/i20/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-useuniqueclassmembernames

# Base components
-include android.pro
-include appcompat.pro

# Libraries
-include gson.pro
-include glide.pro
-include retrofit.pro

-optimizationpasses 5
-allowaccessmodification
-dontpreverify
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-dontwarn android.util.FloatMath
-dontwarn javax.lang.model.element.Modifier

-dontwarn org.jacoco.agent.rt.**

-dontwarn javax.annotation.Nullable
-dontwarn com.google.common.**

-dontwarn java.lang.invoke.*

-dontnote **

-dontwarn ru.campuz.ng.engine.component.DCVoteEntityComponent

-dontwarn com.github.mikephil.charting.data.realm.**

-dontwarn android.databinding.annotationprocessor.**
-dontwarn ru.campuz.ui.widget.rangebar.CampuzRangeBar
-dontwarn android.databinding.tool.**
-dontwarn io.reactivex.**

-dontwarn okhttp3.**
-dontwarn com.squareup.picasso.Transformation

-dontwarn eu.davidea.flexibleadapter.FlexibleAdapter
-dontwarn eu.davidea.flexibleadapter.FlexibleAdapter$OnFilterListener

-keepclassmembers class * extends java.lang.Enum {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepattributes InnerClasses
-keepattributes EnclosingMethod

-keepclasseswithmembers class com.soramitsu.test.domain.models.** { *; }
-keepclasseswithmembers class com.soramitsu.test.repository.model.api.** { *; }
-keepclasseswithmembers class com.soramitsu.test.repository.model.db.** { *; }