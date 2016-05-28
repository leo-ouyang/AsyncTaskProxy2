package leo.android.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import leo.android.cglib.proxy.Enhancer;
import leo.android.cglib.proxy.MethodInterceptor;
import leo.android.cglib.proxy.MethodProxy;
import leo.android.core.MethodAsyncTask.MethodAsyncTaskListener;
import android.content.Context;

public class AsyncTaskProxy<T> implements MethodAsyncTaskListener {
	
	private Context context;
	private Map<String, MethodAsyncTask> asyncTaskMap;
	
	public AsyncTaskProxy(Context context) {
		this.context = context;
	}
	
	@SuppressWarnings("unchecked")
	public final T getProxy() {
		Enhancer e = new Enhancer(context);
		e.setSuperclass(getClass());
		e.setInterceptor(asyncTaskInterceptor);
		return (T) e.create();
	}
	
	private MethodInterceptor asyncTaskInterceptor = new MethodInterceptor() {

		@SuppressWarnings("unchecked")
		@Override
		public Object intercept(Object object, Object[] args, MethodProxy methodProxy) throws Exception {
			if (args.length == 0 || !(args[args.length-1] instanceof AsyncCallback)) {
				// No AsyncTask callback
				return methodProxy.invokeSuper(object, args);
			} else {
				if (asyncTaskMap == null) {
					asyncTaskMap = new HashMap<String, MethodAsyncTask>();
				}
				AsyncCallback<Object, Object> callback = (AsyncCallback<Object, Object>) args[args.length - 1];
				String key = methodProxy.getMethodName();
				if (asyncTaskMap.containsKey(key)) {
					key += "-" + UUID.randomUUID().toString();
				}
				MethodAsyncTask asyncTask = new MethodAsyncTask();
				asyncTask.setName(key);
				asyncTask.setTargetMethod(object, methodProxy.getProxyMethod(), args);
				asyncTask.setMethodAsyncTaskListener(AsyncTaskProxy.this);
				asyncTask.setAsyncCallback(callback);
				callback.setAsyncTask(asyncTask);
				asyncTaskMap.put(key, asyncTask);
				asyncTask.execute();
				return asyncTask;
			}
		}
		
	};
	
	// Cancel all AsyncTask which launched by this class
	public final void cancelAll() {
		if (asyncTaskMap != null) {
			Collection<MethodAsyncTask> values = asyncTaskMap.values();
			if (values != null) {
				for (MethodAsyncTask asyncTask : values) {
					if (asyncTask != null && asyncTask.isRunning()) {
						asyncTask.cancel(true);
					}
				}
			}
		}
	}

	@Override
	public void onDone(String name) {
		if (asyncTaskMap != null) {
			asyncTaskMap.remove(name); // Remove AsyncTask if it finished
		}
	}
	
}
