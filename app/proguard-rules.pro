# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/joshua/dev/android-sdk/tools/proguard/proguard-android.txt
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
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}


# butterknife related
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}

# rxjava related
-dontwarn rx.**

# ok http related
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

# jackson related
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames class com.fasterxml.**
-keepattributes Signature,*Annotation*,EnclosingMethod

-keep class com.fasterxml.jackson.** { *; }

-dontwarn javax.xml.**
-dontwarn javax.xml.stream.events.**
-dontwarn com.fasterxml.jackson.databind.**

# otto related
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}
