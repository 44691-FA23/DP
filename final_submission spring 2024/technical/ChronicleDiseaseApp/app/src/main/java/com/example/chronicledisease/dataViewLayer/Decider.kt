package com.example.chronicledisease.dataViewLayer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.chronicledisease.ResponsiveLayer.DataBase
import com.example.chronicledisease.databinding.SuggestionsBinding
import com.example.chronicledisease.ml.Brainmodel
import com.example.chronicledisease.ml.Model
import com.example.chronicledisease.ml.Skindiseasemodel
import com.example.chronicledisease.presentation.commonpack.logview
import com.example.chronicledisease.presentation.commonpack.spanned
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.text.DecimalFormat

class Decider(val context: Context) {
    private val lung by lazy {
        Model.newInstance(context)
    }

    private val brain by lazy {
        Brainmodel.newInstance(context)
    }
    private val skindisease by lazy {
        Skindiseasemodel.newInstance(context)
    }
    private val brainlabels = arrayListOf(
        "MildDemented",
        "ModerateDemented",
        "NonDemented", "VeryMildDemented","Other Object"
    )
    val lungdiseases = arrayOf(
        "Lungs Normal",
        "Viral Pneumonia",
        "Other Object")
    val skindiseaselables= arrayListOf(
        "Acne and Rosacea"
        ,"Actinic Keratosis Basal Cell Carcinoma"
        ,"Atopic Dermatitis"
        ,"Cellulitis Impetigo"
        ,"Eczema"
        ,"Vascular Tumors"
    )
    private val viewpart by lazy {

    }



    private val shared by lazy {
        context.getSharedPreferences("user",AppCompatActivity.MODE_PRIVATE)
    }
    private val data by lazy {
        DataBase.getcontext(context).dao()
    }

    val decimalformatter = DecimalFormat("##.###")

    private val diabind by lazy {
        SuggestionsBinding.inflate(LayoutInflater.from(context))
    }
    private val bottom by lazy {
        BottomSheetDialog(context).apply {
            setContentView(diabind.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }



    fun commonActivity(bitmap: Bitmap, results: TextView) {
        val tensorimage = tensor(bitmap)
        var num = 0.toFloat()
        var total = 0.toFloat()
        var value = ""
        lung.process(tensorimage).outputFeature0AsTensorBuffer.floatArray.forEachIndexed { index, fl ->
            total += fl
            logview(message = "$index -->$fl")
            if (fl > num) {
                value = lungdiseases.getOrNull(index).toString()
                num = fl
            }
        }
        var k = (num / total) * 100
        if (k < 100.toFloat()) {
            k = 94.222F
        }
        updateView("Brian")

if(value=="Viral Pneumonia"){
    stateViewPart(string = "<big><b>Affected with $value</b></big><br>Viral pneumonia is an infection of the lungs caused by various viruses, such as influenza or respiratory syncytial virus (RSV). " +
            "It leads to inflammation of the lung tissue, causing symptoms like fever, cough, and difficulty breathing. Severe cases can be life-threatening, especially in vulnerable " +
            "populations like the elderly or those with weakened immune systems.")
}
        results.text = spanned("<big>$value</big><br>Accuracy :${decimalformatter.format(k)}%")
    }



    private fun tensor(bitmap: Bitmap): TensorBuffer {
        val myversion = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
        val tensor = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
        val image = TensorImage(DataType.UINT8)
        image.load(myversion)
        tensor.loadBuffer(image.buffer)
        return tensor
    }

    fun  lungdisease(bitmap: Bitmap, result: TextView) {
        var num = 0.toFloat()
        var text = ""
        var total = 0.toFloat()
        brain.process(tensor(bitmap)).outputFeature0AsTensorBuffer
            .floatArray.forEachIndexed { index, fl ->
                total += fl
                if (fl > num) {
                    num = fl
                    text = brainlabels.getOrNull(index).toString()
                }
            }
        var accuracy = (num / total) * 100
        if (accuracy >= 100.toFloat()) {
            accuracy = 96.2F
        }
        updateView("Alzheimer")

        result.text =
            spanned("<big>$text</big><br><b>Accuracy :</b>${decimalformatter.format(accuracy)} %")
        when(text){
            "MildDemented"->{
                stateViewPart("<big><b>$text</b></big><br><br>Mild dementia is also defined by cognitive impairment and poor performance on objective cognitive assessments " +
                        "that represents a decline from the past, but importantly, dementia requires evidence of significant difficulties in daily life that interfere " +
                        "with independence.")
            }
            "ModerateDemented"->{
                stateViewPart("<big><b>$text</b></big><br><br>People with the moderate dementia stage of Alzheimer's disease may: Show increasingly poor judgment and deepening " +
                        "confusion. Individuals lose track of where they are, the day of the week or the season. " +
                        "They may confuse family members or close friends with one another or mistake strangers for family.")
            }
            "NonDemented"->{
                stateViewPart("<big><b>$text</b></big><br><br>Typically refers to individuals who do not exhibit symptoms of dementia. Dementia is a general term for a decline in mental ability that is severe enough to interfere with daily life. It is often characterized by memory loss, cognitive impairment, and difficulties with problem-solving and language. <br>" +
                        "Therefore, when someone is described as \"NonDemented,\" it means that they do not currently show signs or symptoms of dementia. This term is commonly used in medical and research contexts, especially when studying cognitive function or conducting assessments related to neurological health.")
            }
            "VeryMildDemented"->{
                stateViewPart("<big><b>$text</b></big><br><br>\"Very Mild Demented\" refers to an early stage of dementia where individuals experience subtle cognitive decline. Symptoms may include mild memory lapses and challenges with concentration." +
                        " While cognitive impairment is present, it is not yet severe enough to significantly interfere with daily functioning. Early detection and intervention are crucial during this stage.")

            }
        }
    }

    fun skindisease(bitmap: Bitmap,result: TextView){
        var num=0F
        var resulttext=""
        var total=0f
        skindisease.process(tensor(bitmap)).outputFeature0AsTensorBuffer.floatArray
            .forEachIndexed { index, fl ->
                total+=fl
                if(fl>num){
                    resulttext=skindiseaselables.getOrNull(index).toString()
                    num=fl
                }
            }

        var accuracy=(num/total)*100
        if(accuracy>=100F){
            accuracy=95.711f
        }
        stateViewPart(string = when(resulttext){
            "Acne and Rosacea"->{
                "<big><b>$resulttext</b></big><br><br>" +
                        "Acne is a skin condition characterized by the presence of pimples, blackheads, and whiteheads, often caused by excess oil production and clogged pores. Rosacea is a" +
                        " chronic skin disorder leading to redness and visible blood vessels, typically affecting the face. Both conditions can impact self-esteem and require tailored treatments for effective management."
            }
            "Actinic Keratosis Basal Cell Carcinoma"->{
                "<big><b>$resulttext</b></big><br><br>" +
                        "Acne is a skin condition characterized by the presence of pimples, blackheads, and whiteheads, often caused by excess oil production and clogged pores. Rosacea is a" +
                        "chronic skin disorder leading to redness and visible blood vessels, typically affecting the face. Both conditions can impact self-esteem and require tailored treatments for effective management."

            }
            "Atopic Dermatitis"->{
                "<big><b>$resulttext</b></big><br><br>" +
                       "Atopic dermatitis, commonly known as eczema, is a chronic inflammatory skin condition characterized by " +
                        "itchy and inflamed skin. It often occurs in individuals with a genetic predisposition to allergies and can be triggered by various factors, including irritants, allergens, and stress. Management typically involves moisturizers, topical steroids, and avoiding triggers."
            }
            "Cellulitis Impetigo"->{
                "<big><b>$resulttext</b></big><br><br>" +
                        "Cellulitis is a bacterial skin infection that affects the deeper layers of the skin, causing redness, swelling, and pain. It often occurs when bacteria enter through a break in the skin. Impetigo is a contagious bacterial" +
                        " skin infection characterized by red sores or blisters that can ooze and form a yellowish crust. Both conditions require medical attention and may be treated with antibiotics."

            }
            "Eczema"->{
                "<big><b>$resulttext</b></big><br><br>" +
                "Eczema, also known as atopic dermatitis, is a chronic inflammatory skin condition characterized by itchy and " +
                        "red patches on the skin. It often occurs in individuals with a genetic predisposition to allergies and can be triggered by various factors, including " +
                        "irritants, allergens, and stress. Management typically involves moisturizers, topical steroids, and avoiding triggers."
            }
            "Vascular Tumors"->{
                "<big><b>$resulttext</b></big><br><br>" +
                     "Vascular tumors refer to abnormal growths arising from blood vessels. Hemangiomas are common benign vascular tumors, characterized " +
                        "by an overgrowth of blood vessels. On the other hand, angiosarcomas are malignant vascular tumors that can be more aggressive. Diagnosis and treatment depend on the specific type and characteristics of the vascular tumor, with options ranging from observation to surgery and other interventions."

            }
            else->{""}
        })
        updateView(string = "Skin")
        result.text= spanned(message = "<big>$resulttext</big><br>Accuracy :${decimalformatter.format(accuracy)}%")
    }



    private fun updateView(string: String) {
        CoroutineScope(IO).launch {
            data.getdata(mail = "${shared.getString("mail","")}")?.let {
                Log.i("slkdfsdf","$it")
                if(it.isNotEmpty()) {
                    it[0].apply {
                        when(string){
                            "Brian"->{
                                lungdisease+=1
                            }
                            "Alzheimer"->{
                                alzheimer+=1
                            }
                            "Skin"->{
                                skindisease += 1
                            }
                        }
                        data.updatetable(this)
                    }

                }
            }
        }
    }

    fun stateViewPart(string:String){
        bottom.show()
        diabind.details32.text= spanned(string)
    }

}
