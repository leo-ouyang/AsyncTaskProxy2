# AsyncTaskProxy2
Define or call an AsyncTask like to define or call a method, a class can define multiple AsyncTask methods, the AsyncTaskProxy2's implementation based on [CGLIB-for-Android](https://github.com/leo-ouyang/CGLIB-for-Android), which is a library can dynamically generate dex code.

<br>
There's another implementation [AsyncTaskProxy](https://github.com/leo-ouyang/AsyncTaskProxy) which design by JDK dynamic proxy.

## Usage
1 Import cglib-for-android.jar to your project.
* library/libs/cglib-for-android.jar

<br>
2 Copy package leo.android to your project and rename it as you like, or you could pack it as a jar.
* library/src/main/java/leo.android.core

<br>
2 Please refer to [AsyncTaskProxy](https://github.com/leo-ouyang/AsyncTaskProxy) for detail usage, they have the same way of usage except two points below:
<br>
* AsyncTaskProxy2 don't need to define an interface.
* AsyncTaskProxy2 need to define constructor as below:
```Java
public class ArticleDao extends AsyncTaskProxy<ArticleDao> {

    public ArticleDao(Context context) {
        super(context);
    }

    ...
}
```
