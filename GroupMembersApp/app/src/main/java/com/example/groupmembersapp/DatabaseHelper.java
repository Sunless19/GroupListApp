package com.example.groupmembersapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "members.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_MEMBERS = "members";

    // Column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ROLE = "role";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_DETAILS = "details";
    private static final String KEY_WEB_URL = "web_url";

    // Create table SQL query
    private static final String CREATE_TABLE_MEMBERS = "CREATE TABLE " + TABLE_MEMBERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_ROLE + " TEXT,"
            + KEY_IMAGE_URL + " TEXT,"
            + KEY_DETAILS + " TEXT,"
            + KEY_WEB_URL + " TEXT"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEMBERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBERS);
        onCreate(db);
    }

    // Insert a new member
    public long insertMember(Member member) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, member.getId());
        values.put(KEY_NAME, member.getName());
        values.put(KEY_ROLE, member.getRole());
        values.put(KEY_IMAGE_URL, member.getImageUrl());
        values.put(KEY_DETAILS, member.getDetails());
        values.put(KEY_WEB_URL, member.getWebUrl());
        long id = db.insert(TABLE_MEMBERS, null, values);
        db.close();
        return id;
    }

    public Member getMemberById(int id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Member member = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.query(TABLE_MEMBERS, null, KEY_ID + " = ?",
                    new String[]{String.valueOf(id)}, null, null, null, null);



            if (cursor != null && cursor.moveToFirst()) {
                // Get column indices (safer than hardcoding positions)
                int idIndex = cursor.getColumnIndex(KEY_ID);
                int nameIndex = cursor.getColumnIndex(KEY_NAME);
                int roleIndex = cursor.getColumnIndex(KEY_ROLE);
                int imageUrlIndex = cursor.getColumnIndex(KEY_IMAGE_URL);
                int detailsIndex = cursor.getColumnIndex(KEY_DETAILS);
                int webUrlIndex = cursor.getColumnIndex(KEY_WEB_URL);

                // Create member object with null checks
                member = new Member(
                        cursor.getInt(idIndex),
                        cursor.getString(nameIndex),
                        cursor.getString(roleIndex),
                        cursor.getString(imageUrlIndex),
                        cursor.getString(detailsIndex),
                        cursor.getString(webUrlIndex)
                );


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return member;
    }

    // Get all members
    public List<Member> getAllMembers() {
        List<Member> memberList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MEMBERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Member member = new Member(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE_URL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DETAILS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_WEB_URL))
                );
                memberList.add(member);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return memberList;
    }

    // Update a member
    public int updateMember(Member member) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, member.getName());
        values.put(KEY_ROLE, member.getRole());
        values.put(KEY_IMAGE_URL, member.getImageUrl());
        values.put(KEY_DETAILS, member.getDetails());
        values.put(KEY_WEB_URL, member.getWebUrl());

        int result = db.update(TABLE_MEMBERS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(member.getId())});
        db.close();
        return result;
    }
    public void checkDatabaseStatus() {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();

            // Check if the table exists
            cursor = db.rawQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                    new String[]{TABLE_MEMBERS});

            boolean tableExists = cursor != null && cursor.moveToFirst();

            if (cursor != null) {
                cursor.close();
            }

            if (tableExists) {
                cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_MEMBERS, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int count = cursor.getInt(0);
                }
            }
        } catch (Exception e) {}

    }

    public void deleteMember(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEMBERS, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}