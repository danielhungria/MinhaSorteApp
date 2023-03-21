package br.com.minhasortemegasena.repositories

import br.com.minhasortemegasena.model.SupportModel
import br.com.minhasortemegasena.retrofit.RetrofitService
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class MainRepository @Inject constructor(private val retrofitService: RetrofitService) {

    private val firebaseFireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getLotteryData(lotteryName: String) = retrofitService.getLotteryData(lotteryName)

    fun getLotteryWithContestNumber(contestNumber: String, lotteryName: String) = retrofitService.getLotteryWithContestNumber(contestNumber,lotteryName)

    fun querySupportService(): Task<QuerySnapshot> {
        return firebaseFireStore
            .collection("supportService")
            .get()
    }

    fun uploadSupport(supportModel: SupportModel, uuid: String){
        Firebase.firestore
            .collection("support")
            .document(uuid)
            .set(supportModel)
    }

}