package com.example.bookstore;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookstore.adapters.DBHelper2;

import java.text.DateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    DBHelper2 dbHelper2;
    Button BtnProses, BtnBayar;
    EditText TxtId, TxtJudul, TxtAuthor, TxtHarga, TxStatus, TxtBayar;
    TextView TvBayar;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        this.setTitle("Input Data");

        dbHelper2 = new DBHelper2(this);

        id = getIntent().getLongExtra(DBHelper2.row_id, 0);

        TxtId = (EditText)findViewById(R.id.txtId);
        TxtJudul = (EditText)findViewById(R.id.txtJudul);
        TxtAuthor = (EditText)findViewById(R.id.txtBookAuthor);
        TxtHarga = (EditText)findViewById(R.id.txtHarga);
        TxStatus = (EditText)findViewById(R.id.txStatus);
        TxtBayar = (EditText)findViewById(R.id.txtBayar2);
        TvBayar = (TextView)findViewById(R.id.tvBayar);
        BtnProses = (Button)findViewById(R.id.btnProses);
        BtnBayar = (Button)findViewById(R.id.btnBayar2);

        getData();

        BtnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prosesEdit();
            }
        });

        BtnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prosesBayar();
            }
        });

        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setDisplayHomeAsUpEnabled(true);
    }

    private void prosesBayar() {
        int hb = Integer.parseInt(TxtHarga.getText().toString());
        int ub = Integer.parseInt(TxtBayar.getText().toString());

        double uangkembalian = (ub-hb);
        if (TxtBayar.equals("")){
            TxtBayar.setError("Silahkan Isi Jumlah Pembayaran Anda!");
        }
        if (ub==hb) {
            Toast.makeText(AddActivity.this, "Pembayaran Berhasil, Tidak ada uang kembalian.", Toast.LENGTH_SHORT).show();
            dbHelper2.deleteData(id);
            finish();
        } else if (ub<hb) {
            Toast.makeText(AddActivity.this, "Uang Bayar Kurang Rp. " + (-uangkembalian), Toast.LENGTH_SHORT).show();
        } else {
            dbHelper2.deleteData(id);
            Toast.makeText(AddActivity.this, "Pembayaran Berhasil, Uang kembalian Rp. " + (+uangkembalian), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void prosesEdit() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
        builder.setMessage("Proses Edit Buku ?");
        builder.setCancelable(true);
        builder.setPositiveButton("Proses", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                insertAndUpdate();
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getData() {

        Cursor cur = dbHelper2.oneData(id);
        if (cur.moveToFirst()){
            String idBuku = cur.getString(cur.getColumnIndex(DBHelper2.row_id));
            String judul = cur.getString(cur.getColumnIndex(DBHelper2.row_judul));
            String author = cur.getString(cur.getColumnIndex(DBHelper2.row_author));
            String harga = cur.getString(cur.getColumnIndex(DBHelper2.row_harga));

            TxtId.setText(idBuku);
            TxtJudul.setText(judul);
            TxtAuthor.setText(author);
            TxtHarga.setText(harga);

            if (TxtId.equals("")){
                BtnProses.setVisibility(View.GONE);
            }else {
                BtnProses.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        String idpinjam = TxtId.getText().toString().trim();

        MenuItem itemDelete = menu.findItem(R.id.action_delete);
        MenuItem itemClear = menu.findItem(R.id.action_clear);
        MenuItem itemSave = menu.findItem(R.id.action_save);
        MenuItem itemTransaksi = menu.findItem(R.id.action_transaksi);


        if (idpinjam.equals("")){
            itemDelete.setVisible(false);
            itemTransaksi.setVisible(false);
            itemClear.setVisible(true);
        }else {
            this.setTitle("Edit Data");
            itemDelete.setVisible(true);
            itemTransaksi.setVisible(true);
            itemClear.setVisible(false);
            itemSave.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_save:
                insertAndUpdate();
        }
        switch (item.getItemId()){
            case R.id.action_clear:
                TxtJudul.setText("");
                TxtAuthor.setText("");
                TxtHarga.setText("");
        }
        switch (item.getItemId()){
            case R.id.action_delete:
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                builder.setMessage("Data ini akan dihapus");
                builder.setCancelable(true);
                builder.setPositiveButton("hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper2.deleteData(id);
                        Toast.makeText(AddActivity.this,"Terhapus", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
        }
        switch (item.getItemId()){
            case R.id.action_transaksi:
                this.setTitle("Transaksi");

                BtnProses.setVisibility(View.GONE);
                BtnBayar.setVisibility(View.VISIBLE);
                TvBayar.setVisibility(View.VISIBLE);
                TxtBayar.setVisibility(View.VISIBLE);
                TxtJudul.setEnabled(false);
                TxtAuthor.setEnabled(false);
                TxtHarga.setEnabled(false);
        }

        return super.onOptionsItemSelected(item);
    }

    public void insertAndUpdate(){
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        TxStatus.setText(currentDateTimeString);
        String idBuku = TxtId.getText().toString().trim();
        String judul = TxtJudul.getText().toString().trim();
        String author = TxtAuthor.getText().toString().trim();
        String harga = TxtHarga.getText().toString().trim();
        String status = TxStatus.getText().toString().trim();

        ContentValues values = new ContentValues();

        values.put(DBHelper2.row_judul, judul);
        values.put(DBHelper2.row_author, author);
        values.put(DBHelper2.row_harga, harga);

        if (judul.equals("")){
            TxtJudul.setError("Judul Tidak Boleh Kosong!");
        }
        if (author.equals("")){
            TxtAuthor.setError("Author Tidak Boleh Kosong!");
        }
        if (harga.equals("")){
            TxtHarga.setError("Harga Tidak Boleh Kosong!");
        }
        if (judul.equals("") || harga.equals("") || author.equals("")){
            Toast.makeText(AddActivity.this, "Isi data dengan Lengkap!", Toast.LENGTH_SHORT).show();
        }else {
            if (idBuku.equals("")){
                values.put(DBHelper2.row_status, status);
                dbHelper2.insertData(values);
            }else {
                dbHelper2.updateData(values, id);
            }

            Toast.makeText(AddActivity.this,"Data Tersimpan", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}