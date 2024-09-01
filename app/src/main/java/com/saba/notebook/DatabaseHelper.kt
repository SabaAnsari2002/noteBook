package com.saba.notebook

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 3
        private const val DATABASE_NAME = "NoteBook"
        private const val TABLE_USERS = "users"
        private const val TABLE_NOTES = "notes"
        private const val KEY_ID = "id"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_NOTE_ID = "note_id"
        private const val KEY_NOTE_TITLE = "note_title"
        private const val KEY_NOTE_DATE = "note_date"
        private const val KEY_NOTE_TEXT = "note_text"
        private const val KEY_USER_ID = "user_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_USERS_TABLE = ("CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USERNAME + " TEXT UNIQUE,"
                + KEY_PASSWORD + " TEXT" + ")")
        db.execSQL(CREATE_USERS_TABLE)

        val CREATE_NOTES_TABLE = ("CREATE TABLE " + TABLE_NOTES + "("
                + KEY_NOTE_ID + " INTEGER PRIMARY KEY,"
                + KEY_NOTE_TITLE + " TEXT,"
                + KEY_NOTE_DATE + " TEXT,"
                + KEY_NOTE_TEXT + " TEXT,"
                + KEY_USER_ID + " INTEGER,"
                + "FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + ")" + ")")
        db.execSQL(CREATE_NOTES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")

        onCreate(db)
    }

    fun addNote(userId: Int, title: String, date: String, noteText: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NOTE_TITLE, title)
        values.put(KEY_NOTE_DATE, date)
        values.put(KEY_NOTE_TEXT, noteText)
        values.put(KEY_USER_ID, userId)
        return db.insert(TABLE_NOTES, null, values)
    }


    fun getNotesByUserId(userId: Int): List<String> {
        val notes = ArrayList<String>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NOTES, arrayOf(KEY_NOTE_TEXT), "$KEY_USER_ID=?",
            arrayOf(userId.toString()), null, null, null)
        if (cursor.moveToFirst()) {
            do {
                notes.add(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTE_TEXT)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return notes
    }

    fun addUser(username: String, password: String): Long {
        if (isUsernameExists(username)) {
            return -1 // برگرداندن -1 در صورتی که نام کاربری موجود باشد
        }

        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_USERNAME, username)
        values.put(KEY_PASSWORD, password)
        return db.insert(TABLE_USERS, null, values)
    }

    fun isUsernameExists(username: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(KEY_ID), "$KEY_USERNAME=?",
            arrayOf(username), null, null, null)
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun getUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(KEY_ID), "$KEY_USERNAME=? AND $KEY_PASSWORD=?",
            arrayOf(username, password), null, null, null)
        val result = cursor.count > 0
        cursor.close()
        return result
    }

    fun checkTables() {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                Log.d("DB Tables", "Table Name: " + cursor.getString(0))
                cursor.moveToNext()
            }
        }
        cursor.close()
    }
}
