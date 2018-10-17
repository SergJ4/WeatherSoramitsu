# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
# Add the gson class
-keep public class com.google.gson
# Keep the annotations
-keepattributes *Annotation*