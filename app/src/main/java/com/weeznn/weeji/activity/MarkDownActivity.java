package com.weeznn.weeji.activity;

import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.weeznn.mylibrary.utils.DataUtil;
import com.weeznn.mylibrary.utils.FileUtil;
import com.weeznn.weeji.R;
import com.weeznn.weeji.adpater.BtnAdaptar;
import com.zzhoujay.richtext.RichText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MarkDownActivity extends AppCompatActivity implements BtnAdaptar.ItemClickListener {
    private static final String TAG = MarkDownActivity.class.getSimpleName();
    private static final int REQUEST_CODE_IMAGE=1;
    private static final String IMAGE_MD_STRING="IMAGE_MD_STRING";

    public static final String INTENT_FILE_NAME="INTENT_FILE_NAME";
    public static final String INTENT_FILE_TYPE="INTENT_FILE_TYPE";
    public static final String INTENT_FILE_DATA="INTENT_FILE_DATA";

    private NestedScrollView scrollText;
    private NestedScrollView scrollEdit;
    private RecyclerView recyclerView;
    private EditText editText;
    private TextView textView;
    private Toolbar toolbar;
    private ContentLoadingProgressBar progressBar;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            progressBar.setVisibility(View.GONE);

            Snackbar.make(scrollEdit,"文件已保存到本地！",Snackbar.LENGTH_SHORT).show();
            finish();
            return true;
        }
    });

    private String preFlg = "";
    private String filename;
    private String fileType;
    private String file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markdown);

        if (getIntent()!=null) {
            if (getIntent().getStringExtra(INTENT_FILE_NAME) != null) {
                filename = getIntent().getStringExtra(INTENT_FILE_NAME);
            }else {
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                filename= format.format(new Date());
            }
            if (getIntent().getStringExtra(INTENT_FILE_TYPE) != null){
                fileType = getIntent().getStringExtra(INTENT_FILE_TYPE);
            }else {
                fileType=FileUtil.FILE_TYPE_NOTE;
            }
            if (getIntent().getStringExtra(INTENT_FILE_DATA) != null){
                file = getIntent().getStringExtra(INTENT_FILE_DATA);
            }
        }

        progressBar=findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recycler);
        editText = findViewById(R.id.edit);
        textView=findViewById(R.id.text);
        toolbar=findViewById(R.id.toolbar);
        scrollEdit=findViewById(R.id.nestedScrollEdit);
        scrollText=findViewById(R.id.nestedScrollText);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        BtnAdaptar btnAdaptar = new BtnAdaptar(this);
        recyclerView.setAdapter(btnAdaptar);
        btnAdaptar.setItemClickListener(this);

        editText.setFocusable(true);
        editText.append("## "+filename);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Log.i(TAG,"last word is enter "+('\n'==s.charAt(s.length()-1))+"  preFlg :"+preFlg);
                if ('\n'==s.charAt(s.length()-1)&& !"".equals(preFlg)){
                    Log.i(TAG,"insert");
                    s.append(preFlg);
                }
            }
        });

        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMenuItemClickListener(item);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.markdown_menu,menu);
        return true;
    }

    private void onMenuItemClickListener(MenuItem item){
        switch (item.getItemId()){
            case R.id.edit:
                ObjectAnimator animator=ObjectAnimator.ofFloat(recyclerView,
                        "translationY",0);
                animator.setDuration(600)
                        .start();
                scrollText.setVisibility(View.GONE);
                scrollEdit.setVisibility(View.VISIBLE);
                break;
            case R.id.down:
                ObjectAnimator animator1=ObjectAnimator.ofFloat(recyclerView,
                        "translationY",recyclerView.getHeight());
                animator1.setDuration(600)
                        .start();
                scrollText.setVisibility(View.VISIBLE);
                scrollEdit.setVisibility(View.GONE);
                RichText.fromMarkdown(editText.getText().toString())
                        .autoFix(true)
                        .showBorder(false)
                        .borderRadius(10)
                        .clickable(false)
                        .into(textView);
                break;
            case R.id.save:
                progressBar.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileUtil.WriteText(fileType,filename, FileUtil.TYPE_MD,editText.getText().toString());
                        handler.sendEmptyMessage(1);
                    }
                }).start();

                break;
        }
    }
    @Override
    public void onItemClickListener(String string) {
        switch (string) {
            case "* 列表":
                //列表类要在每次回车前添加标记
                preFlg = "* ";
                editText.append("\n" + string);
                editText.setSelection(editText.getText().length() - 2, editText.getText().length());
                break;
            case "> 引用":
                //引用类要在每次回车前添加标记
                preFlg = "> ";
                editText.append("\n" + string);
                editText.setSelection(editText.getText().length() - 2, editText.getText().length());
                break;
            case "\n```\n\n插入代码块\n```\n":
                //光标的位置
                preFlg = "";
                editText.append("\n" + string);
                editText.setSelection(editText.getText().length() - 10, editText.getText().length() - 5);
                break;
            case  "## 标题":
            case "###### 正文":
                preFlg = "";
                editText.append(string);
                editText.setSelection(editText.getText().length() - 2, editText.getText().length());
                break;
            case "  []() [链接文本](链接地址)":
                preFlg = "";
                ClipboardManager clipboardManager= (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ContentResolver contentResolver=getContentResolver();
                ClipData data=clipboardManager.getPrimaryClip();
                if (data!=null){
                    ClipData.Item item=data.getItemAt(0);
                    Uri uri=item.getUri();
                    if (uri!=null && "text/html"==contentResolver.getType(uri)){
                        Cursor cursor=contentResolver.query(uri,null,null,null,null);
                        if (cursor!=null&& cursor.moveToFirst()){
                            string.replace("链接地址",cursor.getString(0));
                        }
                    }
                }
                editText.append("\n" + string);
                break;
            case "~~ 删除的文本 ~~":
                preFlg = "";
                editText.append("\n" + string);
                editText.setSelection(editText.getText().length() - 8, editText.getText().length()-3);
                break;
            case " ![]() ![图片文本](图片地址) ":
                preFlg = "";
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.putExtra(IMAGE_MD_STRING,string);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,REQUEST_CODE_IMAGE);
                break;
            default:
                //其他类别不需要preFlg
                preFlg = "";
                editText.append("\n"+string);
                editText.setSelection(editText.getText().length());
                break;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            String insertString=data.getStringExtra(IMAGE_MD_STRING);
            if (resultCode==RESULT_OK&&requestCode==REQUEST_CODE_IMAGE){
                if (data!=null){
                    Uri uri=data.getData();
                    String[] path={MediaStore.Images.Media.DATA};
                    Cursor cursor=getContentResolver().query(uri,path,null,null,null);
                    if (cursor!=null && cursor.moveToFirst()){
                        int index=cursor.getColumnIndex(path[0]);
                        String string=cursor.getString(index);//图片本地路径
                        cursor.close();//guanbi
                        FileUtil.copyImage(string,uri.hashCode()+"",FileUtil.FILE_TYPE_HEAD);//将图片压缩保存
                        String photo= FileUtil.SAVE_IMAGE+uri.hashCode()+".jpg";//重新命名图片路径
                        insertString.replace("图片地址",photo);
                        editText.append(insertString);
                    }
                }
            }
        }
    }
}
