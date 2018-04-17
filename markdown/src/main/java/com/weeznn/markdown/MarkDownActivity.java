package com.weeznn.markdown;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.Callback;

public class MarkDownActivity extends AppCompatActivity implements BtnAdaptar.ItemClickListener {
    private static final String TAG = MarkDownActivity.class.getSimpleName();
    private static final int REQUEST_CODE_IMAGE=1;
    private static final String IMAGE_MD_STRING="IMAGE_MD_STRING";

    private RecyclerView recyclerView;
    private EditText editText;
    private Toolbar toolbar;
    private String preFlg = "";

    private StringBuilder stringBuilder = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markdown);

        recyclerView = findViewById(R.id.recycler);
        editText = findViewById(R.id.edit);
        toolbar=findViewById(R.id.toolbar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        BtnAdaptar btnAdaptar = new BtnAdaptar(this);
        recyclerView.setAdapter(btnAdaptar);
        btnAdaptar.setItemClickListener(this);

        editText.setFocusable(true);
        final boolean[] insert = {true};
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
                if (item.getItemId()==R.id.down){
                    RichText.fromMarkdown(editText.getText().toString())
                            .autoFix(true)
                            .showBorder(false)
                            .borderRadius(10)
                            .clickable(true)
                            .done(new Callback() {
                                @Override
                                public void done(boolean imageLoadDone) {

                                }
                            })
                            .into(editText);
                }
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
//                    FileUtil.copyImage(path,name,FileUtil.FILE_TYPE_HEAD);//将图片压缩保存
//                    String photo=photo.HEAD_PATH+name+".jpg";//重新命名图片路径
//                    insertString.replace("图片地址",photo);
                }
            }
        }
    }
}
