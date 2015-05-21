# PasswordEditText
实现大圆点形式的密码输入框，可自定义圆点颜色、宽度以及密码最大输入长度，光标的颜色、宽度、闪烁间隔。
效果如下图：

<img src="http://img.blog.csdn.net/20150521091429253?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvYWRhNDk4NjA3MDY3/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast" height="400" width="240"></img>
##Useage
```java
<com.ada.library.PasswordEditText
        android:id="@+id/edittext"
        android:layout_width="500dp"
        android:layout_height="wrap_content"
        ada:cursorColor="@android:color/black"
        ada:cursorInterval="400"
        ada:cursorWidth="4"
        ada:passwordColor="@android:color/white"
        ada:passwordLength="10"
        ada:passwordWidth="10dp" />
```
