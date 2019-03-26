package cn.nlifew.support.task;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.nlifew.support.BaseActivity;
import cn.nlifew.support.fragment.BaseFragment;
import cn.nlifew.support.service.BaseService;

/**
 *  对 Handler 做了进一步封装的类，它提供了和 ASyncTask 不同的 API 接口。
 *  不同于 Handler 和 ASncTask，你不需要担心宿主(如 Activity, Fragment) 的生命周期
 *  带来的内存泄漏问题，因为它会在组件生命周期结束时自动释放所有可能的引用
 *
 *  ESyncFactory 在主线程通过 with() 函数返回工厂实例，并提供了 execute(ESyncInterface action)
 *  的 API 提交异步任务。其中，onIOThread() 会在子线程被调用，而 onUIThread(Object target)
 *  会在前者返回 true，并在宿主正常存活时被调用。
 *
 *  public class MainActivity extends BaseActivity {
 *
 *      private static final class TestTask implements ESyncInterface {
 *
 *          private String mErrInfo;
 *
 *          @Override
 *          public boolean onIOThread() {
 *              try {
 *                  Thread.sleep(5000);
 *              } catch (InterruptedException exp) {
 *                  mErrInfo = exp.toString();
 *              }
 *              return true;
 *          }
 *
 *          @Override
 *          public void onUIThread(Object target) {
 *              MainActivity activity = (MainActivity) target;
 *              Toast.makeText(activity,
 *                  mErrInfo == null ? "OK": mErrInfo,
 *                  Toast.LENGTH_LONG)
 *                  .show();
 *          }
 *      }
 *
 *      @Override
 *      public void onCreate(Bundle savedState) {
 *          super.onCreate(savedState);
 *
 *          ESyncFactory.with(this).execute(new TestTask());
 *      }
 *  }
 *
 *  对于上面的实例，如果用户在子线程执行期间按下了 Home 键，
 *  onUIThread() 会被放进任务队列，在用户再次进入应用时被调用;
 *  若宿主被销毁，任务会被移除。
 *  onUIThread(Object target) 的参数 为 构造工厂实例时传入的参数 target
 */

public class ESyncTaskFactory {

    public interface ESyncInterface {
        /**
         * onIOThread() 会在子线程被调用
         * @return boolean 表示是否需要回调 onUIThread(Object target) 方法
         */
        boolean onIOThread();
        void onUIThread(Object target);
    }

    /**
     *
     * @param target 参数应当根据自己的生命周期通知 EHandler
     * @return 工厂实例
     * 注意：如果 target 为同一个对象，所有返回的工厂实例都包含同一个 EHandler
     */

    public static ESyncTaskFactory with(BaseActivity target) {
        EHandler handler = target.getEHandler();
        if (handler == null) {
            handler = new EHandler();
            target.setEHandler(handler);
        }
        return new ESyncTaskFactory(handler);
    }

    public static ESyncTaskFactory with(BaseFragment target) {
        EHandler handler = target.getEHandler();
        if (handler == null) {
            handler = new EHandler();
            target.setEHandler(handler);
        }
        return new ESyncTaskFactory(handler);
    }

    public static ESyncTaskFactory with(BaseService target) {
        EHandler handler = target.getEHandler();
        if (handler == null) {
            handler = new EHandler();
            target.setEHandler(handler);
        }
        return new ESyncTaskFactory(handler);
    }

    /**
     * @param context 即在整个 Application 生命中始终存活
     * @return 工厂实例
     */

    public static ESyncTaskFactory with(Context context) {
        if (context != context.getApplicationContext()) {
            Log.w(TAG, "with: instanced with partial Context and" +
                    "a memory leaking will be happened." +
                    "Consider using ApplicationContext instead.");
        }
        if (sHandler == null) {
            sHandler = new EHandler();
            sHandler.onResume(context);
        }
        return new ESyncTaskFactory(sHandler);
    }

    private static EHandler sHandler;

    private static final ThreadPoolExecutor sThreadPool;

    static {
        int core = Runtime.getRuntime().availableProcessors();
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(128);
        sThreadPool = new ThreadPoolExecutor(2, core * 2 + 1,
                30, TimeUnit.SECONDS, queue);
    }

    private static final String TAG = "ESyncTaskFactory";

    private final EHandler mHandler;

    private ESyncTaskFactory(EHandler handler) {
        mHandler = handler;
    }

    public synchronized void execute(final ESyncInterface action) {
        sThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                Log.d(TAG, "run: start " + name);
                if (action.onIOThread()) {
                    Message msg = Message.obtain();
                    msg.obj = action;
                    mHandler.sendMessage(msg);
                }
                Log.d(TAG, "run: end " + name);
            }
        });
    }
}
