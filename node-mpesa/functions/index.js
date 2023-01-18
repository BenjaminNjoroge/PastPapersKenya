const functions = require("firebase-functions");
const express= require("express");
const cors= require("cors");
const bodyParser = require('body-parser');

const app= express();
app.use(bodyParser.json());
app.use(cors({origin: true}));

const mpesaUtils= require("./utils/mpesa-utils");
const dbUtils= require("./utils/db-utils");
const woocommerceApi= require("./utils/woocommerce-orders");


app.post("/mpesa/callback", async(req, res)=>{

    const responseData= await req.body.Body.stkCallback;

    console.log(responseData);

    const parsedData= mpesaUtils.callbackDataFunc(responseData);
 
    if(parsedData.resultCode== 0){

        const docId1= await dbUtils.getDocumentId(parsedData.checkoutRequestID);
        console.log(`The document id is ${docId1}`);

        const email= await dbUtils.getUserEmail(docId1);
        console.log(email);

        try{
             
        const orderId =await dbUtils.savePaymentSuccessDetails(docId1, parsedData);

        console.log(orderId);

        const status= await dbUtils.getPaymentStatus(docId1);
        console.log(status);

        const reason= await dbUtils.getReason(docId1);
        
        await dbUtils.setOrderToPaid(email, orderId);

        woocommerceApi.updateOrder(orderId);


        await dbUtils.sendHotNotification(`Order ${orderId} Payment Sucess`,"Your pastpaper is ready to view",docId1, email, status, orderId, reason, docId1);

        res.status(200).send({
            status: "Success",
            message: `The request was successful. ${parsedData.resultDesc}`
        });

        

    } catch(error){
        console.log(error);
         res.send({
            status: "Unable to save successful payment",
            message: error
        });
    }
        
    } else {

        const docId2= await dbUtils.getDocumentId(responseData.CheckoutRequestID);
        console.log(`The document id is ${docId2}`);

        const email= await dbUtils.getUserEmail(docId2);
        console.log(email);

        const reason= await dbUtils.getReason(docId2);

        const status= await dbUtils.getPaymentStatus(docId2);
        console.log(status);

        try{
            
            const orderId =await dbUtils.savePaymentFailedDetails(docId2, responseData);

            await dbUtils.setOrderToUnPaid(email, orderId);

            await dbUtils.sendHotNotification(`Order ${orderId} Failed`, "Sorry!. We could not complete your order", docId2, email, status, orderId, reason, docId2);


             res.status(200).send({
                status: "Response Success",
                message: `But the request for payment failed. Because ${responseData.ResultDesc}`
            });
        } catch(error){
            console.log(error);
             res.send({
                status: "Unable to save failed payment",
                message: error
            });
        }

    }
    
});


 exports.stkpushcallback = functions.https.onRequest(app);
