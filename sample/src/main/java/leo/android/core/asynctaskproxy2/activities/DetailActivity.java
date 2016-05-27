package leo.android.core.asynctaskproxy2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import leo.android.core.AsyncCallback;
import leo.android.core.MethodAsyncTask;
import leo.android.core.asynctaskproxy2.R;
import leo.android.core.asynctaskproxy2.beans.Article;
import leo.android.core.asynctaskproxy2.commons.Consts;
import leo.android.core.asynctaskproxy2.commons.Logger;
import leo.android.core.asynctaskproxy2.dao.ArticleDao;

public class DetailActivity extends AppCompatActivity {

    private ArticleDao mArticleDao;
    private MethodAsyncTask mArticleTextMAT;
    private Article mArticle;
    private TextView mTextView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        mArticle = (Article) intent.getSerializableExtra(Consts.KEY_ARTICLE);
        if (mArticle == null) {
            Logger.e("mArticle is null");
            finish();
            return ;
        }
        getSupportActionBar().setTitle(mArticle.getTitle());

        mTextView = (TextView) findViewById(R.id.text);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);

        mArticleDao = new ArticleDao(this).getProxy();
        mArticleTextMAT = mArticleDao.getArticleText(Consts.ARTICLE_BASE_URL + mArticle.getUrl(), callback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mArticleTextMAT != null && mArticleTextMAT.isRunning()) {
            mArticleTextMAT.cancel(true);
        }
        // or use method below instead, this will cancel all AsyncTasks created by mArticleDao.
        // mArticleDao.cancelAll();
    }

    private AsyncCallback callback = new AsyncCallback<Integer, String>() {
        @Override
        protected void onResult(int err, String... texts) {
            Logger.d("begin, err = " + err);
            if (err == Consts.ERRCODE_FAILED) {
                Toast.makeText(DetailActivity.this, R.string.failed_to_load_data, Toast.LENGTH_LONG).show();
            } else if (err == Consts.ERRCODE_NET_BUGEILI) {
                Toast.makeText(DetailActivity.this, R.string.network_bugeili, Toast.LENGTH_LONG).show();
            } else if (err == Consts.ERRCODE_SUCCESSED) {
                String text = texts[0];
                if (text != null) {
                    mTextView.setText(Html.fromHtml(text));
                }
            }
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onCancel() {
            Toast.makeText(DetailActivity.this, R.string.cancelled_load_article, Toast.LENGTH_LONG).show();
        }
    };
}
