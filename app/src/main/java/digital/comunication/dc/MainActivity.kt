package digital.comunication.dc

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.net.Uri
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.util.Locale;

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    lateinit var TTS: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        call_1.setOnClickListener {
            makeCall("88432782428")
        }
        call_2.setOnClickListener {
            makeCall("88432923359")
        }
        call_3.setOnClickListener {
            makeCall("88432927284")
        }
        call_4.setOnClickListener {
            makeCall("88432925885")
        }

        message_1.setOnClickListener {
            makeSms("88432782428")
        }
        message_2.setOnClickListener {
            makeSms("88432923359")
        }
        message_3.setOnClickListener {
            makeSms("88432927284")
        }
        message_4.setOnClickListener {
            makeSms("88432925885")
        }

        TTS = TextToSpeech(this, this)
        fab.setOnClickListener {
            TTS.speak("Чем я могу помочь?", TextToSpeech.QUEUE_FLUSH, null)
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Чем я могу помочь?")
            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE)
        }
    }

    private fun makeCall(number: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        startActivity(intent)
        overridePendingTransition(R.anim.forward_open, R.anim.forward_close)
    }

    private fun makeSms(number: String, text: String? = null) {
        val uri = Uri.parse("smsto:$number")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        if(text != null) intent.putExtra("sms_body", text)
        startActivity(intent)
    }

    @SuppressLint("DefaultLocale")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val commandList = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            commandList.forEach {
                when {
                    it.toLowerCase().contains("позвон") -> when {
                        it.toLowerCase().contains("теплоснабжен") -> {
                            makeCall("88432782428")
                            return
                        }
                        it.toLowerCase().contains("электросет") -> {
                            makeCall("88432923359")
                            return
                        }
                        it.toLowerCase().contains("горсвет") -> {
                            makeCall("88432927284")
                            return
                        }
                        it.toLowerCase().contains("горгаз") -> {
                            makeCall("88432925885")
                            return
                        }
                    }
                    it.toLowerCase().contains("напиш") -> {
                        var message = ""
                        var isFind = false
                        it.toLowerCase().split(" ").forEach { c ->
                            run {
                                if (isFind) message += "$c "
                                if (
                                    c.contains("теплоснабжен") ||
                                    c.contains("электросет") ||
                                    c.contains("горсвет") ||
                                    c.contains("горгаз")
                                ) isFind = true
                            }
                        }
                        if(message.startsWith("что")) message = message.substringAfter("что ")
                        when {
                            it.toLowerCase().contains("теплоснабжен") -> {
                                makeSms("88432782428", message)
                                return
                            }
                            it.toLowerCase().contains("электросет") -> {
                                makeSms("88432923359", message)
                                return
                            }
                            it.toLowerCase().contains("горсвет") -> {
                                makeSms("88432927284", message)
                                return
                            }
                            it.toLowerCase().contains("горгаз") -> {
                                makeSms("88432925885", message)
                                return
                            }
                        }
                    }
                }
            }

            TTS.speak("Простите, я Вас не понимаю", TextToSpeech.QUEUE_FLUSH, null)
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onInit(status: Int) {
        // TODO Auto-generated method stub
        if (status == TextToSpeech.SUCCESS) {

            val locale = Locale("ru")

            val result = TTS.setLanguage(locale)
            //int result = mTTS.setLanguage(Locale.getDefault());

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {

            }
        }
    }

    companion object {
        const val VOICE_RECOGNITION_REQUEST_CODE = 3
    }
}
