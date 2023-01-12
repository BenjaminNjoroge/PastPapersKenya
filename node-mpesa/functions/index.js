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
        
        await dbUtils.setOrderToPaid(email, orderId);

        res.status(200).send({
            status: "Success",
            message: `The request was successful. ${parsedData.resultDesc}`
        });

        woocommerceApi.updateOrder(orderId);

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

        try{
            
            const orderId =await dbUtils.savePaymentFailedDetails(docId2, responseData);

            await dbUtils.setOrderToUnPaid(email, orderId);

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
