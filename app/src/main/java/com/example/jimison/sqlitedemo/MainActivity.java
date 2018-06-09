package com.example.jimison.sqlitedemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DBHelper dbHelper = null;
    EditText edtUser, edtTel, edtMail, edtId;
    Button btnAdd, btnUpdate, btnDel;
    ListView listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        openDatabase();
        showData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDatabase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                add();
                break;
            case R.id.btnDel:
                delete();
                break;
            case R.id.btnUpdate:
                update();
                break;
        }
        showData();
    }

    public void findViews() {
        edtId = findViewById(R.id.edtId);
        edtMail = findViewById(R.id.edtEmail);
        edtTel = findViewById(R.id.edtTel);
        edtUser = findViewById(R.id.edtUser);
        btnAdd = findViewById(R.id.btnAdd);
        btnDel = findViewById(R.id.btnDel);
        btnUpdate = findViewById(R.id.btnUpdate);
        listData = findViewById(R.id.listData);
        btnUpdate.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnDel.setOnClickListener(this);
    }

    public void openDatabase() {
        dbHelper = new DBHelper(this);
    }

    public void closeDatabase() {
        dbHelper.close();
    }

    public void showData() {  //資料庫中有個專有名詞為資料集(Cursor,DataSet)
        Cursor cursor = getCursor();
        String[] from = {"_id","name", "tel", "email"};
        int[] to = {R.id.txtId,R.id.txtName,R.id.txtTel,R.id.txtMail};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.dataitem,cursor,from,to);
        listData.setAdapter(adapter);
    }

    private void add() {  //欄位的內容不得為空值，實務上前端需做防呆的措施
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", edtUser.getText().toString());
        values.put("tel", edtTel.getText().toString());
        values.put("email", edtMail.getText().toString());
        db.insert("member", null, values); //參數2填入null表示會在資料表中建立一個欄位具有空字串，但是並非null
        cleanEditText();
    }

    private void delete() {  //請記得用AlertDialog向使用者確認是否刪除
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String id = edtId.getText().toString();
        String where = "_id = ?"; //較正規的寫法
        String[] args = {id};
        db.delete("member", where, args);  //參數2為條件式，參數3為條件式的參數
        cleanEditText();
    }

    private void update() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", edtUser.getText().toString());
        values.put("tel", edtTel.getText().toString());
        values.put("email", edtMail.getText().toString());
        String id = edtId.getText().toString();
        db.update("member", values, "_id = " + id, null); //這裡將條件式與條件值寫再一起了
        cleanEditText();
    }

    private void cleanEditText() {
        edtUser.setText("");
        edtTel.setText("");
        edtMail.setText("");
        edtId.setText("");
    }

    private Cursor getCursor(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"_id", "name", "tel", "email"};
        //android提供的
        Cursor cursor = db.query("member", columns, null, null, null, null, null);
        //五個null分別表示，條件式，條件式的值，群組運算，群組的條件式，排序方式
        //db.rawQuery("select * from member", columns);  //標準的sql語法
        return cursor;
    }
}
