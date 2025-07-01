package wifi.key.show.master.show.all.wifi.password.ActivityNew

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import wifi.key.show.master.show.all.wifi.password.Activities.WifiAutomaticActivity
import wifi.key.show.master.show.all.wifi.password.R
import wifi.key.show.master.show.all.wifi.password.Recievers.WifiReciever
import wifi.key.show.master.show.all.wifi.password.SpecificWifiReminder.DatabaseHelper
import wifi.key.show.master.show.all.wifi.password.databinding.ActivitySpecifiTimeNewBinding
import java.text.SimpleDateFormat
import java.util.*

class SetTimeActivity: AppCompatActivity() {
    private lateinit var binding:ActivitySpecifiTimeNewBinding
    var sqLiteDatabase: SQLiteDatabase? = null
    var mydb: DatabaseHelper? = null
    var databaseHelper:DatabaseHelper?=null

    //    ListView listView;
    var wifiManager: WifiManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySpecifiTimeNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mydb = DatabaseHelper(this)
        sqLiteDatabase = mydb?.getWritableDatabase()

        binding.butSelectDate.setOnClickListener{
            binding.datePicker.visibility= View.VISIBLE
            binding.timePicker.visibility= View.GONE
            binding.butSaveTime.visibility= View.GONE
        }


        binding.butSelectTime.setOnClickListener{
            binding.datePicker.visibility= View.GONE
            binding.timePicker.visibility= View.VISIBLE
            binding.butSaveTime.visibility= View.VISIBLE
        }

        databaseHelper= DatabaseHelper(this)

        binding.butSaveTime.setOnClickListener{

            val cv = ContentValues()

            cv.put(DatabaseHelper.TIME, getString(R.string.Not_Set_Alert))

            val calender = Calendar.getInstance()
            calender.clear()
            calender[Calendar.MONTH] = binding.datePicker.getMonth()
            calender[Calendar.DAY_OF_MONTH] = binding.datePicker.getDayOfMonth()
            calender[Calendar.YEAR] = binding.datePicker.getYear()
            calender[Calendar.HOUR] = binding.timePicker.getCurrentHour()
            calender[Calendar.MINUTE] = binding.timePicker.getCurrentMinute()
            calender[Calendar.SECOND] = 0

            val formatter = SimpleDateFormat(getString(R.string.hour_minutes))
            val timeString = formatter.format(Date(calender.timeInMillis))
            val dateformatter = SimpleDateFormat(getString(R.string.dateformate))
            val dateString = dateformatter.format(Date(calender.timeInMillis))

            val alarmMgr = getSystemService(ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, WifiReciever::class.java)

            val pendingIntent: PendingIntent
            pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            alarmMgr[AlarmManager.RTC_WAKEUP, calender.timeInMillis] = pendingIntent
            cv.put(DatabaseHelper.TIME, timeString)
            cv.put(DatabaseHelper.TITLE, "")
            cv.put(DatabaseHelper.DATE, dateString)

            sqLiteDatabase!!.insert(DatabaseHelper.TABLE_NAME, null, cv)

            val openMainScreen = Intent(this, WifiAutomaticActivity::class.java)
            openMainScreen.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(openMainScreen)
            Toast.makeText(this, getString(R.string.alarm_set), Toast.LENGTH_SHORT).show()
        }

    }
}