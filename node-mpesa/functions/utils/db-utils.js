const admin= require("firebase-admin");

const serviceAccount= require("../config.json");

const moment= require("moment");
var date = moment();
var currentDate = date.format('MMMM Do YYYY, h:mm:ss a');



admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://pastpaperskenya-default-rtdb.firebaseio.com"
});

const db= admin.firestore();


function getDocumentId(checkoutRequestID){
    return db.collection("payments")
    .where("checkoutRequestID","==", checkoutRequestID)
    .limit(1)
    .get()
    .then(snapshot=> snapshot.docs[0].id);
}

function getUserEmail(checkoutRequestID){
    return db.collection("payments")
    .doc(checkoutRequestID)
    .get()
    .then(snapshot =>{
        return db.collection("payments")
        .doc(checkoutRequestID)
        .get()
        .then(() => snapshot.data().email);
    });
}

function setOrderToPaid(email, orderId){
    return db.collection("orders")
    .doc(email)
    .collection(orderId)
    .doc(orderId)
    .set({
        paymentStatus: "paid",
        orderId: orderId
    });
}

function setOrderToUnPaid(email, orderId){
    return db.collection("orders")
    .doc(email)
    .collection(orderId)
    .doc(orderId)
    .set({
        paymentStatus: "unpaid",
        orderId: orderId
    });
}

function savePaymentSuccessDetails(id, parsedData){
    return db.collection("payments")
    .doc(id)
    .get()
    .then(snapshot=>{
        return db.collection("payments")
        .doc(id)
        .update({
            checkoutRequestID: parsedData.checkoutRequestID,    
            merchantRequestID: parsedData.merchantRequestID,    
            resultCode: parsedData.resultCode,    
            resultDesc: parsedData.resultDesc,
            mpesaReceiptNumber: parsedData.mpesaReceiptNumber,
            amount: parsedData.amount,
            phoneNumber: parsedData.phoneNumber,
            transactionDate: parsedData.transactionDate,
            date: currentDate,
            status: "completed"
        })
        .then(()=> snapshot.data().orderId);
    });
    
}

function savePaymentFailedDetails(id, parsedData){
    return db.collection("payments")
    .doc(id)
    .get()
    .then(snapshot =>{
        return db.collection("payments")
        .doc(id)
        .update({
            merchantRequestID: parsedData.MerchantRequestID,    
            checkoutRequestID: parsedData.CheckoutRequestID,    
            resultCode: parsedData.ResultCode,    
            resultDesc: parsedData.ResultDesc,
            date: currentDate,
            status: "failed"
        })
        .then(()=> snapshot.data().orderId);
    });
    
}

module.exports= {
    getDocumentId,
    savePaymentSuccessDetails,
    savePaymentFailedDetails,
    setOrderToPaid,
    setOrderToUnPaid,
    getUserEmail
};