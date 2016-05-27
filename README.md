# AsyncTaskProxy2
Define or call an AsyncTask like to define or call a method, a class can define multiple AsyncTask methods, the AsyncTaskProxy2 implemented by dynamically generate sub class.
<br>
There's another implementation [AsyncTaskProxy](https://github.com/leo-ouyang/AsyncTaskProxy) which design by JDK dynamic proxy.

## Usage
1 Copy package leo.android to your project and rename it as you like, or you could pack it as a jar and import into your project.

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
