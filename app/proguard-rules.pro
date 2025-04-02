# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn com.kernal.lisence.**

-keep class com.kernal.lisence.** { *;}

-dontwarn com.sun.crypto.provider.**

-keep class com.sun.crypto.provider.** { *;}

-dontwarn kernal.sun.misc.**

-keep class kernal.sun.misc.** { *;}

-dontwarn com.kernal.imageprocessor.**

-keep class com.kernal.imageprocessor.** { *;}

-dontwarn kernal.idcard.android.**

-keep class kernal.idcard.android.** { *;}

-dontwarn kernal.idcard.camera.**

-keep class kernal.idcard.camera.** { *;}

-dontwarn com.wintone.Adaptor.**

-keep class com.wintone.Adaptor.** { *;}

-dontwarn com.wintone.cert.**

-keep class com.wintone.cert.** { *;}

-dontwarn com.wintone.cipher.**

-keep class com.wintone.cipher.** { *;}


-keep class com.kernal.passportreader.sdk.nfc.**{*;}
-dontwarn com.kernal.lisence.**

-keep class com.kernal.lisence.** { *;}

-dontwarn com.sun.crypto.provider.**

-keep class com.sun.crypto.provider.** { *;}

-dontwarn kernal.sun.misc.**

-keep class kernal.sun.misc.** { *;}

-dontwarn com.kernal.imageprocessor.**

-keep class com.kernal.imageprocessor.** { *;}

-dontwarn kernal.idcard.android.**

-keep class kernal.idcard.android.** { *;}

-dontwarn kernal.idcard.camera.**

-keep class kernal.idcard.camera.** { *;}

-dontwarn com.wintone.Adaptor.**

-keep class com.wintone.Adaptor.** { *;}

-dontwarn com.wintone.cert.**

-keep class com.wintone.cert.** { *;}



-keep class net.sf.scuba.**{*;}
-keep class org.jmrtd.**{*;}
-keep class org.jnbis.**{*;}
-keep class org.bouncycastle.**{*;}
-keep class org.ejbca.cvc.**{*;}
-keep class org.ejbca.cvc.**{*;}
-keep class com.fasterxml.**{*;}
-dontwarn java.awt.image.BufferedImage
-dontwarn java.awt.image.DataBuffer
-dontwarn java.awt.image.DataBufferByte
-dontwarn java.awt.image.Raster
-dontwarn java.awt.image.RenderedImage
-dontwarn java.awt.image.WritableRaster
-dontwarn java.beans.ConstructorProperties
-dontwarn java.beans.Transient
-dontwarn javax.imageio.ImageIO
-dontwarn javax.naming.NamingEnumeration
-dontwarn javax.naming.NamingException
-dontwarn javax.naming.directory.Attribute
-dontwarn javax.naming.directory.Attributes
-dontwarn javax.naming.directory.DirContext
-dontwarn javax.naming.directory.InitialDirContext
-dontwarn javax.naming.directory.SearchControls
-dontwarn javax.naming.directory.SearchResult
-dontwarn org.ksoap2.SoapEnvelope
-dontwarn org.ksoap2.serialization.SoapObject
-dontwarn org.ksoap2.serialization.SoapSerializationEnvelope
-dontwarn org.ksoap2.transport.HttpTransportSE
-dontwarn com.kernal.plateid.PlateIDAPI
-dontwarn com.kernal.smartvision.smartvisionAPI
-dontwarn com.wintone.BussinessLicense.BusinessLicenseAPI
-dontwarn com.wintone.InvoiceMobile.InvoiceMobileAPI
-dontwarn com.wintone.jfromex.JFromEx
-dontwarn java.awt.BorderLayout
-dontwarn java.awt.Canvas
-dontwarn java.awt.Color
-dontwarn java.awt.Component
-dontwarn java.awt.Container
-dontwarn java.awt.Cursor
-dontwarn java.awt.Dimension
-dontwarn java.awt.Frame
-dontwarn java.awt.Image
-dontwarn java.awt.Insets
-dontwarn java.awt.LayoutManager
-dontwarn java.awt.Point
-dontwarn java.awt.Scrollbar
-dontwarn java.awt.Toolkit
-dontwarn java.awt.event.KeyAdapter
-dontwarn java.awt.event.KeyListener
-dontwarn java.awt.event.MouseAdapter
-dontwarn java.awt.event.MouseListener
-dontwarn java.awt.event.MouseMotionListener
-dontwarn java.awt.event.WindowAdapter
-dontwarn java.awt.event.WindowListener
-dontwarn java.awt.image.ColorModel
-dontwarn java.awt.image.ImageObserver
-dontwarn java.awt.image.ImageProducer
-dontwarn kernel.BusinessCard.android.BusinessCardAPI
