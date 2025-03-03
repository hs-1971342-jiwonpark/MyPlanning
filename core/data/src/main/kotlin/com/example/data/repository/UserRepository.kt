package com.example.data.repository

import android.net.Uri
import android.util.Log
import com.example.data.model.CommentUser
import com.example.data.model.User
import com.example.data.model.UserCard
import com.google.firebase.firestore.AggregateSource
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
    private val fireStorageRef = fireStorage.reference
    private val cardStorageRef = fireStorageRef.child("cards")
    private val cardStoreRef = firestore.collection("cards")
    private val userStoreRef = firestore.collection("users")

    //카드 업로드
    suspend fun cardUpload(imageUri: Uri, card: UserCard): Flow<String> = flow {
        try {
            val fileName = "${card.keyWord}.png"
            val imageUrl = suspendCoroutine { continuation ->
                val storageRef = cardStorageRef.child(fileName)

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

    //플래닛 카드 리스트 가저오기
    suspend fun getMainCardList(): Flow<List<UserCard>> = flow {
        try {
            val cardList = suspendCoroutine { continuation ->
                cardStoreRef.get()
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

    suspend fun getHoldCardList(userId: String): Flow<List<String>> = flow {
        try {
            val cardIdList = userStoreRef.document(userId)
                .collection("participateCard")
                .get()
                .await()
                .documents.map { it.id }

            emit(cardIdList) // Flow로 반환
        } catch (e: Exception) {
            throw e
        }
    }

    //선택한 카드 가저오기
    suspend fun getMainCard(cardId: String): Flow<UserCard?> = flow {
        Log.d("아이디", cardId)
        try {
            val card = suspendCoroutine { continuation ->
                cardStoreRef
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

    //마지막에 저장한 카드 가저오기 (카드 추가용)
    suspend fun getRecentStorageCardId(): Flow<Int> = flow {
        try {
            val cid = suspendCoroutine { continuation ->
                cardStoreRef
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

    //해당 행성 참여중인 유저 가저오기
    fun confirmParticipateInUser(cardId: String, uid: String): Flow<Boolean> = flow {
        Log.d("카드 송신", "$cardId")
        val documentSnapshot = cardStoreRef
            .document(cardId)
            .collection("participateUser")
            .document(uid)
            .get()
            .await()
        emit(documentSnapshot.exists())
    }

    //참여중인 유저 수 늘리기
    fun updateParticipateInUser(cardId: String, peopleCount: String, user: User) {
        cardStoreRef
            .document(cardId)
            .update("participatePeople", peopleCount)
            .addOnSuccessListener { Log.d("Firestore", "Success") }
            .addOnFailureListener { e -> Log.e("Firestore", "Failed", e) }

        cardStoreRef
            .document(cardId)
            .collection("participateUser")
            .document(user.uid)
            .set(user)

        userStoreRef
            .document(user.uid)
            .collection("participateCard")
            .document(cardId)
            .set(cardId to cardId)

    }

    //카드 업로드
    suspend fun commentImgUpload(imageUri: Uri, cid: String): Flow<String> = flow {
        try {
            val fileName = "${imageUri.lastPathSegment}.png" //후에수정
            val imageUrl = suspendCoroutine { continuation ->
                val imgRef = cardStorageRef.child("$cid/$fileName")

                imgRef.putFile(imageUri)
                    .continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let { throw it }
                        }
                        imgRef.downloadUrl
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

    //새로운 유저 추가
    fun saveUser(user: User) {
        userStoreRef.document(user.uid).set(user)
            .addOnSuccessListener { Log.d("Firestore", "Success") }
            .addOnFailureListener { e -> Log.e("Firestore", "Failed", e) }
    }

    //카드, 소유 유저 저장
    fun saveCard(card: UserCard): Flow<Boolean> = flow {
        try {
            cardStoreRef.document(card.cid.toString())
                .set(card).await()

            userStoreRef.document(card.userId.toString())
                .collection("cards").document(card.cid.toString())
                .set(card).await()

            emit(true)
        } catch (e: Exception) {
            Log.e("SaveCard", "Failed to save card", e)
            emit(false)
        }
    }.flowOn(Dispatchers.IO)


    //유저 가저오기
    suspend fun getUser(userId: String): Flow<User?> = flow {
        try {
            val document = userStoreRef.document(userId).get().await()
            val user = document.toObject(User::class.java)
            emit(user)
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
        }
    }

    //경험치 증가
    fun expUp(uid: String, exp: Int) {
        val documentReference = userStoreRef
            .document(uid)

        documentReference.update("exp", FieldValue.increment(exp.toLong()))
    }


    //댓글
    suspend fun addComment(cardId: String, commentUser: CommentUser) {

        val commentStore = cardStoreRef
            .document(cardId)
            .collection("comment")
        try {
            val coId = commentStore
                .count()
                .get(AggregateSource.SERVER)
                .await()
                .count

            commentUser.coId = coId.toInt()

            commentStore
                .document(coId.toString())
                .set(commentUser)
                .addOnSuccessListener { Log.d("Firestore", "Success") }
                .addOnFailureListener { e -> Log.e("Firestore", "Failed", e) }
        } catch (e: Exception) {
            Log.e("FirestoreCounter", "Error counting documents", e)
            0
        }

    }

    //댓글 리스트 가저오기
    fun getCommentList(cardId: String): Flow<List<CommentUser>> = flow {
        try {
            val commentList = suspendCoroutine { continuation ->
                cardStoreRef
                    .document(cardId)
                    .collection("comment")
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        val comments =
                            querySnapshot.documents.mapNotNull { it.toObject(CommentUser::class.java) }
                        continuation.resume(comments) // 성공 시 리스트 반환
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception) // 실패 시 예외 반환
                    }
            }
            emit(commentList)
        } catch (e: Exception) {
            throw e
        }
    }

    fun addLike(uid: String, cid: String, coId: String) {
        val coIdRef = cardStoreRef
            .document(cid)
            .collection("comment")
            .document(coId)

        cardStoreRef.firestore.runTransaction { transaction ->
            val snapshot = transaction.get(coIdRef)

            val currentLikeCount = snapshot.getLong("likeCount") ?: 0L

            // 좋아요 추가
            transaction.set(
                coIdRef.collection("commentUser").document(uid),
                mapOf("uid" to uid)
            )

            // likeCount 증가
            transaction.update(coIdRef, "likeCount", currentLikeCount + 1L)

            // 좋아요 여부 저장 (필요한 경우)
            transaction.update(coIdRef, "liked", true)
        }.addOnSuccessListener {
            Log.d("Firestore", "Like added successfully")
        }.addOnFailureListener { e ->
            Log.e("Firestore", "Failed to add like", e)
        }
    }


    fun removeLike(uid: String, cid: String, coId: String) {
        val coIdRef = cardStoreRef
            .document(cid)
            .collection("comment")
            .document(coId)

        cardStoreRef.firestore.runTransaction { transaction ->
            val snapshot = transaction.get(coIdRef)

            // 현재 likeCount 가져오기 (없으면 기본값 0)
            val currentLikeCount = snapshot.getLong("likeCount") ?: 0L

            if (currentLikeCount > 0) {
                // 좋아요 삭제
                transaction.delete(coIdRef.collection("commentUser").document(uid))

                // likeCount 감소
                transaction.update(coIdRef, "likeCount", currentLikeCount - 1L)

                // 좋아요 여부 저장 (필요한 경우)
                transaction.update(coIdRef, "liked", false)
            }
        }.addOnSuccessListener {
            Log.d("Firestore", "Like removed successfully")
        }.addOnFailureListener { e ->
            Log.e("Firestore", "Failed to remove like", e)
        }
    }


}
