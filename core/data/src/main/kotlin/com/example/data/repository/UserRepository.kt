package com.example.data.repository

import android.net.Uri
import android.util.Log
import com.example.data.model.User
import com.example.data.model.UserCard
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val fireStorage: FirebaseStorage
) {
    private val cardRef = fireStorage.reference

    suspend fun cardUpload(imageUri: Uri, card: UserCard): Flow<String> = flow {
        try {
            val fileName = "${card.keyWord}.png"
            val imageUrl = suspendCoroutine { continuation ->
                val storageRef = cardRef.child("cards").child(fileName)

                storageRef.putFile(imageUri)
                    .continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let { throw it }
                        }
                        storageRef.downloadUrl
                    }
                    .addOnSuccessListener { uri ->
                        continuation.resume(uri.toString())
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            }
            emit(imageUrl)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getMainCardList(): Flow<List<UserCard>> = flow {
        try {
            val cardList = suspendCoroutine { continuation ->
                firestore.collection("cards").get()
                    .addOnSuccessListener { querySnapshot ->
                        val cards =
                            querySnapshot.documents.mapNotNull { it.toObject(UserCard::class.java) }
                        continuation.resume(cards) // 성공 시 리스트 반환
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception) // 실패 시 예외 반환
                    }
            }
            emit(cardList)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getMainCard(cardId: String): Flow<UserCard?> = flow {
        Log.d("아이디", cardId)
        try {
            val card = suspendCoroutine { continuation ->
                firestore.collection("cards")
                    .document(cardId)
                    .get() // 특정 조건으로 필터링
                    .addOnSuccessListener { querySnapshot ->
                        val userCard =
                            querySnapshot.toObject(UserCard::class.java) // UserCard 객체로 변환
                        continuation.resume(userCard) // 성공 시 데이터 반환
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception) // 실패 시 예외 반환
                    }
            }
            emit(card) // 결과 방출
        } catch (e: Exception) {
            throw e // 에러 전파
        }
    }

    suspend fun getRecentStorageCardId(): Flow<Int> = flow {
        try {
            val cid = suspendCoroutine { continuation ->
                firestore.collection("cards")
                    .orderBy("cid", Query.Direction.DESCENDING) // 정렬 기준 추가
                    .limit(1) // 마지막 문서 1개만 가져오기
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        val cardId =
                            querySnapshot.documents.firstNotNullOfOrNull { it.toObject(UserCard::class.java)?.cid }
                        continuation.resume(cardId) // 결과 반환
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception) // 예외 처리
                    }
            }

            if (cid != null) {
                emit(cid)
            } else {
                emit(0)
            }
        } catch (e: Exception) {
            throw e // 예외 전달
        }
    }

    fun confirmParticipateInUser(cardId : String, uid : String) : Flow<Boolean> = flow {
            val documentSnapshot = firestore
                .collection("cards")
                .document(cardId)
                .collection("participateUser")
                .document(uid)
                .get()
                .await()
        emit(documentSnapshot.exists())
    }

    fun updateParticipateInUser(cardId: String, peopleCount: String, user: User) {
        firestore
            .collection("cards")
            .document(cardId)
            .update("participatePeople", peopleCount)
            .addOnSuccessListener { Log.d("Firestore", "Success") }
            .addOnFailureListener { e -> Log.e("Firestore", "Failed", e) }
        firestore.collection("cards")
            .document(cardId)
            .collection("participateUser")
            .document(user.uid)
            .set(user)

    }

    fun saveUser(user: User) {
        firestore.collection("users").document(user.uid).set(user)
            .addOnSuccessListener { Log.d("Firestore", "Success") }
            .addOnFailureListener { e -> Log.e("Firestore", "Failed", e) }
    }

    fun saveCard(card: UserCard): Flow<Boolean> = flow {
        try {
            firestore.collection("cards").document(card.cid.toString())
                .set(card).await()

            firestore.collection("users").document(card.userId.toString())
                .collection("cards").document(card.cid.toString())
                .set(card).await()

            emit(true)
        } catch (e: Exception) {
            Log.e("SaveCard", "Failed to save card", e)
            emit(false)
        }
    }.flowOn(Dispatchers.IO)


    suspend fun getUser(userId: String): Flow<User?> = flow {
        try {
            val document = firestore.collection("users").document(userId).get().await()
            val user = document.toObject(User::class.java)
            emit(user)
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
        }
    }


    fun expUp(uid: String, exp: Int) {
        val documentReference = firestore
            .collection("users")
            .document(uid)

        documentReference.update("exp", FieldValue.increment(exp.toLong()))
    }

}
