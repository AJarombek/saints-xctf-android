-dontwarn org.joda.**
-dontwarn com.fasterxml.**
-dontnote

-keep class org.joda.time.** {*;}
-keep class com.github.mikephil.** {*;}
-keep class com.fasterxml.jackson.** {*;}
-keep class com.jarombek.andy.** {*;}

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}
