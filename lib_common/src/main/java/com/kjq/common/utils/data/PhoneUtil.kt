package com.kjq.common.utils.data

import android.net.Uri
import android.provider.ContactsContract
import androidx.core.os.persistableBundleOf
import com.kjq.common.utils.Utils


object PhoneUtil {
    // 号码
    private const val PHONE = ContactsContract.CommonDataKinds.Phone.NUMBER
    // 联系人姓名
    private const val NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME

    //联系人提供者的uri
    private val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

    //获取所有联系人
    val phone: ArrayList<PhoneEntity>
        get() {
            val sPhoneEntities = ArrayList<PhoneEntity>()
            val cr = Utils.getContext().contentResolver
            val cursor = cr.query(phoneUri, arrayOf(PHONE, NAME), null, null, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val sPhoneEntity = PhoneEntity(cursor.getString(cursor.getColumnIndex(NAME)), cursor.getString(cursor.getColumnIndex(PHONE)))
                    sPhoneEntities.add(sPhoneEntity)
                }
            }
            cursor?.close()
            return sPhoneEntities
        }

    fun getPhoneEntity(uri: Uri): PhoneEntity {
        val phoneEntity = PhoneEntity("","")
        //得到ContentResolver对象
        val cr = Utils.getContext().contentResolver
        //取得电话本中开始一项的光标
        val cursor = cr.query(uri, null, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            //取得联系人姓名
            val nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            phoneEntity.name = cursor.getString(nameFieldColumnIndex)
            //取得电话号码
            val contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
            val phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null)
            if (phone != null) {
                phone.moveToFirst()
                phoneEntity.phone = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            }
            phone?.close()
            cursor.close()
        } else {
            return phoneEntity
        }
        return phoneEntity
    }
}
